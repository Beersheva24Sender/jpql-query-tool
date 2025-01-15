package telran.queries.entities;

import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@Table(name = "gamer")
public class Gamer {
    @Id
    String username;
    LocalDate birthdate;

    public Gamer() {
    }

    public Gamer(String username2, LocalDate birthdate2) {
        this.username = username2;
        this.birthdate = birthdate2;
    }

    @Override
    public String toString() {
        return "Gamer [username=" + username + ", birthdate=" + birthdate + "]";
    }

    public String getUsername() {
        return username;
    }
}