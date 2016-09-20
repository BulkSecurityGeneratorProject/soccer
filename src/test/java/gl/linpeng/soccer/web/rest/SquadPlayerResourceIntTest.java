package gl.linpeng.soccer.web.rest;

import gl.linpeng.soccer.SoccerApp;

import gl.linpeng.soccer.domain.SquadPlayer;
import gl.linpeng.soccer.repository.SquadPlayerRepository;

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
 * Test class for the SquadPlayerResource REST controller.
 *
 * @see SquadPlayerResource
 */
@RunWith(SpringRunner.class)

@SpringBootTest(classes = SoccerApp.class)

public class SquadPlayerResourceIntTest {

    private static final Integer DEFAULT_PLAYER_NUMBER = 1;
    private static final Integer UPDATED_PLAYER_NUMBER = 2;

    private static final Boolean DEFAULT_IS_SUBSTITUTE = false;
    private static final Boolean UPDATED_IS_SUBSTITUTE = true;

    @Inject
    private SquadPlayerRepository squadPlayerRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restSquadPlayerMockMvc;

    private SquadPlayer squadPlayer;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SquadPlayerResource squadPlayerResource = new SquadPlayerResource();
        ReflectionTestUtils.setField(squadPlayerResource, "squadPlayerRepository", squadPlayerRepository);
        this.restSquadPlayerMockMvc = MockMvcBuilders.standaloneSetup(squadPlayerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SquadPlayer createEntity(EntityManager em) {
        SquadPlayer squadPlayer = new SquadPlayer();
        squadPlayer.setPlayerNumber(DEFAULT_PLAYER_NUMBER);
        squadPlayer.setIsSubstitute(DEFAULT_IS_SUBSTITUTE);
        return squadPlayer;
    }

    @Before
    public void initTest() {
        squadPlayer = createEntity(em);
    }

    @Test
    @Transactional
    public void createSquadPlayer() throws Exception {
        int databaseSizeBeforeCreate = squadPlayerRepository.findAll().size();

        // Create the SquadPlayer

        restSquadPlayerMockMvc.perform(post("/api/squad-players")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(squadPlayer)))
                .andExpect(status().isCreated());

        // Validate the SquadPlayer in the database
        List<SquadPlayer> squadPlayers = squadPlayerRepository.findAll();
        assertThat(squadPlayers).hasSize(databaseSizeBeforeCreate + 1);
        SquadPlayer testSquadPlayer = squadPlayers.get(squadPlayers.size() - 1);
        assertThat(testSquadPlayer.getPlayerNumber()).isEqualTo(DEFAULT_PLAYER_NUMBER);
        assertThat(testSquadPlayer.isIsSubstitute()).isEqualTo(DEFAULT_IS_SUBSTITUTE);
    }

    @Test
    @Transactional
    public void getAllSquadPlayers() throws Exception {
        // Initialize the database
        squadPlayerRepository.saveAndFlush(squadPlayer);

        // Get all the squadPlayers
        restSquadPlayerMockMvc.perform(get("/api/squad-players?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(squadPlayer.getId().intValue())))
                .andExpect(jsonPath("$.[*].playerNumber").value(hasItem(DEFAULT_PLAYER_NUMBER)))
                .andExpect(jsonPath("$.[*].isSubstitute").value(hasItem(DEFAULT_IS_SUBSTITUTE.booleanValue())));
    }

    @Test
    @Transactional
    public void getSquadPlayer() throws Exception {
        // Initialize the database
        squadPlayerRepository.saveAndFlush(squadPlayer);

        // Get the squadPlayer
        restSquadPlayerMockMvc.perform(get("/api/squad-players/{id}", squadPlayer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(squadPlayer.getId().intValue()))
            .andExpect(jsonPath("$.playerNumber").value(DEFAULT_PLAYER_NUMBER))
            .andExpect(jsonPath("$.isSubstitute").value(DEFAULT_IS_SUBSTITUTE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSquadPlayer() throws Exception {
        // Get the squadPlayer
        restSquadPlayerMockMvc.perform(get("/api/squad-players/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSquadPlayer() throws Exception {
        // Initialize the database
        squadPlayerRepository.saveAndFlush(squadPlayer);
        int databaseSizeBeforeUpdate = squadPlayerRepository.findAll().size();

        // Update the squadPlayer
        SquadPlayer updatedSquadPlayer = squadPlayerRepository.findOne(squadPlayer.getId());
        updatedSquadPlayer.setPlayerNumber(UPDATED_PLAYER_NUMBER);
        updatedSquadPlayer.setIsSubstitute(UPDATED_IS_SUBSTITUTE);

        restSquadPlayerMockMvc.perform(put("/api/squad-players")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedSquadPlayer)))
                .andExpect(status().isOk());

        // Validate the SquadPlayer in the database
        List<SquadPlayer> squadPlayers = squadPlayerRepository.findAll();
        assertThat(squadPlayers).hasSize(databaseSizeBeforeUpdate);
        SquadPlayer testSquadPlayer = squadPlayers.get(squadPlayers.size() - 1);
        assertThat(testSquadPlayer.getPlayerNumber()).isEqualTo(UPDATED_PLAYER_NUMBER);
        assertThat(testSquadPlayer.isIsSubstitute()).isEqualTo(UPDATED_IS_SUBSTITUTE);
    }

    @Test
    @Transactional
    public void deleteSquadPlayer() throws Exception {
        // Initialize the database
        squadPlayerRepository.saveAndFlush(squadPlayer);
        int databaseSizeBeforeDelete = squadPlayerRepository.findAll().size();

        // Get the squadPlayer
        restSquadPlayerMockMvc.perform(delete("/api/squad-players/{id}", squadPlayer.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<SquadPlayer> squadPlayers = squadPlayerRepository.findAll();
        assertThat(squadPlayers).hasSize(databaseSizeBeforeDelete - 1);
    }
}
