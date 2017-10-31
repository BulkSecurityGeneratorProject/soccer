package gl.linpeng.soccer.repository;

import gl.linpeng.soccer.domain.Article;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Article entity.
 */
@SuppressWarnings("unused")
public interface ArticleRepository extends JpaRepository<Article,Long> {

    @Query("select distinct article from Article article left join fetch article.catalogs left join fetch article.tags")
    List<Article> findAllWithEagerRelationships();

    @Query("select article from Article article left join fetch article.catalogs left join fetch article.tags where article.id =:id")
    Article findOneWithEagerRelationships(@Param("id") Long id);

}
