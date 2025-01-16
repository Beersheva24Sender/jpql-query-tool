// Refactored BullsCowsRepositoryJpaImpl to handle username propagation
package telran.games.repo;

import telran.games.exceptions.*;
import telran.queries.entities.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;

public class BullsCowsRepositoryJpaImpl implements BullCowsRepository {
    private final EntityManager em;

    public BullsCowsRepositoryJpaImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Game startGame(long gameId) {
        var transaction = em.getTransaction();
        transaction.begin();
        try {
            Game game = em.find(Game.class, gameId);
            if (game == null) throw new GameNotFoundException(gameId);
            transaction.commit();
            return game;
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    @Override
    public void joinGame(long gameId, Gamer gamer) {
        var transaction = em.getTransaction();
        transaction.begin();
        try {
            Game game = em.find(Game.class, gameId);
            if (game == null) throw new GameNotFoundException(gameId);

            GameGamer gameGamer = new GameGamer(game, gamer);
            em.persist(gameGamer);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    @Override
    public void createGamer(Gamer gamer) {
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
    public void createMove(GameGamer gameGamer, String sequence) {
        var transaction = em.getTransaction();
        transaction.begin();
        try {
            Move move = new Move(gameGamer, sequence);
            em.persist(move);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    @Override
    public List<Game> getNotFinishedGamesByGamer(Gamer gamer) {
        var transaction = em.getTransaction();
        transaction.begin();
        try {
            TypedQuery<Game> query = em.createQuery(
                "SELECT gg.game FROM GameGamer gg WHERE gg.gamer = :gamer AND gg.game.isFinished = false",
                Game.class
            );
            query.setParameter("gamer", gamer);
            List<Game> games = query.getResultList();
            transaction.commit();
            return games;
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    @Override
    public Gamer getGamer(String username) {
        var transaction = em.getTransaction();
        transaction.begin();
        try {
            Gamer gamer = em.find(Gamer.class, username);
            if (gamer == null) throw new GamerNotFoundException(username);
            transaction.commit();
            return gamer;
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    public GameGamer getGameGamerByUserAndGame(String username, long gameId){
        var transaction = em.getTransaction();
        transaction.begin();
        try {
            TypedQuery<GameGamer> query = em.createQuery(
                "SELECT gg FROM GameGamer gg WHERE gg.gamer.username = :username AND gg.game.id = :gameId",
                GameGamer.class
            );
            query.setParameter("username", username);
            query.setParameter("gameId", gameId);
            GameGamer gameGamer = query.getSingleResult();
            transaction.commit();
            return gameGamer;
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }
}
