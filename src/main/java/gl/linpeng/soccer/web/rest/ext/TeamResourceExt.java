package gl.linpeng.soccer.web.rest.ext;

import gl.linpeng.soccer.domain.Game;
import gl.linpeng.soccer.domain.Player;
import gl.linpeng.soccer.domain.Team;
import gl.linpeng.soccer.repository.GameRepository;
import gl.linpeng.soccer.repository.PlayerRepository;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

/**
 * REST ext controller for managing Team.
 * 
 * @author linpeng
 */
@RestController
@RequestMapping("/api")
public class TeamResourceExt {

	private final Logger log = LoggerFactory.getLogger(TeamResourceExt.class);

	@Inject
	private PlayerRepository playerRepository;
	@Inject
	private GameRepository gameRepository;
	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * GET /players : get all the players of team.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of players
	 *         in body
	 */
	@RequestMapping(value = "/teams/{id}/players", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public List<Player> getAllTeamPlayers(@PathVariable Long id) {
		log.debug("REST request to get all team Players");
		Player player = new Player();
		Team team = new Team();
		team.setId(id);
		player.setTeam(team);
		Example<Player> example = Example.of(player);
		List<Player> players = playerRepository.findAll(example);
		return players;
	}

	/**
	 * GET /games : get all the games of team.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of games in
	 *         body
	 */
	@RequestMapping(value = "/teams/{id}/games", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public List<Game> getAllTeamGames(@PathVariable Long id) {
		log.debug("REST request to get all Team {} Games", id);
		// here query recent 1 month game
		return gameRepository.findGamesByTeam(id, 31);
	}

	/**
	 * GET /division-events : get the team player statistics.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         statistics data in body
	 */
	@RequestMapping(value = "/teams/{id}/player-statistics", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public List<Player> getPlayerStatistics(@PathVariable Long id) {
		log.debug("REST request to get Player statistics data of team : {}", id);

		String sql = String
				.format("select p.id,p.name,p.birth,"
						+ "sum(CASE rf.name WHEN '进球' THEN rd.value END)  goal,"
						+ "sum(CASE rf.name WHEN '助攻' THEN rd.value END)  assist,"
						+ "sum(CASE rf.name WHEN '黄牌' THEN rd.value END)  yellow,"
						+ "sum(CASE rf.name WHEN '红牌' THEN rd.value END)  red"
						+ " from Team tm "
						+ "left outer join squad sq "
						+ "left outer join squad_player sp "
						+ "left outer join player p on p.id = sp.player_id "
						+ "left outer join Result_data rd "
						+ "left outer join result_field rf on rf.id = rd.result_field_id "
						+ "on rd.squad_player_id = sp.id "
						+ "on sp.squad_id = sq.id " + "on sq.team_id = tm.id"
						+ " where tm.id='" + id + "'"
						+ "group by p.id,p.name,p.birth order by p.name");
		Query query = entityManager.createNativeQuery(sql);
		return query.getResultList();
	}
}
