package gl.linpeng.soccer.web.rest.ext;

import gl.linpeng.soccer.domain.Dict;
import gl.linpeng.soccer.domain.DictKind;
import gl.linpeng.soccer.repository.DictRepository;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

/**
 * REST ext controller for managing DictKind.
 * 
 * @author linpeng
 */
@RestController
@RequestMapping("/api")
public class DictKindResourceExt {
	private final Logger log = LoggerFactory
			.getLogger(DictKindResourceExt.class);

	@Inject
	private DictRepository dictRepository;

	/**
	 * GET /dict-kinds/{id}/dicts : get all the dicts of dictKind.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of dicts in
	 *         body
	 */
	@RequestMapping(value = "/dict-kinds/{id}/dicts", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public List<Dict> getAllDictsByDictKind(@PathVariable Long id) {
		log.debug("REST request to get all Dicts of dictKind {}", id);
		Dict example = new Dict();
		DictKind exampleDictKind = new DictKind();
		exampleDictKind.setId(id);
		example.setDictKind(exampleDictKind);
		List<Dict> dicts = dictRepository.findAll(Example.of(example));
		return dicts;
	}
}
