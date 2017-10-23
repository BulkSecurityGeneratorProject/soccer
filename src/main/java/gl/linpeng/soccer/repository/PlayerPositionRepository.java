package gl.linpeng.soccer.repository;

import gl.linpeng.soccer.domain.PlayerPosition;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the PlayerPosition entity.
 */
@SuppressWarnings("unused")
public interface PlayerPositionRepository extends JpaRepository<PlayerPosition,Long> {

}
