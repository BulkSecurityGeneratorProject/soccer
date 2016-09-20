package gl.linpeng.soccer.repository;

import gl.linpeng.soccer.domain.SquadPlayer;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the SquadPlayer entity.
 */
@SuppressWarnings("unused")
public interface SquadPlayerRepository extends JpaRepository<SquadPlayer,Long> {

}
