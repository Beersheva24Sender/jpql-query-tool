package telran.queries.client;

import telran.view.InputOutput;

public interface Client {

    void startGame(InputOutput io);

    void createGamer(InputOutput io);

    void signIn(InputOutput io);

    void joinGame(InputOutput io);

    void createMove(InputOutput io);

    void getNotFinishedGamesByUserName(InputOutput io);
}
