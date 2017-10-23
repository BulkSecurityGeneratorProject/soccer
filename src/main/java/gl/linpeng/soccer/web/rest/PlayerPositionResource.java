package gl.linpeng.soccer.web.rest;

import com.codahale.metrics.annotation.Timed;
import gl.linpeng.soccer.domain.PlayerPosition;

import gl.linpeng.soccer.repository.PlayerPositionRepository;
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
 * REST controller for managing PlayerPosition.
 */
@RestController
@RequestMapping("/api")
public class PlayerPositionResource {

    private final Logger log = LoggerFactory.getLogger(PlayerPositionResource.class);
        
    @Inject
    private PlayerPositionRepository playerPositionRepository;

    /**
     * POST  /player-positions : Create a new playerPosition.
     *
     * @param playerPosition the playerPosition to create
     * @return the ResponseEntity with status 201 (Created) and with body the new playerPosition, or with status 400 (Bad Request) if the playerPosition has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/player-positions",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PlayerPosition> createPlayerPosition(@RequestBody PlayerPosition playerPosition) throws URISyntaxException {
        log.debug("REST request to save PlayerPosition : {}", playerPosition);
        if (playerPosition.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("playerPosition", "idexists", "A new playerPosition cannot already have an ID")).body(null);
        }
        PlayerPosition result = playerPositionRepository.save(playerPosition);
        return ResponseEntity.created(new URI("/api/player-positions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("playerPosition", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /player-positions : Updates an existing playerPosition.
     *
     * @param playerPosition the playerPosition to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated playerPosition,
     * or with status 400 (Bad Request) if the playerPosition is not valid,
     * or with status 500 (Internal Server Error) if the playerPosition couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/player-positions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PlayerPosition> updatePlayerPosition(@RequestBody PlayerPosition playerPosition) throws URISyntaxException {
        log.debug("REST request to update PlayerPosition : {}", playerPosition);
        if (playerPosition.getId() == null) {
            return createPlayerPosition(playerPosition);
        }
        PlayerPosition result = playerPositionRepository.save(playerPosition);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("playerPosition", playerPosition.getId().toString()))
            .body(result);
    }

    /**
     * GET  /player-positions : get all the playerPositions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of playerPositions in body
     */
    @RequestMapping(value = "/player-positions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<PlayerPosition> getAllPlayerPositions() {
        log.debug("REST request to get all PlayerPositions");
        List<PlayerPosition> playerPositions = playerPositionRepository.findAll();
        return playerPositions;
    }

    /**
     * GET  /player-positions/:id : get the "id" playerPosition.
     *
     * @param id the id of the playerPosition to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the playerPosition, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/player-positions/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PlayerPosition> getPlayerPosition(@PathVariable Long id) {
        log.debug("REST request to get PlayerPosition : {}", id);
        PlayerPosition playerPosition = playerPositionRepository.findOne(id);
        return Optional.ofNullable(playerPosition)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /player-positions/:id : delete the "id" playerPosition.
     *
     * @param id the id of the playerPosition to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/player-positions/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePlayerPosition(@PathVariable Long id) {
        log.debug("REST request to delete PlayerPosition : {}", id);
        playerPositionRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("playerPosition", id.toString())).build();
    }

}
