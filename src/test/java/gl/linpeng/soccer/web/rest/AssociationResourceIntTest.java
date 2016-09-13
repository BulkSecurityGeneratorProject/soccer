package gl.linpeng.soccer.web.rest;

import gl.linpeng.soccer.SoccerApp;

import gl.linpeng.soccer.domain.Association;
import gl.linpeng.soccer.repository.AssociationRepository;

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
 * Test class for the AssociationResource REST controller.
 *
 * @see AssociationResource
 */
@RunWith(SpringRunner.class)

@SpringBootTest(classes = SoccerApp.class)

public class AssociationResourceIntTest {
    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final LocalDate DEFAULT_CREATE_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_AT = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private AssociationRepository associationRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restAssociationMockMvc;

    private Association association;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AssociationResource associationResource = new AssociationResource();
        ReflectionTestUtils.setField(associationResource, "associationRepository", associationRepository);
        this.restAssociationMockMvc = MockMvcBuilders.standaloneSetup(associationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Association createEntity(EntityManager em) {
        Association association = new Association();
        association.setName(DEFAULT_NAME);
        association.setCreateAt(DEFAULT_CREATE_AT);
        return association;
    }

    @Before
    public void initTest() {
        association = createEntity(em);
    }

    @Test
    @Transactional
    public void createAssociation() throws Exception {
        int databaseSizeBeforeCreate = associationRepository.findAll().size();

        // Create the Association

        restAssociationMockMvc.perform(post("/api/associations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(association)))
                .andExpect(status().isCreated());

        // Validate the Association in the database
        List<Association> associations = associationRepository.findAll();
        assertThat(associations).hasSize(databaseSizeBeforeCreate + 1);
        Association testAssociation = associations.get(associations.size() - 1);
        assertThat(testAssociation.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAssociation.getCreateAt()).isEqualTo(DEFAULT_CREATE_AT);
    }

    @Test
    @Transactional
    public void getAllAssociations() throws Exception {
        // Initialize the database
        associationRepository.saveAndFlush(association);

        // Get all the associations
        restAssociationMockMvc.perform(get("/api/associations?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(association.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT.toString())));
    }

    @Test
    @Transactional
    public void getAssociation() throws Exception {
        // Initialize the database
        associationRepository.saveAndFlush(association);

        // Get the association
        restAssociationMockMvc.perform(get("/api/associations/{id}", association.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(association.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.createAt").value(DEFAULT_CREATE_AT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAssociation() throws Exception {
        // Get the association
        restAssociationMockMvc.perform(get("/api/associations/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAssociation() throws Exception {
        // Initialize the database
        associationRepository.saveAndFlush(association);
        int databaseSizeBeforeUpdate = associationRepository.findAll().size();

        // Update the association
        Association updatedAssociation = associationRepository.findOne(association.getId());
        updatedAssociation.setName(UPDATED_NAME);
        updatedAssociation.setCreateAt(UPDATED_CREATE_AT);

        restAssociationMockMvc.perform(put("/api/associations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedAssociation)))
                .andExpect(status().isOk());

        // Validate the Association in the database
        List<Association> associations = associationRepository.findAll();
        assertThat(associations).hasSize(databaseSizeBeforeUpdate);
        Association testAssociation = associations.get(associations.size() - 1);
        assertThat(testAssociation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAssociation.getCreateAt()).isEqualTo(UPDATED_CREATE_AT);
    }

    @Test
    @Transactional
    public void deleteAssociation() throws Exception {
        // Initialize the database
        associationRepository.saveAndFlush(association);
        int databaseSizeBeforeDelete = associationRepository.findAll().size();

        // Get the association
        restAssociationMockMvc.perform(delete("/api/associations/{id}", association.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Association> associations = associationRepository.findAll();
        assertThat(associations).hasSize(databaseSizeBeforeDelete - 1);
    }
}
