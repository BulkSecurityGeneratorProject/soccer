package gl.linpeng.soccer.web.rest;

import gl.linpeng.soccer.SoccerApp;

import gl.linpeng.soccer.domain.Coach;
import gl.linpeng.soccer.repository.CoachRepository;

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
 * Test class for the CoachResource REST controller.
 *
 * @see CoachResource
 */
@RunWith(SpringRunner.class)

@SpringBootTest(classes = SoccerApp.class)

public class CoachResourceIntTest {
    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_PICTURE = "AAAAA";
    private static final String UPDATED_PICTURE = "BBBBB";

    @Inject
    private CoachRepository coachRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restCoachMockMvc;

    private Coach coach;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CoachResource coachResource = new CoachResource();
        ReflectionTestUtils.setField(coachResource, "coachRepository", coachRepository);
        this.restCoachMockMvc = MockMvcBuilders.standaloneSetup(coachResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Coach createEntity(EntityManager em) {
        Coach coach = new Coach()
                .name(DEFAULT_NAME)
                .picture(DEFAULT_PICTURE);
        return coach;
    }

    @Before
    public void initTest() {
        coach = createEntity(em);
    }

    @Test
    @Transactional
    public void createCoach() throws Exception {
        int databaseSizeBeforeCreate = coachRepository.findAll().size();

        // Create the Coach

        restCoachMockMvc.perform(post("/api/coaches")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(coach)))
                .andExpect(status().isCreated());

        // Validate the Coach in the database
        List<Coach> coaches = coachRepository.findAll();
        assertThat(coaches).hasSize(databaseSizeBeforeCreate + 1);
        Coach testCoach = coaches.get(coaches.size() - 1);
        assertThat(testCoach.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCoach.getPicture()).isEqualTo(DEFAULT_PICTURE);
    }

    @Test
    @Transactional
    public void getAllCoaches() throws Exception {
        // Initialize the database
        coachRepository.saveAndFlush(coach);

        // Get all the coaches
        restCoachMockMvc.perform(get("/api/coaches?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(coach.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].picture").value(hasItem(DEFAULT_PICTURE.toString())));
    }

    @Test
    @Transactional
    public void getCoach() throws Exception {
        // Initialize the database
        coachRepository.saveAndFlush(coach);

        // Get the coach
        restCoachMockMvc.perform(get("/api/coaches/{id}", coach.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(coach.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.picture").value(DEFAULT_PICTURE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCoach() throws Exception {
        // Get the coach
        restCoachMockMvc.perform(get("/api/coaches/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCoach() throws Exception {
        // Initialize the database
        coachRepository.saveAndFlush(coach);
        int databaseSizeBeforeUpdate = coachRepository.findAll().size();

        // Update the coach
        Coach updatedCoach = coachRepository.findOne(coach.getId());
        updatedCoach
                .name(UPDATED_NAME)
                .picture(UPDATED_PICTURE);

        restCoachMockMvc.perform(put("/api/coaches")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCoach)))
                .andExpect(status().isOk());

        // Validate the Coach in the database
        List<Coach> coaches = coachRepository.findAll();
        assertThat(coaches).hasSize(databaseSizeBeforeUpdate);
        Coach testCoach = coaches.get(coaches.size() - 1);
        assertThat(testCoach.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCoach.getPicture()).isEqualTo(UPDATED_PICTURE);
    }

    @Test
    @Transactional
    public void deleteCoach() throws Exception {
        // Initialize the database
        coachRepository.saveAndFlush(coach);
        int databaseSizeBeforeDelete = coachRepository.findAll().size();

        // Get the coach
        restCoachMockMvc.perform(delete("/api/coaches/{id}", coach.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Coach> coaches = coachRepository.findAll();
        assertThat(coaches).hasSize(databaseSizeBeforeDelete - 1);
    }
}
