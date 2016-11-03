package gl.linpeng.soccer.web.rest;

import gl.linpeng.soccer.domain.Association;
import gl.linpeng.soccer.domain.Club;
import gl.linpeng.soccer.domain.Division;
import gl.linpeng.soccer.domain.DivisionEvent;
import gl.linpeng.soccer.domain.Game;
import gl.linpeng.soccer.domain.Player;
import gl.linpeng.soccer.domain.Team;
import gl.linpeng.soccer.domain.Timeslot;
import gl.linpeng.soccer.repository.AssociationRepository;
import gl.linpeng.soccer.repository.ClubRepository;
import gl.linpeng.soccer.repository.DivisionEventRepository;
import gl.linpeng.soccer.repository.DivisionRepository;
import gl.linpeng.soccer.repository.GameRepository;
import gl.linpeng.soccer.repository.PlayerRepository;
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
 * REST controller for managing Association.
 */
@RestController
@RequestMapping("/api")
public class AssociationResource {

	private final Logger log = LoggerFactory
			.getLogger(AssociationResource.class);

	@Inject
	private AssociationRepository associationRepository;
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
	 * POST /associations : Create a new association.
	 *
	 * @param association
	 *            the association to create
	 * @return the ResponseEntity with status 201 (Created) and with body the
	 *         new association, or with status 400 (Bad Request) if the
	 *         association has already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@RequestMapping(value = "/associations", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Association> createAssociation(
			@RequestBody Association association) throws URISyntaxException {
		log.debug("REST request to save Association : {}", association);
		if (association.getId() != null) {
			return ResponseEntity
					.badRequest()
					.headers(
							HeaderUtil
									.createFailureAlert("association",
											"idexists",
											"A new association cannot already have an ID"))
					.body(null);
		}
		Association result = associationRepository.save(association);
		return ResponseEntity
				.created(new URI("/api/associations/" + result.getId()))
				.headers(
						HeaderUtil.createEntityCreationAlert("association",
								result.getId().toString())).body(result);
	}

	/**
	 * PUT /associations : Updates an existing association.
	 *
	 * @param association
	 *            the association to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         association, or with status 400 (Bad Request) if the association
	 *         is not valid, or with status 500 (Internal Server Error) if the
	 *         association couldnt be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@RequestMapping(value = "/associations", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Association> updateAssociation(
			@RequestBody Association association) throws URISyntaxException {
		log.debug("REST request to update Association : {}", association);
		if (association.getId() == null) {
			return createAssociation(association);
		}
		Association result = associationRepository.save(association);
		return ResponseEntity
				.ok()
				.headers(
						HeaderUtil.createEntityUpdateAlert("association",
								association.getId().toString())).body(result);
	}

	/**
	 * GET /associations : get all the associations.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         associations in body
	 */
	@RequestMapping(value = "/associations", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public List<Association> getAllAssociations() {
		log.debug("REST request to get all Associations");
		List<Association> associations = associationRepository.findAll();
		return associations;
	}

	/**
	 * GET /associations/:id : get the "id" association.
	 *
	 * @param id
	 *            the id of the association to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         association, or with status 404 (Not Found)
	 */
	@RequestMapping(value = "/associations/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Association> getAssociation(@PathVariable Long id) {
		log.debug("REST request to get Association : {}", id);
		Association association = associationRepository.findOne(id);
		return Optional.ofNullable(association)
				.map(result -> new ResponseEntity<>(result, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	/**
	 * DELETE /associations/:id : delete the "id" association.
	 *
	 * @param id
	 *            the id of the association to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@RequestMapping(value = "/associations/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Void> deleteAssociation(@PathVariable Long id) {
		log.debug("REST request to delete Association : {}", id);
		associationRepository.delete(id);
		return ResponseEntity
				.ok()
				.headers(
						HeaderUtil.createEntityDeletionAlert("association",
								id.toString())).build();
	}

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
