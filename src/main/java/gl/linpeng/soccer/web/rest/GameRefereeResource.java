package gl.linpeng.soccer.web.rest;

import com.codahale.metrics.annotation.Timed;
import gl.linpeng.soccer.domain.GameReferee;

import gl.linpeng.soccer.repository.GameRefereeRepository;
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
 * REST controller for managing GameReferee.
 */
@RestController
@RequestMapping("/api")
public class GameRefereeResource {

    private final Logger log = LoggerFactory.getLogger(GameRefereeResource.class);
        
    @Inject
    private GameRefereeRepository gameRefereeRepository;

    /**
     * POST  /game-referees : Create a new gameReferee.
     *
     * @param gameReferee the gameReferee to create
     * @return the ResponseEntity with status 201 (Created) and with body the new gameReferee, or with status 400 (Bad Request) if the gameReferee has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/game-referees",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<GameReferee> createGameReferee(@RequestBody GameReferee gameReferee) throws URISyntaxException {
        log.debug("REST request to save GameReferee : {}", gameReferee);
        if (gameReferee.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("gameReferee", "idexists", "A new gameReferee cannot already have an ID")).body(null);
        }
        GameReferee result = gameRefereeRepository.save(gameReferee);
        return ResponseEntity.created(new URI("/api/game-referees/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("gameReferee", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /game-referees : Updates an existing gameReferee.
     *
     * @param gameReferee the gameReferee to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated gameReferee,
     * or with status 400 (Bad Request) if the gameReferee is not valid,
     * or with status 500 (Internal Server Error) if the gameReferee couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/game-referees",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<GameReferee> updateGameReferee(@RequestBody GameReferee gameReferee) throws URISyntaxException {
        log.debug("REST request to update GameReferee : {}", gameReferee);
        if (gameReferee.getId() == null) {
            return createGameReferee(gameReferee);
        }
        GameReferee result = gameRefereeRepository.save(gameReferee);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("gameReferee", gameReferee.getId().toString()))
            .body(result);
    }

    /**
     * GET  /game-referees : get all the gameReferees.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of gameReferees in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/game-referees",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<GameReferee>> getAllGameReferees(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of GameReferees");
        Page<GameReferee> page = gameRefereeRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/game-referees");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /game-referees/:id : get the "id" gameReferee.
     *
     * @param id the id of the gameReferee to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the gameReferee, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/game-referees/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<GameReferee> getGameReferee(@PathVariable Long id) {
        log.debug("REST request to get GameReferee : {}", id);
        GameReferee gameReferee = gameRefereeRepository.findOne(id);
        return Optional.ofNullable(gameReferee)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /game-referees/:id : delete the "id" gameReferee.
     *
     * @param id the id of the gameReferee to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/game-referees/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteGameReferee(@PathVariable Long id) {
        log.debug("REST request to delete GameReferee : {}", id);
        gameRefereeRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("gameReferee", id.toString())).build();
    }

}
