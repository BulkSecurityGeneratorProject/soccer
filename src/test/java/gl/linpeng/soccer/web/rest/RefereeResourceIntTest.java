package gl.linpeng.soccer.web.rest;

import gl.linpeng.soccer.SoccerApp;

import gl.linpeng.soccer.domain.Referee;
import gl.linpeng.soccer.repository.RefereeRepository;

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
 * Test class for the RefereeResource REST controller.
 *
 * @see RefereeResource
 */
@RunWith(SpringRunner.class)

@SpringBootTest(classes = SoccerApp.class)

public class RefereeResourceIntTest {
    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_PICTURE = "AAAAA";
    private static final String UPDATED_PICTURE = "BBBBB";

    @Inject
    private RefereeRepository refereeRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restRefereeMockMvc;

    private Referee referee;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RefereeResource refereeResource = new RefereeResource();
        ReflectionTestUtils.setField(refereeResource, "refereeRepository", refereeRepository);
        this.restRefereeMockMvc = MockMvcBuilders.standaloneSetup(refereeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Referee createEntity(EntityManager em) {
        Referee referee = new Referee()
                .name(DEFAULT_NAME)
                .picture(DEFAULT_PICTURE);
        return referee;
    }

    @Before
    public void initTest() {
        referee = createEntity(em);
    }

    @Test
    @Transactional
    public void createReferee() throws Exception {
        int databaseSizeBeforeCreate = refereeRepository.findAll().size();

        // Create the Referee

        restRefereeMockMvc.perform(post("/api/referees")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(referee)))
                .andExpect(status().isCreated());

        // Validate the Referee in the database
        List<Referee> referees = refereeRepository.findAll();
        assertThat(referees).hasSize(databaseSizeBeforeCreate + 1);
        Referee testReferee = referees.get(referees.size() - 1);
        assertThat(testReferee.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testReferee.getPicture()).isEqualTo(DEFAULT_PICTURE);
    }

    @Test
    @Transactional
    public void getAllReferees() throws Exception {
        // Initialize the database
        refereeRepository.saveAndFlush(referee);

        // Get all the referees
        restRefereeMockMvc.perform(get("/api/referees?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(referee.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].picture").value(hasItem(DEFAULT_PICTURE.toString())));
    }

    @Test
    @Transactional
    public void getReferee() throws Exception {
        // Initialize the database
        refereeRepository.saveAndFlush(referee);

        // Get the referee
        restRefereeMockMvc.perform(get("/api/referees/{id}", referee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(referee.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.picture").value(DEFAULT_PICTURE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingReferee() throws Exception {
        // Get the referee
        restRefereeMockMvc.perform(get("/api/referees/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReferee() throws Exception {
        // Initialize the database
        refereeRepository.saveAndFlush(referee);
        int databaseSizeBeforeUpdate = refereeRepository.findAll().size();

        // Update the referee
        Referee updatedReferee = refereeRepository.findOne(referee.getId());
        updatedReferee
                .name(UPDATED_NAME)
                .picture(UPDATED_PICTURE);

        restRefereeMockMvc.perform(put("/api/referees")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedReferee)))
                .andExpect(status().isOk());

        // Validate the Referee in the database
        List<Referee> referees = refereeRepository.findAll();
        assertThat(referees).hasSize(databaseSizeBeforeUpdate);
        Referee testReferee = referees.get(referees.size() - 1);
        assertThat(testReferee.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testReferee.getPicture()).isEqualTo(UPDATED_PICTURE);
    }

    @Test
    @Transactional
    public void deleteReferee() throws Exception {
        // Initialize the database
        refereeRepository.saveAndFlush(referee);
        int databaseSizeBeforeDelete = refereeRepository.findAll().size();

        // Get the referee
        restRefereeMockMvc.perform(delete("/api/referees/{id}", referee.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Referee> referees = refereeRepository.findAll();
        assertThat(referees).hasSize(databaseSizeBeforeDelete - 1);
    }
}
