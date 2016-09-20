package gl.linpeng.soccer.web.rest;

import gl.linpeng.soccer.domain.DivisionEvent;
import gl.linpeng.soccer.domain.Team;
import gl.linpeng.soccer.repository.DivisionEventRepository;
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
				.format("select id,name,count(id) as GP,count(WIN) as W"
						+ ",count(LOSS) as L,count(DRAW) as D,sum(GS) as GS"
						+ ",sum(GA) as GA,sum(GS)- sum(GA) as GD,(count(WIN)*3+count(DRAW)*1) AS PTS "
						+ "from "
						+ "(select t.id,t.name"
						+ ",case when g.home_score > g.road_score then '1' end as WIN"
						+ ",case when g.home_score < g.road_score then '1' end as LOSS"
						+ ",case when g.home_score = g.road_score  then '1' end AS DRAW"
						+ ",g.home_score as GS,g.road_score as GA "
						+ "from team t,game g where t.division_event_id= "
						+ id
						+ "and g.home_team_id=t.id "
						+ "union all "
						+ "select t.id,t.name"
						+ ",case when g2.home_score < g2.road_score then '1' end as WIN"
						+ ",case when g2.home_score > g2.road_score then '1' end as LOSS"
						+ ",case when g2.home_score = g2.road_score  then '1' end AS DRAW"
						+ ",g2.road_score as GS,g2.home_score as GA "
						+ "from team t,game g2 where t.division_event_id= "
						+ id + "and g2.road_team_id=t.id) "
						+ "group by name order by PTS DESC,GD DESC");
		Query query = entityManager.createNativeQuery(sql);
		return query.getResultList();
	}

}
