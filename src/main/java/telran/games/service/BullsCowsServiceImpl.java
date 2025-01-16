package telran.games.service;

import telran.games.repo.BullCowsRepository;
import telran.queries.entities.*;

import java.time.LocalDate;
import java.util.List;

public class BullsCowsServiceImpl implements BullsCowsService {
    private final BullCowsRepository repository;

    public BullsCowsServiceImpl(BullCowsRepository repository) {
        this.repository = repository;
    }

    @Override
    public Game startGame(long gameId, Gamer gamer) {
        return repository.startGame(gameId);
    }

    @Override
    public void createGamer(String username, LocalDate birthdate) {
        Gamer gamer = new Gamer(username, birthdate);
        repository.createGamer(gamer);
    }

    @Override
    public Gamer signIn(String username) {
        return repository.getGamer(username);
    }

    @Override
    public void joinGame(long gameId, Gamer gamer) {
        repository.joinGame(gameId, gamer);
    }

    @Override
    public void createMove(Gamer gamer, long gameId, String sequence) {
        GameGamer gameGamer = repository.getGameGamerByUserAndGame(gamer.getUsername(), gameId);
        repository.createMove(gameGamer, sequence);
    }

    @Override
    public List<Game> getNotFinishedGamesByGamer(Gamer gamer) {
        return repository.getNotFinishedGamesByGamer(gamer);
    }
}