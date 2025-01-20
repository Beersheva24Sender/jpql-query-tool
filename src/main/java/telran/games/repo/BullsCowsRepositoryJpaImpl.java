// Refactored BullsCowsRepositoryJpaImpl to handle username propagation
package telran.games.repo;

import telran.games.exceptions.*;
import telran.queries.entities.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class BullsCowsRepositoryJpaImpl implements BullCowsRepository {
    private final EntityManager em;

    public BullsCowsRepositoryJpaImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Game createGame(Gamer gamer) {
        var transaction = em.getTransaction();
        transaction.begin();
        try {
            Game game = new Game();
            em.persist(game);
            GameGamer gameGamer = new GameGamer(game, gamer);
            em.persist(gameGamer);
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
            if (game == null)
                throw new GameNotFoundException(gameId);

            GameGamer gameGamer = getGameGamerByUserAndGame(gamer.getUsername(), game.getId());
            if (gameGamer == null)
                throw new GameGamerNotFoundException(gameId, gamer.getUsername());
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
    public Move createMove(Gamer gamer, long gameId, String sequence) {
        var transaction = em.getTransaction();
        if (!transaction.isActive()) {
            transaction.begin();
        }
        try {
            GameGamer gameGamer = getGameGamerByUserAndGame(gamer.getUsername(), gameId);
            Move move = new Move(gameGamer, sequence);
            em.persist(move);
            transaction.commit();
            return move;
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
                    Game.class);
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
            if (gamer == null)
                throw new GamerNotFoundException(username);
            transaction.commit();
            return gamer;
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    public GameGamer getGameGamerByUserAndGame(String username, long gameId) {
        TypedQuery<GameGamer> query = em.createQuery("select gg from GameGamer gg " +
                "where gg.gamer.username = :gamerUsername and gg.game.id = :gameId",
                GameGamer.class);
        query.setParameter("gamerUsername", username);
        query.setParameter("gameId", gameId);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}