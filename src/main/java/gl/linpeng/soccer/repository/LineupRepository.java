package gl.linpeng.soccer.repository;

import gl.linpeng.soccer.domain.Lineup;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Lineup entity.
 */
@SuppressWarnings("unused")
public interface LineupRepository extends JpaRepository<Lineup,Long> {

}
