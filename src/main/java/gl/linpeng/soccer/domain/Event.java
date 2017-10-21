package gl.linpeng.soccer.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Event.
 */
@Entity
@Table(name = "event")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Event implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "event_time")
    private LocalDate eventTime;

    @Column(name = "event_type")
    private Integer eventType;

    @Column(name = "event_sub_type")
    private Integer eventSubType;

    @Column(name = "amount", precision=10, scale=2)
    private BigDecimal amount;

    @ManyToOne
    private Association association;

    @ManyToOne
    private DivisionEvent divisionEvent;

    @ManyToOne
    private Game game;

    @ManyToOne
    private Club club;

    @ManyToOne
    private Team team;

    @ManyToOne
    private Club targetClub;

    @ManyToOne
    private Player player;

    @ManyToOne
    private Player targetPlayer;

    @ManyToOne
    private Referee referee;

    @ManyToOne
    private Coach coach;

    @ManyToOne
    private Team targetTeam;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getEventTime() {
        return eventTime;
    }

    public Event eventTime(LocalDate eventTime) {
        this.eventTime = eventTime;
        return this;
    }

    public void setEventTime(LocalDate eventTime) {
        this.eventTime = eventTime;
    }

    public Integer getEventType() {
        return eventType;
    }

    public Event eventType(Integer eventType) {
        this.eventType = eventType;
        return this;
    }

    public void setEventType(Integer eventType) {
        this.eventType = eventType;
    }

    public Integer getEventSubType() {
        return eventSubType;
    }

    public Event eventSubType(Integer eventSubType) {
        this.eventSubType = eventSubType;
        return this;
    }

    public void setEventSubType(Integer eventSubType) {
        this.eventSubType = eventSubType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Event amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Association getAssociation() {
        return association;
    }

    public Event association(Association association) {
        this.association = association;
        return this;
    }

    public void setAssociation(Association association) {
        this.association = association;
    }

    public DivisionEvent getDivisionEvent() {
        return divisionEvent;
    }

    public Event divisionEvent(DivisionEvent divisionEvent) {
        this.divisionEvent = divisionEvent;
        return this;
    }

    public void setDivisionEvent(DivisionEvent divisionEvent) {
        this.divisionEvent = divisionEvent;
    }

    public Game getGame() {
        return game;
    }

    public Event game(Game game) {
        this.game = game;
        return this;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Club getClub() {
        return club;
    }

    public Event club(Club club) {
        this.club = club;
        return this;
    }

    public void setClub(Club club) {
        this.club = club;
    }

    public Team getTeam() {
        return team;
    }

    public Event team(Team team) {
        this.team = team;
        return this;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Club getTargetClub() {
        return targetClub;
    }

    public Event targetClub(Club club) {
        this.targetClub = club;
        return this;
    }

    public void setTargetClub(Club club) {
        this.targetClub = club;
    }

    public Player getPlayer() {
        return player;
    }

    public Event player(Player player) {
        this.player = player;
        return this;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getTargetPlayer() {
        return targetPlayer;
    }

    public Event targetPlayer(Player player) {
        this.targetPlayer = player;
        return this;
    }

    public void setTargetPlayer(Player player) {
        this.targetPlayer = player;
    }

    public Referee getReferee() {
        return referee;
    }

    public Event referee(Referee referee) {
        this.referee = referee;
        return this;
    }

    public void setReferee(Referee referee) {
        this.referee = referee;
    }

    public Coach getCoach() {
        return coach;
    }

    public Event coach(Coach coach) {
        this.coach = coach;
        return this;
    }

    public void setCoach(Coach coach) {
        this.coach = coach;
    }

    public Team getTargetTeam() {
        return targetTeam;
    }

    public Event targetTeam(Team team) {
        this.targetTeam = team;
        return this;
    }

    public void setTargetTeam(Team team) {
        this.targetTeam = team;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Event event = (Event) o;
        if(event.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, event.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Event{" +
            "id=" + id +
            ", eventTime='" + eventTime + "'" +
            ", eventType='" + eventType + "'" +
            ", eventSubType='" + eventSubType + "'" +
            ", amount='" + amount + "'" +
            '}';
    }
}
