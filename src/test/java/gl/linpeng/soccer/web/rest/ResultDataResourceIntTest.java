package gl.linpeng.soccer.web.rest;

import gl.linpeng.soccer.SoccerApp;

import gl.linpeng.soccer.domain.ResultData;
import gl.linpeng.soccer.repository.ResultDataRepository;

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
 * Test class for the ResultDataResource REST controller.
 *
 * @see ResultDataResource
 */
@RunWith(SpringRunner.class)

@SpringBootTest(classes = SoccerApp.class)

public class ResultDataResourceIntTest {

    private static final Integer DEFAULT_VALUE = 1;
    private static final Integer UPDATED_VALUE = 2;

    @Inject
    private ResultDataRepository resultDataRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restResultDataMockMvc;

    private ResultData resultData;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ResultDataResource resultDataResource = new ResultDataResource();
        ReflectionTestUtils.setField(resultDataResource, "resultDataRepository", resultDataRepository);
        this.restResultDataMockMvc = MockMvcBuilders.standaloneSetup(resultDataResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ResultData createEntity(EntityManager em) {
        ResultData resultData = new ResultData();
        resultData.setValue(DEFAULT_VALUE);
        return resultData;
    }

    @Before
    public void initTest() {
        resultData = createEntity(em);
    }

    @Test
    @Transactional
    public void createResultData() throws Exception {
        int databaseSizeBeforeCreate = resultDataRepository.findAll().size();

        // Create the ResultData

        restResultDataMockMvc.perform(post("/api/result-data")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(resultData)))
                .andExpect(status().isCreated());

        // Validate the ResultData in the database
        List<ResultData> resultData = resultDataRepository.findAll();
        assertThat(resultData).hasSize(databaseSizeBeforeCreate + 1);
        ResultData testResultData = resultData.get(resultData.size() - 1);
        assertThat(testResultData.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void getAllResultData() throws Exception {
        // Initialize the database
        resultDataRepository.saveAndFlush(resultData);

        // Get all the resultData
        restResultDataMockMvc.perform(get("/api/result-data?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(resultData.getId().intValue())))
                .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }

    @Test
    @Transactional
    public void getResultData() throws Exception {
        // Initialize the database
        resultDataRepository.saveAndFlush(resultData);

        // Get the resultData
        restResultDataMockMvc.perform(get("/api/result-data/{id}", resultData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(resultData.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE));
    }

    @Test
    @Transactional
    public void getNonExistingResultData() throws Exception {
        // Get the resultData
        restResultDataMockMvc.perform(get("/api/result-data/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateResultData() throws Exception {
        // Initialize the database
        resultDataRepository.saveAndFlush(resultData);
        int databaseSizeBeforeUpdate = resultDataRepository.findAll().size();

        // Update the resultData
        ResultData updatedResultData = resultDataRepository.findOne(resultData.getId());
        updatedResultData.setValue(UPDATED_VALUE);

        restResultDataMockMvc.perform(put("/api/result-data")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedResultData)))
                .andExpect(status().isOk());

        // Validate the ResultData in the database
        List<ResultData> resultData = resultDataRepository.findAll();
        assertThat(resultData).hasSize(databaseSizeBeforeUpdate);
        ResultData testResultData = resultData.get(resultData.size() - 1);
        assertThat(testResultData.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void deleteResultData() throws Exception {
        // Initialize the database
        resultDataRepository.saveAndFlush(resultData);
        int databaseSizeBeforeDelete = resultDataRepository.findAll().size();

        // Get the resultData
        restResultDataMockMvc.perform(delete("/api/result-data/{id}", resultData.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ResultData> resultData = resultDataRepository.findAll();
        assertThat(resultData).hasSize(databaseSizeBeforeDelete - 1);
    }
}
