package gl.linpeng.soccer.web.rest;

import gl.linpeng.soccer.SoccerApp;

import gl.linpeng.soccer.domain.Squad;
import gl.linpeng.soccer.repository.SquadRepository;

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
 * Test class for the SquadResource REST controller.
 *
 * @see SquadResource
 */
@RunWith(SpringRunner.class)

@SpringBootTest(classes = SoccerApp.class)

public class SquadResourceIntTest {

    @Inject
    private SquadRepository squadRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restSquadMockMvc;

    private Squad squad;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SquadResource squadResource = new SquadResource();
        ReflectionTestUtils.setField(squadResource, "squadRepository", squadRepository);
        this.restSquadMockMvc = MockMvcBuilders.standaloneSetup(squadResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Squad createEntity(EntityManager em) {
        Squad squad = new Squad();
        return squad;
    }

    @Before
    public void initTest() {
        squad = createEntity(em);
    }

    @Test
    @Transactional
    public void createSquad() throws Exception {
        int databaseSizeBeforeCreate = squadRepository.findAll().size();

        // Create the Squad

        restSquadMockMvc.perform(post("/api/squads")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(squad)))
                .andExpect(status().isCreated());

        // Validate the Squad in the database
        List<Squad> squads = squadRepository.findAll();
        assertThat(squads).hasSize(databaseSizeBeforeCreate + 1);
        Squad testSquad = squads.get(squads.size() - 1);
    }

    @Test
    @Transactional
    public void getAllSquads() throws Exception {
        // Initialize the database
        squadRepository.saveAndFlush(squad);

        // Get all the squads
        restSquadMockMvc.perform(get("/api/squads?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(squad.getId().intValue())));
    }

    @Test
    @Transactional
    public void getSquad() throws Exception {
        // Initialize the database
        squadRepository.saveAndFlush(squad);

        // Get the squad
        restSquadMockMvc.perform(get("/api/squads/{id}", squad.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(squad.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSquad() throws Exception {
        // Get the squad
        restSquadMockMvc.perform(get("/api/squads/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSquad() throws Exception {
        // Initialize the database
        squadRepository.saveAndFlush(squad);
        int databaseSizeBeforeUpdate = squadRepository.findAll().size();

        // Update the squad
        Squad updatedSquad = squadRepository.findOne(squad.getId());

        restSquadMockMvc.perform(put("/api/squads")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedSquad)))
                .andExpect(status().isOk());

        // Validate the Squad in the database
        List<Squad> squads = squadRepository.findAll();
        assertThat(squads).hasSize(databaseSizeBeforeUpdate);
        Squad testSquad = squads.get(squads.size() - 1);
    }

    @Test
    @Transactional
    public void deleteSquad() throws Exception {
        // Initialize the database
        squadRepository.saveAndFlush(squad);
        int databaseSizeBeforeDelete = squadRepository.findAll().size();

        // Get the squad
        restSquadMockMvc.perform(delete("/api/squads/{id}", squad.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Squad> squads = squadRepository.findAll();
        assertThat(squads).hasSize(databaseSizeBeforeDelete - 1);
    }
}
