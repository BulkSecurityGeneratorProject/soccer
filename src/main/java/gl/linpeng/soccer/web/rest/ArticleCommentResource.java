package gl.linpeng.soccer.web.rest;

import com.codahale.metrics.annotation.Timed;
import gl.linpeng.soccer.domain.ArticleComment;

import gl.linpeng.soccer.repository.ArticleCommentRepository;
import gl.linpeng.soccer.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ArticleComment.
 */
@RestController
@RequestMapping("/api")
public class ArticleCommentResource {

    private final Logger log = LoggerFactory.getLogger(ArticleCommentResource.class);
        
    @Inject
    private ArticleCommentRepository articleCommentRepository;

    /**
     * POST  /article-comments : Create a new articleComment.
     *
     * @param articleComment the articleComment to create
     * @return the ResponseEntity with status 201 (Created) and with body the new articleComment, or with status 400 (Bad Request) if the articleComment has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/article-comments",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ArticleComment> createArticleComment(@RequestBody ArticleComment articleComment) throws URISyntaxException {
        log.debug("REST request to save ArticleComment : {}", articleComment);
        if (articleComment.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("articleComment", "idexists", "A new articleComment cannot already have an ID")).body(null);
        }
        ArticleComment result = articleCommentRepository.save(articleComment);
        return ResponseEntity.created(new URI("/api/article-comments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("articleComment", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /article-comments : Updates an existing articleComment.
     *
     * @param articleComment the articleComment to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated articleComment,
     * or with status 400 (Bad Request) if the articleComment is not valid,
     * or with status 500 (Internal Server Error) if the articleComment couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/article-comments",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ArticleComment> updateArticleComment(@RequestBody ArticleComment articleComment) throws URISyntaxException {
        log.debug("REST request to update ArticleComment : {}", articleComment);
        if (articleComment.getId() == null) {
            return createArticleComment(articleComment);
        }
        ArticleComment result = articleCommentRepository.save(articleComment);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("articleComment", articleComment.getId().toString()))
            .body(result);
    }

    /**
     * GET  /article-comments : get all the articleComments.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of articleComments in body
     */
    @RequestMapping(value = "/article-comments",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ArticleComment> getAllArticleComments() {
        log.debug("REST request to get all ArticleComments");
        List<ArticleComment> articleComments = articleCommentRepository.findAll();
        return articleComments;
    }

    /**
     * GET  /article-comments/:id : get the "id" articleComment.
     *
     * @param id the id of the articleComment to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the articleComment, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/article-comments/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ArticleComment> getArticleComment(@PathVariable Long id) {
        log.debug("REST request to get ArticleComment : {}", id);
        ArticleComment articleComment = articleCommentRepository.findOne(id);
        return Optional.ofNullable(articleComment)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /article-comments/:id : delete the "id" articleComment.
     *
     * @param id the id of the articleComment to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/article-comments/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteArticleComment(@PathVariable Long id) {
        log.debug("REST request to delete ArticleComment : {}", id);
        articleCommentRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("articleComment", id.toString())).build();
    }

}
