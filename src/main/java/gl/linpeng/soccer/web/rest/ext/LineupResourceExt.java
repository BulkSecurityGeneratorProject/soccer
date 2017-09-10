package gl.linpeng.soccer.web.rest.ext;

import gl.linpeng.soccer.domain.DivisionEvent;
import gl.linpeng.soccer.domain.Lineup;
import gl.linpeng.soccer.domain.Team;
import gl.linpeng.soccer.repository.LineupRepository;
import gl.linpeng.soccer.web.rest.util.HeaderUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

/**
 * REST ext controller for managing Lineup.
 * 
 * @author linpeng
 */
@RestController
@RequestMapping("/api")
public class LineupResourceExt {

	private final Logger log = LoggerFactory.getLogger(LineupResourceExt.class);

	@Inject
	private LineupRepository lineupRepository;

	/**
	 * POST /lineups-batch : Create batch lineup.
	 *
	 * @param lineup
	 *            the lineup to create
	 * @return the ResponseEntity with status 201 (Created) and with body the
	 *         new lineup, or with status 400 (Bad Request) if the lineup has
	 *         already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@RequestMapping(value = "/lineups-batch", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<List<Lineup>> createBatchLineup(
			@RequestBody List<Lineup> lineups) throws URISyntaxException {
		log.debug("REST request to save batch Lineup : {}", lineups);

		for (Lineup lineup : lineups) {
			if (lineup.getId() != null) {
				return ResponseEntity
						.badRequest()
						.headers(
								HeaderUtil
										.createFailureAlert("lineup",
												"idexists",
												"A new lineup cannot already have an ID"))
						.body(null);
			}
		}

		List<Lineup> result = lineupRepository.save(lineups);
		return ResponseEntity.created(new URI("/api/lineups-batch")).body(
				result);
	}

	/**
	 * GET /lineups/division-event/{divisionEventId}/team/{teamId} : get all the
	 * lineups of team by division.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of lineups
	 *         in body
	 */
	@RequestMapping(value = "/lineups/division-event/{divisionEventId}/team/{teamId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public List<Lineup> getAllLineupsByDivisionEventAndTeam(
			@PathVariable Long divisionEventId, @PathVariable Long teamId) {
		log.debug(
				"REST request to get all Lineups of team {} by division event {}.",
				teamId, divisionEventId);
		Lineup exampleLineup = new Lineup();
		DivisionEvent divisionEvent = new DivisionEvent();
		Team team = new Team();
		divisionEvent.setId(divisionEventId);
		team.setId(teamId);
		exampleLineup.setDivisionEvent(divisionEvent);
		exampleLineup.setTeam(team);
		List<Lineup> lineups = lineupRepository.findAll(Example
				.of(exampleLineup));
		return lineups;
	}
}
