package gl.linpeng.soccer.web.rest;

import com.codahale.metrics.annotation.Timed;
import gl.linpeng.soccer.domain.RankingRule;

import gl.linpeng.soccer.repository.RankingRuleRepository;
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
 * REST controller for managing RankingRule.
 */
@RestController
@RequestMapping("/api")
public class RankingRuleResource {

    private final Logger log = LoggerFactory.getLogger(RankingRuleResource.class);
        
    @Inject
    private RankingRuleRepository rankingRuleRepository;

    /**
     * POST  /ranking-rules : Create a new rankingRule.
     *
     * @param rankingRule the rankingRule to create
     * @return the ResponseEntity with status 201 (Created) and with body the new rankingRule, or with status 400 (Bad Request) if the rankingRule has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/ranking-rules",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RankingRule> createRankingRule(@RequestBody RankingRule rankingRule) throws URISyntaxException {
        log.debug("REST request to save RankingRule : {}", rankingRule);
        if (rankingRule.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("rankingRule", "idexists", "A new rankingRule cannot already have an ID")).body(null);
        }
        RankingRule result = rankingRuleRepository.save(rankingRule);
        return ResponseEntity.created(new URI("/api/ranking-rules/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("rankingRule", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /ranking-rules : Updates an existing rankingRule.
     *
     * @param rankingRule the rankingRule to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated rankingRule,
     * or with status 400 (Bad Request) if the rankingRule is not valid,
     * or with status 500 (Internal Server Error) if the rankingRule couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/ranking-rules",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RankingRule> updateRankingRule(@RequestBody RankingRule rankingRule) throws URISyntaxException {
        log.debug("REST request to update RankingRule : {}", rankingRule);
        if (rankingRule.getId() == null) {
            return createRankingRule(rankingRule);
        }
        RankingRule result = rankingRuleRepository.save(rankingRule);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("rankingRule", rankingRule.getId().toString()))
            .body(result);
    }

    /**
     * GET  /ranking-rules : get all the rankingRules.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of rankingRules in body
     */
    @RequestMapping(value = "/ranking-rules",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<RankingRule> getAllRankingRules() {
        log.debug("REST request to get all RankingRules");
        List<RankingRule> rankingRules = rankingRuleRepository.findAll();
        return rankingRules;
    }

    /**
     * GET  /ranking-rules/:id : get the "id" rankingRule.
     *
     * @param id the id of the rankingRule to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the rankingRule, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/ranking-rules/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RankingRule> getRankingRule(@PathVariable Long id) {
        log.debug("REST request to get RankingRule : {}", id);
        RankingRule rankingRule = rankingRuleRepository.findOne(id);
        return Optional.ofNullable(rankingRule)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /ranking-rules/:id : delete the "id" rankingRule.
     *
     * @param id the id of the rankingRule to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/ranking-rules/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteRankingRule(@PathVariable Long id) {
        log.debug("REST request to delete RankingRule : {}", id);
        rankingRuleRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("rankingRule", id.toString())).build();
    }

}
