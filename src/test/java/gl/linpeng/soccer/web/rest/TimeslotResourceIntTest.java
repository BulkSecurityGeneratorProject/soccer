package gl.linpeng.soccer.web.rest;

import gl.linpeng.soccer.SoccerApp;

import gl.linpeng.soccer.domain.Timeslot;
import gl.linpeng.soccer.repository.TimeslotRepository;

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
 * Test class for the TimeslotResource REST controller.
 *
 * @see TimeslotResource
 */
@RunWith(SpringRunner.class)

@SpringBootTest(classes = SoccerApp.class)

public class TimeslotResourceIntTest {

    private static final Integer DEFAULT_ROUND = 1;
    private static final Integer UPDATED_ROUND = 2;

    @Inject
    private TimeslotRepository timeslotRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restTimeslotMockMvc;

    private Timeslot timeslot;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TimeslotResource timeslotResource = new TimeslotResource();
        ReflectionTestUtils.setField(timeslotResource, "timeslotRepository", timeslotRepository);
        this.restTimeslotMockMvc = MockMvcBuilders.standaloneSetup(timeslotResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Timeslot createEntity(EntityManager em) {
        Timeslot timeslot = new Timeslot();
        timeslot.setRound(DEFAULT_ROUND);
        return timeslot;
    }

    @Before
    public void initTest() {
        timeslot = createEntity(em);
    }

    @Test
    @Transactional
    public void createTimeslot() throws Exception {
        int databaseSizeBeforeCreate = timeslotRepository.findAll().size();

        // Create the Timeslot

        restTimeslotMockMvc.perform(post("/api/timeslots")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(timeslot)))
                .andExpect(status().isCreated());

        // Validate the Timeslot in the database
        List<Timeslot> timeslots = timeslotRepository.findAll();
        assertThat(timeslots).hasSize(databaseSizeBeforeCreate + 1);
        Timeslot testTimeslot = timeslots.get(timeslots.size() - 1);
        assertThat(testTimeslot.getRound()).isEqualTo(DEFAULT_ROUND);
    }

    @Test
    @Transactional
    public void getAllTimeslots() throws Exception {
        // Initialize the database
        timeslotRepository.saveAndFlush(timeslot);

        // Get all the timeslots
        restTimeslotMockMvc.perform(get("/api/timeslots?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(timeslot.getId().intValue())))
                .andExpect(jsonPath("$.[*].round").value(hasItem(DEFAULT_ROUND)));
    }

    @Test
    @Transactional
    public void getTimeslot() throws Exception {
        // Initialize the database
        timeslotRepository.saveAndFlush(timeslot);

        // Get the timeslot
        restTimeslotMockMvc.perform(get("/api/timeslots/{id}", timeslot.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(timeslot.getId().intValue()))
            .andExpect(jsonPath("$.round").value(DEFAULT_ROUND));
    }

    @Test
    @Transactional
    public void getNonExistingTimeslot() throws Exception {
        // Get the timeslot
        restTimeslotMockMvc.perform(get("/api/timeslots/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTimeslot() throws Exception {
        // Initialize the database
        timeslotRepository.saveAndFlush(timeslot);
        int databaseSizeBeforeUpdate = timeslotRepository.findAll().size();

        // Update the timeslot
        Timeslot updatedTimeslot = timeslotRepository.findOne(timeslot.getId());
        updatedTimeslot.setRound(UPDATED_ROUND);

        restTimeslotMockMvc.perform(put("/api/timeslots")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedTimeslot)))
                .andExpect(status().isOk());

        // Validate the Timeslot in the database
        List<Timeslot> timeslots = timeslotRepository.findAll();
        assertThat(timeslots).hasSize(databaseSizeBeforeUpdate);
        Timeslot testTimeslot = timeslots.get(timeslots.size() - 1);
        assertThat(testTimeslot.getRound()).isEqualTo(UPDATED_ROUND);
    }

    @Test
    @Transactional
    public void deleteTimeslot() throws Exception {
        // Initialize the database
        timeslotRepository.saveAndFlush(timeslot);
        int databaseSizeBeforeDelete = timeslotRepository.findAll().size();

        // Get the timeslot
        restTimeslotMockMvc.perform(delete("/api/timeslots/{id}", timeslot.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Timeslot> timeslots = timeslotRepository.findAll();
        assertThat(timeslots).hasSize(databaseSizeBeforeDelete - 1);
    }
}
