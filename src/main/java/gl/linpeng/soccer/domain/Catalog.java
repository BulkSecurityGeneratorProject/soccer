package gl.linpeng.soccer.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Catalog.
 */
@Entity
@Table(name = "catalog")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Catalog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "type")
    private Integer type;

    @Column(name = "open_comment")
    private Boolean openComment;

    @Column(name = "status")
    private Integer status;

    @ManyToOne
    private Catalog parent;

    @ManyToOne
    private Association association;

    @ManyToOne
    private Club club;

    @ManyToMany(mappedBy = "catalogs")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Article> articles = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Catalog title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getType() {
        return type;
    }

    public Catalog type(Integer type) {
        this.type = type;
        return this;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Boolean isOpenComment() {
        return openComment;
    }

    public Catalog openComment(Boolean openComment) {
        this.openComment = openComment;
        return this;
    }

    public void setOpenComment(Boolean openComment) {
        this.openComment = openComment;
    }

    public Integer getStatus() {
        return status;
    }

    public Catalog status(Integer status) {
        this.status = status;
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Catalog getParent() {
        return parent;
    }

    public Catalog parent(Catalog catalog) {
        this.parent = catalog;
        return this;
    }

    public void setParent(Catalog catalog) {
        this.parent = catalog;
    }

    public Association getAssociation() {
        return association;
    }

    public Catalog association(Association association) {
        this.association = association;
        return this;
    }

    public void setAssociation(Association association) {
        this.association = association;
    }

    public Club getClub() {
        return club;
    }

    public Catalog club(Club club) {
        this.club = club;
        return this;
    }

    public void setClub(Club club) {
        this.club = club;
    }

    public Set<Article> getArticles() {
        return articles;
    }

    public Catalog articles(Set<Article> articles) {
        this.articles = articles;
        return this;
    }

    public Catalog addArticle(Article article) {
        articles.add(article);
        article.getCatalogs().add(this);
        return this;
    }

    public Catalog removeArticle(Article article) {
        articles.remove(article);
        article.getCatalogs().remove(this);
        return this;
    }

    public void setArticles(Set<Article> articles) {
        this.articles = articles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Catalog catalog = (Catalog) o;
        if(catalog.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, catalog.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Catalog{" +
            "id=" + id +
            ", title='" + title + "'" +
            ", type='" + type + "'" +
            ", openComment='" + openComment + "'" +
            ", status='" + status + "'" +
            '}';
    }
}
