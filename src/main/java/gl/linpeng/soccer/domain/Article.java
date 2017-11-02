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
 * A Article.
 */
@Entity
@Table(name = "article")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "summary")
    private String summary;

    @Column(name = "content")
    private String content;

    @Column(name = "picture")
    private String picture;

    @Column(name = "author")
    private String author;

    @Column(name = "create_at")
    private LocalDate createAt;

    @Column(name = "type")
    private Integer type;

    @Column(name = "status")
    private Integer status;

    @Column(name = "open_comment")
    private Boolean openComment;

    @Column(name = "sort")
    private Integer sort;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "article_catalog",
               joinColumns = @JoinColumn(name="articles_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="catalogs_id", referencedColumnName="ID"))
    private Set<Catalog> catalogs = new HashSet<>();

    @ManyToOne
    private Association association;

    @ManyToOne
    private Club club;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "article_tag",
               joinColumns = @JoinColumn(name="articles_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="tags_id", referencedColumnName="ID"))
    private Set<Tag> tags = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSummary() {
        return summary;
    }

    public Article summary(String summary) {
        this.summary = summary;
        return this;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public Article content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPicture() {
        return picture;
    }

    public Article picture(String picture) {
        this.picture = picture;
        return this;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getAuthor() {
        return author;
    }

    public Article author(String author) {
        this.author = author;
        return this;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDate getCreateAt() {
        return createAt;
    }

    public Article createAt(LocalDate createAt) {
        this.createAt = createAt;
        return this;
    }

    public void setCreateAt(LocalDate createAt) {
        this.createAt = createAt;
    }

    public Integer getType() {
        return type;
    }

    public Article type(Integer type) {
        this.type = type;
        return this;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public Article status(Integer status) {
        this.status = status;
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Boolean isOpenComment() {
        return openComment;
    }

    public Article openComment(Boolean openComment) {
        this.openComment = openComment;
        return this;
    }

    public void setOpenComment(Boolean openComment) {
        this.openComment = openComment;
    }

    public Integer getSort() {
        return sort;
    }

    public Article sort(Integer sort) {
        this.sort = sort;
        return this;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Set<Catalog> getCatalogs() {
        return catalogs;
    }

    public Article catalogs(Set<Catalog> catalogs) {
        this.catalogs = catalogs;
        return this;
    }

    public Article addCatalog(Catalog catalog) {
        catalogs.add(catalog);
        catalog.getArticles().add(this);
        return this;
    }

    public Article removeCatalog(Catalog catalog) {
        catalogs.remove(catalog);
        catalog.getArticles().remove(this);
        return this;
    }

    public void setCatalogs(Set<Catalog> catalogs) {
        this.catalogs = catalogs;
    }

    public Association getAssociation() {
        return association;
    }

    public Article association(Association association) {
        this.association = association;
        return this;
    }

    public void setAssociation(Association association) {
        this.association = association;
    }

    public Club getClub() {
        return club;
    }

    public Article club(Club club) {
        this.club = club;
        return this;
    }

    public void setClub(Club club) {
        this.club = club;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public Article tags(Set<Tag> tags) {
        this.tags = tags;
        return this;
    }

    public Article addTag(Tag tag) {
        tags.add(tag);
        tag.getArticles().add(this);
        return this;
    }

    public Article removeTag(Tag tag) {
        tags.remove(tag);
        tag.getArticles().remove(this);
        return this;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Article article = (Article) o;
        if(article.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, article.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Article{" +
            "id=" + id +
            ", summary='" + summary + "'" +
            ", content='" + content + "'" +
            ", picture='" + picture + "'" +
            ", author='" + author + "'" +
            ", createAt='" + createAt + "'" +
            ", type='" + type + "'" +
            ", status='" + status + "'" +
            ", openComment='" + openComment + "'" +
            ", sort='" + sort + "'" +
            '}';
    }
}
