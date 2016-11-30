package gl.linpeng.soccer.web.rest.ext;

import gl.linpeng.soccer.domain.ResultData;
import gl.linpeng.soccer.domain.Squad;
import gl.linpeng.soccer.domain.SquadPlayer;
import gl.linpeng.soccer.repository.ResultDataRepository;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

/**
 * REST ext controller for managing Squad.
 * 
 * @author linpeng
 */
@RestController
@RequestMapping("/api")
public class SquadResourceExt {

	private final Logger log = LoggerFactory.getLogger(SquadResourceExt.class);

	@Inject
	private ResultDataRepository resultDataRepository;

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
