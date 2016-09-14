package gl.linpeng.soccer.web.rest;

import gl.linpeng.soccer.SoccerApp;

import gl.linpeng.soccer.domain.DictKind;
import gl.linpeng.soccer.repository.DictKindRepository;

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
 * Test class for the DictKindResource REST controller.
 *
 * @see DictKindResource
 */
@RunWith(SpringRunner.class)

@SpringBootTest(classes = SoccerApp.class)

public class DictKindResourceIntTest {
    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private DictKindRepository dictKindRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restDictKindMockMvc;

    private DictKind dictKind;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DictKindResource dictKindResource = new DictKindResource();
        ReflectionTestUtils.setField(dictKindResource, "dictKindRepository", dictKindRepository);
        this.restDictKindMockMvc = MockMvcBuilders.standaloneSetup(dictKindResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DictKind createEntity(EntityManager em) {
        DictKind dictKind = new DictKind();
        dictKind.setName(DEFAULT_NAME);
        return dictKind;
    }

    @Before
    public void initTest() {
        dictKind = createEntity(em);
    }

    @Test
    @Transactional
    public void createDictKind() throws Exception {
        int databaseSizeBeforeCreate = dictKindRepository.findAll().size();

        // Create the DictKind

        restDictKindMockMvc.perform(post("/api/dict-kinds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dictKind)))
                .andExpect(status().isCreated());

        // Validate the DictKind in the database
        List<DictKind> dictKinds = dictKindRepository.findAll();
        assertThat(dictKinds).hasSize(databaseSizeBeforeCreate + 1);
        DictKind testDictKind = dictKinds.get(dictKinds.size() - 1);
        assertThat(testDictKind.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void getAllDictKinds() throws Exception {
        // Initialize the database
        dictKindRepository.saveAndFlush(dictKind);

        // Get all the dictKinds
        restDictKindMockMvc.perform(get("/api/dict-kinds?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(dictKind.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getDictKind() throws Exception {
        // Initialize the database
        dictKindRepository.saveAndFlush(dictKind);

        // Get the dictKind
        restDictKindMockMvc.perform(get("/api/dict-kinds/{id}", dictKind.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(dictKind.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDictKind() throws Exception {
        // Get the dictKind
        restDictKindMockMvc.perform(get("/api/dict-kinds/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDictKind() throws Exception {
        // Initialize the database
        dictKindRepository.saveAndFlush(dictKind);
        int databaseSizeBeforeUpdate = dictKindRepository.findAll().size();

        // Update the dictKind
        DictKind updatedDictKind = dictKindRepository.findOne(dictKind.getId());
        updatedDictKind.setName(UPDATED_NAME);

        restDictKindMockMvc.perform(put("/api/dict-kinds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedDictKind)))
                .andExpect(status().isOk());

        // Validate the DictKind in the database
        List<DictKind> dictKinds = dictKindRepository.findAll();
        assertThat(dictKinds).hasSize(databaseSizeBeforeUpdate);
        DictKind testDictKind = dictKinds.get(dictKinds.size() - 1);
        assertThat(testDictKind.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteDictKind() throws Exception {
        // Initialize the database
        dictKindRepository.saveAndFlush(dictKind);
        int databaseSizeBeforeDelete = dictKindRepository.findAll().size();

        // Get the dictKind
        restDictKindMockMvc.perform(delete("/api/dict-kinds/{id}", dictKind.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<DictKind> dictKinds = dictKindRepository.findAll();
        assertThat(dictKinds).hasSize(databaseSizeBeforeDelete - 1);
    }
}
