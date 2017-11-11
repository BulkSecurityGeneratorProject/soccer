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

import java.net.URISyntaxException;
import java.util.List;

import javax.inject.Inject;

import gl.linpeng.soccer.repository.ext.GameRepositoryExt;
import gl.linpeng.soccer.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    private GameRepositoryExt gameRepositoryExt;
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
	 * @param associationId
	 *            the id of the association to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the clubs,
	 *         or with status 404 (Not Found)
	 */
	@RequestMapping(value = "/associations/{associationId}/players", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<List<Player>> getPlayers(@PathVariable Long associationId,Player player,Pageable pageable) throws URISyntaxException {
		log.debug("REST request to get Association Players: {}", associationId);
		Player example = new Player();
		Association exampleAssociation = new Association();
		exampleAssociation.setId(associationId);
		Team exampleTeam = new Team();
		Club exampleClub = new Club();
		exampleClub.setAssociation(exampleAssociation);
		exampleTeam.setClub(exampleClub);
		example.setTeam(exampleTeam);
		exampleClub.setAssociation(exampleAssociation);
        example.setTeam(exampleTeam);
        Page<Player> page = playerRepository.findAll(Example.of(example),pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/associations/"+associationId+"/players");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}


    @RequestMapping(value = "/associations/{associationId}/fixtures", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Game>> getFixtures(@PathVariable Long associationId,Game game,Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get Association Games: {}", associationId);
//        Game example = new Game();
//        DivisionEvent exampleDivisionEvent = new DivisionEvent();
//        Association exampleAssociation = new Association();
//        exampleAssociation.setId(associationId);
//        Division exampleDivision = new Division();
//        exampleDivision.setAssociation(exampleAssociation);
//        exampleDivisionEvent.setDivision(exampleDivision);
//        Timeslot exampleTimeslot = new Timeslot();
//        exampleTimeslot.setDivisionEvent(exampleDivisionEvent);
//        example.setTimeslot(exampleTimeslot);

        Page<Game> page = null;
        if(game!=null && game.getTimeslot()!=null
            && game.getTimeslot().getDivisionEvent()!=null
            && game.getTimeslot().getDivisionEvent().getDivision()!=null
            && game.getTimeslot().getDivisionEvent().getDivision().getId()!=null){
            page = gameRepositoryExt.findNextGamesByDivision(game.getTimeslot().getDivisionEvent().getDivision().getId(),pageable);
        }else{
            page = gameRepositoryExt.findNextGamesByAssociation(associationId,pageable);
        }

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/associations/"+associationId+"/fixtures");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


    @RequestMapping(value = "/associations/{associationId}/results", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Game>> getResults(@PathVariable Long associationId,Game game,Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get Association Games: {}", associationId);
        Page<Game> page = null;
        if(game!=null && game.getTimeslot()!=null
            && game.getTimeslot().getDivisionEvent()!=null
            && game.getTimeslot().getDivisionEvent().getDivision()!=null
            && game.getTimeslot().getDivisionEvent().getDivision().getId()!=null){
            page = gameRepositoryExt.findPassedGamesByDivision(game.getTimeslot().getDivisionEvent().getDivision().getId(),pageable);
        }else{
            page = gameRepositoryExt.findPassedGamesByAssociation(associationId,pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/associations/"+associationId+"/results");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
}
