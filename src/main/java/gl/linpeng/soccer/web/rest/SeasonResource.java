package gl.linpeng.soccer.web.rest;

import com.codahale.metrics.annotation.Timed;
import gl.linpeng.soccer.domain.Season;

import gl.linpeng.soccer.repository.SeasonRepository;
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
 * REST controller for managing Season.
 */
@RestController
@RequestMapping("/api")
public class SeasonResource {

    private final Logger log = LoggerFactory.getLogger(SeasonResource.class);
        
    @Inject
    private SeasonRepository seasonRepository;

    /**
     * POST  /seasons : Create a new season.
     *
     * @param season the season to create
     * @return the ResponseEntity with status 201 (Created) and with body the new season, or with status 400 (Bad Request) if the season has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/seasons",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Season> createSeason(@RequestBody Season season) throws URISyntaxException {
        log.debug("REST request to save Season : {}", season);
        if (season.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("season", "idexists", "A new season cannot already have an ID")).body(null);
        }
        Season result = seasonRepository.save(season);
        return ResponseEntity.created(new URI("/api/seasons/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("season", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /seasons : Updates an existing season.
     *
     * @param season the season to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated season,
     * or with status 400 (Bad Request) if the season is not valid,
     * or with status 500 (Internal Server Error) if the season couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/seasons",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Season> updateSeason(@RequestBody Season season) throws URISyntaxException {
        log.debug("REST request to update Season : {}", season);
        if (season.getId() == null) {
            return createSeason(season);
        }
        Season result = seasonRepository.save(season);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("season", season.getId().toString()))
            .body(result);
    }

    /**
     * GET  /seasons : get all the seasons.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of seasons in body
     */
    @RequestMapping(value = "/seasons",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Season> getAllSeasons() {
        log.debug("REST request to get all Seasons");
        List<Season> seasons = seasonRepository.findAll();
        return seasons;
    }

    /**
     * GET  /seasons/:id : get the "id" season.
     *
     * @param id the id of the season to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the season, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/seasons/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Season> getSeason(@PathVariable Long id) {
        log.debug("REST request to get Season : {}", id);
        Season season = seasonRepository.findOne(id);
        return Optional.ofNullable(season)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /seasons/:id : delete the "id" season.
     *
     * @param id the id of the season to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/seasons/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSeason(@PathVariable Long id) {
        log.debug("REST request to delete Season : {}", id);
        seasonRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("season", id.toString())).build();
    }

}
