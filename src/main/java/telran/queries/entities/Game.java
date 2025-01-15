
package telran.queries.entities;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "game")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @Column(name = "date_time")
    public LocalDateTime dateTime;
    @Column(name = "is_finished")
    public boolean isFinished;

    String sequence;

    public Game() {
    }

    public Game(String sequence) {
        this.sequence = sequence;
        this.isFinished = false;
    }

    public void setGameIsFinished() {
        isFinished = true;
    }

    @Override
    public String toString() {
        return "Game [id=" + id + ", dateTime=" + dateTime + ", isFinished=" + isFinished + ", sequence=" + sequence
                + "]";
    }

    public long getId() {
        return id;
    }

    public String getSequence() {
        return sequence;
    }

}