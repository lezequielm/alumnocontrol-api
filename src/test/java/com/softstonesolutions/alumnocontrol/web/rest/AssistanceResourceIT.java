package com.softstonesolutions.alumnocontrol.web.rest;

import com.softstonesolutions.alumnocontrol.AlumnocontrolApp;
import com.softstonesolutions.alumnocontrol.domain.Assistance;
import com.softstonesolutions.alumnocontrol.repository.AssistanceRepository;
import com.softstonesolutions.alumnocontrol.service.AssistanceService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link AssistanceResource} REST controller.
 */
@SpringBootTest(classes = AlumnocontrolApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class AssistanceResourceIT {

    private static final Boolean DEFAULT_PRESENT = false;
    private static final Boolean UPDATED_PRESENT = true;

    private static final Boolean DEFAULT_DELAYED = false;
    private static final Boolean UPDATED_DELAYED = true;

    private static final Boolean DEFAULT_JUSTIFIED = false;
    private static final Boolean UPDATED_JUSTIFIED = true;

    private static final String DEFAULT_JUSTIFICATION = "AAAAAAAAAA";
    private static final String UPDATED_JUSTIFICATION = "BBBBBBBBBB";

    @Autowired
    private AssistanceRepository assistanceRepository;

    @Autowired
    private AssistanceService assistanceService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAssistanceMockMvc;

    private Assistance assistance;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Assistance createEntity(EntityManager em) {
        Assistance assistance = new Assistance()
            .present(DEFAULT_PRESENT)
            .delayed(DEFAULT_DELAYED)
            .justified(DEFAULT_JUSTIFIED)
            .justification(DEFAULT_JUSTIFICATION);
        return assistance;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Assistance createUpdatedEntity(EntityManager em) {
        Assistance assistance = new Assistance()
            .present(UPDATED_PRESENT)
            .delayed(UPDATED_DELAYED)
            .justified(UPDATED_JUSTIFIED)
            .justification(UPDATED_JUSTIFICATION);
        return assistance;
    }

    @BeforeEach
    public void initTest() {
        assistance = createEntity(em);
    }

    @Test
    @Transactional
    public void createAssistance() throws Exception {
        int databaseSizeBeforeCreate = assistanceRepository.findAll().size();
        // Create the Assistance
        restAssistanceMockMvc.perform(post("/api/assistances")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(assistance)))
            .andExpect(status().isCreated());

        // Validate the Assistance in the database
        List<Assistance> assistanceList = assistanceRepository.findAll();
        assertThat(assistanceList).hasSize(databaseSizeBeforeCreate + 1);
        Assistance testAssistance = assistanceList.get(assistanceList.size() - 1);
        assertThat(testAssistance.isPresent()).isEqualTo(DEFAULT_PRESENT);
        assertThat(testAssistance.isDelayed()).isEqualTo(DEFAULT_DELAYED);
        assertThat(testAssistance.isJustified()).isEqualTo(DEFAULT_JUSTIFIED);
        assertThat(testAssistance.getJustification()).isEqualTo(DEFAULT_JUSTIFICATION);
    }

    @Test
    @Transactional
    public void createAssistanceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = assistanceRepository.findAll().size();

        // Create the Assistance with an existing ID
        assistance.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAssistanceMockMvc.perform(post("/api/assistances")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(assistance)))
            .andExpect(status().isBadRequest());

        // Validate the Assistance in the database
        List<Assistance> assistanceList = assistanceRepository.findAll();
        assertThat(assistanceList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkPresentIsRequired() throws Exception {
        int databaseSizeBeforeTest = assistanceRepository.findAll().size();
        // set the field null
        assistance.setPresent(null);

        // Create the Assistance, which fails.


        restAssistanceMockMvc.perform(post("/api/assistances")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(assistance)))
            .andExpect(status().isBadRequest());

        List<Assistance> assistanceList = assistanceRepository.findAll();
        assertThat(assistanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAssistances() throws Exception {
        // Initialize the database
        assistanceRepository.saveAndFlush(assistance);

        // Get all the assistanceList
        restAssistanceMockMvc.perform(get("/api/assistances?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assistance.getId().intValue())))
            .andExpect(jsonPath("$.[*].present").value(hasItem(DEFAULT_PRESENT.booleanValue())))
            .andExpect(jsonPath("$.[*].delayed").value(hasItem(DEFAULT_DELAYED.booleanValue())))
            .andExpect(jsonPath("$.[*].justified").value(hasItem(DEFAULT_JUSTIFIED.booleanValue())))
            .andExpect(jsonPath("$.[*].justification").value(hasItem(DEFAULT_JUSTIFICATION.toString())));
    }
    
    @Test
    @Transactional
    public void getAssistance() throws Exception {
        // Initialize the database
        assistanceRepository.saveAndFlush(assistance);

        // Get the assistance
        restAssistanceMockMvc.perform(get("/api/assistances/{id}", assistance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(assistance.getId().intValue()))
            .andExpect(jsonPath("$.present").value(DEFAULT_PRESENT.booleanValue()))
            .andExpect(jsonPath("$.delayed").value(DEFAULT_DELAYED.booleanValue()))
            .andExpect(jsonPath("$.justified").value(DEFAULT_JUSTIFIED.booleanValue()))
            .andExpect(jsonPath("$.justification").value(DEFAULT_JUSTIFICATION.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingAssistance() throws Exception {
        // Get the assistance
        restAssistanceMockMvc.perform(get("/api/assistances/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAssistance() throws Exception {
        // Initialize the database
        assistanceService.save(assistance);

        int databaseSizeBeforeUpdate = assistanceRepository.findAll().size();

        // Update the assistance
        Assistance updatedAssistance = assistanceRepository.findById(assistance.getId()).get();
        // Disconnect from session so that the updates on updatedAssistance are not directly saved in db
        em.detach(updatedAssistance);
        updatedAssistance
            .present(UPDATED_PRESENT)
            .delayed(UPDATED_DELAYED)
            .justified(UPDATED_JUSTIFIED)
            .justification(UPDATED_JUSTIFICATION);

        restAssistanceMockMvc.perform(put("/api/assistances")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedAssistance)))
            .andExpect(status().isOk());

        // Validate the Assistance in the database
        List<Assistance> assistanceList = assistanceRepository.findAll();
        assertThat(assistanceList).hasSize(databaseSizeBeforeUpdate);
        Assistance testAssistance = assistanceList.get(assistanceList.size() - 1);
        assertThat(testAssistance.isPresent()).isEqualTo(UPDATED_PRESENT);
        assertThat(testAssistance.isDelayed()).isEqualTo(UPDATED_DELAYED);
        assertThat(testAssistance.isJustified()).isEqualTo(UPDATED_JUSTIFIED);
        assertThat(testAssistance.getJustification()).isEqualTo(UPDATED_JUSTIFICATION);
    }

    @Test
    @Transactional
    public void updateNonExistingAssistance() throws Exception {
        int databaseSizeBeforeUpdate = assistanceRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssistanceMockMvc.perform(put("/api/assistances")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(assistance)))
            .andExpect(status().isBadRequest());

        // Validate the Assistance in the database
        List<Assistance> assistanceList = assistanceRepository.findAll();
        assertThat(assistanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAssistance() throws Exception {
        // Initialize the database
        assistanceService.save(assistance);

        int databaseSizeBeforeDelete = assistanceRepository.findAll().size();

        // Delete the assistance
        restAssistanceMockMvc.perform(delete("/api/assistances/{id}", assistance.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Assistance> assistanceList = assistanceRepository.findAll();
        assertThat(assistanceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
