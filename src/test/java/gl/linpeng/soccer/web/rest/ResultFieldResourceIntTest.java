package gl.linpeng.soccer.web.rest;

import gl.linpeng.soccer.SoccerApp;

import gl.linpeng.soccer.domain.ResultField;
import gl.linpeng.soccer.repository.ResultFieldRepository;

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
 * Test class for the ResultFieldResource REST controller.
 *
 * @see ResultFieldResource
 */
@RunWith(SpringRunner.class)

@SpringBootTest(classes = SoccerApp.class)

public class ResultFieldResourceIntTest {
    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private ResultFieldRepository resultFieldRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restResultFieldMockMvc;

    private ResultField resultField;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ResultFieldResource resultFieldResource = new ResultFieldResource();
        ReflectionTestUtils.setField(resultFieldResource, "resultFieldRepository", resultFieldRepository);
        this.restResultFieldMockMvc = MockMvcBuilders.standaloneSetup(resultFieldResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ResultField createEntity(EntityManager em) {
        ResultField resultField = new ResultField();
        resultField.setName(DEFAULT_NAME);
        return resultField;
    }

    @Before
    public void initTest() {
        resultField = createEntity(em);
    }

    @Test
    @Transactional
    public void createResultField() throws Exception {
        int databaseSizeBeforeCreate = resultFieldRepository.findAll().size();

        // Create the ResultField

        restResultFieldMockMvc.perform(post("/api/result-fields")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(resultField)))
                .andExpect(status().isCreated());

        // Validate the ResultField in the database
        List<ResultField> resultFields = resultFieldRepository.findAll();
        assertThat(resultFields).hasSize(databaseSizeBeforeCreate + 1);
        ResultField testResultField = resultFields.get(resultFields.size() - 1);
        assertThat(testResultField.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void getAllResultFields() throws Exception {
        // Initialize the database
        resultFieldRepository.saveAndFlush(resultField);

        // Get all the resultFields
        restResultFieldMockMvc.perform(get("/api/result-fields?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(resultField.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getResultField() throws Exception {
        // Initialize the database
        resultFieldRepository.saveAndFlush(resultField);

        // Get the resultField
        restResultFieldMockMvc.perform(get("/api/result-fields/{id}", resultField.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(resultField.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingResultField() throws Exception {
        // Get the resultField
        restResultFieldMockMvc.perform(get("/api/result-fields/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateResultField() throws Exception {
        // Initialize the database
        resultFieldRepository.saveAndFlush(resultField);
        int databaseSizeBeforeUpdate = resultFieldRepository.findAll().size();

        // Update the resultField
        ResultField updatedResultField = resultFieldRepository.findOne(resultField.getId());
        updatedResultField.setName(UPDATED_NAME);

        restResultFieldMockMvc.perform(put("/api/result-fields")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedResultField)))
                .andExpect(status().isOk());

        // Validate the ResultField in the database
        List<ResultField> resultFields = resultFieldRepository.findAll();
        assertThat(resultFields).hasSize(databaseSizeBeforeUpdate);
        ResultField testResultField = resultFields.get(resultFields.size() - 1);
        assertThat(testResultField.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteResultField() throws Exception {
        // Initialize the database
        resultFieldRepository.saveAndFlush(resultField);
        int databaseSizeBeforeDelete = resultFieldRepository.findAll().size();

        // Get the resultField
        restResultFieldMockMvc.perform(delete("/api/result-fields/{id}", resultField.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ResultField> resultFields = resultFieldRepository.findAll();
        assertThat(resultFields).hasSize(databaseSizeBeforeDelete - 1);
    }
}
