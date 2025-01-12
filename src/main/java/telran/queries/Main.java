package telran.queries;

import telran.queries.config.BullsCowsPersistenceUnitInfo;
import telran.view.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.hibernate.jpa.HibernatePersistenceProvider;

import jakarta.persistence.*;
import jakarta.persistence.spi.PersistenceUnitInfo;

public class Main {
    static InputOutput io = new StandardInputOutput();
    static EntityManager em;

    public static void main(String[] args) {
        createEntityManager();
        Item[] items = getItems();
        Menu menu = new Menu("Query tool", items);
        menu.perform(io);
    }

    private static Item[] getItems() {
        return new Item[] {
                Item.of("enter JPQL query", Main::queryProcessing),
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
        String queryString = io.readString("enter JPQL query string");
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