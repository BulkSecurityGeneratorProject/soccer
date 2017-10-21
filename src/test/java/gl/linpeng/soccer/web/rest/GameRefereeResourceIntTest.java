package gl.linpeng.soccer.web.rest;

import gl.linpeng.soccer.SoccerApp;

import gl.linpeng.soccer.domain.GameReferee;
import gl.linpeng.soccer.repository.GameRefereeRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the GameRefereeResource REST controller.
 *
 * @see GameRefereeResource
 */
@RunWith(SpringRunner.class)

@SpringBootTest(classes = SoccerApp.class)

public class GameRefereeResourceIntTest {

    @Inject
    private GameRefereeRepository gameRefereeRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restGameRefereeMockMvc;

    private GameReferee gameReferee;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        GameRefereeResource gameRefereeResource = new GameRefereeResource();
        ReflectionTestUtils.setField(gameRefereeResource, "gameRefereeRepository", gameRefereeRepository);
        this.restGameRefereeMockMvc = MockMvcBuilders.standaloneSetup(gameRefereeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GameReferee createEntity(EntityManager em) {
        GameReferee gameReferee = new GameReferee();
        return gameReferee;
    }

    @Before
    public void initTest() {
        gameReferee = createEntity(em);
    }

    @Test
    @Transactional
    public void createGameReferee() throws Exception {
        int databaseSizeBeforeCreate = gameRefereeRepository.findAll().size();

        // Create the GameReferee

        restGameRefereeMockMvc.perform(post("/api/game-referees")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(gameReferee)))
                .andExpect(status().isCreated());

        // Validate the GameReferee in the database
        List<GameReferee> gameReferees = gameRefereeRepository.findAll();
        assertThat(gameReferees).hasSize(databaseSizeBeforeCreate + 1);
        GameReferee testGameReferee = gameReferees.get(gameReferees.size() - 1);
    }

    @Test
    @Transactional
    public void getAllGameReferees() throws Exception {
        // Initialize the database
        gameRefereeRepository.saveAndFlush(gameReferee);

        // Get all the gameReferees
        restGameRefereeMockMvc.perform(get("/api/game-referees?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(gameReferee.getId().intValue())));
    }

    @Test
    @Transactional
    public void getGameReferee() throws Exception {
        // Initialize the database
        gameRefereeRepository.saveAndFlush(gameReferee);

        // Get the gameReferee
        restGameRefereeMockMvc.perform(get("/api/game-referees/{id}", gameReferee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(gameReferee.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingGameReferee() throws Exception {
        // Get the gameReferee
        restGameRefereeMockMvc.perform(get("/api/game-referees/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGameReferee() throws Exception {
        // Initialize the database
        gameRefereeRepository.saveAndFlush(gameReferee);
        int databaseSizeBeforeUpdate = gameRefereeRepository.findAll().size();

        // Update the gameReferee
        GameReferee updatedGameReferee = gameRefereeRepository.findOne(gameReferee.getId());

        restGameRefereeMockMvc.perform(put("/api/game-referees")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedGameReferee)))
                .andExpect(status().isOk());

        // Validate the GameReferee in the database
        List<GameReferee> gameReferees = gameRefereeRepository.findAll();
        assertThat(gameReferees).hasSize(databaseSizeBeforeUpdate);
        GameReferee testGameReferee = gameReferees.get(gameReferees.size() - 1);
    }

    @Test
    @Transactional
    public void deleteGameReferee() throws Exception {
        // Initialize the database
        gameRefereeRepository.saveAndFlush(gameReferee);
        int databaseSizeBeforeDelete = gameRefereeRepository.findAll().size();

        // Get the gameReferee
        restGameRefereeMockMvc.perform(delete("/api/game-referees/{id}", gameReferee.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<GameReferee> gameReferees = gameRefereeRepository.findAll();
        assertThat(gameReferees).hasSize(databaseSizeBeforeDelete - 1);
    }
}
