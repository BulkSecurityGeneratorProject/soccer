package gl.linpeng.soccer.web.rest;

import gl.linpeng.soccer.domain.Player;
import gl.linpeng.soccer.domain.Team;
import gl.linpeng.soccer.repository.PlayerRepository;
import gl.linpeng.soccer.web.rest.util.HeaderUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing Player.
 */
@RestController
@RequestMapping("/api")
public class PlayerResource {

	private final Logger log = LoggerFactory.getLogger(PlayerResource.class);

	@Inject
	private PlayerRepository playerRepository;
	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * POST /players : Create a new player.
	 *
	 * @param player
	 *            the player to create
	 * @return the ResponseEntity with status 201 (Created) and with body the
	 *         new player, or with status 400 (Bad Request) if the player has
	 *         already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@RequestMapping(value = "/players", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Player> createPlayer(@RequestBody Player player)
			throws URISyntaxException {
		log.debug("REST request to save Player : {}", player);
		if (player.getId() != null) {
			return ResponseEntity
					.badRequest()
					.headers(
							HeaderUtil.createFailureAlert("player", "idexists",
									"A new player cannot already have an ID"))
					.body(null);
		}
		Player result = playerRepository.save(player);
		return ResponseEntity
				.created(new URI("/api/players/" + result.getId()))
				.headers(
						HeaderUtil.createEntityCreationAlert("player", result
								.getId().toString())).body(result);
	}

	/**
	 * PUT /players : Updates an existing player.
	 *
	 * @param player
	 *            the player to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         player, or with status 400 (Bad Request) if the player is not
	 *         valid, or with status 500 (Internal Server Error) if the player
	 *         couldnt be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@RequestMapping(value = "/players", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Player> updatePlayer(@RequestBody Player player)
			throws URISyntaxException {
		log.debug("REST request to update Player : {}", player);
		if (player.getId() == null) {
			return createPlayer(player);
		}
		Player result = playerRepository.save(player);
		return ResponseEntity
				.ok()
				.headers(
						HeaderUtil.createEntityUpdateAlert("player", player
								.getId().toString())).body(result);
	}

	/**
	 * GET /players : get all the players.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of players
	 *         in body
	 */
	@RequestMapping(value = "/players", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public List<Player> getAllPlayers() {
		log.debug("REST request to get all Players");
		List<Player> players = playerRepository.findAll();
		return players;
	}

	/**
	 * GET /players/:id : get the "id" player.
	 *
	 * @param id
	 *            the id of the player to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the player,
	 *         or with status 404 (Not Found)
	 */
	@RequestMapping(value = "/players/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Player> getPlayer(@PathVariable Long id) {
		log.debug("REST request to get Player : {}", id);
		Player player = playerRepository.findOne(id);
		return Optional.ofNullable(player)
				.map(result -> new ResponseEntity<>(result, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	/**
	 * DELETE /players/:id : delete the "id" player.
	 *
	 * @param id
	 *            the id of the player to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@RequestMapping(value = "/players/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Void> deletePlayer(@PathVariable Long id) {
		log.debug("REST request to delete Player : {}", id);
		playerRepository.delete(id);
		return ResponseEntity
				.ok()
				.headers(
						HeaderUtil.createEntityDeletionAlert("player",
								id.toString())).build();
	}

	/**
	 * GET /division-events : get the player career data.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of career
	 *         data in body
	 */
	@RequestMapping(value = "/players/{id}/career", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public List<Player> getPlayerCareer(@PathVariable Long id) {
		log.debug("REST request to get career data of Player: {}", id);

		String sql = String
				.format("SELECT "
						+ "p.id,"
						+ "p.NAME,"
						+ "d.NAME as division_name,"
						+ "s.NAME as season_name,"
						+ "tm.name as team_name,"
						+ "sum(CASE rf.name WHEN '进球' THEN rd.value END)  goal,"
						+ "sum(CASE rf.name WHEN '助攻' THEN rd.value END)  assist "
						+ "FROM "
						+ "Player p "
						+ "LEFT OUTER JOIN SQUAD_PLAYER sp "
						+ "LEFT OUTER JOIN Result_data rd "
						+ "LEFT OUTER JOIN Game g "
						+ "LEFT OUTER JOIN Timeslot tl "
						+ "LEFT OUTER JOIN division_event de "
						+ "LEFT OUTER JOIN season s ON s.id = de.season_id "
						+ "LEFT OUTER JOIN division d ON d.id = de.division_id "
						+ "ON de.id = tl.division_event_id ON tl.id = g.timeslot_id "
						+ "ON g.id = rd.game_id "
						+ "left outer join result_field rf on rf.id=rd.result_field_id ON rd.squad_player_id = sp.id "
						+ "left outer join squad sq "
						+ "left outer join Team tm on tm.id = sq.team_id on sq.id = sp.squad_id "
						+ "ON sp.player_id = p.id WHERE p.id = "
						+ id
						+ " group by p.id,p.NAME,d.NAME,s.name order by s.name desc");
		Query query = entityManager.createNativeQuery(sql);
		return query.getResultList();
	}

}
