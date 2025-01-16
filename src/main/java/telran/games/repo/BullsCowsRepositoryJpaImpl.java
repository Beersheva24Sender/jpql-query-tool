package telran.games.repo;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;
import telran.games.exceptions.GameGamerNotFoundException;
import telran.games.exceptions.GameNotFoundException;
import telran.games.exceptions.GamerNotFoundException;
import telran.queries.entities.*;

public class BullsCowsRepositoryJpaImpl implements BullCowsRepository {
    EntityManager em;

    public BullsCowsRepositoryJpaImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Game startGame(long gameId, String username) {
        Game game = em.find(Game.class, gameId);
        if (game == null) {
            throw new IllegalArgumentException("Game not found with ID: " + gameId);
        }

        Gamer gamer = em.find(Gamer.class, username);
        if (gamer == null) {
            throw new IllegalArgumentException("Gamer not found with username: " + username);
        }

        GameGamer gameGamer = em.createQuery(
                "SELECT gg FROM GameGamer gg WHERE gg.game.id = :gameId AND gg.gamer.username = :username",
                GameGamer.class)
                .setParameter("gameId", gameId)
                .setParameter("username", username)
                .getResultStream()
                .findFirst()
                .orElse(null);

        if (gameGamer == null) {
            throw new IllegalStateException("User is not a participant in this game.");
        }

        return game; 
    }

    @Override
    public void joinGame(long gameId, String username) {
        var transaction = em.getTransaction();
        transaction.begin();
        try {
            Game game = getGame(gameId);
            Gamer gamer = getGamer(username);
            GameGamer gameGamer = new GameGamer(game, gamer);
            em.persist(gameGamer);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    public Gamer getGamer(String username) {
        Gamer gamer = em.find(Gamer.class, username);
        if (gamer == null) {
            throw new GamerNotFoundException(username);
        }
        return gamer;
    }

    private Game getGame(long gameId) {
        Game game = em.find(Game.class, gameId);
        if (game == null) {
            throw new GameNotFoundException(gameId);
        }
        return game;
    }

    public GameGamer getGameGamer(long gameGamerId) {
        GameGamer gameGamer = em.find(GameGamer.class, gameGamerId);
        if (gameGamer == null) {
            throw new GameGamerNotFoundException(gameGamerId);
        }
        return gameGamer;

    }

    @Override
    public void setFinishGame(long gameId) {
        var transaction = em.getTransaction();
        try {
            transaction.begin();
            Game game = getGame(gameId);
            game.setGameIsFinished();
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }

    }

    @Override
    public void setWinnerGame(String username, long gameGamerId) {
        var transaction = em.getTransaction();
        try {
            transaction.begin();
            GameGamer gameGamer = getGameGamer(gameGamerId);
            gameGamer.setWinnerGame();
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    @Override
    public void createMove(long gameGamerId, String sequence) {
        GameGamer gameGamer = getGameGamer(gameGamerId);
        Move move = new Move(gameGamer, sequence);
        var transaction = em.getTransaction();
        transaction.begin();
        move.setGameGamer(gameGamer);
        try {
            em.persist(move);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    @Override
    public List<Game> getFinishedGamesByUserName(String username) {
        TypedQuery<Game> unstartedGames = em.createQuery(
                "SELECT gg.game FROM GameGamer gg WHERE gg.gamer.username = :username AND gg.game.isFinished = false",
                Game.class);
        unstartedGames.setParameter("username", username);
        return unstartedGames.getResultList();
    }

    @Override
    public void createGamer(String username, LocalDate birthdate) {
        Gamer gamer = new Gamer(username, birthdate);
        var transaction = em.getTransaction();
        transaction.begin();
        try {
            em.persist(gamer);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    @Override
    public void createUser(String username) {
        Gamer gamer = new Gamer(username, LocalDate.now());
        var transaction = em.getTransaction();
        transaction.begin();
        try {
            em.persist(gamer);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    @Override
    public void createGame(String sequence) {
        Game game = new Game(sequence);
        var transaction = em.getTransaction();
        transaction.begin();
        try {
            em.persist(game);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    @Override
    public List<Game> getNotFinishedGamesByUserName(String username) {
        TypedQuery<Game> query = em.createQuery(
                "SELECT gg.game FROM GameGamer gg WHERE gg.gamer.username = :username AND gg.game.isFinished = false",
                Game.class);
        query.setParameter("username", username);
        return query.getResultList();
    }

}