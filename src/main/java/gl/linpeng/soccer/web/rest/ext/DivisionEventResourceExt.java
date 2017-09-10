package gl.linpeng.soccer.web.rest.ext;

import gl.linpeng.soccer.domain.DivisionEvent;
import gl.linpeng.soccer.domain.Game;
import gl.linpeng.soccer.domain.Team;
import gl.linpeng.soccer.domain.Timeslot;
import gl.linpeng.soccer.repository.GameRepository;
import gl.linpeng.soccer.repository.TeamRepository;
import gl.linpeng.soccer.repository.TimeslotRepository;
import gl.linpeng.soccer.web.rest.util.HeaderUtil;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

/**
 * REST ext controller for managing DivisionEvent.
 * 
 * @author linpeng
 */
@RestController
@RequestMapping("/api")
public class DivisionEventResourceExt {
	private final Logger log = LoggerFactory
			.getLogger(DivisionEventResourceExt.class);

	@Inject
	private GameRepository gameRepository;
	@Inject
	private TeamRepository teamRepository;
	@Inject
	private TimeslotRepository timeslotRepository;
	@PersistenceContext
	private EntityManager entityManager;

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
						+ " left outer join game g left outer join timeslot t "
						+ "left outer join division_event de on de.id = t.division_event_id "
						+ "on t.id = g.timeslot_id on g.id = sq.game_id "
						+ "on sq.id = sp.squad_id,PLAYER p "
						+ "WHERE sp.id = rd.squad_player_id "
						+ "AND sp.player_id = p.id "
						+ "AND rd.result_field_id=3 AND de.id=" + id
						+ "GROUP BY p.name " + "ORDER BY goal DESC");
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
						+ " left outer join game g left outer join timeslot t "
						+ "left outer join division_event de on de.id = t.division_event_id "
						+ "on t.id = g.timeslot_id on g.id = sq.game_id "
						+ "on sq.id = sp.squad_id,PLAYER p "
						+ "WHERE sp.id = rd.squad_player_id "
						+ "AND sp.player_id = p.id "
						+ "AND rd.result_field_id=4 AND de.id=" + id
						+ "GROUP BY p.name " + "ORDER BY assist DESC");
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

	/**
	 * 
	 * POST /division-events : save all the divisionEvent games.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         divisionEvent games in body
	 */
	@RequestMapping(value = "/division-events/{id}/games", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<List<Game>> saveAllDivisionEventGames(
			@PathVariable Long id, @RequestBody List<Game> games) {
		log.debug("REST request to save all DivisionEvent games,{},{}", id,
				games);

		for (Game game : games) {
			if (game.getTimeslot() == null) { // 比赛的轮次必须存在,强制验证
				return ResponseEntity
						.badRequest()
						.headers(
								HeaderUtil.createFailureAlert(
										"divisionEventGame", "error",
										"timeslot cannot be null")).body(null);
			}
		}

		// 这里使用遍历，保证在高并发提交时不会事务交叉
		for (Game game : games) {
			Timeslot example = new Timeslot();
			example.setRound(game.getTimeslot().getRound());
			DivisionEvent exampleDivisionEvent = new DivisionEvent();
			exampleDivisionEvent.setId(id);
			example.setDivisionEvent(exampleDivisionEvent);
			Timeslot timeslot = timeslotRepository.findOne(Example.of(example));

			if (null == timeslot) {
				// create a new timeslot of division event
				timeslot = timeslotRepository.save(example);
			}
			game.setTimeslot(timeslot);
		}

		// 设置好timeslot之后再统一保存比赛信息
		games = gameRepository.save(games);

		return ResponseEntity
				.ok()
				.headers(
						HeaderUtil.createEntityCreationAlert(
								"divisionEventGame", id.toString()))
				.body(games);
	}
}
