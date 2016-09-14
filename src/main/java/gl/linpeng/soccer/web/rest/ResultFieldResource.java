package gl.linpeng.soccer.web.rest;

import com.codahale.metrics.annotation.Timed;
import gl.linpeng.soccer.domain.ResultField;

import gl.linpeng.soccer.repository.ResultFieldRepository;
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
 * REST controller for managing ResultField.
 */
@RestController
@RequestMapping("/api")
public class ResultFieldResource {

    private final Logger log = LoggerFactory.getLogger(ResultFieldResource.class);
        
    @Inject
    private ResultFieldRepository resultFieldRepository;

    /**
     * POST  /result-fields : Create a new resultField.
     *
     * @param resultField the resultField to create
     * @return the ResponseEntity with status 201 (Created) and with body the new resultField, or with status 400 (Bad Request) if the resultField has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/result-fields",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ResultField> createResultField(@RequestBody ResultField resultField) throws URISyntaxException {
        log.debug("REST request to save ResultField : {}", resultField);
        if (resultField.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("resultField", "idexists", "A new resultField cannot already have an ID")).body(null);
        }
        ResultField result = resultFieldRepository.save(resultField);
        return ResponseEntity.created(new URI("/api/result-fields/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("resultField", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /result-fields : Updates an existing resultField.
     *
     * @param resultField the resultField to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated resultField,
     * or with status 400 (Bad Request) if the resultField is not valid,
     * or with status 500 (Internal Server Error) if the resultField couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/result-fields",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ResultField> updateResultField(@RequestBody ResultField resultField) throws URISyntaxException {
        log.debug("REST request to update ResultField : {}", resultField);
        if (resultField.getId() == null) {
            return createResultField(resultField);
        }
        ResultField result = resultFieldRepository.save(resultField);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("resultField", resultField.getId().toString()))
            .body(result);
    }

    /**
     * GET  /result-fields : get all the resultFields.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of resultFields in body
     */
    @RequestMapping(value = "/result-fields",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ResultField> getAllResultFields() {
        log.debug("REST request to get all ResultFields");
        List<ResultField> resultFields = resultFieldRepository.findAll();
        return resultFields;
    }

    /**
     * GET  /result-fields/:id : get the "id" resultField.
     *
     * @param id the id of the resultField to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the resultField, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/result-fields/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ResultField> getResultField(@PathVariable Long id) {
        log.debug("REST request to get ResultField : {}", id);
        ResultField resultField = resultFieldRepository.findOne(id);
        return Optional.ofNullable(resultField)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /result-fields/:id : delete the "id" resultField.
     *
     * @param id the id of the resultField to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/result-fields/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteResultField(@PathVariable Long id) {
        log.debug("REST request to delete ResultField : {}", id);
        resultFieldRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("resultField", id.toString())).build();
    }

}
