package gl.linpeng.soccer.repository;

import gl.linpeng.soccer.domain.Player;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Player entity.
 */
@SuppressWarnings("unused")
public interface PlayerRepository extends JpaRepository<Player, Long>,
		JpaSpecificationExecutor<Player> {

}
