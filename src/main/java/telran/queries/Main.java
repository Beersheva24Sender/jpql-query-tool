package telran.queries;

import telran.games.repo.*;
import telran.games.service.*;
import telran.queries.client.*;
import telran.queries.config.BullsCowsPersistenceUnitInfo;
import telran.view.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.jpa.HibernatePersistenceProvider;

import java.util.HashMap;

public class Main {
    private static InputOutput io = new StandardInputOutput();
    private static EntityManager em;
    private static BullsCowsService service;
    private static Client client;

    public static void main(String[] args) {
        createEntityManager();

        BullCowsRepository repository = new BullsCowsRepositoryJpaImpl(em);
        service = new BullsCowsServiceImpl(repository);
        client = new ClientImpl(service, io);

        Menu mainMenu = new Menu("Bulls and Cows Game", getMainMenuItems());
        mainMenu.perform(io);
    }

    private static Item[] getMainMenuItems() {
        return new Item[] {
            Item.of("Register", io -> client.createGamer()),
            Item.of("Sign In", io -> signInMenu()),
            Item.ofExit()
        };
    }

    private static void signInMenu() {
        client.signIn();
        Menu signInMenu = new Menu("Game Options", getSignInMenuItems());
        signInMenu.perform(io);
    }

    private static Item[] getSignInMenuItems() {
        return new Item[] {
            Item.of("View Available Games", io -> viewGamesMenu()),
            Item.ofExit()
        };
    }

    private static void viewGamesMenu() {
        client.getNotFinishedGamesByUserName();
        Menu gamesMenu = new Menu("Available Games Options", getAvailableGamesMenuItems());
        gamesMenu.perform(io);
    }

    private static Item[] getAvailableGamesMenuItems() {
        return new Item[] {
            Item.of("Start a New Game", io -> client.startGame()),
            Item.of("Join an Existing Game", io -> playGameMenu()),
            Item.ofExit()
        };
    }

    private static void playGameMenu() {
        client.joinGame();
        client.playGame();
    }

    private static void createEntityManager() {
        HashMap<String, Object> hibernateProperties = new HashMap<>();
        hibernateProperties.put("hibernate.hbm2ddl.auto", "update");

        HibernatePersistenceProvider provider = new HibernatePersistenceProvider();
        EntityManagerFactory emf = provider.createContainerEntityManagerFactory(new BullsCowsPersistenceUnitInfo(), hibernateProperties);
        em = emf.createEntityManager();
    }
}
