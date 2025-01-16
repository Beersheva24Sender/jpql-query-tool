package telran.queries.client;

public interface Client {

    void startGame();

    void createGamer();

    void signIn();

    void joinGame();

    void playGame();

    void getNotFinishedGamesByUserName();
}
