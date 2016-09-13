package gl.linpeng.soccer.repository;

import gl.linpeng.soccer.domain.Association;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Association entity.
 */
@SuppressWarnings("unused")
public interface AssociationRepository extends JpaRepository<Association,Long> {

}
