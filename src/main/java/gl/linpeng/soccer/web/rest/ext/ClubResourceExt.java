package gl.linpeng.soccer.web.rest.ext;

import gl.linpeng.soccer.domain.Game;
import gl.linpeng.soccer.repository.ext.GameRepositoryExt;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

/**
 * REST ext controller for managing Club.
 * 
 * @author linpeng
 */
@RestController
@RequestMapping("/api")
public class ClubResourceExt {

	private final Logger log = LoggerFactory.getLogger(ClubResourceExt.class);

	@Inject
	private GameRepositoryExt gameRepositoryExt;

	/**
	 * GET /clubs : get all the club games.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of games in
	 *         body
	 */
	@RequestMapping(value = "/clubs/{id}/games", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public List<Game> getClubAllGames(@PathVariable Long id) {
		log.debug("REST request to get all club games,{}", id);
		// How recent days game want to get
		int recentDays = 30;
		List<Game> games = gameRepositoryExt.findGamesByClub(id, recentDays);
		return games;
	}
}
