package gl.linpeng.soccer.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Coach.
 */
@Entity
@Table(name = "coach")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Coach implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "picture")
    private String picture;

    @ManyToOne
    private Dict type;

    @ManyToOne
    private Dict status;

    @ManyToOne
    private Association association;

    @ManyToOne
    private Team team;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Coach name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public Coach picture(String picture) {
        this.picture = picture;
        return this;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Dict getType() {
        return type;
    }

    public Coach type(Dict dict) {
        this.type = dict;
        return this;
    }

    public void setType(Dict dict) {
        this.type = dict;
    }

    public Dict getStatus() {
        return status;
    }

    public Coach status(Dict dict) {
        this.status = dict;
        return this;
    }

    public void setStatus(Dict dict) {
        this.status = dict;
    }

    public Association getAssociation() {
        return association;
    }

    public Coach association(Association association) {
        this.association = association;
        return this;
    }

    public void setAssociation(Association association) {
        this.association = association;
    }

    public Team getTeam() {
        return team;
    }

    public Coach team(Team team) {
        this.team = team;
        return this;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Coach coach = (Coach) o;
        if(coach.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, coach.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Coach{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", picture='" + picture + "'" +
            '}';
    }
}
