package gl.linpeng.soccer.web.rest;

import gl.linpeng.soccer.SoccerApp;

import gl.linpeng.soccer.domain.Catalog;
import gl.linpeng.soccer.repository.CatalogRepository;

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
 * Test class for the CatalogResource REST controller.
 *
 * @see CatalogResource
 */
@RunWith(SpringRunner.class)

@SpringBootTest(classes = SoccerApp.class)

public class CatalogResourceIntTest {
    private static final String DEFAULT_TITLE = "AAAAA";
    private static final String UPDATED_TITLE = "BBBBB";

    private static final Integer DEFAULT_TYPE = 1;
    private static final Integer UPDATED_TYPE = 2;

    private static final Boolean DEFAULT_OPEN_COMMENT = false;
    private static final Boolean UPDATED_OPEN_COMMENT = true;

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;

    @Inject
    private CatalogRepository catalogRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restCatalogMockMvc;

    private Catalog catalog;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CatalogResource catalogResource = new CatalogResource();
        ReflectionTestUtils.setField(catalogResource, "catalogRepository", catalogRepository);
        this.restCatalogMockMvc = MockMvcBuilders.standaloneSetup(catalogResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Catalog createEntity(EntityManager em) {
        Catalog catalog = new Catalog()
                .title(DEFAULT_TITLE)
                .type(DEFAULT_TYPE)
                .openComment(DEFAULT_OPEN_COMMENT)
                .status(DEFAULT_STATUS);
        return catalog;
    }

    @Before
    public void initTest() {
        catalog = createEntity(em);
    }

    @Test
    @Transactional
    public void createCatalog() throws Exception {
        int databaseSizeBeforeCreate = catalogRepository.findAll().size();

        // Create the Catalog

        restCatalogMockMvc.perform(post("/api/catalogs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(catalog)))
                .andExpect(status().isCreated());

        // Validate the Catalog in the database
        List<Catalog> catalogs = catalogRepository.findAll();
        assertThat(catalogs).hasSize(databaseSizeBeforeCreate + 1);
        Catalog testCatalog = catalogs.get(catalogs.size() - 1);
        assertThat(testCatalog.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testCatalog.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testCatalog.isOpenComment()).isEqualTo(DEFAULT_OPEN_COMMENT);
        assertThat(testCatalog.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void getAllCatalogs() throws Exception {
        // Initialize the database
        catalogRepository.saveAndFlush(catalog);

        // Get all the catalogs
        restCatalogMockMvc.perform(get("/api/catalogs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(catalog.getId().intValue())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
                .andExpect(jsonPath("$.[*].openComment").value(hasItem(DEFAULT_OPEN_COMMENT.booleanValue())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    @Test
    @Transactional
    public void getCatalog() throws Exception {
        // Initialize the database
        catalogRepository.saveAndFlush(catalog);

        // Get the catalog
        restCatalogMockMvc.perform(get("/api/catalogs/{id}", catalog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(catalog.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.openComment").value(DEFAULT_OPEN_COMMENT.booleanValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }

    @Test
    @Transactional
    public void getNonExistingCatalog() throws Exception {
        // Get the catalog
        restCatalogMockMvc.perform(get("/api/catalogs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCatalog() throws Exception {
        // Initialize the database
        catalogRepository.saveAndFlush(catalog);
        int databaseSizeBeforeUpdate = catalogRepository.findAll().size();

        // Update the catalog
        Catalog updatedCatalog = catalogRepository.findOne(catalog.getId());
        updatedCatalog
                .title(UPDATED_TITLE)
                .type(UPDATED_TYPE)
                .openComment(UPDATED_OPEN_COMMENT)
                .status(UPDATED_STATUS);

        restCatalogMockMvc.perform(put("/api/catalogs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCatalog)))
                .andExpect(status().isOk());

        // Validate the Catalog in the database
        List<Catalog> catalogs = catalogRepository.findAll();
        assertThat(catalogs).hasSize(databaseSizeBeforeUpdate);
        Catalog testCatalog = catalogs.get(catalogs.size() - 1);
        assertThat(testCatalog.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testCatalog.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testCatalog.isOpenComment()).isEqualTo(UPDATED_OPEN_COMMENT);
        assertThat(testCatalog.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void deleteCatalog() throws Exception {
        // Initialize the database
        catalogRepository.saveAndFlush(catalog);
        int databaseSizeBeforeDelete = catalogRepository.findAll().size();

        // Get the catalog
        restCatalogMockMvc.perform(delete("/api/catalogs/{id}", catalog.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Catalog> catalogs = catalogRepository.findAll();
        assertThat(catalogs).hasSize(databaseSizeBeforeDelete - 1);
    }
}
