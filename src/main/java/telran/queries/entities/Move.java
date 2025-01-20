package telran.queries.entities;

import jakarta.persistence.*;

@Table(name = "move")
@Entity
public class Move {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    int bulls;
    int cows;
    String sequence;

    @ManyToOne
    @JoinColumn(name = "game_gamer_id")
    GameGamer gameGamer;

    public Move() {
    }

    public Move(GameGamer gameGamer, String sequence) {
        this.gameGamer = gameGamer;
        this.sequence = sequence;
        setBullsAndCows();
        checkIfWinner();
    }

    @Override
    public String toString() {
        return "Move [id=" + id + ", bulls=" + bulls + ", cows=" + cows + ", sequence=" + sequence + ", gameGamer="
                + gameGamer.getId() + "]";
    }

    public void setGameGamer(GameGamer gameGamer2) {
        this.gameGamer = gameGamer2;
    }

    public void setBulls() {
    }

    private void setBullsAndCows() {
        String gameSequence = gameGamer.getGame().getSequence();
        for (int i = 0; i < 4; i++) {
            if (gameSequence.charAt(i) == sequence.charAt(i)) {
                bulls++;
            } else if (gameSequence.contains(String.valueOf(sequence.charAt(i)))) {
                cows++;
            }
        }
    }

    private void checkIfWinner() {
        if (bulls == 4) {
            gameGamer.setWinnerGame();
            gameGamer.getGame().setGameIsFinished();
        }
    }

    public int getBulls() {
        return bulls;
    }

    public int getCows() {
        return cows;
    }

}