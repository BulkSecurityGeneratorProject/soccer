package gl.linpeng.soccer.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Association.
 */
@Entity
@Table(name = "association")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Association implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "create_at")
    private LocalDate createAt;

    @Column(name = "picture")
    private String picture;

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

    public Association name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getCreateAt() {
        return createAt;
    }

    public Association createAt(LocalDate createAt) {
        this.createAt = createAt;
        return this;
    }

    public void setCreateAt(LocalDate createAt) {
        this.createAt = createAt;
    }

    public String getPicture() {
        return picture;
    }

    public Association picture(String picture) {
        this.picture = picture;
        return this;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Dict getStatus() {
        return status;
    }

    public Association status(Dict dict) {
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
        Association association = (Association) o;
        if(association.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, association.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Association{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", createAt='" + createAt + "'" +
            ", picture='" + picture + "'" +
            '}';
    }
}
