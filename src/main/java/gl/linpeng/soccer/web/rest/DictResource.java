package gl.linpeng.soccer.web.rest;

import com.codahale.metrics.annotation.Timed;
import gl.linpeng.soccer.domain.Dict;

import gl.linpeng.soccer.repository.DictRepository;
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
 * REST controller for managing Dict.
 */
@RestController
@RequestMapping("/api")
public class DictResource {

    private final Logger log = LoggerFactory.getLogger(DictResource.class);
        
    @Inject
    private DictRepository dictRepository;

    /**
     * POST  /dicts : Create a new dict.
     *
     * @param dict the dict to create
     * @return the ResponseEntity with status 201 (Created) and with body the new dict, or with status 400 (Bad Request) if the dict has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/dicts",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Dict> createDict(@RequestBody Dict dict) throws URISyntaxException {
        log.debug("REST request to save Dict : {}", dict);
        if (dict.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("dict", "idexists", "A new dict cannot already have an ID")).body(null);
        }
        Dict result = dictRepository.save(dict);
        return ResponseEntity.created(new URI("/api/dicts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("dict", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /dicts : Updates an existing dict.
     *
     * @param dict the dict to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated dict,
     * or with status 400 (Bad Request) if the dict is not valid,
     * or with status 500 (Internal Server Error) if the dict couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/dicts",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Dict> updateDict(@RequestBody Dict dict) throws URISyntaxException {
        log.debug("REST request to update Dict : {}", dict);
        if (dict.getId() == null) {
            return createDict(dict);
        }
        Dict result = dictRepository.save(dict);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("dict", dict.getId().toString()))
            .body(result);
    }

    /**
     * GET  /dicts : get all the dicts.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of dicts in body
     */
    @RequestMapping(value = "/dicts",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Dict> getAllDicts() {
        log.debug("REST request to get all Dicts");
        List<Dict> dicts = dictRepository.findAll();
        return dicts;
    }

    /**
     * GET  /dicts/:id : get the "id" dict.
     *
     * @param id the id of the dict to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the dict, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/dicts/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Dict> getDict(@PathVariable Long id) {
        log.debug("REST request to get Dict : {}", id);
        Dict dict = dictRepository.findOne(id);
        return Optional.ofNullable(dict)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /dicts/:id : delete the "id" dict.
     *
     * @param id the id of the dict to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/dicts/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDict(@PathVariable Long id) {
        log.debug("REST request to delete Dict : {}", id);
        dictRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("dict", id.toString())).build();
    }

}
