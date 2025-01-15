package telran.games.repo;

import telran.queries.entities.*;

import java.time.LocalDate;
import java.util.*;

public interface BullCowsRepository {

   Game startGame(long gameId, String username);
   
   void joinGame(long gameId, String username);

   void createUser(String username);

   void createGame(String sequence);

   void createGamer(String username, LocalDate birthdate);

   void createMove(long gameGamerId, String sequence);

   List<Game> getNotFinishedGamesByUserName(String username);

   List<Game> getFinishedGamesByUserName(String username);

   void setFinishGame(long id);

   void setWinnerGame(String username, long gameId);

   Gamer getGamer(String username);

}