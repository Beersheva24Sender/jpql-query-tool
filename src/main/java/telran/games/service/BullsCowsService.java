package telran.games.service;

import java.time.LocalDate;
import java.util.List;

import telran.queries.entities.Game;
import telran.queries.entities.Gamer;

public interface BullsCowsService {

    Game startGame(long gameId, String username);

    void createGamer(String username, LocalDate birthdate);

    Gamer signIn(String username);

    void joinGame(long gameId, String username);

    void createMove(long gameGamerId, String sequence);

    List<Game> getNotFinishedGamesByUserName(String username);

}