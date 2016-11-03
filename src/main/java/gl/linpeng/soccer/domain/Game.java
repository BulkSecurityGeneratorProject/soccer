package gl.linpeng.soccer.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Game.
 */
@Entity
@Table(name = "game")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Game implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "start_at")
    private LocalDate startAt;

    @Column(name = "home_score")
    private Integer homeScore;

    @Column(name = "road_score")
    private Integer roadScore;

    @Column(name = "note")
    private String note;

    @Column(name = "home_score_half")
    private Integer homeScoreHalf;

    @Column(name = "road_score_half")
    private Integer roadScoreHalf;

    @ManyToOne
    private Timeslot timeslot;

    @ManyToOne
    private Venue venue;

    @ManyToOne
    private Dict status;

    @ManyToOne
    private Team homeTeam;

    @ManyToOne
    private Team roadTeam;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartAt() {
        return startAt;
    }

    public void setStartAt(LocalDate startAt) {
        this.startAt = startAt;
    }

    public Integer getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(Integer homeScore) {
        this.homeScore = homeScore;
    }

    public Integer getRoadScore() {
        return roadScore;
    }

    public void setRoadScore(Integer roadScore) {
        this.roadScore = roadScore;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getHomeScoreHalf() {
        return homeScoreHalf;
    }

    public void setHomeScoreHalf(Integer homeScoreHalf) {
        this.homeScoreHalf = homeScoreHalf;
    }

    public Integer getRoadScoreHalf() {
        return roadScoreHalf;
    }

    public void setRoadScoreHalf(Integer roadScoreHalf) {
        this.roadScoreHalf = roadScoreHalf;
    }

    public Timeslot getTimeslot() {
        return timeslot;
    }

    public void setTimeslot(Timeslot timeslot) {
        this.timeslot = timeslot;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public Dict getStatus() {
        return status;
    }

    public void setStatus(Dict dict) {
        this.status = dict;
    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(Team team) {
        this.homeTeam = team;
    }

    public Team getRoadTeam() {
        return roadTeam;
    }

    public void setRoadTeam(Team team) {
        this.roadTeam = team;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Game game = (Game) o;
        if(game.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, game.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Game{" +
            "id=" + id +
            ", startAt='" + startAt + "'" +
            ", homeScore='" + homeScore + "'" +
            ", roadScore='" + roadScore + "'" +
            ", note='" + note + "'" +
            ", homeScoreHalf='" + homeScoreHalf + "'" +
            ", roadScoreHalf='" + roadScoreHalf + "'" +
            ","+homeTeam.toString()+","+roadTeam.toString()+
            ","+timeslot.toString()+
            '}';
    }
}
