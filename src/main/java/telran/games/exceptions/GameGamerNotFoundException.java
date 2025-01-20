package telran.games.exceptions;

import java.util.NoSuchElementException;

public class GameGamerNotFoundException extends NoSuchElementException{
    public GameGamerNotFoundException(long gameId, String gamerId){
        super(String.format("Gamer %d is not part of the game %s", gameId, gameId));
    }
}
