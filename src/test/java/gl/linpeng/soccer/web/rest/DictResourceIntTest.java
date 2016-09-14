package gl.linpeng.soccer.web.rest;

import gl.linpeng.soccer.SoccerApp;

import gl.linpeng.soccer.domain.Dict;
import gl.linpeng.soccer.repository.DictRepository;

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
 * Test class for the DictResource REST controller.
 *
 * @see DictResource
 */
@RunWith(SpringRunner.class)

@SpringBootTest(classes = SoccerApp.class)

public class DictResourceIntTest {
    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_CODE = "AAAAA";
    private static final String UPDATED_CODE = "BBBBB";

    @Inject
    private DictRepository dictRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restDictMockMvc;

    private Dict dict;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DictResource dictResource = new DictResource();
        ReflectionTestUtils.setField(dictResource, "dictRepository", dictRepository);
        this.restDictMockMvc = MockMvcBuilders.standaloneSetup(dictResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dict createEntity(EntityManager em) {
        Dict dict = new Dict();
        dict.setName(DEFAULT_NAME);
        dict.setCode(DEFAULT_CODE);
        return dict;
    }

    @Before
    public void initTest() {
        dict = createEntity(em);
    }

    @Test
    @Transactional
    public void createDict() throws Exception {
        int databaseSizeBeforeCreate = dictRepository.findAll().size();

        // Create the Dict

        restDictMockMvc.perform(post("/api/dicts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dict)))
                .andExpect(status().isCreated());

        // Validate the Dict in the database
        List<Dict> dicts = dictRepository.findAll();
        assertThat(dicts).hasSize(databaseSizeBeforeCreate + 1);
        Dict testDict = dicts.get(dicts.size() - 1);
        assertThat(testDict.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDict.getCode()).isEqualTo(DEFAULT_CODE);
    }

    @Test
    @Transactional
    public void getAllDicts() throws Exception {
        // Initialize the database
        dictRepository.saveAndFlush(dict);

        // Get all the dicts
        restDictMockMvc.perform(get("/api/dicts?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(dict.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())));
    }

    @Test
    @Transactional
    public void getDict() throws Exception {
        // Initialize the database
        dictRepository.saveAndFlush(dict);

        // Get the dict
        restDictMockMvc.perform(get("/api/dicts/{id}", dict.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(dict.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDict() throws Exception {
        // Get the dict
        restDictMockMvc.perform(get("/api/dicts/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDict() throws Exception {
        // Initialize the database
        dictRepository.saveAndFlush(dict);
        int databaseSizeBeforeUpdate = dictRepository.findAll().size();

        // Update the dict
        Dict updatedDict = dictRepository.findOne(dict.getId());
        updatedDict.setName(UPDATED_NAME);
        updatedDict.setCode(UPDATED_CODE);

        restDictMockMvc.perform(put("/api/dicts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedDict)))
                .andExpect(status().isOk());

        // Validate the Dict in the database
        List<Dict> dicts = dictRepository.findAll();
        assertThat(dicts).hasSize(databaseSizeBeforeUpdate);
        Dict testDict = dicts.get(dicts.size() - 1);
        assertThat(testDict.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDict.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    public void deleteDict() throws Exception {
        // Initialize the database
        dictRepository.saveAndFlush(dict);
        int databaseSizeBeforeDelete = dictRepository.findAll().size();

        // Get the dict
        restDictMockMvc.perform(delete("/api/dicts/{id}", dict.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Dict> dicts = dictRepository.findAll();
        assertThat(dicts).hasSize(databaseSizeBeforeDelete - 1);
    }
}
