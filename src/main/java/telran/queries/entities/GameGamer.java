package telran.queries.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "game_gamer")
public class GameGamer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @ManyToOne
    @JoinColumn(name = "gamer_id")
    private Gamer gamer;
    
    @Column(name = "is_winner")
    private boolean isWinner;

    @Override
    public String toString() {
        return "GameGamer [id=" + id + ", game=" + game.getId() + ", gamer=" + gamer.username + ", isWinner=" + isWinner
                + "]";
    }

    public GameGamer() {

    }

    public GameGamer(Game game, Gamer gamer) {
        this.game = game;
        this.gamer = gamer;
        this.isWinner = false;
    }

    public void setWinnerGame() {
        isWinner = true;
    }

    public Game getGame() {
        return game;
    }

    public long getId() {
        return id;
    }

}