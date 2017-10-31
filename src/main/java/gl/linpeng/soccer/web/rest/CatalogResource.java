package gl.linpeng.soccer.web.rest;

import com.codahale.metrics.annotation.Timed;
import gl.linpeng.soccer.domain.Catalog;

import gl.linpeng.soccer.repository.CatalogRepository;
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
 * REST controller for managing Catalog.
 */
@RestController
@RequestMapping("/api")
public class CatalogResource {

    private final Logger log = LoggerFactory.getLogger(CatalogResource.class);
        
    @Inject
    private CatalogRepository catalogRepository;

    /**
     * POST  /catalogs : Create a new catalog.
     *
     * @param catalog the catalog to create
     * @return the ResponseEntity with status 201 (Created) and with body the new catalog, or with status 400 (Bad Request) if the catalog has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/catalogs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Catalog> createCatalog(@RequestBody Catalog catalog) throws URISyntaxException {
        log.debug("REST request to save Catalog : {}", catalog);
        if (catalog.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("catalog", "idexists", "A new catalog cannot already have an ID")).body(null);
        }
        Catalog result = catalogRepository.save(catalog);
        return ResponseEntity.created(new URI("/api/catalogs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("catalog", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /catalogs : Updates an existing catalog.
     *
     * @param catalog the catalog to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated catalog,
     * or with status 400 (Bad Request) if the catalog is not valid,
     * or with status 500 (Internal Server Error) if the catalog couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/catalogs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Catalog> updateCatalog(@RequestBody Catalog catalog) throws URISyntaxException {
        log.debug("REST request to update Catalog : {}", catalog);
        if (catalog.getId() == null) {
            return createCatalog(catalog);
        }
        Catalog result = catalogRepository.save(catalog);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("catalog", catalog.getId().toString()))
            .body(result);
    }

    /**
     * GET  /catalogs : get all the catalogs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of catalogs in body
     */
    @RequestMapping(value = "/catalogs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Catalog> getAllCatalogs() {
        log.debug("REST request to get all Catalogs");
        List<Catalog> catalogs = catalogRepository.findAll();
        return catalogs;
    }

    /**
     * GET  /catalogs/:id : get the "id" catalog.
     *
     * @param id the id of the catalog to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the catalog, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/catalogs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Catalog> getCatalog(@PathVariable Long id) {
        log.debug("REST request to get Catalog : {}", id);
        Catalog catalog = catalogRepository.findOne(id);
        return Optional.ofNullable(catalog)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /catalogs/:id : delete the "id" catalog.
     *
     * @param id the id of the catalog to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/catalogs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCatalog(@PathVariable Long id) {
        log.debug("REST request to delete Catalog : {}", id);
        catalogRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("catalog", id.toString())).build();
    }

}
