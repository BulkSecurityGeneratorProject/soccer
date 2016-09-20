package gl.linpeng.soccer.web.rest;

import com.codahale.metrics.annotation.Timed;
import gl.linpeng.soccer.domain.SquadPlayer;

import gl.linpeng.soccer.repository.SquadPlayerRepository;
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
 * REST controller for managing SquadPlayer.
 */
@RestController
@RequestMapping("/api")
public class SquadPlayerResource {

    private final Logger log = LoggerFactory.getLogger(SquadPlayerResource.class);
        
    @Inject
    private SquadPlayerRepository squadPlayerRepository;

    /**
     * POST  /squad-players : Create a new squadPlayer.
     *
     * @param squadPlayer the squadPlayer to create
     * @return the ResponseEntity with status 201 (Created) and with body the new squadPlayer, or with status 400 (Bad Request) if the squadPlayer has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/squad-players",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SquadPlayer> createSquadPlayer(@RequestBody SquadPlayer squadPlayer) throws URISyntaxException {
        log.debug("REST request to save SquadPlayer : {}", squadPlayer);
        if (squadPlayer.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("squadPlayer", "idexists", "A new squadPlayer cannot already have an ID")).body(null);
        }
        SquadPlayer result = squadPlayerRepository.save(squadPlayer);
        return ResponseEntity.created(new URI("/api/squad-players/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("squadPlayer", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /squad-players : Updates an existing squadPlayer.
     *
     * @param squadPlayer the squadPlayer to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated squadPlayer,
     * or with status 400 (Bad Request) if the squadPlayer is not valid,
     * or with status 500 (Internal Server Error) if the squadPlayer couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/squad-players",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SquadPlayer> updateSquadPlayer(@RequestBody SquadPlayer squadPlayer) throws URISyntaxException {
        log.debug("REST request to update SquadPlayer : {}", squadPlayer);
        if (squadPlayer.getId() == null) {
            return createSquadPlayer(squadPlayer);
        }
        SquadPlayer result = squadPlayerRepository.save(squadPlayer);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("squadPlayer", squadPlayer.getId().toString()))
            .body(result);
    }

    /**
     * GET  /squad-players : get all the squadPlayers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of squadPlayers in body
     */
    @RequestMapping(value = "/squad-players",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<SquadPlayer> getAllSquadPlayers() {
        log.debug("REST request to get all SquadPlayers");
        List<SquadPlayer> squadPlayers = squadPlayerRepository.findAll();
        return squadPlayers;
    }

    /**
     * GET  /squad-players/:id : get the "id" squadPlayer.
     *
     * @param id the id of the squadPlayer to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the squadPlayer, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/squad-players/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SquadPlayer> getSquadPlayer(@PathVariable Long id) {
        log.debug("REST request to get SquadPlayer : {}", id);
        SquadPlayer squadPlayer = squadPlayerRepository.findOne(id);
        return Optional.ofNullable(squadPlayer)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /squad-players/:id : delete the "id" squadPlayer.
     *
     * @param id the id of the squadPlayer to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/squad-players/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSquadPlayer(@PathVariable Long id) {
        log.debug("REST request to delete SquadPlayer : {}", id);
        squadPlayerRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("squadPlayer", id.toString())).build();
    }

}
