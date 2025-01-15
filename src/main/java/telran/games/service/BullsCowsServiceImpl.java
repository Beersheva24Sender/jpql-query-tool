package telran.games.service;

import java.time.LocalDate;
import java.util.List;

import telran.games.repo.BullCowsRepository;
import telran.queries.entities.Game;
import telran.queries.entities.Gamer;

public class BullsCowsServiceImpl implements BullsCowsService {
    private final BullCowsRepository repository;

    public BullsCowsServiceImpl(BullCowsRepository repository) {
        this.repository = repository;
    }

    @Override
    public Game startGame(long gameId, String username) {
        return repository.startGame(gameId, username);
    }

    @Override
    public void createGamer(String username, LocalDate birthdate) {
        try {
            repository.createGamer(username, birthdate);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to register gamer: " + username, e);
        }
    }

    @Override
    public Gamer signIn(String username) {
        try {
            Gamer gamer = repository.getGamer(username);
            if (gamer == null) {
                throw new RuntimeException("Gamer not found with username: " + username);
            }
            return gamer;
        } catch (Exception e) {
            throw new RuntimeException("Failed to sign in user: " + username, e);
        }
    }

    @Override
    public void joinGame(long gameId, String username) {
        try {
            repository.joinGame(gameId, username);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to join game. Check if the game exists and is not started.", e);
        }
    }

    @Override
    public void createMove(long gameGamerId, String sequence) {
        try {
            repository.createMove(gameGamerId, sequence);
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Move not allowed: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error while setting the move.", e);
        }
    }

    @Override
    public List<Game> getNotFinishedGamesByUserName(String username) {
        try {
            return repository.getNotFinishedGamesByUserName(username);
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve startable games for user: " + username, e);
        }
    }

}