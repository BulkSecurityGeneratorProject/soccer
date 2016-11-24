package gl.linpeng.soccer.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import gl.linpeng.soccer.SoccerApp;
import gl.linpeng.soccer.domain.Game;
import gl.linpeng.soccer.repository.GameRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

/**
 * Test class for the GameResource REST controller.
 *
 * @see GameResource
 */
@RunWith(SpringRunner.class)

@SpringBootTest(classes = SoccerApp.class)

public class GameResourceIntTest {

    private static final LocalDateTime DEFAULT_START_AT = LocalDateTime.ofEpochSecond(0L, 0,ZoneOffset.ofHours(0));
    private static final LocalDateTime UPDATED_START_AT = LocalDateTime.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_HOME_SCORE = 1;
    private static final Integer UPDATED_HOME_SCORE = 2;

    private static final Integer DEFAULT_ROAD_SCORE = 1;
    private static final Integer UPDATED_ROAD_SCORE = 2;
    private static final String DEFAULT_NOTE = "AAAAA";
    private static final String UPDATED_NOTE = "BBBBB";

    private static final Integer DEFAULT_HOME_SCORE_HALF = 1;
    private static final Integer UPDATED_HOME_SCORE_HALF = 2;

    private static final Integer DEFAULT_ROAD_SCORE_HALF = 1;
    private static final Integer UPDATED_ROAD_SCORE_HALF = 2;

    @Inject
    private GameRepository gameRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restGameMockMvc;

    private Game game;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        GameResource gameResource = new GameResource();
        ReflectionTestUtils.setField(gameResource, "gameRepository", gameRepository);
        this.restGameMockMvc = MockMvcBuilders.standaloneSetup(gameResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Game createEntity(EntityManager em) {
        Game game = new Game();
        game.setStartAt(DEFAULT_START_AT);
        game.setHomeScore(DEFAULT_HOME_SCORE);
        game.setRoadScore(DEFAULT_ROAD_SCORE);
        game.setNote(DEFAULT_NOTE);
        game.setHomeScoreHalf(DEFAULT_HOME_SCORE_HALF);
        game.setRoadScoreHalf(DEFAULT_ROAD_SCORE_HALF);
        return game;
    }

    @Before
    public void initTest() {
        game = createEntity(em);
    }

    @Test
    @Transactional
    public void createGame() throws Exception {
        int databaseSizeBeforeCreate = gameRepository.findAll().size();

        // Create the Game

        restGameMockMvc.perform(post("/api/games")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(game)))
                .andExpect(status().isCreated());

        // Validate the Game in the database
        List<Game> games = gameRepository.findAll();
        assertThat(games).hasSize(databaseSizeBeforeCreate + 1);
        Game testGame = games.get(games.size() - 1);
        assertThat(testGame.getStartAt()).isEqualTo(DEFAULT_START_AT);
        assertThat(testGame.getHomeScore()).isEqualTo(DEFAULT_HOME_SCORE);
        assertThat(testGame.getRoadScore()).isEqualTo(DEFAULT_ROAD_SCORE);
        assertThat(testGame.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testGame.getHomeScoreHalf()).isEqualTo(DEFAULT_HOME_SCORE_HALF);
        assertThat(testGame.getRoadScoreHalf()).isEqualTo(DEFAULT_ROAD_SCORE_HALF);
    }

    @Test
    @Transactional
    public void getAllGames() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the games
        restGameMockMvc.perform(get("/api/games?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(game.getId().intValue())))
                .andExpect(jsonPath("$.[*].startAt").value(hasItem(DEFAULT_START_AT.toString())))
                .andExpect(jsonPath("$.[*].homeScore").value(hasItem(DEFAULT_HOME_SCORE)))
                .andExpect(jsonPath("$.[*].roadScore").value(hasItem(DEFAULT_ROAD_SCORE)))
                .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE.toString())))
                .andExpect(jsonPath("$.[*].homeScoreHalf").value(hasItem(DEFAULT_HOME_SCORE_HALF)))
                .andExpect(jsonPath("$.[*].roadScoreHalf").value(hasItem(DEFAULT_ROAD_SCORE_HALF)));
    }

    @Test
    @Transactional
    public void getGame() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get the game
        restGameMockMvc.perform(get("/api/games/{id}", game.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(game.getId().intValue()))
            .andExpect(jsonPath("$.startAt").value(DEFAULT_START_AT.toString()))
            .andExpect(jsonPath("$.homeScore").value(DEFAULT_HOME_SCORE))
            .andExpect(jsonPath("$.roadScore").value(DEFAULT_ROAD_SCORE))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE.toString()))
            .andExpect(jsonPath("$.homeScoreHalf").value(DEFAULT_HOME_SCORE_HALF))
            .andExpect(jsonPath("$.roadScoreHalf").value(DEFAULT_ROAD_SCORE_HALF));
    }

    @Test
    @Transactional
    public void getNonExistingGame() throws Exception {
        // Get the game
        restGameMockMvc.perform(get("/api/games/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGame() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);
        int databaseSizeBeforeUpdate = gameRepository.findAll().size();

        // Update the game
        Game updatedGame = gameRepository.findOne(game.getId());
        updatedGame.setStartAt(UPDATED_START_AT);
        updatedGame.setHomeScore(UPDATED_HOME_SCORE);
        updatedGame.setRoadScore(UPDATED_ROAD_SCORE);
        updatedGame.setNote(UPDATED_NOTE);
        updatedGame.setHomeScoreHalf(UPDATED_HOME_SCORE_HALF);
        updatedGame.setRoadScoreHalf(UPDATED_ROAD_SCORE_HALF);

        restGameMockMvc.perform(put("/api/games")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedGame)))
                .andExpect(status().isOk());

        // Validate the Game in the database
        List<Game> games = gameRepository.findAll();
        assertThat(games).hasSize(databaseSizeBeforeUpdate);
        Game testGame = games.get(games.size() - 1);
        assertThat(testGame.getStartAt()).isEqualTo(UPDATED_START_AT);
        assertThat(testGame.getHomeScore()).isEqualTo(UPDATED_HOME_SCORE);
        assertThat(testGame.getRoadScore()).isEqualTo(UPDATED_ROAD_SCORE);
        assertThat(testGame.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testGame.getHomeScoreHalf()).isEqualTo(UPDATED_HOME_SCORE_HALF);
        assertThat(testGame.getRoadScoreHalf()).isEqualTo(UPDATED_ROAD_SCORE_HALF);
    }

    @Test
    @Transactional
    public void deleteGame() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);
        int databaseSizeBeforeDelete = gameRepository.findAll().size();

        // Get the game
        restGameMockMvc.perform(delete("/api/games/{id}", game.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Game> games = gameRepository.findAll();
        assertThat(games).hasSize(databaseSizeBeforeDelete - 1);
    }
}
