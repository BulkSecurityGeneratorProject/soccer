package gl.linpeng.soccer.web.rest;

import com.codahale.metrics.annotation.Timed;
import gl.linpeng.soccer.domain.ResultData;

import gl.linpeng.soccer.repository.ResultDataRepository;
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
 * REST controller for managing ResultData.
 */
@RestController
@RequestMapping("/api")
public class ResultDataResource {

    private final Logger log = LoggerFactory.getLogger(ResultDataResource.class);
        
    @Inject
    private ResultDataRepository resultDataRepository;

    /**
     * POST  /result-data : Create a new resultData.
     *
     * @param resultData the resultData to create
     * @return the ResponseEntity with status 201 (Created) and with body the new resultData, or with status 400 (Bad Request) if the resultData has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/result-data",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ResultData> createResultData(@RequestBody ResultData resultData) throws URISyntaxException {
        log.debug("REST request to save ResultData : {}", resultData);
        if (resultData.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("resultData", "idexists", "A new resultData cannot already have an ID")).body(null);
        }
        ResultData result = resultDataRepository.save(resultData);
        return ResponseEntity.created(new URI("/api/result-data/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("resultData", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /result-data : Updates an existing resultData.
     *
     * @param resultData the resultData to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated resultData,
     * or with status 400 (Bad Request) if the resultData is not valid,
     * or with status 500 (Internal Server Error) if the resultData couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/result-data",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ResultData> updateResultData(@RequestBody ResultData resultData) throws URISyntaxException {
        log.debug("REST request to update ResultData : {}", resultData);
        if (resultData.getId() == null) {
            return createResultData(resultData);
        }
        ResultData result = resultDataRepository.save(resultData);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("resultData", resultData.getId().toString()))
            .body(result);
    }

    /**
     * GET  /result-data : get all the resultData.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of resultData in body
     */
    @RequestMapping(value = "/result-data",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ResultData> getAllResultData() {
        log.debug("REST request to get all ResultData");
        List<ResultData> resultData = resultDataRepository.findAll();
        return resultData;
    }

    /**
     * GET  /result-data/:id : get the "id" resultData.
     *
     * @param id the id of the resultData to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the resultData, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/result-data/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ResultData> getResultData(@PathVariable Long id) {
        log.debug("REST request to get ResultData : {}", id);
        ResultData resultData = resultDataRepository.findOne(id);
        return Optional.ofNullable(resultData)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /result-data/:id : delete the "id" resultData.
     *
     * @param id the id of the resultData to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/result-data/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteResultData(@PathVariable Long id) {
        log.debug("REST request to delete ResultData : {}", id);
        resultDataRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("resultData", id.toString())).build();
    }

}
