package gl.linpeng.soccer.repository.ext;

import gl.linpeng.soccer.domain.Game;
import gl.linpeng.soccer.repository.GameRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

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
	 * Get all games by club 这将会查询俱乐部前后30天的所有比赛，即总共60天的比赛日程
	 *
	 * @param id
	 *            club id
	 * @param recent
	 *            recent count
	 * @return
	 */
	@Query("select g from Game g where (g.homeTeam.club.id = ?#{[0]} or g.roadTeam.club.id=?#{[0]}) and abs(datediff(day,now(),g.startAt)) <= ?#{[1]} order by g.startAt ASC")
	List<Game> findGamesByClub(Long id, int recent);

	/**
	 * 与findGamesByClub不同，这里只关心接下来的比赛
	 *
	 * @param id
	 *            俱乐部id
	 * @param page
	 *            接下来的几场比赛
	 * @return
	 */
	@Query("select g from Game g where (g.homeTeam.club.id = :clubId or g.roadTeam.club.id =  :clubId) and datediff(second,now(),g.startAt) > 0 ORDER BY g.startAt ASC ")
	List<Game> findNextGamesByClub(@Param("clubId") Long id, Pageable page);

	/**
	 * 与上面两个方法按俱乐部维度查不同，这里是按球队id查，而且是查已经结束的比赛
	 *
	 * @param id
	 *            team id
	 * @param pageable
	 * @return
	 */
	@Query("select g from Game g where (g.homeTeam.id = :teamId or g.roadTeam.id =  :teamId) and datediff(second,now(),g.startAt) < 0 ORDER BY g.startAt DESC ")
	List<Game> findPassedGamesByTeam(@Param("teamId") Long id, Pageable pageable);

    /**
     * Find Next games according association id
     * @param associationId which association
     * @param pageable page object
     * @return games
     */
    @Query("select g from Game g where (g.timeslot.divisionEvent.division.association.id = :associationId) and datediff(second,now(),g.startAt) > 0 ORDER BY g.startAt ASC ")
	List<Game> findNextGamesByAssociation(@Param("associationId") Long associationId,Pageable pageable);

    @Query("select g from Game g where (g.timeslot.divisionEvent.division.id = :divisionId) and datediff(second,now(),g.startAt) >0 ORDER BY g.startAt ASC")
    List<Game> findNextGamesByDivision(@Param("divisionId") Long divisionId,Pageable pageable);

}
