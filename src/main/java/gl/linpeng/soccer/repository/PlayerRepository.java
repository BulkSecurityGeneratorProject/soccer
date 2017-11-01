package gl.linpeng.soccer.repository;

import gl.linpeng.soccer.domain.Player;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Player entity.
 */
@SuppressWarnings("unused")
public interface PlayerRepository extends JpaRepository<Player,Long> {

    @Query("select distinct player from Player player left join fetch player.positions")
    List<Player> findAllWithEagerRelationships();

    @Query("select player from Player player left join fetch player.positions where player.id =:id")
    Player findOneWithEagerRelationships(@Param("id") Long id);

}
