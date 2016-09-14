package gl.linpeng.soccer.web.rest;

import gl.linpeng.soccer.SoccerApp;

import gl.linpeng.soccer.domain.DivisionEvent;
import gl.linpeng.soccer.repository.DivisionEventRepository;

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
 * Test class for the DivisionEventResource REST controller.
 *
 * @see DivisionEventResource
 */
@RunWith(SpringRunner.class)

@SpringBootTest(classes = SoccerApp.class)

public class DivisionEventResourceIntTest {

    @Inject
    private DivisionEventRepository divisionEventRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restDivisionEventMockMvc;

    private DivisionEvent divisionEvent;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DivisionEventResource divisionEventResource = new DivisionEventResource();
        ReflectionTestUtils.setField(divisionEventResource, "divisionEventRepository", divisionEventRepository);
        this.restDivisionEventMockMvc = MockMvcBuilders.standaloneSetup(divisionEventResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DivisionEvent createEntity(EntityManager em) {
        DivisionEvent divisionEvent = new DivisionEvent();
        return divisionEvent;
    }

    @Before
    public void initTest() {
        divisionEvent = createEntity(em);
    }

    @Test
    @Transactional
    public void createDivisionEvent() throws Exception {
        int databaseSizeBeforeCreate = divisionEventRepository.findAll().size();

        // Create the DivisionEvent

        restDivisionEventMockMvc.perform(post("/api/division-events")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(divisionEvent)))
                .andExpect(status().isCreated());

        // Validate the DivisionEvent in the database
        List<DivisionEvent> divisionEvents = divisionEventRepository.findAll();
        assertThat(divisionEvents).hasSize(databaseSizeBeforeCreate + 1);
        DivisionEvent testDivisionEvent = divisionEvents.get(divisionEvents.size() - 1);
    }

    @Test
    @Transactional
    public void getAllDivisionEvents() throws Exception {
        // Initialize the database
        divisionEventRepository.saveAndFlush(divisionEvent);

        // Get all the divisionEvents
        restDivisionEventMockMvc.perform(get("/api/division-events?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(divisionEvent.getId().intValue())));
    }

    @Test
    @Transactional
    public void getDivisionEvent() throws Exception {
        // Initialize the database
        divisionEventRepository.saveAndFlush(divisionEvent);

        // Get the divisionEvent
        restDivisionEventMockMvc.perform(get("/api/division-events/{id}", divisionEvent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(divisionEvent.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingDivisionEvent() throws Exception {
        // Get the divisionEvent
        restDivisionEventMockMvc.perform(get("/api/division-events/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDivisionEvent() throws Exception {
        // Initialize the database
        divisionEventRepository.saveAndFlush(divisionEvent);
        int databaseSizeBeforeUpdate = divisionEventRepository.findAll().size();

        // Update the divisionEvent
        DivisionEvent updatedDivisionEvent = divisionEventRepository.findOne(divisionEvent.getId());

        restDivisionEventMockMvc.perform(put("/api/division-events")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedDivisionEvent)))
                .andExpect(status().isOk());

        // Validate the DivisionEvent in the database
        List<DivisionEvent> divisionEvents = divisionEventRepository.findAll();
        assertThat(divisionEvents).hasSize(databaseSizeBeforeUpdate);
        DivisionEvent testDivisionEvent = divisionEvents.get(divisionEvents.size() - 1);
    }

    @Test
    @Transactional
    public void deleteDivisionEvent() throws Exception {
        // Initialize the database
        divisionEventRepository.saveAndFlush(divisionEvent);
        int databaseSizeBeforeDelete = divisionEventRepository.findAll().size();

        // Get the divisionEvent
        restDivisionEventMockMvc.perform(delete("/api/division-events/{id}", divisionEvent.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<DivisionEvent> divisionEvents = divisionEventRepository.findAll();
        assertThat(divisionEvents).hasSize(databaseSizeBeforeDelete - 1);
    }
}
