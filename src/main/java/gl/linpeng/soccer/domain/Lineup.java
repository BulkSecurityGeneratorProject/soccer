package gl.linpeng.soccer.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Lineup.
 */
@Entity
@Table(name = "lineup")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Lineup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "player_number")
    private Integer playerNumber;

    @ManyToOne
    private Player player;

    @ManyToOne
    private Team team;

    @ManyToOne
    private DivisionEvent divisionEvent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPlayerNumber() {
        return playerNumber;
    }

    public Lineup playerNumber(Integer playerNumber) {
        this.playerNumber = playerNumber;
        return this;
    }

    public void setPlayerNumber(Integer playerNumber) {
        this.playerNumber = playerNumber;
    }

    public Player getPlayer() {
        return player;
    }

    public Lineup player(Player player) {
        this.player = player;
        return this;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Team getTeam() {
        return team;
    }

    public Lineup team(Team team) {
        this.team = team;
        return this;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public DivisionEvent getDivisionEvent() {
        return divisionEvent;
    }

    public Lineup divisionEvent(DivisionEvent divisionEvent) {
        this.divisionEvent = divisionEvent;
        return this;
    }

    public void setDivisionEvent(DivisionEvent divisionEvent) {
        this.divisionEvent = divisionEvent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Lineup lineup = (Lineup) o;
        if(lineup.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, lineup.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Lineup{" +
            "id=" + id +
            ", playerNumber='" + playerNumber + "'" +
            '}';
    }
}
