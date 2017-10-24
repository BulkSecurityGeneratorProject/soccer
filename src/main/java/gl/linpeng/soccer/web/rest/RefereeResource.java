package gl.linpeng.soccer.web.rest;

import com.codahale.metrics.annotation.Timed;
import gl.linpeng.soccer.domain.Referee;

import gl.linpeng.soccer.repository.RefereeRepository;
import gl.linpeng.soccer.web.rest.util.HeaderUtil;
import gl.linpeng.soccer.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
 * REST controller for managing Referee.
 */
@RestController
@RequestMapping("/api")
public class RefereeResource {

    private final Logger log = LoggerFactory.getLogger(RefereeResource.class);
        
    @Inject
    private RefereeRepository refereeRepository;

    /**
     * POST  /referees : Create a new referee.
     *
     * @param referee the referee to create
     * @return the ResponseEntity with status 201 (Created) and with body the new referee, or with status 400 (Bad Request) if the referee has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/referees",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Referee> createReferee(@RequestBody Referee referee) throws URISyntaxException {
        log.debug("REST request to save Referee : {}", referee);
        if (referee.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("referee", "idexists", "A new referee cannot already have an ID")).body(null);
        }
        Referee result = refereeRepository.save(referee);
        return ResponseEntity.created(new URI("/api/referees/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("referee", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /referees : Updates an existing referee.
     *
     * @param referee the referee to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated referee,
     * or with status 400 (Bad Request) if the referee is not valid,
     * or with status 500 (Internal Server Error) if the referee couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/referees",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Referee> updateReferee(@RequestBody Referee referee) throws URISyntaxException {
        log.debug("REST request to update Referee : {}", referee);
        if (referee.getId() == null) {
            return createReferee(referee);
        }
        Referee result = refereeRepository.save(referee);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("referee", referee.getId().toString()))
            .body(result);
    }

    /**
     * GET  /referees : get all the referees.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of referees in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/referees",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Referee>> getAllReferees(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Referees");
        Page<Referee> page = refereeRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/referees");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /referees/:id : get the "id" referee.
     *
     * @param id the id of the referee to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the referee, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/referees/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Referee> getReferee(@PathVariable Long id) {
        log.debug("REST request to get Referee : {}", id);
        Referee referee = refereeRepository.findOne(id);
        return Optional.ofNullable(referee)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /referees/:id : delete the "id" referee.
     *
     * @param id the id of the referee to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/referees/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteReferee(@PathVariable Long id) {
        log.debug("REST request to delete Referee : {}", id);
        refereeRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("referee", id.toString())).build();
    }

}
