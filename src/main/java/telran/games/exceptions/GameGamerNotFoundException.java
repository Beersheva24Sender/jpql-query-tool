package telran.games.exceptions;

import java.util.NoSuchElementException;

public class GameGamerNotFoundException extends NoSuchElementException{
    public GameGamerNotFoundException(long gameGamerId){
        super(String.format("gameGamer with id: %d not found", gameGamerId));
    }
}
