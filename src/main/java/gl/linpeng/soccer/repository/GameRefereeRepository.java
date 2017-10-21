package gl.linpeng.soccer.repository;

import gl.linpeng.soccer.domain.GameReferee;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the GameReferee entity.
 */
@SuppressWarnings("unused")
public interface GameRefereeRepository extends JpaRepository<GameReferee,Long> {

}
