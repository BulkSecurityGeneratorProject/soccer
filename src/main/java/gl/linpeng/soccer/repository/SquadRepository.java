package gl.linpeng.soccer.repository;

import gl.linpeng.soccer.domain.Squad;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Squad entity.
 */
@SuppressWarnings("unused")
public interface SquadRepository extends JpaRepository<Squad,Long> {

}
