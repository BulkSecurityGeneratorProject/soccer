package gl.linpeng.soccer.repository.ext;

import gl.linpeng.soccer.domain.Game;
import gl.linpeng.soccer.repository.GameRepository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

/**
 * Game repository ext from Spring Data JPA repository
 * 
 * @author linpeng
 */
public interface GameRepositoryExt extends GameRepository {
	/**
	 * Get all games by team,either home or road team
	 * 
	 * @param id
	 *            team id
	 * @param recent
	 *            recent count
	 * @return
	 */
	@Query("select g from Game g where (g.homeTeam.id = ?#{[0]} or g.roadTeam.id=?#{[0]}) and abs(datediff(day,now(),g.startAt)) <= ?#{[1]} order by g.startAt ASC")
	List<Game> findGamesByTeam(Long id, int recent);
	
	/**
	 * Get all games by club
	 * 
	 * @param id
	 *            club id
	 * @param recent
	 *            recent count
	 * @return
	 */
	@Query("select g from Game g where (g.homeTeam.club.id = ?#{[0]} or g.roadTeam.club.id=?#{[0]}) and abs(datediff(day,now(),g.startAt)) <= ?#{[1]} order by g.startAt ASC")
	List<Game> findGamesByClub(Long id, int recent);
}
