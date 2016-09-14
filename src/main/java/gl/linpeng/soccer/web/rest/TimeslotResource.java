package gl.linpeng.soccer.web.rest;

import com.codahale.metrics.annotation.Timed;
import gl.linpeng.soccer.domain.Timeslot;

import gl.linpeng.soccer.repository.TimeslotRepository;
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
 * REST controller for managing Timeslot.
 */
@RestController
@RequestMapping("/api")
public class TimeslotResource {

    private final Logger log = LoggerFactory.getLogger(TimeslotResource.class);
        
    @Inject
    private TimeslotRepository timeslotRepository;

    /**
     * POST  /timeslots : Create a new timeslot.
     *
     * @param timeslot the timeslot to create
     * @return the ResponseEntity with status 201 (Created) and with body the new timeslot, or with status 400 (Bad Request) if the timeslot has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/timeslots",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Timeslot> createTimeslot(@RequestBody Timeslot timeslot) throws URISyntaxException {
        log.debug("REST request to save Timeslot : {}", timeslot);
        if (timeslot.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("timeslot", "idexists", "A new timeslot cannot already have an ID")).body(null);
        }
        Timeslot result = timeslotRepository.save(timeslot);
        return ResponseEntity.created(new URI("/api/timeslots/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("timeslot", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /timeslots : Updates an existing timeslot.
     *
     * @param timeslot the timeslot to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated timeslot,
     * or with status 400 (Bad Request) if the timeslot is not valid,
     * or with status 500 (Internal Server Error) if the timeslot couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/timeslots",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Timeslot> updateTimeslot(@RequestBody Timeslot timeslot) throws URISyntaxException {
        log.debug("REST request to update Timeslot : {}", timeslot);
        if (timeslot.getId() == null) {
            return createTimeslot(timeslot);
        }
        Timeslot result = timeslotRepository.save(timeslot);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("timeslot", timeslot.getId().toString()))
            .body(result);
    }

    /**
     * GET  /timeslots : get all the timeslots.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of timeslots in body
     */
    @RequestMapping(value = "/timeslots",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Timeslot> getAllTimeslots() {
        log.debug("REST request to get all Timeslots");
        List<Timeslot> timeslots = timeslotRepository.findAll();
        return timeslots;
    }

    /**
     * GET  /timeslots/:id : get the "id" timeslot.
     *
     * @param id the id of the timeslot to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the timeslot, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/timeslots/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Timeslot> getTimeslot(@PathVariable Long id) {
        log.debug("REST request to get Timeslot : {}", id);
        Timeslot timeslot = timeslotRepository.findOne(id);
        return Optional.ofNullable(timeslot)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /timeslots/:id : delete the "id" timeslot.
     *
     * @param id the id of the timeslot to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/timeslots/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTimeslot(@PathVariable Long id) {
        log.debug("REST request to delete Timeslot : {}", id);
        timeslotRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("timeslot", id.toString())).build();
    }

}
