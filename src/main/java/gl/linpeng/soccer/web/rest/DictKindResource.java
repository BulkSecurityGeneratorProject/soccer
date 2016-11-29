package gl.linpeng.soccer.web.rest;

import com.codahale.metrics.annotation.Timed;

import gl.linpeng.soccer.domain.Dict;
import gl.linpeng.soccer.domain.DictKind;
import gl.linpeng.soccer.repository.DictKindRepository;
import gl.linpeng.soccer.repository.DictRepository;
import gl.linpeng.soccer.web.rest.util.HeaderUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
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
 * REST controller for managing DictKind.
 */
@RestController
@RequestMapping("/api")
public class DictKindResource {

    private final Logger log = LoggerFactory.getLogger(DictKindResource.class);
        
    @Inject
    private DictKindRepository dictKindRepository;
    @Inject
    private DictRepository dictRepository;
    
    /**
     * POST  /dict-kinds : Create a new dictKind.
     *
     * @param dictKind the dictKind to create
     * @return the ResponseEntity with status 201 (Created) and with body the new dictKind, or with status 400 (Bad Request) if the dictKind has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/dict-kinds",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DictKind> createDictKind(@RequestBody DictKind dictKind) throws URISyntaxException {
        log.debug("REST request to save DictKind : {}", dictKind);
        if (dictKind.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("dictKind", "idexists", "A new dictKind cannot already have an ID")).body(null);
        }
        DictKind result = dictKindRepository.save(dictKind);
        return ResponseEntity.created(new URI("/api/dict-kinds/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("dictKind", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /dict-kinds : Updates an existing dictKind.
     *
     * @param dictKind the dictKind to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated dictKind,
     * or with status 400 (Bad Request) if the dictKind is not valid,
     * or with status 500 (Internal Server Error) if the dictKind couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/dict-kinds",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DictKind> updateDictKind(@RequestBody DictKind dictKind) throws URISyntaxException {
        log.debug("REST request to update DictKind : {}", dictKind);
        if (dictKind.getId() == null) {
            return createDictKind(dictKind);
        }
        DictKind result = dictKindRepository.save(dictKind);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("dictKind", dictKind.getId().toString()))
            .body(result);
    }

    /**
     * GET  /dict-kinds : get all the dictKinds.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of dictKinds in body
     */
    @RequestMapping(value = "/dict-kinds",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<DictKind> getAllDictKinds() {
        log.debug("REST request to get all DictKinds");
        List<DictKind> dictKinds = dictKindRepository.findAll();
        return dictKinds;
    }

    /**
     * GET  /dict-kinds/:id : get the "id" dictKind.
     *
     * @param id the id of the dictKind to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the dictKind, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/dict-kinds/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DictKind> getDictKind(@PathVariable Long id) {
        log.debug("REST request to get DictKind : {}", id);
        DictKind dictKind = dictKindRepository.findOne(id);
        return Optional.ofNullable(dictKind)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /dict-kinds/:id : delete the "id" dictKind.
     *
     * @param id the id of the dictKind to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/dict-kinds/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDictKind(@PathVariable Long id) {
        log.debug("REST request to delete DictKind : {}", id);
        dictKindRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("dictKind", id.toString())).build();
    }
    
    /**
     * GET  /dict-kinds/{id}/dicts : get all the dicts of dictKind.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of dicts in body
     */
    @RequestMapping(value = "/dict-kinds/{id}/dicts",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Dict> getAllDictsByDictKind(@PathVariable Long id) {
        log.debug("REST request to get all Dicts of dictKind {}",id);
        Dict example = new Dict();
        DictKind exampleDictKind = new DictKind();
        exampleDictKind.setId(id);
        example.setDictKind(exampleDictKind);
        List<Dict> dicts = dictRepository.findAll(Example.of(example));
        return dicts;
    }

}
