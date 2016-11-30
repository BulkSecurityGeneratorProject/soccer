package gl.linpeng.soccer.web.rest;

import com.codahale.metrics.annotation.Timed;
import gl.linpeng.soccer.domain.DivisionEvent;

import gl.linpeng.soccer.repository.DivisionEventRepository;
import gl.linpeng.soccer.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing DivisionEvent.
 */
@RestController
@RequestMapping("/api")
public class DivisionEventResource {

    private final Logger log = LoggerFactory.getLogger(DivisionEventResource.class);
        
    @Inject
    private DivisionEventRepository divisionEventRepository;

    /**
     * POST  /division-events : Create a new divisionEvent.
     *
     * @param divisionEvent the divisionEvent to create
     * @return the ResponseEntity with status 201 (Created) and with body the new divisionEvent, or with status 400 (Bad Request) if the divisionEvent has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/division-events",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DivisionEvent> createDivisionEvent(@RequestBody DivisionEvent divisionEvent) throws URISyntaxException {
        log.debug("REST request to save DivisionEvent : {}", divisionEvent);
        if (divisionEvent.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("divisionEvent", "idexists", "A new divisionEvent cannot already have an ID")).body(null);
        }
        DivisionEvent result = divisionEventRepository.save(divisionEvent);
        return ResponseEntity.created(new URI("/api/division-events/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("divisionEvent", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /division-events : Updates an existing divisionEvent.
     *
     * @param divisionEvent the divisionEvent to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated divisionEvent,
     * or with status 400 (Bad Request) if the divisionEvent is not valid,
     * or with status 500 (Internal Server Error) if the divisionEvent couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/division-events",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DivisionEvent> updateDivisionEvent(@RequestBody DivisionEvent divisionEvent) throws URISyntaxException {
        log.debug("REST request to update DivisionEvent : {}", divisionEvent);
        if (divisionEvent.getId() == null) {
            return createDivisionEvent(divisionEvent);
        }
        DivisionEvent result = divisionEventRepository.save(divisionEvent);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("divisionEvent", divisionEvent.getId().toString()))
            .body(result);
    }

    /**
     * GET  /division-events : get all the divisionEvents.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of divisionEvents in body
     */
    @RequestMapping(value = "/division-events",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<DivisionEvent> getAllDivisionEvents() {
        log.debug("REST request to get all DivisionEvents");
        List<DivisionEvent> divisionEvents = divisionEventRepository.findAll();
        return divisionEvents;
    }

    /**
     * GET  /division-events/:id : get the "id" divisionEvent.
     *
     * @param id the id of the divisionEvent to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the divisionEvent, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/division-events/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DivisionEvent> getDivisionEvent(@PathVariable Long id) {
        log.debug("REST request to get DivisionEvent : {}", id);
        DivisionEvent divisionEvent = divisionEventRepository.findOne(id);
        return Optional.ofNullable(divisionEvent)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /division-events/:id : delete the "id" divisionEvent.
     *
     * @param id the id of the divisionEvent to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/division-events/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDivisionEvent(@PathVariable Long id) {
        log.debug("REST request to delete DivisionEvent : {}", id);
        divisionEventRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("divisionEvent", id.toString())).build();
    }

}
