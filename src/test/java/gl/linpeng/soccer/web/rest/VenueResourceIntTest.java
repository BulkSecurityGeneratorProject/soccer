package gl.linpeng.soccer.web.rest;

import gl.linpeng.soccer.SoccerApp;

import gl.linpeng.soccer.domain.Venue;
import gl.linpeng.soccer.repository.VenueRepository;

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
 * Test class for the VenueResource REST controller.
 *
 * @see VenueResource
 */
@RunWith(SpringRunner.class)

@SpringBootTest(classes = SoccerApp.class)

public class VenueResourceIntTest {
    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_SHORT_NAME = "AAAAA";
    private static final String UPDATED_SHORT_NAME = "BBBBB";

    private static final LocalDate DEFAULT_CREATE_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_AT = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_ADDRESS = "AAAAA";
    private static final String UPDATED_ADDRESS = "BBBBB";
    private static final String DEFAULT_CITY = "AAAAA";
    private static final String UPDATED_CITY = "BBBBB";
    private static final String DEFAULT_PROVINCE = "AAAAA";
    private static final String UPDATED_PROVINCE = "BBBBB";
    private static final String DEFAULT_TOWN = "AAAAA";
    private static final String UPDATED_TOWN = "BBBBB";
    private static final String DEFAULT_COUNTRY = "AAAAA";
    private static final String UPDATED_COUNTRY = "BBBBB";
    private static final String DEFAULT_ZIP = "AAAAA";
    private static final String UPDATED_ZIP = "BBBBB";
    private static final String DEFAULT_TELEPHONE = "AAAAA";
    private static final String UPDATED_TELEPHONE = "BBBBB";
    private static final String DEFAULT_LATLNG = "AAAAA";
    private static final String UPDATED_LATLNG = "BBBBB";

    @Inject
    private VenueRepository venueRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restVenueMockMvc;

    private Venue venue;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        VenueResource venueResource = new VenueResource();
        ReflectionTestUtils.setField(venueResource, "venueRepository", venueRepository);
        this.restVenueMockMvc = MockMvcBuilders.standaloneSetup(venueResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Venue createEntity(EntityManager em) {
        Venue venue = new Venue();
        venue.setName(DEFAULT_NAME);
        venue.setShortName(DEFAULT_SHORT_NAME);
        venue.setCreateAt(DEFAULT_CREATE_AT);
        venue.setAddress(DEFAULT_ADDRESS);
        venue.setCity(DEFAULT_CITY);
        venue.setProvince(DEFAULT_PROVINCE);
        venue.setTown(DEFAULT_TOWN);
        venue.setCountry(DEFAULT_COUNTRY);
        venue.setZip(DEFAULT_ZIP);
        venue.setTelephone(DEFAULT_TELEPHONE);
        venue.setLatlng(DEFAULT_LATLNG);
        return venue;
    }

    @Before
    public void initTest() {
        venue = createEntity(em);
    }

    @Test
    @Transactional
    public void createVenue() throws Exception {
        int databaseSizeBeforeCreate = venueRepository.findAll().size();

        // Create the Venue

        restVenueMockMvc.perform(post("/api/venues")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(venue)))
                .andExpect(status().isCreated());

        // Validate the Venue in the database
        List<Venue> venues = venueRepository.findAll();
        assertThat(venues).hasSize(databaseSizeBeforeCreate + 1);
        Venue testVenue = venues.get(venues.size() - 1);
        assertThat(testVenue.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testVenue.getShortName()).isEqualTo(DEFAULT_SHORT_NAME);
        assertThat(testVenue.getCreateAt()).isEqualTo(DEFAULT_CREATE_AT);
        assertThat(testVenue.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testVenue.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testVenue.getProvince()).isEqualTo(DEFAULT_PROVINCE);
        assertThat(testVenue.getTown()).isEqualTo(DEFAULT_TOWN);
        assertThat(testVenue.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testVenue.getZip()).isEqualTo(DEFAULT_ZIP);
        assertThat(testVenue.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testVenue.getLatlng()).isEqualTo(DEFAULT_LATLNG);
    }

    @Test
    @Transactional
    public void getAllVenues() throws Exception {
        // Initialize the database
        venueRepository.saveAndFlush(venue);

        // Get all the venues
        restVenueMockMvc.perform(get("/api/venues?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(venue.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].shortName").value(hasItem(DEFAULT_SHORT_NAME.toString())))
                .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT.toString())))
                .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
                .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
                .andExpect(jsonPath("$.[*].province").value(hasItem(DEFAULT_PROVINCE.toString())))
                .andExpect(jsonPath("$.[*].town").value(hasItem(DEFAULT_TOWN.toString())))
                .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY.toString())))
                .andExpect(jsonPath("$.[*].zip").value(hasItem(DEFAULT_ZIP.toString())))
                .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE.toString())))
                .andExpect(jsonPath("$.[*].latlng").value(hasItem(DEFAULT_LATLNG.toString())));
    }

    @Test
    @Transactional
    public void getVenue() throws Exception {
        // Initialize the database
        venueRepository.saveAndFlush(venue);

        // Get the venue
        restVenueMockMvc.perform(get("/api/venues/{id}", venue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(venue.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.shortName").value(DEFAULT_SHORT_NAME.toString()))
            .andExpect(jsonPath("$.createAt").value(DEFAULT_CREATE_AT.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.province").value(DEFAULT_PROVINCE.toString()))
            .andExpect(jsonPath("$.town").value(DEFAULT_TOWN.toString()))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY.toString()))
            .andExpect(jsonPath("$.zip").value(DEFAULT_ZIP.toString()))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE.toString()))
            .andExpect(jsonPath("$.latlng").value(DEFAULT_LATLNG.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingVenue() throws Exception {
        // Get the venue
        restVenueMockMvc.perform(get("/api/venues/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVenue() throws Exception {
        // Initialize the database
        venueRepository.saveAndFlush(venue);
        int databaseSizeBeforeUpdate = venueRepository.findAll().size();

        // Update the venue
        Venue updatedVenue = venueRepository.findOne(venue.getId());
        updatedVenue.setName(UPDATED_NAME);
        updatedVenue.setShortName(UPDATED_SHORT_NAME);
        updatedVenue.setCreateAt(UPDATED_CREATE_AT);
        updatedVenue.setAddress(UPDATED_ADDRESS);
        updatedVenue.setCity(UPDATED_CITY);
        updatedVenue.setProvince(UPDATED_PROVINCE);
        updatedVenue.setTown(UPDATED_TOWN);
        updatedVenue.setCountry(UPDATED_COUNTRY);
        updatedVenue.setZip(UPDATED_ZIP);
        updatedVenue.setTelephone(UPDATED_TELEPHONE);
        updatedVenue.setLatlng(UPDATED_LATLNG);

        restVenueMockMvc.perform(put("/api/venues")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedVenue)))
                .andExpect(status().isOk());

        // Validate the Venue in the database
        List<Venue> venues = venueRepository.findAll();
        assertThat(venues).hasSize(databaseSizeBeforeUpdate);
        Venue testVenue = venues.get(venues.size() - 1);
        assertThat(testVenue.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testVenue.getShortName()).isEqualTo(UPDATED_SHORT_NAME);
        assertThat(testVenue.getCreateAt()).isEqualTo(UPDATED_CREATE_AT);
        assertThat(testVenue.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testVenue.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testVenue.getProvince()).isEqualTo(UPDATED_PROVINCE);
        assertThat(testVenue.getTown()).isEqualTo(UPDATED_TOWN);
        assertThat(testVenue.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testVenue.getZip()).isEqualTo(UPDATED_ZIP);
        assertThat(testVenue.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testVenue.getLatlng()).isEqualTo(UPDATED_LATLNG);
    }

    @Test
    @Transactional
    public void deleteVenue() throws Exception {
        // Initialize the database
        venueRepository.saveAndFlush(venue);
        int databaseSizeBeforeDelete = venueRepository.findAll().size();

        // Get the venue
        restVenueMockMvc.perform(delete("/api/venues/{id}", venue.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Venue> venues = venueRepository.findAll();
        assertThat(venues).hasSize(databaseSizeBeforeDelete - 1);
    }
}
