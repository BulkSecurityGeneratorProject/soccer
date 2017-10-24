package gl.linpeng.soccer.web.rest;

import com.codahale.metrics.annotation.Timed;
import gl.linpeng.soccer.domain.Lineup;

import gl.linpeng.soccer.repository.LineupRepository;
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
 * REST controller for managing Lineup.
 */
@RestController
@RequestMapping("/api")
public class LineupResource {

    private final Logger log = LoggerFactory.getLogger(LineupResource.class);
        
    @Inject
    private LineupRepository lineupRepository;

    /**
     * POST  /lineups : Create a new lineup.
     *
     * @param lineup the lineup to create
     * @return the ResponseEntity with status 201 (Created) and with body the new lineup, or with status 400 (Bad Request) if the lineup has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/lineups",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Lineup> createLineup(@RequestBody Lineup lineup) throws URISyntaxException {
        log.debug("REST request to save Lineup : {}", lineup);
        if (lineup.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("lineup", "idexists", "A new lineup cannot already have an ID")).body(null);
        }
        Lineup result = lineupRepository.save(lineup);
        return ResponseEntity.created(new URI("/api/lineups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("lineup", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /lineups : Updates an existing lineup.
     *
     * @param lineup the lineup to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated lineup,
     * or with status 400 (Bad Request) if the lineup is not valid,
     * or with status 500 (Internal Server Error) if the lineup couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/lineups",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Lineup> updateLineup(@RequestBody Lineup lineup) throws URISyntaxException {
        log.debug("REST request to update Lineup : {}", lineup);
        if (lineup.getId() == null) {
            return createLineup(lineup);
        }
        Lineup result = lineupRepository.save(lineup);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("lineup", lineup.getId().toString()))
            .body(result);
    }

    /**
     * GET  /lineups : get all the lineups.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of lineups in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/lineups",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Lineup>> getAllLineups(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Lineups");
        Page<Lineup> page = lineupRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/lineups");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /lineups/:id : get the "id" lineup.
     *
     * @param id the id of the lineup to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the lineup, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/lineups/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Lineup> getLineup(@PathVariable Long id) {
        log.debug("REST request to get Lineup : {}", id);
        Lineup lineup = lineupRepository.findOne(id);
        return Optional.ofNullable(lineup)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /lineups/:id : delete the "id" lineup.
     *
     * @param id the id of the lineup to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/lineups/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteLineup(@PathVariable Long id) {
        log.debug("REST request to delete Lineup : {}", id);
        lineupRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("lineup", id.toString())).build();
    }

}
