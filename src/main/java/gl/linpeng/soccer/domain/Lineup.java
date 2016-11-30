package gl.linpeng.soccer.domain;

import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * <Enter note text here>                                                      
 * 
 */
@ApiModel(description = ""
    + "<Enter note text here>                                                 "
    + "")
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

    @ManyToOne
    private Dict playerPosition;

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

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public DivisionEvent getDivisionEvent() {
        return divisionEvent;
    }

    public void setDivisionEvent(DivisionEvent divisionEvent) {
        this.divisionEvent = divisionEvent;
    }

    public Dict getPlayerPosition() {
        return playerPosition;
    }

    public void setPlayerPosition(Dict dict) {
        this.playerPosition = dict;
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
