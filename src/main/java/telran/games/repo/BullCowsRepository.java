package telran.games.repo;

import telran.queries.entities.*;

import java.util.List;

public interface BullCowsRepository {
    Game createGame(Gamer gamer);

    void joinGame(long gameId, Gamer gamer);

    void createGamer(Gamer gamer);

    Move createMove(Gamer gamer, long gameId, String sequence);

    List<Game> getNotFinishedGamesByGamer(Gamer gamer);

    Gamer getGamer(String username);

    GameGamer getGameGamerByUserAndGame(String username, long gameId);

}
