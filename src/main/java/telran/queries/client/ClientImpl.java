package telran.queries.client;

import telran.games.service.BullsCowsService;
import telran.queries.entities.*;
import telran.view.InputOutput;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ClientImpl implements Client {
    private final BullsCowsService service;
    private final InputOutput io;
    private Gamer loggedInGamer;
    private Long activeGameId;

    public ClientImpl(BullsCowsService service, InputOutput io) {
        this.service = service;
        this.io = io;
    }

    @Override
    public void createGame() {
        service.createGame(loggedInGamer);
        io.writeLine("Game created!");
        getNotFinishedGamesByUserName();
    }

    @Override
    public void createGamer() {
        String username = io.readString("Enter username:");
        String birthdate = io.readString("Enter birthdate (YYYY-MM-DD):");
        service.createGamer(username, LocalDate.parse(birthdate, DateTimeFormatter.ISO_LOCAL_DATE));
        io.writeLine("Gamer registered.");
    }

    @Override
    public void signIn() {
        String username = io.readString("Enter username:");
        loggedInGamer = service.signIn(username);
        io.writeLine("Signed in successfully as " + username + ".");
    }

    @Override
    public void joinGame() {
        activeGameId = io.readLong("Enter game ID to join:", "should be a long");
        service.joinGame(activeGameId, loggedInGamer);
        io.writeLine("Joined game successfully!");
        playGame();
    }

    @Override
    public void playGame() {
        if (activeGameId == null) {
            io.writeLine("You are not part of any active game. Please join or start a game first.");
            return;
        }

        while (true) {
            String guess = io.readString("Enter your guess sequence:");
            Move move = service.createMove(loggedInGamer, activeGameId, guess);
            if (move.getBulls() == 4) {
                io.writeLine("You won the game!!");
                getNotFinishedGamesByUserName();
            } else {
                io.writeLine(String.format("You got %d bulls and %d cows!\n", move.getBulls(), move.getCows()));
                io.writeLine("Move registered. Try to guess again.");
            }
        }
    }

    @Override
    public void getNotFinishedGamesByUserName() {
        List<Game> games = service.getNotFinishedGamesByGamer(loggedInGamer);
        if (games.isEmpty()) {
            io.writeLine("No available games found.");
        } else {
            io.writeLine("Available Games:");
            for (Game game : games) {
                io.writeLine(String.format("Game ID: %d, Is finished: %s", game.getId(), game.isFinished()));
            }
        }
    }
}
