package kdp.fretquiz.game.model;

import kdp.fretquiz.user.User;

import java.util.Objects;

public class Player {
    private Long id;
    private String name;
    private int score;

    public Player(Long id, String name, int score) {
        this.id = id;
        this.name = name;
        this.score = score;
    }

    public static Player from(User user) {
        return new Player(user.id(), user.name(), 0);
    }

    public Player incrementScore() {
        score++;
        return this;
    }

    public Long id() {
        return id;
    }

    public String name() {
        return name;
    }

    public int score() {
        return score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return score == player.score && id.equals(player.id) && name.equals(player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, score);
    }

    @Override
    public String toString() {
        return "Player{" +
                       "id=" + id +
                       ", name='" + name + '\'' +
                       ", score=" + score +
                       '}';
    }
}
