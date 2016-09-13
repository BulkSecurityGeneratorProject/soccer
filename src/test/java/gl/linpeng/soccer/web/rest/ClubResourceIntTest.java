package gl.linpeng.soccer.web.rest;

import gl.linpeng.soccer.SoccerApp;

import gl.linpeng.soccer.domain.Club;
import gl.linpeng.soccer.repository.ClubRepository;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ClubResource REST controller.
 *
 * @see ClubResource
 */
@RunWith(SpringRunner.class)

@SpringBootTest(classes = SoccerApp.class)

public class ClubResourceIntTest {

    private static final LocalDate DEFAULT_CREATE_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_AT = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_SHORT_NAME = "AAAAA";
    private static final String UPDATED_SHORT_NAME = "BBBBB";

    @Inject
    private ClubRepository clubRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restClubMockMvc;

    private Club club;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ClubResource clubResource = new ClubResource();
        ReflectionTestUtils.setField(clubResource, "clubRepository", clubRepository);
        this.restClubMockMvc = MockMvcBuilders.standaloneSetup(clubResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Club createEntity(EntityManager em) {
        Club club = new Club();
        club.setCreateAt(DEFAULT_CREATE_AT);
        club.setName(DEFAULT_NAME);
        club.setShortName(DEFAULT_SHORT_NAME);
        return club;
    }

    @Before
    public void initTest() {
        club = createEntity(em);
    }

    @Test
    @Transactional
    public void createClub() throws Exception {
        int databaseSizeBeforeCreate = clubRepository.findAll().size();

        // Create the Club

        restClubMockMvc.perform(post("/api/clubs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(club)))
                .andExpect(status().isCreated());

        // Validate the Club in the database
        List<Club> clubs = clubRepository.findAll();
        assertThat(clubs).hasSize(databaseSizeBeforeCreate + 1);
        Club testClub = clubs.get(clubs.size() - 1);
        assertThat(testClub.getCreateAt()).isEqualTo(DEFAULT_CREATE_AT);
        assertThat(testClub.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testClub.getShortName()).isEqualTo(DEFAULT_SHORT_NAME);
    }

    @Test
    @Transactional
    public void getAllClubs() throws Exception {
        // Initialize the database
        clubRepository.saveAndFlush(club);

        // Get all the clubs
        restClubMockMvc.perform(get("/api/clubs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(club.getId().intValue())))
                .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT.toString())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].shortName").value(hasItem(DEFAULT_SHORT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getClub() throws Exception {
        // Initialize the database
        clubRepository.saveAndFlush(club);

        // Get the club
        restClubMockMvc.perform(get("/api/clubs/{id}", club.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(club.getId().intValue()))
            .andExpect(jsonPath("$.createAt").value(DEFAULT_CREATE_AT.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.shortName").value(DEFAULT_SHORT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingClub() throws Exception {
        // Get the club
        restClubMockMvc.perform(get("/api/clubs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateClub() throws Exception {
        // Initialize the database
        clubRepository.saveAndFlush(club);
        int databaseSizeBeforeUpdate = clubRepository.findAll().size();

        // Update the club
        Club updatedClub = clubRepository.findOne(club.getId());
        updatedClub.setCreateAt(UPDATED_CREATE_AT);
        updatedClub.setName(UPDATED_NAME);
        updatedClub.setShortName(UPDATED_SHORT_NAME);

        restClubMockMvc.perform(put("/api/clubs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedClub)))
                .andExpect(status().isOk());

        // Validate the Club in the database
        List<Club> clubs = clubRepository.findAll();
        assertThat(clubs).hasSize(databaseSizeBeforeUpdate);
        Club testClub = clubs.get(clubs.size() - 1);
        assertThat(testClub.getCreateAt()).isEqualTo(UPDATED_CREATE_AT);
        assertThat(testClub.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testClub.getShortName()).isEqualTo(UPDATED_SHORT_NAME);
    }

    @Test
    @Transactional
    public void deleteClub() throws Exception {
        // Initialize the database
        clubRepository.saveAndFlush(club);
        int databaseSizeBeforeDelete = clubRepository.findAll().size();

        // Get the club
        restClubMockMvc.perform(delete("/api/clubs/{id}", club.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Club> clubs = clubRepository.findAll();
        assertThat(clubs).hasSize(databaseSizeBeforeDelete - 1);
    }
}
