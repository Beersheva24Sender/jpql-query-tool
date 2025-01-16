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
                + gameGamer.id + "]";
    }

    public void setGameGamer(GameGamer gameGamer2) {
        this.gameGamer = gameGamer2;
    }

    public void setBulls() {
    }

    private void setBullsAndCows() {
        String gameSequence = gameGamer.getGame().getSequence();

        if (gameSequence.length() != sequence.length()) {
            throw new IllegalArgumentException("Guess and target sequence must have the same length");
        }

        int[] gameSequenceCount = new int[10];
        int[] guessSequenceCount = new int[10];

        for (int i = 0; i < sequence.length(); i++) {
            if (gameSequence.charAt(i) == sequence.charAt(i)) {
                bulls++;
            } else {
                gameSequenceCount[gameSequence.charAt(i) - '0']++;
                guessSequenceCount[sequence.charAt(i) - '0']++;
            }
        }

        for (int i = 0; i < 10; i++) {
            cows += Math.min(gameSequenceCount[i], guessSequenceCount[i]);
        }
    }

    private void checkIfWinner() {
        if (bulls == 4) {
            gameGamer.setWinnerGame();
            gameGamer.getGame().setGameIsFinished();
        }
    }

}