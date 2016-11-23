package gl.linpeng.soccer.repository;

import gl.linpeng.soccer.domain.Game;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Spring Data JPA repository for the Game entity.
 */
@SuppressWarnings("unused")
public interface GameRepository extends JpaRepository<Game, Long> {

	/**
	 * Get all games by team,either home or road team
	 * 
	 * @param id
	 *            team id
	 * @param recent
	 *            recent count
	 * @return
	 */
	@Query("select g from Game g where (g.homeTeam.id = ?#{[0]} or g.roadTeam.id=?#{[0]}) and abs(datediff(day,now(),g.startAt)) <= ?#{[1]}")
	List<Game> findGamesByTeam(Long id, int recent);
}
