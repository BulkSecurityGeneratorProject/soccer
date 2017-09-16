package gl.linpeng.soccer.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import gl.linpeng.soccer.domain.util.SimpleJsonSerializer;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A SquadPlayer.
 */
@Entity
@Table(name = "squad_player")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SquadPlayer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "player_number")
    private Integer playerNumber;

    @Column(name = "is_substitute")
    private Boolean isSubstitute;

    @ManyToOne
    @JsonSerialize(using = SimpleJsonSerializer.class)
    private Squad squad;

    @ManyToOne
    @JsonSerialize(using = SimpleJsonSerializer.class)
    private Player player;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPlayerNumber() {
        return playerNumber;
    }

    public void setPlayerNumber(Integer playerNumber) {
        this.playerNumber = playerNumber;
    }

    public Boolean isIsSubstitute() {
        return isSubstitute;
    }

    public void setIsSubstitute(Boolean isSubstitute) {
        this.isSubstitute = isSubstitute;
    }

    public Squad getSquad() {
        return squad;
    }

    public void setSquad(Squad squad) {
        this.squad = squad;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SquadPlayer squadPlayer = (SquadPlayer) o;
        if(squadPlayer.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, squadPlayer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SquadPlayer{" +
            "id=" + id +
            ", playerNumber='" + playerNumber + "'" +
            ", isSubstitute='" + isSubstitute + "'" +
            '}';
    }
}
