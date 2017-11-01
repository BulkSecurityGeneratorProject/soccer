package gl.linpeng.soccer.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Player.
 */
@Entity
@Table(name = "player")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Player implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "birth")
    private LocalDate birth;

    @Column(name = "height")
    private Integer height;

    @Column(name = "weight")
    private Integer weight;

    @Column(name = "picture")
    private String picture;

    @ManyToOne
    private Team team;

    @ManyToOne
    private Dict status;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "player_positions",
               joinColumns = @JoinColumn(name="players_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="positions_id", referencedColumnName="ID"))
    private Set<Dict> positions = new HashSet<>();

    @ManyToOne
    private Association association;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Player name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirth() {
        return birth;
    }

    public Player birth(LocalDate birth) {
        this.birth = birth;
        return this;
    }

    public void setBirth(LocalDate birth) {
        this.birth = birth;
    }

    public Integer getHeight() {
        return height;
    }

    public Player height(Integer height) {
        this.height = height;
        return this;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWeight() {
        return weight;
    }

    public Player weight(Integer weight) {
        this.weight = weight;
        return this;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getPicture() {
        return picture;
    }

    public Player picture(String picture) {
        this.picture = picture;
        return this;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Team getTeam() {
        return team;
    }

    public Player team(Team team) {
        this.team = team;
        return this;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Dict getStatus() {
        return status;
    }

    public Player status(Dict dict) {
        this.status = dict;
        return this;
    }

    public void setStatus(Dict dict) {
        this.status = dict;
    }

    public Set<Dict> getPositions() {
        return positions;
    }

    public Player positions(Set<Dict> dicts) {
        this.positions = dicts;
        return this;
    }

    public Player addPositions(Dict dict) {
        positions.add(dict);
        dict.getPlayers().add(this);
        return this;
    }

    public Player removePositions(Dict dict) {
        positions.remove(dict);
        dict.getPlayers().remove(this);
        return this;
    }

    public void setPositions(Set<Dict> dicts) {
        this.positions = dicts;
    }

    public Association getAssociation() {
        return association;
    }

    public Player association(Association association) {
        this.association = association;
        return this;
    }

    public void setAssociation(Association association) {
        this.association = association;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Player player = (Player) o;
        if(player.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, player.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Player{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", birth='" + birth + "'" +
            ", height='" + height + "'" +
            ", weight='" + weight + "'" +
            ", picture='" + picture + "'" +
            '}';
    }
}
