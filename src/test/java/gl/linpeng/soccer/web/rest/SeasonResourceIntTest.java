package gl.linpeng.soccer.web.rest;

import gl.linpeng.soccer.SoccerApp;

import gl.linpeng.soccer.domain.Season;
import gl.linpeng.soccer.repository.SeasonRepository;

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
 * Test class for the SeasonResource REST controller.
 *
 * @see SeasonResource
 */
@RunWith(SpringRunner.class)

@SpringBootTest(classes = SoccerApp.class)

public class SeasonResourceIntTest {
    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final LocalDate DEFAULT_START_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_AT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_AT = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private SeasonRepository seasonRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restSeasonMockMvc;

    private Season season;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SeasonResource seasonResource = new SeasonResource();
        ReflectionTestUtils.setField(seasonResource, "seasonRepository", seasonRepository);
        this.restSeasonMockMvc = MockMvcBuilders.standaloneSetup(seasonResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Season createEntity(EntityManager em) {
        Season season = new Season();
        season.setName(DEFAULT_NAME);
        season.setStartAt(DEFAULT_START_AT);
        season.setEndAt(DEFAULT_END_AT);
        return season;
    }

    @Before
    public void initTest() {
        season = createEntity(em);
    }

    @Test
    @Transactional
    public void createSeason() throws Exception {
        int databaseSizeBeforeCreate = seasonRepository.findAll().size();

        // Create the Season

        restSeasonMockMvc.perform(post("/api/seasons")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(season)))
                .andExpect(status().isCreated());

        // Validate the Season in the database
        List<Season> seasons = seasonRepository.findAll();
        assertThat(seasons).hasSize(databaseSizeBeforeCreate + 1);
        Season testSeason = seasons.get(seasons.size() - 1);
        assertThat(testSeason.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSeason.getStartAt()).isEqualTo(DEFAULT_START_AT);
        assertThat(testSeason.getEndAt()).isEqualTo(DEFAULT_END_AT);
    }

    @Test
    @Transactional
    public void getAllSeasons() throws Exception {
        // Initialize the database
        seasonRepository.saveAndFlush(season);

        // Get all the seasons
        restSeasonMockMvc.perform(get("/api/seasons?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(season.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].startAt").value(hasItem(DEFAULT_START_AT.toString())))
                .andExpect(jsonPath("$.[*].endAt").value(hasItem(DEFAULT_END_AT.toString())));
    }

    @Test
    @Transactional
    public void getSeason() throws Exception {
        // Initialize the database
        seasonRepository.saveAndFlush(season);

        // Get the season
        restSeasonMockMvc.perform(get("/api/seasons/{id}", season.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(season.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.startAt").value(DEFAULT_START_AT.toString()))
            .andExpect(jsonPath("$.endAt").value(DEFAULT_END_AT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSeason() throws Exception {
        // Get the season
        restSeasonMockMvc.perform(get("/api/seasons/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSeason() throws Exception {
        // Initialize the database
        seasonRepository.saveAndFlush(season);
        int databaseSizeBeforeUpdate = seasonRepository.findAll().size();

        // Update the season
        Season updatedSeason = seasonRepository.findOne(season.getId());
        updatedSeason.setName(UPDATED_NAME);
        updatedSeason.setStartAt(UPDATED_START_AT);
        updatedSeason.setEndAt(UPDATED_END_AT);

        restSeasonMockMvc.perform(put("/api/seasons")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedSeason)))
                .andExpect(status().isOk());

        // Validate the Season in the database
        List<Season> seasons = seasonRepository.findAll();
        assertThat(seasons).hasSize(databaseSizeBeforeUpdate);
        Season testSeason = seasons.get(seasons.size() - 1);
        assertThat(testSeason.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSeason.getStartAt()).isEqualTo(UPDATED_START_AT);
        assertThat(testSeason.getEndAt()).isEqualTo(UPDATED_END_AT);
    }

    @Test
    @Transactional
    public void deleteSeason() throws Exception {
        // Initialize the database
        seasonRepository.saveAndFlush(season);
        int databaseSizeBeforeDelete = seasonRepository.findAll().size();

        // Get the season
        restSeasonMockMvc.perform(delete("/api/seasons/{id}", season.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Season> seasons = seasonRepository.findAll();
        assertThat(seasons).hasSize(databaseSizeBeforeDelete - 1);
    }
}
