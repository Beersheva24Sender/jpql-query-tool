package telran.queries;

import telran.queries.config.BullsCowsPersistenceUnitInfo;
import telran.queries.entities.Game;
import telran.queries.entities.GameGamer;
import telran.queries.entities.Gamer;
import telran.view.*;
import telran.games.repo.BullCowsRepository;
import telran.games.repo.BullsCowsRepositoryJpaImpl;
import telran.games.service.BullsCowsService;
import telran.games.service.BullsCowsServiceImpl;
import telran.queries.client.Client;
import telran.queries.client.ClientImpl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.hibernate.jpa.HibernatePersistenceProvider;
import jakarta.persistence.*;
import jakarta.persistence.spi.PersistenceUnitInfo;

public class Main {
    static InputOutput io = new StandardInputOutput();
    static EntityManager em;

    static BullsCowsService bullsCowsService;
    static ClientImpl client;

    public static void main(String[] args) {
        createEntityManager();

        BullCowsRepository repository = new BullsCowsRepositoryJpaImpl(em);
        bullsCowsService = new BullsCowsServiceImpl(repository);
        client = new ClientImpl(bullsCowsService, io);

        Item[] items = getMainMenuItems();
        Menu menu = new Menu("Bulls Cows Game", items);
        menu.perform(io);

    }

    private static Item[] getMainMenuItems() {
        return new Item[]{
            Item.of("Register Gamer", io -> client.createGamer()),
            Item.of("Sign In", io -> signInMenu()),
            Item.ofExit()
        };
    }
    
    private static void signInMenu() {
        client.signIn();
        Menu signInMenu = new Menu("Sign-In Options", getSignInMenuItems());
        signInMenu.perform(io);
    }
    
    private static Item[] getSignInMenuItems() {
        return new Item[]{
            Item.of("Get Available Games", io -> getAvailableGamesMenu()),
            Item.ofExit()
        };
    }

    private static void getAvailableGamesMenu() {
        client.getNotFinishedGamesByUserName();
        Menu availableGamesMenu = new Menu("Available Games Options", getAvailableGamesMenuItems());
        availableGamesMenu.perform(io);
    }
    
    private static Item[] getAvailableGamesMenuItems() {
        return new Item[]{
            Item.of("Join Game", io -> joinGameMenu()),
            Item.of("Start Game", io -> client.startGame()),
            Item.ofExit()
        };
    }


    private static void joinGameMenu() {
        client.joinGame();
        Menu joinGameMenu = new Menu("Join Game Options", getJoinGameMenuItems());
        joinGameMenu.perform(io);
    }
    
    private static Item[] getJoinGameMenuItems() {
        return new Item[]{
            Item.of("Set Move", io -> client.createMove()),
            Item.ofExit()
        };
    }

    private static void createEntityManager() {
        HashMap<String, Object> hibernateProperties = new HashMap<>();
        hibernateProperties.put("hibernate.hbm2ddl.auto", "update");
        PersistenceUnitInfo persistenceUnit = new BullsCowsPersistenceUnitInfo();
        HibernatePersistenceProvider hibernatePersistenceProvider = new HibernatePersistenceProvider();
        EntityManagerFactory emf = hibernatePersistenceProvider.createContainerEntityManagerFactory(persistenceUnit,
                hibernateProperties);
        em = emf.createEntityManager();
    }

    static void queryProcessing(InputOutput io) {
        String queryString = io.readString("Enter JPQL query string");
        Query query = em.createQuery(queryString);
        @SuppressWarnings({ "rawtypes" })
        List result = query.getResultList();
        if (result.isEmpty()) {
            io.writeLine("No data");
        } else {
            @SuppressWarnings("unchecked")
            List<String> lines = result.get(0).getClass().isArray() ? getArrayLines(result) : getLines(result);
            lines.forEach(io::writeLine);
        }
    }

    private static List<String> getLines(List<Object> result) {
        return result.stream().map(Object::toString).toList();
    }

    private static List<String> getArrayLines(List<Object[]> result) {
        return result.stream().map(a -> Arrays.deepToString(a)).toList();
    }
}
