package gl.linpeng.soccer.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A GameReferee.
 */
@Entity
@Table(name = "game_referee")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class GameReferee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Game game;

    @ManyToOne
    private Referee referee;

    @ManyToOne
    private Dict type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Game getGame() {
        return game;
    }

    public GameReferee game(Game game) {
        this.game = game;
        return this;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Referee getReferee() {
        return referee;
    }

    public GameReferee referee(Referee referee) {
        this.referee = referee;
        return this;
    }

    public void setReferee(Referee referee) {
        this.referee = referee;
    }

    public Dict getType() {
        return type;
    }

    public GameReferee type(Dict dict) {
        this.type = dict;
        return this;
    }

    public void setType(Dict dict) {
        this.type = dict;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GameReferee gameReferee = (GameReferee) o;
        if(gameReferee.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, gameReferee.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "GameReferee{" +
            "id=" + id +
            '}';
    }
}
