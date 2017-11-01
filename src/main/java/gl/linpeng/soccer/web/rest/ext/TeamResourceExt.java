package gl.linpeng.soccer.web.rest.ext;

import gl.linpeng.soccer.domain.Club;
import gl.linpeng.soccer.domain.DivisionEvent;
import gl.linpeng.soccer.domain.Game;
import gl.linpeng.soccer.domain.Player;
import gl.linpeng.soccer.domain.Team;
import gl.linpeng.soccer.repository.PlayerRepository;
import gl.linpeng.soccer.repository.TeamRepository;
import gl.linpeng.soccer.repository.ext.GameRepositoryExt;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
	private GameRepositoryExt gameRepositoryExt;
	@Inject
	private TeamRepository teamRepository;
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
		return gameRepositoryExt.findGamesByTeam(id, 31);
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
						+ " from Player p "
						+ "left outer join Team tm "
						+ "left outer join squad sq "
						+ "left outer join squad_player sp "
						+ "left outer join Result_data rd "
						+ "left outer join result_field rf on rf.id = rd.result_field_id "
						+ "on rd.squad_player_id = sp.id "
						+ "on sp.squad_id = sq.id " + "on sq.team_id = tm.id"
						+" on p.team_id = tm.id"
						+ " where p.id = sp.player_id and p.team_id='" + id + "'"
						+ "group by p.id,p.name,p.birth order by p.name");
		Query query = entityManager.createNativeQuery(sql);
		return query.getResultList();
	}

	/**
	 * GET /teams/:id/passedgames/{count} : get passwd game of team "id".
	 *
	 * @param id
	 *            the id of the team
	 * @return the passed game list
	 */
	@RequestMapping(value = "/teams/{id}/passedgames/{count}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public List<Game> getPassedGames(@PathVariable Long id,
			@PathVariable int count) {
		log.debug("REST request to get passed {} games of team: {}", count, id);
		List<Game> games = gameRepositoryExt.findPassedGamesByTeam(id,
				new PageRequest(0, count));
		return games;
	}

	/**
	 * GET /teams/:id/result-statistics : get total season game result
	 * statistics data
	 * 
	 * @param id
	 *            team id
	 * @return statistics result
	 */
	@RequestMapping(value = "/teams/{id}/result-statistics", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public List gameResultStatistics(@PathVariable Long id) {
		String sql = String
				.format("select TYPE,count(W) as W, count(D) as D,count(L) as L,sum(GS) as GS,"
						+ "sum(GA) as GA,(count(W)*3+count(D)) as PTS,count(NGS) as NGS,count(NGA) as NGA from "
						+ "(select 'HOME' as TYPE,CONCAT(g.home_score,':',g.road_score) as SCORE,"
						+ "(case when g.home_score > g.road_score then 1 end) as W ,"
						+ "(case when g.home_score = g.road_score then 1 end) as D,"
						+ "(case when g.home_score <  g.road_score then 1 end) as L,"
						+ "g.home_score as GS,g.road_score as GA,"
						+ "(case when g.home_score = 0 then 1 end) as NGS,"
						+ "(case when g.road_score = 0 then 1 end) as NGA ,"
						+ "(g.home_score - g.road_score) as SD from game g where g.home_team_id = "
						+ id
						+ "union all "
						+ "select 'ROAD' as TYPE,CONCAT(g.home_score,':',g.road_score) as SCORE"
						+ ",(case when g.home_score < g.road_score then 1 end) as W ,"
						+ "(case when g.home_score = g.road_score then 1 end) as D,"
						+ "(case when g.home_score >  g.road_score then 1 end) as L,"
						+ "g.road_score as GS,g.home_score as GA,"
						+ "(case when g.road_score = 0 then 1 end) as NGS,"
						+ "(case when g.home_score = 0 then 1 end) as NGA,"
						+ "(g.road_score - g.home_score) as SD from game g where g.road_team_id =  "
						+ id + ") group by TYPE", id);
		Query query = entityManager.createNativeQuery(sql);
		return query.getResultList();
	}

	/**
	 * GET /teams/club/{club_id}/division-event/{division_id} : get the team by
	 * club and division event.
	 *
	 * @param clubId
	 *            the id of the club
	 * @param divisionEventId
	 *            the id of the division event
	 * @return the ResponseEntity with status 200 (OK) and with body the team,
	 *         or with status 404 (Not Found)
	 */
	@RequestMapping(value = "/teams/{clubId}/division-event/{divisionEventId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Team> getTeamByClubAndDivisionEvent(
			@PathVariable Long clubId, @PathVariable Long divisionEventId) {
		log.debug(
				"REST request to get Team by club {} and division event : {}",
				clubId, divisionEventId);
		Team exampleTeam = new Team();
		Club club = new Club();
		DivisionEvent divisionEvent = new DivisionEvent();
		club.setId(clubId);
		divisionEvent.setId(divisionEventId);
		exampleTeam.setClub(club);
		exampleTeam.setDivisionEvent(divisionEvent);
		Team team = teamRepository.findOne(Example.of(exampleTeam));
		return Optional.ofNullable(team)
				.map(result -> new ResponseEntity<>(result, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
}
