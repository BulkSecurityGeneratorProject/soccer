package gl.linpeng.soccer.web.rest;

import gl.linpeng.soccer.SoccerApp;

import gl.linpeng.soccer.domain.PlayerPosition;
import gl.linpeng.soccer.repository.PlayerPositionRepository;

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
 * Test class for the PlayerPositionResource REST controller.
 *
 * @see PlayerPositionResource
 */
@RunWith(SpringRunner.class)

@SpringBootTest(classes = SoccerApp.class)

public class PlayerPositionResourceIntTest {

    @Inject
    private PlayerPositionRepository playerPositionRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restPlayerPositionMockMvc;

    private PlayerPosition playerPosition;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PlayerPositionResource playerPositionResource = new PlayerPositionResource();
        ReflectionTestUtils.setField(playerPositionResource, "playerPositionRepository", playerPositionRepository);
        this.restPlayerPositionMockMvc = MockMvcBuilders.standaloneSetup(playerPositionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlayerPosition createEntity(EntityManager em) {
        PlayerPosition playerPosition = new PlayerPosition();
        return playerPosition;
    }

    @Before
    public void initTest() {
        playerPosition = createEntity(em);
    }

    @Test
    @Transactional
    public void createPlayerPosition() throws Exception {
        int databaseSizeBeforeCreate = playerPositionRepository.findAll().size();

        // Create the PlayerPosition

        restPlayerPositionMockMvc.perform(post("/api/player-positions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(playerPosition)))
                .andExpect(status().isCreated());

        // Validate the PlayerPosition in the database
        List<PlayerPosition> playerPositions = playerPositionRepository.findAll();
        assertThat(playerPositions).hasSize(databaseSizeBeforeCreate + 1);
        PlayerPosition testPlayerPosition = playerPositions.get(playerPositions.size() - 1);
    }

    @Test
    @Transactional
    public void getAllPlayerPositions() throws Exception {
        // Initialize the database
        playerPositionRepository.saveAndFlush(playerPosition);

        // Get all the playerPositions
        restPlayerPositionMockMvc.perform(get("/api/player-positions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(playerPosition.getId().intValue())));
    }

    @Test
    @Transactional
    public void getPlayerPosition() throws Exception {
        // Initialize the database
        playerPositionRepository.saveAndFlush(playerPosition);

        // Get the playerPosition
        restPlayerPositionMockMvc.perform(get("/api/player-positions/{id}", playerPosition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(playerPosition.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPlayerPosition() throws Exception {
        // Get the playerPosition
        restPlayerPositionMockMvc.perform(get("/api/player-positions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePlayerPosition() throws Exception {
        // Initialize the database
        playerPositionRepository.saveAndFlush(playerPosition);
        int databaseSizeBeforeUpdate = playerPositionRepository.findAll().size();

        // Update the playerPosition
        PlayerPosition updatedPlayerPosition = playerPositionRepository.findOne(playerPosition.getId());

        restPlayerPositionMockMvc.perform(put("/api/player-positions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedPlayerPosition)))
                .andExpect(status().isOk());

        // Validate the PlayerPosition in the database
        List<PlayerPosition> playerPositions = playerPositionRepository.findAll();
        assertThat(playerPositions).hasSize(databaseSizeBeforeUpdate);
        PlayerPosition testPlayerPosition = playerPositions.get(playerPositions.size() - 1);
    }

    @Test
    @Transactional
    public void deletePlayerPosition() throws Exception {
        // Initialize the database
        playerPositionRepository.saveAndFlush(playerPosition);
        int databaseSizeBeforeDelete = playerPositionRepository.findAll().size();

        // Get the playerPosition
        restPlayerPositionMockMvc.perform(delete("/api/player-positions/{id}", playerPosition.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<PlayerPosition> playerPositions = playerPositionRepository.findAll();
        assertThat(playerPositions).hasSize(databaseSizeBeforeDelete - 1);
    }
}
