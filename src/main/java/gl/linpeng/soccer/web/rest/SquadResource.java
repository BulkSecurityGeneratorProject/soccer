package gl.linpeng.soccer.web.rest;

import gl.linpeng.soccer.domain.ResultData;
import gl.linpeng.soccer.domain.Squad;
import gl.linpeng.soccer.domain.SquadPlayer;
import gl.linpeng.soccer.repository.ResultDataRepository;
import gl.linpeng.soccer.repository.SquadRepository;
import gl.linpeng.soccer.web.rest.util.HeaderUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

/**
 * REST controller for managing Squad.
 */
@RestController
@RequestMapping("/api")
public class SquadResource {

	private final Logger log = LoggerFactory.getLogger(SquadResource.class);

	@Inject
	private SquadRepository squadRepository;
	@Inject
	private ResultDataRepository resultDataRepository;

	/**
	 * POST /squads : Create a new squad.
	 *
	 * @param squad
	 *            the squad to create
	 * @return the ResponseEntity with status 201 (Created) and with body the
	 *         new squad, or with status 400 (Bad Request) if the squad has
	 *         already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@RequestMapping(value = "/squads", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Squad> createSquad(@RequestBody Squad squad)
			throws URISyntaxException {
		log.debug("REST request to save Squad : {}", squad);
		if (squad.getId() != null) {
			return ResponseEntity
					.badRequest()
					.headers(
							HeaderUtil.createFailureAlert("squad", "idexists",
									"A new squad cannot already have an ID"))
					.body(null);
		}
		Squad result = squadRepository.save(squad);
		return ResponseEntity
				.created(new URI("/api/squads/" + result.getId()))
				.headers(
						HeaderUtil.createEntityCreationAlert("squad", result
								.getId().toString())).body(result);
	}

	/**
	 * PUT /squads : Updates an existing squad.
	 *
	 * @param squad
	 *            the squad to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         squad, or with status 400 (Bad Request) if the squad is not
	 *         valid, or with status 500 (Internal Server Error) if the squad
	 *         couldnt be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@RequestMapping(value = "/squads", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Squad> updateSquad(@RequestBody Squad squad)
			throws URISyntaxException {
		log.debug("REST request to update Squad : {}", squad);
		if (squad.getId() == null) {
			return createSquad(squad);
		}
		Squad result = squadRepository.save(squad);
		return ResponseEntity
				.ok()
				.headers(
						HeaderUtil.createEntityUpdateAlert("squad", squad
								.getId().toString())).body(result);
	}

	/**
	 * GET /squads : get all the squads.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of squads in
	 *         body
	 */
	@RequestMapping(value = "/squads", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public List<Squad> getAllSquads() {
		log.debug("REST request to get all Squads");
		List<Squad> squads = squadRepository.findAll();
		return squads;
	}

	/**
	 * GET /squads/:id : get the "id" squad.
	 *
	 * @param id
	 *            the id of the squad to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the squad,
	 *         or with status 404 (Not Found)
	 */
	@RequestMapping(value = "/squads/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Squad> getSquad(@PathVariable Long id) {
		log.debug("REST request to get Squad : {}", id);
		Squad squad = squadRepository.findOne(id);
		return Optional.ofNullable(squad)
				.map(result -> new ResponseEntity<>(result, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	/**
	 * DELETE /squads/:id : delete the "id" squad.
	 *
	 * @param id
	 *            the id of the squad to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@RequestMapping(value = "/squads/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Void> deleteSquad(@PathVariable Long id) {
		log.debug("REST request to delete Squad : {}", id);
		squadRepository.delete(id);
		return ResponseEntity
				.ok()
				.headers(
						HeaderUtil.createEntityDeletionAlert("squad",
								id.toString())).build();
	}

	/**
	 * GET /squads : get all the result data of squad.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of result
	 *         data in body
	 */
	@RequestMapping(value = "/squads/{id}/result-data", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public List<ResultData> getAllSquadResultData(@PathVariable Long id) {
		log.debug("REST request to get all Squad result data {}", id);
		ResultData example = new ResultData();
		SquadPlayer exampleSquadPlayer = new SquadPlayer();
		Squad exampleSquad = new Squad();
		exampleSquad.setId(id);
		exampleSquadPlayer.setSquad(exampleSquad);
		example.setSquadPlayer(exampleSquadPlayer);
		List<ResultData> resultDatas = resultDataRepository.findAll(Example
				.of(example));
		return resultDatas;
	}

	/**
	 * POST /squads : save all the result data of squad.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of result
	 *         data in body
	 */
	@RequestMapping(value = "/squads/{id}/result-data", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public List<ResultData> saveAllSquadResultData(@PathVariable Long id,
			@RequestBody List<ResultData> resultDatas) {
		log.debug("REST request to save all Squad result data {} , {}", id,
				resultDatas);
		resultDatas = resultDataRepository.save(resultDatas);
		// final : get LTS resultData of this squad
		return resultDatas;
	}

}
