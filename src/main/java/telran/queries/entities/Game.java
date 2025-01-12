package telran.queries.entities;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "game")
public class Game {

    @Id
    @GeneratedValue
    long id;

    @Column(name = "date_time")
    LocalDateTime dateTime;

    @Column(name = "is_finished")
    boolean isFinished;
    String sequence;

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", isFinished=" + isFinished +
                ", sequence='" + sequence + '\'' +
                '}';
    }
}
