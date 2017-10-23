package gl.linpeng.soccer.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Club.
 */
@Entity
@Table(name = "club")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Club implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "create_at")
    private LocalDate createAt;

    @Column(name = "name")
    private String name;

    @Column(name = "short_name")
    private String shortName;

    @Column(name = "picture")
    private String picture;

    @ManyToOne
    private Association association;

    @ManyToOne
    private Venue venue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getCreateAt() {
        return createAt;
    }

    public Club createAt(LocalDate createAt) {
        this.createAt = createAt;
        return this;
    }

    public void setCreateAt(LocalDate createAt) {
        this.createAt = createAt;
    }

    public String getName() {
        return name;
    }

    public Club name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public Club shortName(String shortName) {
        this.shortName = shortName;
        return this;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getPicture() {
        return picture;
    }

    public Club picture(String picture) {
        this.picture = picture;
        return this;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Association getAssociation() {
        return association;
    }

    public Club association(Association association) {
        this.association = association;
        return this;
    }

    public void setAssociation(Association association) {
        this.association = association;
    }

    public Venue getVenue() {
        return venue;
    }

    public Club venue(Venue venue) {
        this.venue = venue;
        return this;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Club club = (Club) o;
        if(club.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, club.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Club{" +
            "id=" + id +
            ", createAt='" + createAt + "'" +
            ", name='" + name + "'" +
            ", shortName='" + shortName + "'" +
            ", picture='" + picture + "'" +
            '}';
    }
}
