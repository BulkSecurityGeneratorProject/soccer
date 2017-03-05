package gl.linpeng.soccer.web.rest.ext;

import gl.linpeng.soccer.domain.Club;
import gl.linpeng.soccer.domain.Game;
import gl.linpeng.soccer.domain.Team;
import gl.linpeng.soccer.repository.TeamRepository;
import gl.linpeng.soccer.repository.ext.GameRepositoryExt;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
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

	@Inject
	private TeamRepository teamRepository;

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

	/**
	 * GET /clubs/:id/nextGame : get next game of club "id".
	 *
	 * @param id
	 *            the id of the club
	 * @return the next game
	 */
	@RequestMapping(value = "/clubs/{id}/nextgame", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public Game getNextGame(@PathVariable Long id) {
		log.debug("REST request to get next game of club : {}", id);
		List<Game> games = gameRepositoryExt.findNextGamesByClub(id,
				new PageRequest(0, 1));
		Game game = null;
		if (games.size() > 0) {
			game = games.get(0);
		}
		return game;
	}

	/**
	 * GET /clubs/:id/teams : get all team of club "id".
	 *
	 * @param id
	 *            the id of the club
	 * @return team list
	 */
	@RequestMapping(value = "/clubs/{id}/teams", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public List<Team> getClubTeams(@PathVariable Long id) {
		Club club = new Club();
		Team exampleTeam = new Team();
		club.setId(id);
		exampleTeam.setClub(club);
		List<Team> teams = teamRepository.findAll(Example.of(exampleTeam));
		return teams;
	}
}
