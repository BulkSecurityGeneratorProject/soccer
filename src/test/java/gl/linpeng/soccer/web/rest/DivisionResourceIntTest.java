package gl.linpeng.soccer.web.rest;

import gl.linpeng.soccer.SoccerApp;

import gl.linpeng.soccer.domain.Division;
import gl.linpeng.soccer.repository.DivisionRepository;

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
 * Test class for the DivisionResource REST controller.
 *
 * @see DivisionResource
 */
@RunWith(SpringRunner.class)

@SpringBootTest(classes = SoccerApp.class)

public class DivisionResourceIntTest {
    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final LocalDate DEFAULT_CREATE_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_AT = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_PICTURE = "AAAAA";
    private static final String UPDATED_PICTURE = "BBBBB";

    @Inject
    private DivisionRepository divisionRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restDivisionMockMvc;

    private Division division;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DivisionResource divisionResource = new DivisionResource();
        ReflectionTestUtils.setField(divisionResource, "divisionRepository", divisionRepository);
        this.restDivisionMockMvc = MockMvcBuilders.standaloneSetup(divisionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Division createEntity(EntityManager em) {
        Division division = new Division()
                .name(DEFAULT_NAME)
                .createAt(DEFAULT_CREATE_AT)
                .picture(DEFAULT_PICTURE);
        return division;
    }

    @Before
    public void initTest() {
        division = createEntity(em);
    }

    @Test
    @Transactional
    public void createDivision() throws Exception {
        int databaseSizeBeforeCreate = divisionRepository.findAll().size();

        // Create the Division

        restDivisionMockMvc.perform(post("/api/divisions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(division)))
                .andExpect(status().isCreated());

        // Validate the Division in the database
        List<Division> divisions = divisionRepository.findAll();
        assertThat(divisions).hasSize(databaseSizeBeforeCreate + 1);
        Division testDivision = divisions.get(divisions.size() - 1);
        assertThat(testDivision.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDivision.getCreateAt()).isEqualTo(DEFAULT_CREATE_AT);
        assertThat(testDivision.getPicture()).isEqualTo(DEFAULT_PICTURE);
    }

    @Test
    @Transactional
    public void getAllDivisions() throws Exception {
        // Initialize the database
        divisionRepository.saveAndFlush(division);

        // Get all the divisions
        restDivisionMockMvc.perform(get("/api/divisions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(division.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT.toString())))
                .andExpect(jsonPath("$.[*].picture").value(hasItem(DEFAULT_PICTURE.toString())));
    }

    @Test
    @Transactional
    public void getDivision() throws Exception {
        // Initialize the database
        divisionRepository.saveAndFlush(division);

        // Get the division
        restDivisionMockMvc.perform(get("/api/divisions/{id}", division.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(division.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.createAt").value(DEFAULT_CREATE_AT.toString()))
            .andExpect(jsonPath("$.picture").value(DEFAULT_PICTURE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDivision() throws Exception {
        // Get the division
        restDivisionMockMvc.perform(get("/api/divisions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDivision() throws Exception {
        // Initialize the database
        divisionRepository.saveAndFlush(division);
        int databaseSizeBeforeUpdate = divisionRepository.findAll().size();

        // Update the division
        Division updatedDivision = divisionRepository.findOne(division.getId());
        updatedDivision
                .name(UPDATED_NAME)
                .createAt(UPDATED_CREATE_AT)
                .picture(UPDATED_PICTURE);

        restDivisionMockMvc.perform(put("/api/divisions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedDivision)))
                .andExpect(status().isOk());

        // Validate the Division in the database
        List<Division> divisions = divisionRepository.findAll();
        assertThat(divisions).hasSize(databaseSizeBeforeUpdate);
        Division testDivision = divisions.get(divisions.size() - 1);
        assertThat(testDivision.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDivision.getCreateAt()).isEqualTo(UPDATED_CREATE_AT);
        assertThat(testDivision.getPicture()).isEqualTo(UPDATED_PICTURE);
    }

    @Test
    @Transactional
    public void deleteDivision() throws Exception {
        // Initialize the database
        divisionRepository.saveAndFlush(division);
        int databaseSizeBeforeDelete = divisionRepository.findAll().size();

        // Get the division
        restDivisionMockMvc.perform(delete("/api/divisions/{id}", division.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Division> divisions = divisionRepository.findAll();
        assertThat(divisions).hasSize(databaseSizeBeforeDelete - 1);
    }
}
