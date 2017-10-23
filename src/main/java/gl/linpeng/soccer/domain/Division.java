package gl.linpeng.soccer.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Division.
 */
@Entity
@Table(name = "division")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Division implements Serializable {

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
    private Dict type;

    @ManyToOne
    private Association association;

    @ManyToOne
    private RankingRule rankingRule;

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

    public Division name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getCreateAt() {
        return createAt;
    }

    public Division createAt(LocalDate createAt) {
        this.createAt = createAt;
        return this;
    }

    public void setCreateAt(LocalDate createAt) {
        this.createAt = createAt;
    }

    public String getPicture() {
        return picture;
    }

    public Division picture(String picture) {
        this.picture = picture;
        return this;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Dict getType() {
        return type;
    }

    public Division type(Dict dict) {
        this.type = dict;
        return this;
    }

    public void setType(Dict dict) {
        this.type = dict;
    }

    public Association getAssociation() {
        return association;
    }

    public Division association(Association association) {
        this.association = association;
        return this;
    }

    public void setAssociation(Association association) {
        this.association = association;
    }

    public RankingRule getRankingRule() {
        return rankingRule;
    }

    public Division rankingRule(RankingRule rankingRule) {
        this.rankingRule = rankingRule;
        return this;
    }

    public void setRankingRule(RankingRule rankingRule) {
        this.rankingRule = rankingRule;
    }

    public Dict getStatus() {
        return status;
    }

    public Division status(Dict dict) {
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
        Division division = (Division) o;
        if(division.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, division.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Division{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", createAt='" + createAt + "'" +
            ", picture='" + picture + "'" +
            '}';
    }
}
