package gl.linpeng.soccer.web.rest;

import com.codahale.metrics.annotation.Timed;
import gl.linpeng.soccer.domain.Venue;

import gl.linpeng.soccer.repository.VenueRepository;
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
 * REST controller for managing Venue.
 */
@RestController
@RequestMapping("/api")
public class VenueResource {

    private final Logger log = LoggerFactory.getLogger(VenueResource.class);
        
    @Inject
    private VenueRepository venueRepository;

    /**
     * POST  /venues : Create a new venue.
     *
     * @param venue the venue to create
     * @return the ResponseEntity with status 201 (Created) and with body the new venue, or with status 400 (Bad Request) if the venue has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/venues",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Venue> createVenue(@RequestBody Venue venue) throws URISyntaxException {
        log.debug("REST request to save Venue : {}", venue);
        if (venue.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("venue", "idexists", "A new venue cannot already have an ID")).body(null);
        }
        Venue result = venueRepository.save(venue);
        return ResponseEntity.created(new URI("/api/venues/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("venue", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /venues : Updates an existing venue.
     *
     * @param venue the venue to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated venue,
     * or with status 400 (Bad Request) if the venue is not valid,
     * or with status 500 (Internal Server Error) if the venue couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/venues",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Venue> updateVenue(@RequestBody Venue venue) throws URISyntaxException {
        log.debug("REST request to update Venue : {}", venue);
        if (venue.getId() == null) {
            return createVenue(venue);
        }
        Venue result = venueRepository.save(venue);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("venue", venue.getId().toString()))
            .body(result);
    }

    /**
     * GET  /venues : get all the venues.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of venues in body
     */
    @RequestMapping(value = "/venues",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Venue> getAllVenues() {
        log.debug("REST request to get all Venues");
        List<Venue> venues = venueRepository.findAll();
        return venues;
    }

    /**
     * GET  /venues/:id : get the "id" venue.
     *
     * @param id the id of the venue to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the venue, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/venues/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Venue> getVenue(@PathVariable Long id) {
        log.debug("REST request to get Venue : {}", id);
        Venue venue = venueRepository.findOne(id);
        return Optional.ofNullable(venue)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /venues/:id : delete the "id" venue.
     *
     * @param id the id of the venue to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/venues/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteVenue(@PathVariable Long id) {
        log.debug("REST request to delete Venue : {}", id);
        venueRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("venue", id.toString())).build();
    }

}
