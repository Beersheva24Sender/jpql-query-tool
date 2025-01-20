
package telran.queries.entities;

import java.time.LocalDateTime;
import java.security.SecureRandom;

import jakarta.persistence.*;

@Entity
@Table(name = "game")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "date_time")
    private LocalDateTime dateTime;

    @Column(name = "is_finished")
    private boolean isFinished;

    @Column(name = "sequence")
    private String sequence;

    public Game() {
        this.dateTime = LocalDateTime.now();
        this.isFinished = false;
        this.sequence =  generateSequence();
    }

    public void setGameIsFinished() {
        isFinished = true;
    }

    public long getId() {
        return id;
    }

    public String getSequence() {
        return sequence;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean isFinished) {
        this.isFinished = isFinished;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String generateSequence() {
        SecureRandom secureRandom = new SecureRandom();
        return sequence = String.format("%04d", secureRandom.nextInt(9000) + 1000);
    }

    @Override
    public String toString() {
        return String.format("Game [id=%d, dateTime=%s, isFinished=%b, sequence=%s]", id, dateTime, isFinished,
                sequence);
    }
}