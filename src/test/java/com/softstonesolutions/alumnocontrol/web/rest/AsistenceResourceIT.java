package com.softstonesolutions.alumnocontrol.web.rest;

import com.softstonesolutions.alumnocontrol.AlumnocontrolApp;
import com.softstonesolutions.alumnocontrol.domain.Asistence;
import com.softstonesolutions.alumnocontrol.repository.AsistenceRepository;
import com.softstonesolutions.alumnocontrol.service.AsistenceService;

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
 * Integration tests for the {@link AsistenceResource} REST controller.
 */
@SpringBootTest(classes = AlumnocontrolApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class AsistenceResourceIT {

    private static final Boolean DEFAULT_PRESENT = false;
    private static final Boolean UPDATED_PRESENT = true;

    private static final Boolean DEFAULT_DELAYED = false;
    private static final Boolean UPDATED_DELAYED = true;

    private static final Boolean DEFAULT_JUSTIFIED = false;
    private static final Boolean UPDATED_JUSTIFIED = true;

    private static final String DEFAULT_JUSTIFICATION = "AAAAAAAAAA";
    private static final String UPDATED_JUSTIFICATION = "BBBBBBBBBB";

    @Autowired
    private AsistenceRepository asistenceRepository;

    @Autowired
    private AsistenceService asistenceService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAsistenceMockMvc;

    private Asistence asistence;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Asistence createEntity(EntityManager em) {
        Asistence asistence = new Asistence()
            .present(DEFAULT_PRESENT)
            .delayed(DEFAULT_DELAYED)
            .justified(DEFAULT_JUSTIFIED)
            .justification(DEFAULT_JUSTIFICATION);
        return asistence;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Asistence createUpdatedEntity(EntityManager em) {
        Asistence asistence = new Asistence()
            .present(UPDATED_PRESENT)
            .delayed(UPDATED_DELAYED)
            .justified(UPDATED_JUSTIFIED)
            .justification(UPDATED_JUSTIFICATION);
        return asistence;
    }

    @BeforeEach
    public void initTest() {
        asistence = createEntity(em);
    }

    @Test
    @Transactional
    public void createAsistence() throws Exception {
        int databaseSizeBeforeCreate = asistenceRepository.findAll().size();
        // Create the Asistence
        restAsistenceMockMvc.perform(post("/api/asistences")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(asistence)))
            .andExpect(status().isCreated());

        // Validate the Asistence in the database
        List<Asistence> asistenceList = asistenceRepository.findAll();
        assertThat(asistenceList).hasSize(databaseSizeBeforeCreate + 1);
        Asistence testAsistence = asistenceList.get(asistenceList.size() - 1);
        assertThat(testAsistence.isPresent()).isEqualTo(DEFAULT_PRESENT);
        assertThat(testAsistence.isDelayed()).isEqualTo(DEFAULT_DELAYED);
        assertThat(testAsistence.isJustified()).isEqualTo(DEFAULT_JUSTIFIED);
        assertThat(testAsistence.getJustification()).isEqualTo(DEFAULT_JUSTIFICATION);
    }

    @Test
    @Transactional
    public void createAsistenceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = asistenceRepository.findAll().size();

        // Create the Asistence with an existing ID
        asistence.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAsistenceMockMvc.perform(post("/api/asistences")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(asistence)))
            .andExpect(status().isBadRequest());

        // Validate the Asistence in the database
        List<Asistence> asistenceList = asistenceRepository.findAll();
        assertThat(asistenceList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkPresentIsRequired() throws Exception {
        int databaseSizeBeforeTest = asistenceRepository.findAll().size();
        // set the field null
        asistence.setPresent(null);

        // Create the Asistence, which fails.


        restAsistenceMockMvc.perform(post("/api/asistences")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(asistence)))
            .andExpect(status().isBadRequest());

        List<Asistence> asistenceList = asistenceRepository.findAll();
        assertThat(asistenceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAsistences() throws Exception {
        // Initialize the database
        asistenceRepository.saveAndFlush(asistence);

        // Get all the asistenceList
        restAsistenceMockMvc.perform(get("/api/asistences?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(asistence.getId().intValue())))
            .andExpect(jsonPath("$.[*].present").value(hasItem(DEFAULT_PRESENT.booleanValue())))
            .andExpect(jsonPath("$.[*].delayed").value(hasItem(DEFAULT_DELAYED.booleanValue())))
            .andExpect(jsonPath("$.[*].justified").value(hasItem(DEFAULT_JUSTIFIED.booleanValue())))
            .andExpect(jsonPath("$.[*].justification").value(hasItem(DEFAULT_JUSTIFICATION.toString())));
    }
    
    @Test
    @Transactional
    public void getAsistence() throws Exception {
        // Initialize the database
        asistenceRepository.saveAndFlush(asistence);

        // Get the asistence
        restAsistenceMockMvc.perform(get("/api/asistences/{id}", asistence.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(asistence.getId().intValue()))
            .andExpect(jsonPath("$.present").value(DEFAULT_PRESENT.booleanValue()))
            .andExpect(jsonPath("$.delayed").value(DEFAULT_DELAYED.booleanValue()))
            .andExpect(jsonPath("$.justified").value(DEFAULT_JUSTIFIED.booleanValue()))
            .andExpect(jsonPath("$.justification").value(DEFAULT_JUSTIFICATION.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingAsistence() throws Exception {
        // Get the asistence
        restAsistenceMockMvc.perform(get("/api/asistences/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAsistence() throws Exception {
        // Initialize the database
        asistenceService.save(asistence);

        int databaseSizeBeforeUpdate = asistenceRepository.findAll().size();

        // Update the asistence
        Asistence updatedAsistence = asistenceRepository.findById(asistence.getId()).get();
        // Disconnect from session so that the updates on updatedAsistence are not directly saved in db
        em.detach(updatedAsistence);
        updatedAsistence
            .present(UPDATED_PRESENT)
            .delayed(UPDATED_DELAYED)
            .justified(UPDATED_JUSTIFIED)
            .justification(UPDATED_JUSTIFICATION);

        restAsistenceMockMvc.perform(put("/api/asistences")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedAsistence)))
            .andExpect(status().isOk());

        // Validate the Asistence in the database
        List<Asistence> asistenceList = asistenceRepository.findAll();
        assertThat(asistenceList).hasSize(databaseSizeBeforeUpdate);
        Asistence testAsistence = asistenceList.get(asistenceList.size() - 1);
        assertThat(testAsistence.isPresent()).isEqualTo(UPDATED_PRESENT);
        assertThat(testAsistence.isDelayed()).isEqualTo(UPDATED_DELAYED);
        assertThat(testAsistence.isJustified()).isEqualTo(UPDATED_JUSTIFIED);
        assertThat(testAsistence.getJustification()).isEqualTo(UPDATED_JUSTIFICATION);
    }

    @Test
    @Transactional
    public void updateNonExistingAsistence() throws Exception {
        int databaseSizeBeforeUpdate = asistenceRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAsistenceMockMvc.perform(put("/api/asistences")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(asistence)))
            .andExpect(status().isBadRequest());

        // Validate the Asistence in the database
        List<Asistence> asistenceList = asistenceRepository.findAll();
        assertThat(asistenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAsistence() throws Exception {
        // Initialize the database
        asistenceService.save(asistence);

        int databaseSizeBeforeDelete = asistenceRepository.findAll().size();

        // Delete the asistence
        restAsistenceMockMvc.perform(delete("/api/asistences/{id}", asistence.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Asistence> asistenceList = asistenceRepository.findAll();
        assertThat(asistenceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
