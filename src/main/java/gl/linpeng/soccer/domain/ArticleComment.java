package gl.linpeng.soccer.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A ArticleComment.
 */
@Entity
@Table(name = "article_comment")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ArticleComment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "author")
    private String author;

    @Column(name = "create_at")
    private LocalDate createAt;

    @Column(name = "content")
    private String content;

    @Column(name = "status")
    private Integer status;

    @ManyToOne
    private Article article;

    @ManyToOne
    private ArticleComment parent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public ArticleComment author(String author) {
        this.author = author;
        return this;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDate getCreateAt() {
        return createAt;
    }

    public ArticleComment createAt(LocalDate createAt) {
        this.createAt = createAt;
        return this;
    }

    public void setCreateAt(LocalDate createAt) {
        this.createAt = createAt;
    }

    public String getContent() {
        return content;
    }

    public ArticleComment content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getStatus() {
        return status;
    }

    public ArticleComment status(Integer status) {
        this.status = status;
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Article getArticle() {
        return article;
    }

    public ArticleComment article(Article article) {
        this.article = article;
        return this;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public ArticleComment getParent() {
        return parent;
    }

    public ArticleComment parent(ArticleComment articleComment) {
        this.parent = articleComment;
        return this;
    }

    public void setParent(ArticleComment articleComment) {
        this.parent = articleComment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ArticleComment articleComment = (ArticleComment) o;
        if(articleComment.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, articleComment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ArticleComment{" +
            "id=" + id +
            ", author='" + author + "'" +
            ", createAt='" + createAt + "'" +
            ", content='" + content + "'" +
            ", status='" + status + "'" +
            '}';
    }
}
