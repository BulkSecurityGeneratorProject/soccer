package gl.linpeng.soccer.repository;

import gl.linpeng.soccer.domain.Coach;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Coach entity.
 */
@SuppressWarnings("unused")
public interface CoachRepository extends JpaRepository<Coach,Long> {

}
