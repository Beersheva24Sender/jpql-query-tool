package telran.queries.client;

public interface Client {

    void createGame();

    void createGamer();

    void signIn();

    void joinGame();

    void playGame();

    void getNotFinishedGamesByUserName();
}
