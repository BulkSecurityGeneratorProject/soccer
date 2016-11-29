package gl.linpeng.soccer.web.rest;

import gl.linpeng.soccer.domain.DivisionEvent;
import gl.linpeng.soccer.domain.Game;
import gl.linpeng.soccer.domain.Team;
import gl.linpeng.soccer.domain.Timeslot;
import gl.linpeng.soccer.repository.DivisionEventRepository;
import gl.linpeng.soccer.repository.GameRepository;
import gl.linpeng.soccer.repository.TeamRepository;
import gl.linpeng.soccer.repository.TimeslotRepository;
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
import org.springframework.data.domain.Sort;
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
 * REST controller for managing DivisionEvent.
 */
@RestController
@RequestMapping("/api")
public class DivisionEventResource {

	private final Logger log = LoggerFactory
			.getLogger(DivisionEventResource.class);

	@Inject
	private DivisionEventRepository divisionEventRepository;
	@Inject
	private GameRepository gameRepository;
	@Inject
	private TeamRepository teamRepository;
	@Inject
	private TimeslotRepository timeslotRepository;
	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * POST /division-events : Create a new divisionEvent.
	 *
	 * @param divisionEvent
	 *            the divisionEvent to create
	 * @return the ResponseEntity with status 201 (Created) and with body the
	 *         new divisionEvent, or with status 400 (Bad Request) if the
	 *         divisionEvent has already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@RequestMapping(value = "/division-events", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<DivisionEvent> createDivisionEvent(
			@RequestBody DivisionEvent divisionEvent) throws URISyntaxException {
		log.debug("REST request to save DivisionEvent : {}", divisionEvent);
		if (divisionEvent.getId() != null) {
			return ResponseEntity
					.badRequest()
					.headers(
							HeaderUtil
									.createFailureAlert("divisionEvent",
											"idexists",
											"A new divisionEvent cannot already have an ID"))
					.body(null);
		}
		DivisionEvent result = divisionEventRepository.save(divisionEvent);
		return ResponseEntity
				.created(new URI("/api/division-events/" + result.getId()))
				.headers(
						HeaderUtil.createEntityCreationAlert("divisionEvent",
								result.getId().toString())).body(result);
	}

	/**
	 * PUT /division-events : Updates an existing divisionEvent.
	 *
	 * @param divisionEvent
	 *            the divisionEvent to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         divisionEvent, or with status 400 (Bad Request) if the
	 *         divisionEvent is not valid, or with status 500 (Internal Server
	 *         Error) if the divisionEvent couldnt be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@RequestMapping(value = "/division-events", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<DivisionEvent> updateDivisionEvent(
			@RequestBody DivisionEvent divisionEvent) throws URISyntaxException {
		log.debug("REST request to update DivisionEvent : {}", divisionEvent);
		if (divisionEvent.getId() == null) {
			return createDivisionEvent(divisionEvent);
		}
		DivisionEvent result = divisionEventRepository.save(divisionEvent);
		return ResponseEntity
				.ok()
				.headers(
						HeaderUtil.createEntityUpdateAlert("divisionEvent",
								divisionEvent.getId().toString())).body(result);
	}

	/**
	 * GET /division-events : get all the divisionEvents.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         divisionEvents in body
	 */
	@RequestMapping(value = "/division-events", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public List<DivisionEvent> getAllDivisionEvents() {
		log.debug("REST request to get all DivisionEvents");
		List<DivisionEvent> divisionEvents = divisionEventRepository.findAll();
		return divisionEvents;
	}

	/**
	 * GET /division-events/:id : get the "id" divisionEvent.
	 *
	 * @param id
	 *            the id of the divisionEvent to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         divisionEvent, or with status 404 (Not Found)
	 */
	@RequestMapping(value = "/division-events/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<DivisionEvent> getDivisionEvent(@PathVariable Long id) {
		log.debug("REST request to get DivisionEvent : {}", id);
		DivisionEvent divisionEvent = divisionEventRepository.findOne(id);
		return Optional.ofNullable(divisionEvent)
				.map(result -> new ResponseEntity<>(result, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	/**
	 * DELETE /division-events/:id : delete the "id" divisionEvent.
	 *
	 * @param id
	 *            the id of the divisionEvent to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@RequestMapping(value = "/division-events/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Void> deleteDivisionEvent(@PathVariable Long id) {
		log.debug("REST request to delete DivisionEvent : {}", id);
		divisionEventRepository.delete(id);
		return ResponseEntity
				.ok()
				.headers(
						HeaderUtil.createEntityDeletionAlert("divisionEvent",
								id.toString())).build();
	}

	/**
	 * GET /division-events : get the divisionEvents table.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         divisionEvents in body
	 */
	@RequestMapping(value = "/division-event/{id}/table", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public List<Team> getDivisionEventTable(@PathVariable Long id) {
		log.debug("REST request to get table of DivisionEvents : {}", id);

		String sql = String
				.format("select id,name,club_id,count(id) as GP,count(WIN) as W"
						+ ",count(LOSS) as L,count(DRAW) as D,sum(GS) as GS"
						+ ",sum(GA) as GA,sum(GS)- sum(GA) as GD,(count(WIN)*3+count(DRAW)*1) AS PTS "
						+ "from "
						+ "(select t.id,t.name,c.id as club_id"
						+ ",case when g.home_score > g.road_score then '1' end as WIN"
						+ ",case when g.home_score < g.road_score then '1' end as LOSS"
						+ ",case when g.home_score = g.road_score  then '1' end AS DRAW"
						+ ",g.home_score as GS,g.road_score as GA "
						+ "from team t LEFT OUTER JOIN club c on c.id = t.club_id,game g where t.division_event_id= "
						+ id
						+ " and g.home_team_id=t.id "
						+ "union all "
						+ "select t.id,t.name,c.id as club_id"
						+ ",case when g2.home_score < g2.road_score then '1' end as WIN"
						+ ",case when g2.home_score > g2.road_score then '1' end as LOSS"
						+ ",case when g2.home_score = g2.road_score  then '1' end AS DRAW"
						+ ",g2.road_score as GS,g2.home_score as GA "
						+ "from team t LEFT OUTER JOIN club c on c.id = t.club_id,game g2 where t.division_event_id= "
						+ id
						+ "and g2.road_team_id=t.id) "
						+ "group by name order by PTS DESC,GD DESC");
		Query query = entityManager.createNativeQuery(sql);
		return query.getResultList();
	}

	/**
	 * GET /division-events : get the divisionEvents goal-ranking.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         divisionEvents in body
	 */
	@RequestMapping(value = "/division-event/{id}/goal-ranking", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public List<Team> getDivisionEventGoalRanking(@PathVariable Long id) {
		log.debug("REST request to get Goal-Ranking of DivisionEvents : {}", id);

		String sql = String
				.format("SELECT "
						+ "p.id,p.name,tm.name as team_name,tm.id as team_id,SUM(rd.value) AS goal FROM "
						+ "RESULT_DATA rd,SQUAD_PLAYER sp "
						+ "left outer join squad sq "
						+ "left outer join team tm on tm.id = sq.team_id "
						+" left outer join game g left outer join timeslot t "
						+ "left outer join division_event de on de.id = t.division_event_id "
						+ "on t.id = g.timeslot_id on g.id = sq.game_id "
						+ "on sq.id = sp.squad_id,PLAYER p "
						+ "WHERE sp.id = rd.squad_player_id "
						+ "AND sp.player_id = p.id "
						+ "AND rd.result_field_id=3 AND de.id="+id 
						+ "GROUP BY p.name "
						+ "ORDER BY goal DESC");
		Query query = entityManager.createNativeQuery(sql);
		return query.getResultList();
	}

	/**
	 * GET /division-events : get the divisionEvents assist-ranking.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         divisionEvents in body
	 */
	@RequestMapping(value = "/division-event/{id}/assist-ranking", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public List<Team> getDivisionEventAssistRanking(@PathVariable Long id) {
		log.debug("REST request to get Assist-Ranking of DivisionEvents : {}",
				id);

		String sql = String
				.format("SELECT "
						+ "p.id,p.name,tm.name as team_name,tm.id as team_id,SUM(rd.value) AS assist FROM "
						+ "RESULT_DATA rd,SQUAD_PLAYER sp "
						+ "left outer join squad sq "
						+ "left outer join team tm on tm.id = sq.team_id "
						+" left outer join game g left outer join timeslot t "
						+ "left outer join division_event de on de.id = t.division_event_id "
						+ "on t.id = g.timeslot_id on g.id = sq.game_id "
						+ "on sq.id = sp.squad_id,PLAYER p "
						+ "WHERE sp.id = rd.squad_player_id "
						+ "AND sp.player_id = p.id "
						+ "AND rd.result_field_id=4 AND de.id="+id 
						+ "GROUP BY p.name "
						+ "ORDER BY assist DESC");
		Query query = entityManager.createNativeQuery(sql);
		return query.getResultList();
	}

	/**
	 * GET /division-events : get all the divisionEvent games.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         divisionEvent games in body
	 */
	@RequestMapping(value = "/division-events/{id}/games", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public List<Game> getAllDivisionEventGames(@PathVariable Long id) {
		log.debug("REST request to get all DivisionEvent games,{}", id);
		Game example = new Game();
		Timeslot exampleTimeslot = new Timeslot();
		DivisionEvent exampleDivisionEvent = new DivisionEvent();
		exampleDivisionEvent.setId(id);
		exampleTimeslot.setDivisionEvent(exampleDivisionEvent);
		example.setTimeslot(exampleTimeslot);
		List<Game> games = gameRepository.findAll(Example.of(example),
				new Sort(new Sort.Order(Sort.Direction.ASC, "startAt")));
		return games;
	}

	/**
	 * POST /division-events : save all the divisionEvent games.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         divisionEvent games in body
	 */
	@RequestMapping(value = "/division-events/{id}/games", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Game> saveAllDivisionEventGames(
			@PathVariable Long id, @RequestBody Game game) {
		log.debug("REST request to save all DivisionEvent games,{},{}", id,
				game);
		if (game.getTimeslot() == null) {
			return ResponseEntity
					.badRequest()
					.headers(
							HeaderUtil.createFailureAlert("divisionEventGame",
									"error", "timeslot cannot be null"))
					.body(null);
		}

		Timeslot example = new Timeslot();
		example.setRound(game.getTimeslot().getRound());
		DivisionEvent exampleDivisionEvent = new DivisionEvent();
		exampleDivisionEvent.setId(id);
		example.setDivisionEvent(exampleDivisionEvent);
		Timeslot timeslot = timeslotRepository.findOne(Example.of(example));
		if (null != timeslot) {
			game.setTimeslot(timeslot);
		} else {
			// create a new timeslot of division event
			timeslot = timeslotRepository.save(example);
			game.setTimeslot(timeslot);
		}

		if (game.getId() == null) {
			game = gameRepository.save(game);
		} else {
			game = gameRepository.saveAndFlush(game);
		}
		return ResponseEntity
				.ok()
				.headers(
						HeaderUtil.createEntityCreationAlert(
								"divisionEventGame", id.toString())).body(game);
	}

	/**
	 * GET /division-events : get all the divisionEvent teams.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         divisionEvent teams in body
	 */
	@RequestMapping(value = "/division-events/{id}/teams", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public List<Team> getAllDivisionEventTeams(@PathVariable Long id) {
		log.debug("REST request to get all DivisionEvent teams,{}", id);

		Team example = new Team();
		DivisionEvent exampleDivisionEvent = new DivisionEvent();
		exampleDivisionEvent.setId(id);
		example.setDivisionEvent(exampleDivisionEvent);
		List<Team> teams = teamRepository.findAll(Example.of(example));
		return teams;
	}

}
