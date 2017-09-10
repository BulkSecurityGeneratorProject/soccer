package gl.linpeng.soccer.web.rest.ext;

import gl.linpeng.soccer.domain.Association;
import gl.linpeng.soccer.domain.Club;
import gl.linpeng.soccer.domain.Division;
import gl.linpeng.soccer.domain.DivisionEvent;
import gl.linpeng.soccer.domain.Game;
import gl.linpeng.soccer.domain.Player;
import gl.linpeng.soccer.domain.Team;
import gl.linpeng.soccer.domain.Timeslot;
import gl.linpeng.soccer.repository.ClubRepository;
import gl.linpeng.soccer.repository.DivisionEventRepository;
import gl.linpeng.soccer.repository.DivisionRepository;
import gl.linpeng.soccer.repository.GameRepository;
import gl.linpeng.soccer.repository.PlayerRepository;

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
 * REST ext controller for managing Association.
 * 
 * @author linpeng
 *
 */
@RestController
@RequestMapping("/api")
public class AssociationResourceExt {
	private final Logger log = LoggerFactory
			.getLogger(AssociationResourceExt.class);

	@Inject
	private DivisionRepository divisionRepository;
	@Inject
	private DivisionEventRepository divisionEventRepository;
	@Inject
	private GameRepository gameRepository;
	@Inject
	private ClubRepository clubRepository;
	@Inject
	private PlayerRepository playerRepository;

	/**
	 * GET /associations/:id/games : get games by "id" of association.
	 *
	 * @param id
	 *            the id of the association to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the games,
	 *         or with status 404 (Not Found)
	 */
	@RequestMapping(value = "/associations/{id}/divisions", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public List<Division> getAssociationGames(@PathVariable Long id) {
		log.debug("REST request to get Association games: {}", id);
		Division example = new Division();
		Association exampleAssociation = new Association();
		exampleAssociation.setId(id);
		example.setAssociation(exampleAssociation);
		return divisionRepository.findAll(Example.of(example));
	}

	/**
	 * GET /associations/:id/division-events : get division-events by "id" of
	 * association.
	 *
	 * @param id
	 *            the id of the association to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         division-events, or with status 404 (Not Found)
	 */
	@RequestMapping(value = "/associations/{id}/division-events", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public List<DivisionEvent> getDivisionEvents(@PathVariable Long id) {
		log.debug("REST request to get Association DivisionEvents: {}", id);
		DivisionEvent example = new DivisionEvent();
		Association exampleAssociation = new Association();
		exampleAssociation.setId(id);
		Division exampleDivision = new Division();
		exampleDivision.setAssociation(exampleAssociation);
		example.setDivision(exampleDivision);
		return divisionEventRepository.findAll(Example.of(example));
	}

	/**
	 * GET /associations/:id/games : get games by "id" of association.
	 *
	 * @param id
	 *            the id of the association to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the games,
	 *         or with status 404 (Not Found)
	 */
	@RequestMapping(value = "/associations/{id}/games", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public List<Game> getGames(@PathVariable Long id) {
		log.debug("REST request to get Association Games: {}", id);
		Game example = new Game();
		DivisionEvent exampleDivisionEvent = new DivisionEvent();
		Association exampleAssociation = new Association();
		exampleAssociation.setId(id);
		Division exampleDivision = new Division();
		exampleDivision.setAssociation(exampleAssociation);
		exampleDivisionEvent.setDivision(exampleDivision);
		Timeslot exampleTimeslot = new Timeslot();
		exampleTimeslot.setDivisionEvent(exampleDivisionEvent);
		example.setTimeslot(exampleTimeslot);
		return gameRepository.findAll(Example.of(example));
	}

	/**
	 * GET /associations/:id/clubs : get clubs by "id" of association.
	 *
	 * @param id
	 *            the id of the association to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the clubs,
	 *         or with status 404 (Not Found)
	 */
	@RequestMapping(value = "/associations/{id}/clubs", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public List<Club> getClubs(@PathVariable Long id) {
		log.debug("REST request to get Association Clubs: {}", id);
		Club example = new Club();
		Association exampleAssociation = new Association();
		exampleAssociation.setId(id);
		example.setAssociation(exampleAssociation);
		return clubRepository.findAll(Example.of(example));
	}

	/**
	 * GET /associations/:id/players : get players by "id" of association.
	 *
	 * @param id
	 *            the id of the association to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the clubs,
	 *         or with status 404 (Not Found)
	 */
	@RequestMapping(value = "/associations/{id}/players", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public List<Player> getPlayers(@PathVariable Long id) {
		log.debug("REST request to get Association Players: {}", id);
		Player example = new Player();
		Association exampleAssociation = new Association();
		exampleAssociation.setId(id);
		Team exampleTeam = new Team();
		Club exampleClub = new Club();
		exampleClub.setAssociation(exampleAssociation);
		exampleTeam.setClub(exampleClub);
		example.setTeam(exampleTeam);
		return playerRepository.findAll(Example.of(example));
	}
}
