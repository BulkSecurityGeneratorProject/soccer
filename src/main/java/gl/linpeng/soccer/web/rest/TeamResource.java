package gl.linpeng.soccer.web.rest;

import gl.linpeng.soccer.domain.Game;
import gl.linpeng.soccer.domain.Player;
import gl.linpeng.soccer.domain.Team;
import gl.linpeng.soccer.repository.GameRepository;
import gl.linpeng.soccer.repository.PlayerRepository;
import gl.linpeng.soccer.repository.TeamRepository;
import gl.linpeng.soccer.web.rest.util.HeaderUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

/**
 * REST controller for managing Team.
 */
@RestController
@RequestMapping("/api")
public class TeamResource {

	private final Logger log = LoggerFactory.getLogger(TeamResource.class);

	@Inject
	private TeamRepository teamRepository;
	@Inject
	private PlayerRepository playerRepository;
	@Inject
	private GameRepository gameRepository;
	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * POST /teams : Create a new team.
	 *
	 * @param team
	 *            the team to create
	 * @return the ResponseEntity with status 201 (Created) and with body the
	 *         new team, or with status 400 (Bad Request) if the team has
	 *         already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@RequestMapping(value = "/teams", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Team> createTeam(@RequestBody Team team)
			throws URISyntaxException {
		log.debug("REST request to save Team : {}", team);
		if (team.getId() != null) {
			return ResponseEntity
					.badRequest()
					.headers(
							HeaderUtil.createFailureAlert("team", "idexists",
									"A new team cannot already have an ID"))
					.body(null);
		}
		Team result = teamRepository.save(team);
		return ResponseEntity
				.created(new URI("/api/teams/" + result.getId()))
				.headers(
						HeaderUtil.createEntityCreationAlert("team", result
								.getId().toString())).body(result);
	}

	/**
	 * PUT /teams : Updates an existing team.
	 *
	 * @param team
	 *            the team to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         team, or with status 400 (Bad Request) if the team is not valid,
	 *         or with status 500 (Internal Server Error) if the team couldnt be
	 *         updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@RequestMapping(value = "/teams", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Team> updateTeam(@RequestBody Team team)
			throws URISyntaxException {
		log.debug("REST request to update Team : {}", team);
		if (team.getId() == null) {
			return createTeam(team);
		}
		Team result = teamRepository.save(team);
		return ResponseEntity
				.ok()
				.headers(
						HeaderUtil.createEntityUpdateAlert("team", team.getId()
								.toString())).body(result);
	}

	/**
	 * GET /teams : get all the teams.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of teams in
	 *         body
	 */
	@RequestMapping(value = "/teams", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public List<Team> getAllTeams() {
		log.debug("REST request to get all Teams");
		List<Team> teams = teamRepository.findAll();
		return teams;
	}

	/**
	 * GET /teams/:id : get the "id" team.
	 *
	 * @param id
	 *            the id of the team to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the team,
	 *         or with status 404 (Not Found)
	 */
	@RequestMapping(value = "/teams/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Team> getTeam(@PathVariable Long id) {
		log.debug("REST request to get Team : {}", id);
		Team team = teamRepository.findOne(id);
		return Optional.ofNullable(team)
				.map(result -> new ResponseEntity<>(result, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	/**
	 * DELETE /teams/:id : delete the "id" team.
	 *
	 * @param id
	 *            the id of the team to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@RequestMapping(value = "/teams/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Void> deleteTeam(@PathVariable Long id) {
		log.debug("REST request to delete Team : {}", id);
		teamRepository.delete(id);
		return ResponseEntity
				.ok()
				.headers(
						HeaderUtil.createEntityDeletionAlert("team",
								id.toString())).build();
	}

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
		Game example = new Game();
		Team exampleTeam = new Team();
		exampleTeam.setId(id);
		example.setHomeTeam(exampleTeam);
		// TODO ? How to query by 'OR' operation in Spring Data DSL
		// example.setRoadTeam(exampleTeam);
		List<Game> games = gameRepository.findAll(Example.of(example));
		return games;
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
