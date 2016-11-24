package gl.linpeng.soccer.web.rest;

import gl.linpeng.soccer.domain.Division;
import gl.linpeng.soccer.domain.DivisionEvent;
import gl.linpeng.soccer.domain.Season;
import gl.linpeng.soccer.repository.DivisionEventRepository;
import gl.linpeng.soccer.repository.DivisionRepository;
import gl.linpeng.soccer.web.rest.util.HeaderUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

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
 * REST controller for managing Division.
 */
@RestController
@RequestMapping("/api")
public class DivisionResource {

	private final Logger log = LoggerFactory.getLogger(DivisionResource.class);

	@Inject
	private DivisionRepository divisionRepository;
	@Inject
	private DivisionEventRepository divisionEventRepository;

	/**
	 * POST /divisions : Create a new division.
	 *
	 * @param division
	 *            the division to create
	 * @return the ResponseEntity with status 201 (Created) and with body the
	 *         new division, or with status 400 (Bad Request) if the division
	 *         has already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@RequestMapping(value = "/divisions", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Division> createDivision(
			@RequestBody Division division) throws URISyntaxException {
		log.debug("REST request to save Division : {}", division);
		if (division.getId() != null) {
			return ResponseEntity
					.badRequest()
					.headers(
							HeaderUtil.createFailureAlert("division",
									"idexists",
									"A new division cannot already have an ID"))
					.body(null);
		}
		Division result = divisionRepository.save(division);
		return ResponseEntity
				.created(new URI("/api/divisions/" + result.getId()))
				.headers(
						HeaderUtil.createEntityCreationAlert("division", result
								.getId().toString())).body(result);
	}

	/**
	 * PUT /divisions : Updates an existing division.
	 *
	 * @param division
	 *            the division to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         division, or with status 400 (Bad Request) if the division is not
	 *         valid, or with status 500 (Internal Server Error) if the division
	 *         couldnt be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@RequestMapping(value = "/divisions", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Division> updateDivision(
			@RequestBody Division division) throws URISyntaxException {
		log.debug("REST request to update Division : {}", division);
		if (division.getId() == null) {
			return createDivision(division);
		}
		Division result = divisionRepository.save(division);
		return ResponseEntity
				.ok()
				.headers(
						HeaderUtil.createEntityUpdateAlert("division", division
								.getId().toString())).body(result);
	}

	/**
	 * GET /divisions : get all the divisions.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of divisions
	 *         in body
	 */
	@RequestMapping(value = "/divisions", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public List<Division> getAllDivisions() {
		log.debug("REST request to get all Divisions");
		List<Division> divisions = divisionRepository.findAll();
		return divisions;
	}

	/**
	 * GET /divisions/:id : get the "id" division.
	 *
	 * @param id
	 *            the id of the division to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         division, or with status 404 (Not Found)
	 */
	@RequestMapping(value = "/divisions/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Division> getDivision(@PathVariable Long id) {
		log.debug("REST request to get Division : {}", id);
		Division division = divisionRepository.findOne(id);
		return Optional.ofNullable(division)
				.map(result -> new ResponseEntity<>(result, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	/**
	 * DELETE /divisions/:id : delete the "id" division.
	 *
	 * @param id
	 *            the id of the division to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@RequestMapping(value = "/divisions/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Void> deleteDivision(@PathVariable Long id) {
		log.debug("REST request to delete Division : {}", id);
		divisionRepository.delete(id);
		return ResponseEntity
				.ok()
				.headers(
						HeaderUtil.createEntityDeletionAlert("division",
								id.toString())).build();
	}

	/**
	 * GET /divisions/:id/seasons : get all the division-event of division.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         division-events in body
	 */
	@RequestMapping(value = "/divisions/{id}/division-events", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public List<DivisionEvent> getAllDivisionEvents(@PathVariable Long id) {
		log.debug("REST request to get all division-event of Division {}", id);
		Division exampleDivision = new Division();
		exampleDivision.setId(id);
		DivisionEvent exampleDivisionEvent = new DivisionEvent();
		exampleDivisionEvent.setDivision(exampleDivision);
		List<DivisionEvent> divisionEvents = divisionEventRepository.findAll(
				Example.of(exampleDivisionEvent), new Sort(new Sort.Order(
						Sort.Direction.DESC, "season.startAt")));
		return divisionEvents;
	}

}
