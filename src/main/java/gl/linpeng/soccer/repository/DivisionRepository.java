package gl.linpeng.soccer.repository;

import gl.linpeng.soccer.domain.Division;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Division entity.
 */
@SuppressWarnings("unused")
public interface DivisionRepository extends JpaRepository<Division,Long> {

}
