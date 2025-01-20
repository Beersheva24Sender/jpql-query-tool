package telran.games.service;

import telran.queries.entities.*;

import java.time.LocalDate;
import java.util.List;

public interface BullsCowsService {

    Game createGame(Gamer gamer);

    void createGamer(String username, LocalDate birthdate);

    Gamer signIn(String username);

    void joinGame(long gameId, Gamer gamer);

    Move createMove(Gamer gamer, long gameId, String sequence);

    List<Game> getNotFinishedGamesByGamer(Gamer gamer);

}