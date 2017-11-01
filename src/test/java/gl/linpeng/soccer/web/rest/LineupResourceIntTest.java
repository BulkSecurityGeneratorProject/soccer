package gl.linpeng.soccer.web.rest;

import gl.linpeng.soccer.SoccerApp;

import gl.linpeng.soccer.domain.Lineup;
import gl.linpeng.soccer.repository.LineupRepository;

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
 * Test class for the LineupResource REST controller.
 *
 * @see LineupResource
 */
@RunWith(SpringRunner.class)

@SpringBootTest(classes = SoccerApp.class)

public class LineupResourceIntTest {

    private static final Integer DEFAULT_PLAYER_NUMBER = 1;
    private static final Integer UPDATED_PLAYER_NUMBER = 2;

    @Inject
    private LineupRepository lineupRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restLineupMockMvc;

    private Lineup lineup;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LineupResource lineupResource = new LineupResource();
        ReflectionTestUtils.setField(lineupResource, "lineupRepository", lineupRepository);
        this.restLineupMockMvc = MockMvcBuilders.standaloneSetup(lineupResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lineup createEntity(EntityManager em) {
        Lineup lineup = new Lineup()
                .playerNumber(DEFAULT_PLAYER_NUMBER);
        return lineup;
    }

    @Before
    public void initTest() {
        lineup = createEntity(em);
    }

    @Test
    @Transactional
    public void createLineup() throws Exception {
        int databaseSizeBeforeCreate = lineupRepository.findAll().size();

        // Create the Lineup

        restLineupMockMvc.perform(post("/api/lineups")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lineup)))
                .andExpect(status().isCreated());

        // Validate the Lineup in the database
        List<Lineup> lineups = lineupRepository.findAll();
        assertThat(lineups).hasSize(databaseSizeBeforeCreate + 1);
        Lineup testLineup = lineups.get(lineups.size() - 1);
        assertThat(testLineup.getPlayerNumber()).isEqualTo(DEFAULT_PLAYER_NUMBER);
    }

    @Test
    @Transactional
    public void getAllLineups() throws Exception {
        // Initialize the database
        lineupRepository.saveAndFlush(lineup);

        // Get all the lineups
        restLineupMockMvc.perform(get("/api/lineups?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(lineup.getId().intValue())))
                .andExpect(jsonPath("$.[*].playerNumber").value(hasItem(DEFAULT_PLAYER_NUMBER)));
    }

    @Test
    @Transactional
    public void getLineup() throws Exception {
        // Initialize the database
        lineupRepository.saveAndFlush(lineup);

        // Get the lineup
        restLineupMockMvc.perform(get("/api/lineups/{id}", lineup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(lineup.getId().intValue()))
            .andExpect(jsonPath("$.playerNumber").value(DEFAULT_PLAYER_NUMBER));
    }

    @Test
    @Transactional
    public void getNonExistingLineup() throws Exception {
        // Get the lineup
        restLineupMockMvc.perform(get("/api/lineups/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLineup() throws Exception {
        // Initialize the database
        lineupRepository.saveAndFlush(lineup);
        int databaseSizeBeforeUpdate = lineupRepository.findAll().size();

        // Update the lineup
        Lineup updatedLineup = lineupRepository.findOne(lineup.getId());
        updatedLineup
                .playerNumber(UPDATED_PLAYER_NUMBER);

        restLineupMockMvc.perform(put("/api/lineups")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedLineup)))
                .andExpect(status().isOk());

        // Validate the Lineup in the database
        List<Lineup> lineups = lineupRepository.findAll();
        assertThat(lineups).hasSize(databaseSizeBeforeUpdate);
        Lineup testLineup = lineups.get(lineups.size() - 1);
        assertThat(testLineup.getPlayerNumber()).isEqualTo(UPDATED_PLAYER_NUMBER);
    }

    @Test
    @Transactional
    public void deleteLineup() throws Exception {
        // Initialize the database
        lineupRepository.saveAndFlush(lineup);
        int databaseSizeBeforeDelete = lineupRepository.findAll().size();

        // Get the lineup
        restLineupMockMvc.perform(delete("/api/lineups/{id}", lineup.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Lineup> lineups = lineupRepository.findAll();
        assertThat(lineups).hasSize(databaseSizeBeforeDelete - 1);
    }
}
