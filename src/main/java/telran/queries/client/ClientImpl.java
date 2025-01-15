package telran.queries.client;

import telran.view.*;
import telran.games.service.BullsCowsService;
import telran.queries.entities.Game;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ClientImpl {

    private BullsCowsService bullsCowsService;
    private InputOutput io;

    public ClientImpl(BullsCowsService bullsCowsService, InputOutput io) {
        this.bullsCowsService = bullsCowsService;
        this.io = io;
    }

    public void startGame() {
        long gameId = io.readLong("Enter the game ID to start", "Entered string must be a number otherwise");
        String username = io.readString("Enter your username");

        try {
            bullsCowsService.startGame(gameId, username);
            io.writeLine("Game started successfully. Have fun!");
        } catch (Exception e) {
            io.writeLine("Error starting game: " + e.getMessage());
        }
    }

    public void createGamer() {
        String username = io.readString("Enter username to register");
        String birthdate = io.readString("Enter your birthdate (YYYY-MM-DD)");
        try {
            bullsCowsService.createGamer(username, LocalDate.parse(birthdate, DateTimeFormatter.ISO_LOCAL_DATE));
            io.writeLine("Gamer registered successfully.");
        } catch (Exception e) {
            io.writeLine("Error registering gamer: " + e.getMessage());
        }
    }

    public void signIn(){
        String username = io.readString("Enter your username");
        try {
            bullsCowsService.signIn(username);
            io.writeLine("Signed in successfully.");
        } catch (Exception e) {
            io.writeLine("Error signing in: " + e.getMessage());
        }
    }

    public void joinGame() {
        String username = io.readString("Enter your username");
        long gameId = io.readLong("Enter game ID to join", "Entered string must be a number otherwise");
        try {
            bullsCowsService.joinGame(gameId, username);
            io.writeLine("Joined game successfully.");
        } catch (Exception e) {
            io.writeLine("Error joining game: " + e.getMessage());
        }
    }

    public void createMove() {
        long gameGamerId = io.readLong("Enter your game gamer ID", "Entered string must be a number otherwise");
        String sequence = io.readString("Enter the guessed sequence");
        try {
            bullsCowsService.createMove(gameGamerId, sequence);
            io.writeLine("Move set successfully.");
        } catch (Exception e) {
            io.writeLine("Error setting move: " + e.getMessage());
        }
    }

    public void getNotFinishedGamesByUserName() {
        String username = io.readString("Enter your username to get startable games");
        try {
            List<Game> startableGames = bullsCowsService.getNotFinishedGamesByUserName(username);
            if (startableGames.isEmpty()) {
                io.writeLine("No startable games found.");
            } else {
                io.writeLine("Startable Games:");
                startableGames.forEach(game -> io.writeLine("Game ID: " + game.getId()));
            }
        } catch (Exception e) {
            io.writeLine("Error retrieving startable games: " + e.getMessage());
        }
    }
}
