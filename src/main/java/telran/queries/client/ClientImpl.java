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

    public ClientImpl(BullsCowsService service, InputOutput io) {
        this.service = service;
        this.io = io;
    }

    public void startGame() {
        long gameId = io.readLong("Enter game ID to start:", "should be a long");
        service.startGame(gameId, loggedInGamer);
        io.writeLine("Game started!");
    }

    public void createGamer() {
        String username = io.readString("Enter username:");
        String birthdate = io.readString("Enter birthdate (YYYY-MM-DD):");
        service.createGamer(username, LocalDate.parse(birthdate, DateTimeFormatter.ISO_LOCAL_DATE));
        io.writeLine("Gamer registered.");
    }

    public void signIn() {
        String username = io.readString("Enter username:");
        loggedInGamer = service.signIn(username);
        io.writeLine("Signed in successfully as " + username + ".");
    }

    public void joinGame() {
        long gameId = io.readLong("Enter game ID:", "should be a long");
        service.joinGame(gameId, loggedInGamer);
        io.writeLine("Joined game.");
    }

    public void playGame() {
        while (true) {
            long gameId = io.readLong("Enter game ID:", "should be a long");
            String guess = io.readString("Enter your guess sequence:");
            service.createMove(loggedInGamer, gameId, guess);
            io.writeLine("Move registered. Guess again or exit.");
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
                io.writeLine(String.format("Game ID: %d, Status: %s", game.getId(), game.toString()));
            }
        }
    }
}