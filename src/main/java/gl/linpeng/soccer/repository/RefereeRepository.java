package gl.linpeng.soccer.repository;

import gl.linpeng.soccer.domain.Referee;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Referee entity.
 */
@SuppressWarnings("unused")
public interface RefereeRepository extends JpaRepository<Referee,Long> {

}
