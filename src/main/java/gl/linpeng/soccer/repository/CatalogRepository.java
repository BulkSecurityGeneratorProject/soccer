package gl.linpeng.soccer.repository;

import gl.linpeng.soccer.domain.Catalog;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Catalog entity.
 */
@SuppressWarnings("unused")
public interface CatalogRepository extends JpaRepository<Catalog,Long> {

}
