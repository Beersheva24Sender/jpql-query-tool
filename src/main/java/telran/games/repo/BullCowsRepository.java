package telran.games.repo;

import telran.queries.entities.*;

import java.time.LocalDate;
import java.util.List;

public interface BullCowsRepository {
    Game startGame(long gameId);

    void joinGame(long gameId, Gamer gamer);

    void createGamer(Gamer gamer);

    void createMove(GameGamer gameGamer, String sequence);

    List<Game> getNotFinishedGamesByGamer(Gamer gamer);

    Gamer getGamer(String username);

    GameGamer getGameGamerByUserAndGame(String username, long gameId);

}
