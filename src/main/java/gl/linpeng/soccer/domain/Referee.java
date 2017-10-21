package gl.linpeng.soccer.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Referee.
 */
@Entity
@Table(name = "referee")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Referee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "picture")
    private String picture;

    @ManyToOne
    private Association association;

    @ManyToOne
    private Dict type;

    @ManyToOne
    private Dict status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Referee name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public Referee picture(String picture) {
        this.picture = picture;
        return this;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Association getAssociation() {
        return association;
    }

    public Referee association(Association association) {
        this.association = association;
        return this;
    }

    public void setAssociation(Association association) {
        this.association = association;
    }

    public Dict getType() {
        return type;
    }

    public Referee type(Dict dict) {
        this.type = dict;
        return this;
    }

    public void setType(Dict dict) {
        this.type = dict;
    }

    public Dict getStatus() {
        return status;
    }

    public Referee status(Dict dict) {
        this.status = dict;
        return this;
    }

    public void setStatus(Dict dict) {
        this.status = dict;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Referee referee = (Referee) o;
        if(referee.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, referee.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Referee{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", picture='" + picture + "'" +
            '}';
    }
}
