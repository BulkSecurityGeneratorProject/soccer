package gl.linpeng.soccer.web.rest;

import gl.linpeng.soccer.SoccerApp;

import gl.linpeng.soccer.domain.RankingRule;
import gl.linpeng.soccer.repository.RankingRuleRepository;

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
 * Test class for the RankingRuleResource REST controller.
 *
 * @see RankingRuleResource
 */
@RunWith(SpringRunner.class)

@SpringBootTest(classes = SoccerApp.class)

public class RankingRuleResourceIntTest {

    private static final Integer DEFAULT_WIN = 1;
    private static final Integer UPDATED_WIN = 2;

    private static final Integer DEFAULT_LOSS = 1;
    private static final Integer UPDATED_LOSS = 2;

    private static final Integer DEFAULT_DRAW = 1;
    private static final Integer UPDATED_DRAW = 2;

    private static final Integer DEFAULT_SCORE_FOR = 1;
    private static final Integer UPDATED_SCORE_FOR = 2;

    private static final Integer DEFAULT_SCORE_AGAIN = 1;
    private static final Integer UPDATED_SCORE_AGAIN = 2;

    @Inject
    private RankingRuleRepository rankingRuleRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restRankingRuleMockMvc;

    private RankingRule rankingRule;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RankingRuleResource rankingRuleResource = new RankingRuleResource();
        ReflectionTestUtils.setField(rankingRuleResource, "rankingRuleRepository", rankingRuleRepository);
        this.restRankingRuleMockMvc = MockMvcBuilders.standaloneSetup(rankingRuleResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RankingRule createEntity(EntityManager em) {
        RankingRule rankingRule = new RankingRule();
        rankingRule.setWin(DEFAULT_WIN);
        rankingRule.setLoss(DEFAULT_LOSS);
        rankingRule.setDraw(DEFAULT_DRAW);
        rankingRule.setScoreFor(DEFAULT_SCORE_FOR);
        rankingRule.setScoreAgain(DEFAULT_SCORE_AGAIN);
        return rankingRule;
    }

    @Before
    public void initTest() {
        rankingRule = createEntity(em);
    }

    @Test
    @Transactional
    public void createRankingRule() throws Exception {
        int databaseSizeBeforeCreate = rankingRuleRepository.findAll().size();

        // Create the RankingRule

        restRankingRuleMockMvc.perform(post("/api/ranking-rules")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(rankingRule)))
                .andExpect(status().isCreated());

        // Validate the RankingRule in the database
        List<RankingRule> rankingRules = rankingRuleRepository.findAll();
        assertThat(rankingRules).hasSize(databaseSizeBeforeCreate + 1);
        RankingRule testRankingRule = rankingRules.get(rankingRules.size() - 1);
        assertThat(testRankingRule.getWin()).isEqualTo(DEFAULT_WIN);
        assertThat(testRankingRule.getLoss()).isEqualTo(DEFAULT_LOSS);
        assertThat(testRankingRule.getDraw()).isEqualTo(DEFAULT_DRAW);
        assertThat(testRankingRule.getScoreFor()).isEqualTo(DEFAULT_SCORE_FOR);
        assertThat(testRankingRule.getScoreAgain()).isEqualTo(DEFAULT_SCORE_AGAIN);
    }

    @Test
    @Transactional
    public void getAllRankingRules() throws Exception {
        // Initialize the database
        rankingRuleRepository.saveAndFlush(rankingRule);

        // Get all the rankingRules
        restRankingRuleMockMvc.perform(get("/api/ranking-rules?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(rankingRule.getId().intValue())))
                .andExpect(jsonPath("$.[*].win").value(hasItem(DEFAULT_WIN)))
                .andExpect(jsonPath("$.[*].loss").value(hasItem(DEFAULT_LOSS)))
                .andExpect(jsonPath("$.[*].draw").value(hasItem(DEFAULT_DRAW)))
                .andExpect(jsonPath("$.[*].scoreFor").value(hasItem(DEFAULT_SCORE_FOR)))
                .andExpect(jsonPath("$.[*].scoreAgain").value(hasItem(DEFAULT_SCORE_AGAIN)));
    }

    @Test
    @Transactional
    public void getRankingRule() throws Exception {
        // Initialize the database
        rankingRuleRepository.saveAndFlush(rankingRule);

        // Get the rankingRule
        restRankingRuleMockMvc.perform(get("/api/ranking-rules/{id}", rankingRule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(rankingRule.getId().intValue()))
            .andExpect(jsonPath("$.win").value(DEFAULT_WIN))
            .andExpect(jsonPath("$.loss").value(DEFAULT_LOSS))
            .andExpect(jsonPath("$.draw").value(DEFAULT_DRAW))
            .andExpect(jsonPath("$.scoreFor").value(DEFAULT_SCORE_FOR))
            .andExpect(jsonPath("$.scoreAgain").value(DEFAULT_SCORE_AGAIN));
    }

    @Test
    @Transactional
    public void getNonExistingRankingRule() throws Exception {
        // Get the rankingRule
        restRankingRuleMockMvc.perform(get("/api/ranking-rules/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRankingRule() throws Exception {
        // Initialize the database
        rankingRuleRepository.saveAndFlush(rankingRule);
        int databaseSizeBeforeUpdate = rankingRuleRepository.findAll().size();

        // Update the rankingRule
        RankingRule updatedRankingRule = rankingRuleRepository.findOne(rankingRule.getId());
        updatedRankingRule.setWin(UPDATED_WIN);
        updatedRankingRule.setLoss(UPDATED_LOSS);
        updatedRankingRule.setDraw(UPDATED_DRAW);
        updatedRankingRule.setScoreFor(UPDATED_SCORE_FOR);
        updatedRankingRule.setScoreAgain(UPDATED_SCORE_AGAIN);

        restRankingRuleMockMvc.perform(put("/api/ranking-rules")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedRankingRule)))
                .andExpect(status().isOk());

        // Validate the RankingRule in the database
        List<RankingRule> rankingRules = rankingRuleRepository.findAll();
        assertThat(rankingRules).hasSize(databaseSizeBeforeUpdate);
        RankingRule testRankingRule = rankingRules.get(rankingRules.size() - 1);
        assertThat(testRankingRule.getWin()).isEqualTo(UPDATED_WIN);
        assertThat(testRankingRule.getLoss()).isEqualTo(UPDATED_LOSS);
        assertThat(testRankingRule.getDraw()).isEqualTo(UPDATED_DRAW);
        assertThat(testRankingRule.getScoreFor()).isEqualTo(UPDATED_SCORE_FOR);
        assertThat(testRankingRule.getScoreAgain()).isEqualTo(UPDATED_SCORE_AGAIN);
    }

    @Test
    @Transactional
    public void deleteRankingRule() throws Exception {
        // Initialize the database
        rankingRuleRepository.saveAndFlush(rankingRule);
        int databaseSizeBeforeDelete = rankingRuleRepository.findAll().size();

        // Get the rankingRule
        restRankingRuleMockMvc.perform(delete("/api/ranking-rules/{id}", rankingRule.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<RankingRule> rankingRules = rankingRuleRepository.findAll();
        assertThat(rankingRules).hasSize(databaseSizeBeforeDelete - 1);
    }
}
