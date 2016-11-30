package gl.linpeng.soccer.web.rest.ext;

import gl.linpeng.soccer.domain.Division;
import gl.linpeng.soccer.domain.DivisionEvent;
import gl.linpeng.soccer.repository.DivisionEventRepository;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

/**
 * REST ext controller for managing Division.
 * 
 * @author linpeng
 */
@RestController
@RequestMapping("/api")
public class DivisionResourceExt {
	private final Logger log = LoggerFactory
			.getLogger(DivisionResourceExt.class);
	@Inject
	private DivisionEventRepository divisionEventRepository;

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
