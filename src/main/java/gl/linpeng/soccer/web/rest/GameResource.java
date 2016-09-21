package gl.linpeng.soccer.web.rest;

import gl.linpeng.soccer.domain.Game;
import gl.linpeng.soccer.domain.Player;
import gl.linpeng.soccer.domain.Squad;
import gl.linpeng.soccer.domain.SquadPlayer;
import gl.linpeng.soccer.domain.Team;
import gl.linpeng.soccer.repository.GameRepository;
import gl.linpeng.soccer.repository.SquadPlayerRepository;
import gl.linpeng.soccer.repository.SquadRepository;
import gl.linpeng.soccer.repository.TeamRepository;
import gl.linpeng.soccer.web.rest.util.HeaderUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
 * REST controller for managing Game.
 */
@RestController
@RequestMapping("/api")
public class GameResource {

	private final Logger log = LoggerFactory.getLogger(GameResource.class);

	@Inject
	private GameRepository gameRepository;
	@Inject
	private TeamRepository teamRepository;
	@Inject
	private SquadRepository squadRepository;
	@Inject
	private SquadPlayerRepository squadPlayerRepository;

	/**
	 * POST /games : Create a new game.
	 *
	 * @param game
	 *            the game to create
	 * @return the ResponseEntity with status 201 (Created) and with body the
	 *         new game, or with status 400 (Bad Request) if the game has
	 *         already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@RequestMapping(value = "/games", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Game> createGame(@RequestBody Game game)
			throws URISyntaxException {
		log.debug("REST request to save Game : {}", game);
		if (game.getId() != null) {
			return ResponseEntity
					.badRequest()
					.headers(
							HeaderUtil.createFailureAlert("game", "idexists",
									"A new game cannot already have an ID"))
					.body(null);
		}
		Game result = gameRepository.save(game);
		return ResponseEntity
				.created(new URI("/api/games/" + result.getId()))
				.headers(
						HeaderUtil.createEntityCreationAlert("game", result
								.getId().toString())).body(result);
	}

	/**
	 * PUT /games : Updates an existing game.
	 *
	 * @param game
	 *            the game to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         game, or with status 400 (Bad Request) if the game is not valid,
	 *         or with status 500 (Internal Server Error) if the game couldnt be
	 *         updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@RequestMapping(value = "/games", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Game> updateGame(@RequestBody Game game)
			throws URISyntaxException {
		log.debug("REST request to update Game : {}", game);
		if (game.getId() == null) {
			return createGame(game);
		}
		Game result = gameRepository.save(game);
		return ResponseEntity
				.ok()
				.headers(
						HeaderUtil.createEntityUpdateAlert("game", game.getId()
								.toString())).body(result);
	}

	/**
	 * GET /games : get all the games.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of games in
	 *         body
	 */
	@RequestMapping(value = "/games", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public List<Game> getAllGames() {
		log.debug("REST request to get all Games");
		List<Game> games = gameRepository.findAll();
		return games;
	}

	/**
	 * GET /games/:id : get the "id" game.
	 *
	 * @param id
	 *            the id of the game to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the game,
	 *         or with status 404 (Not Found)
	 */
	@RequestMapping(value = "/games/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Game> getGame(@PathVariable Long id) {
		log.debug("REST request to get Game : {}", id);
		Game game = gameRepository.findOne(id);
		return Optional.ofNullable(game)
				.map(result -> new ResponseEntity<>(result, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	/**
	 * DELETE /games/:id : delete the "id" game.
	 *
	 * @param id
	 *            the id of the game to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@RequestMapping(value = "/games/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Void> deleteGame(@PathVariable Long id) {
		log.debug("REST request to delete Game : {}", id);
		gameRepository.delete(id);
		return ResponseEntity
				.ok()
				.headers(
						HeaderUtil.createEntityDeletionAlert("game",
								id.toString())).build();
	}

	/**
	 * get game squad by game id and team id
	 * 
	 * @param id
	 *            game id
	 * @param tid
	 *            team id
	 * @return
	 */
	@RequestMapping(value = "/games/{id}/squad/{tid}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public List<SquadPlayer> getGameSquad(@PathVariable Long id,
			@PathVariable Long tid) {
		log.debug("REST request to get Game home squad : {},{}", id, tid);

		Game exampleGame = new Game();
		exampleGame.setId(id);
		Team exampleTeam = new Team();
		exampleTeam.setId(tid);
		Squad exampleSquad = new Squad();
		exampleSquad.setGame(exampleGame);
		exampleSquad.setTeam(exampleTeam);

		SquadPlayer example = new SquadPlayer();
		example.setSquad(exampleSquad);
		List<SquadPlayer> squadPlayers = squadPlayerRepository.findAll(Example
				.of(example));
		return squadPlayers;
	}

	/**
	 * POST /games : Create a new game-squad.
	 *
	 * @param game
	 *            the game to create
	 * @return the ResponseEntity with status 201 (Created) and with body the
	 *         new game, or with status 400 (Bad Request) if the game has
	 *         already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@RequestMapping(value = "/games/squad", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public List<SquadPlayer> createGameSquad(@RequestBody Map map)
			throws URISyntaxException {
		log.debug("REST request to save Game squad : {}", map);

		// pre-data
		String gameId = map.get("id").toString();
		String teamId = map.get("team").toString();
		Object collectionObject = map.get("players");

		// step0 is squad entity exist
		Game game = gameRepository.findOne(Long.valueOf(gameId));
		Team team = teamRepository.findOne(Long.valueOf(teamId));
		// validate is this valid game team
		if (!team.getId().equals(game.getHomeTeam().getId())
				&& !team.getId().equals(game.getRoadTeam().getId())) {
			log.error(
					"Holly shit,this is not a valid team of this game : {},team {}",
					game.getId(), team.getId());
			Map errorMap = new HashMap<String, Object>();
			errorMap.put(
					"errormsg",
					"This is not a valid team of this game. game: "
							+ game.getId() + ",team: " + team.getId());
			return null;
		}
		Squad squad = new Squad();
		Game exampleGame = new Game();
		exampleGame.setId(game.getId());

		Team exampleTeam = new Team();
		exampleTeam.setId(team.getId());

		squad.setGame(exampleGame);
		squad.setTeam(exampleTeam);
		Squad existSquad = squadRepository.findOne(Example.of(squad));
		// step1 get or save squad
		if (null == existSquad) {
			// create a new squad
			squad = squadRepository.save(squad);
		} else {
			squad = existSquad;
		}
		// clear all squrd-player of this squrd
		SquadPlayer example = new SquadPlayer();
		example.setSquad(squad);
		List<SquadPlayer> squadPlayers = squadPlayerRepository.findAll(Example
				.of(example));
		squadPlayerRepository.deleteInBatch(squadPlayers);

		// step2 save squad-players

		if (collectionObject instanceof List) {
			List<SquadPlayer> entities = new ArrayList<SquadPlayer>();
			List list = (List) collectionObject;
			for (Object obj : list) {
				String playerId = null;
				if (obj instanceof Map) {
					Map objMap = (Map) obj;
					playerId = objMap.get("id").toString();
				}
				SquadPlayer squadPlayer = new SquadPlayer();
				squadPlayer.setIsSubstitute(false);
				squadPlayer.setSquad(squad);
				Player player = new Player();
				player.setId(Long.valueOf(playerId));
				squadPlayer.setPlayer(player);
				// get playerNumber from team number
				// squadPlayer.setPlayerNumber(playerNumber);
				entities.add(squadPlayer);
			}

			squadPlayers = squadPlayerRepository.save(entities);
		}

		// step3 return game entity
		return squadPlayers;
	}

}
