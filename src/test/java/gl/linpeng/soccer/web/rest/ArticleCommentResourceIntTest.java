package gl.linpeng.soccer.web.rest;

import gl.linpeng.soccer.SoccerApp;

import gl.linpeng.soccer.domain.ArticleComment;
import gl.linpeng.soccer.repository.ArticleCommentRepository;

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
 * Test class for the ArticleCommentResource REST controller.
 *
 * @see ArticleCommentResource
 */
@RunWith(SpringRunner.class)

@SpringBootTest(classes = SoccerApp.class)

public class ArticleCommentResourceIntTest {
    private static final String DEFAULT_AUTHOR = "AAAAA";
    private static final String UPDATED_AUTHOR = "BBBBB";

    private static final LocalDate DEFAULT_CREATE_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_AT = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_CONTENT = "AAAAA";
    private static final String UPDATED_CONTENT = "BBBBB";

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;

    @Inject
    private ArticleCommentRepository articleCommentRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restArticleCommentMockMvc;

    private ArticleComment articleComment;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ArticleCommentResource articleCommentResource = new ArticleCommentResource();
        ReflectionTestUtils.setField(articleCommentResource, "articleCommentRepository", articleCommentRepository);
        this.restArticleCommentMockMvc = MockMvcBuilders.standaloneSetup(articleCommentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ArticleComment createEntity(EntityManager em) {
        ArticleComment articleComment = new ArticleComment()
                .author(DEFAULT_AUTHOR)
                .createAt(DEFAULT_CREATE_AT)
                .content(DEFAULT_CONTENT)
                .status(DEFAULT_STATUS);
        return articleComment;
    }

    @Before
    public void initTest() {
        articleComment = createEntity(em);
    }

    @Test
    @Transactional
    public void createArticleComment() throws Exception {
        int databaseSizeBeforeCreate = articleCommentRepository.findAll().size();

        // Create the ArticleComment

        restArticleCommentMockMvc.perform(post("/api/article-comments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(articleComment)))
                .andExpect(status().isCreated());

        // Validate the ArticleComment in the database
        List<ArticleComment> articleComments = articleCommentRepository.findAll();
        assertThat(articleComments).hasSize(databaseSizeBeforeCreate + 1);
        ArticleComment testArticleComment = articleComments.get(articleComments.size() - 1);
        assertThat(testArticleComment.getAuthor()).isEqualTo(DEFAULT_AUTHOR);
        assertThat(testArticleComment.getCreateAt()).isEqualTo(DEFAULT_CREATE_AT);
        assertThat(testArticleComment.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testArticleComment.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void getAllArticleComments() throws Exception {
        // Initialize the database
        articleCommentRepository.saveAndFlush(articleComment);

        // Get all the articleComments
        restArticleCommentMockMvc.perform(get("/api/article-comments?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(articleComment.getId().intValue())))
                .andExpect(jsonPath("$.[*].author").value(hasItem(DEFAULT_AUTHOR.toString())))
                .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT.toString())))
                .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    @Test
    @Transactional
    public void getArticleComment() throws Exception {
        // Initialize the database
        articleCommentRepository.saveAndFlush(articleComment);

        // Get the articleComment
        restArticleCommentMockMvc.perform(get("/api/article-comments/{id}", articleComment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(articleComment.getId().intValue()))
            .andExpect(jsonPath("$.author").value(DEFAULT_AUTHOR.toString()))
            .andExpect(jsonPath("$.createAt").value(DEFAULT_CREATE_AT.toString()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }

    @Test
    @Transactional
    public void getNonExistingArticleComment() throws Exception {
        // Get the articleComment
        restArticleCommentMockMvc.perform(get("/api/article-comments/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateArticleComment() throws Exception {
        // Initialize the database
        articleCommentRepository.saveAndFlush(articleComment);
        int databaseSizeBeforeUpdate = articleCommentRepository.findAll().size();

        // Update the articleComment
        ArticleComment updatedArticleComment = articleCommentRepository.findOne(articleComment.getId());
        updatedArticleComment
                .author(UPDATED_AUTHOR)
                .createAt(UPDATED_CREATE_AT)
                .content(UPDATED_CONTENT)
                .status(UPDATED_STATUS);

        restArticleCommentMockMvc.perform(put("/api/article-comments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedArticleComment)))
                .andExpect(status().isOk());

        // Validate the ArticleComment in the database
        List<ArticleComment> articleComments = articleCommentRepository.findAll();
        assertThat(articleComments).hasSize(databaseSizeBeforeUpdate);
        ArticleComment testArticleComment = articleComments.get(articleComments.size() - 1);
        assertThat(testArticleComment.getAuthor()).isEqualTo(UPDATED_AUTHOR);
        assertThat(testArticleComment.getCreateAt()).isEqualTo(UPDATED_CREATE_AT);
        assertThat(testArticleComment.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testArticleComment.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void deleteArticleComment() throws Exception {
        // Initialize the database
        articleCommentRepository.saveAndFlush(articleComment);
        int databaseSizeBeforeDelete = articleCommentRepository.findAll().size();

        // Get the articleComment
        restArticleCommentMockMvc.perform(delete("/api/article-comments/{id}", articleComment.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ArticleComment> articleComments = articleCommentRepository.findAll();
        assertThat(articleComments).hasSize(databaseSizeBeforeDelete - 1);
    }
}
