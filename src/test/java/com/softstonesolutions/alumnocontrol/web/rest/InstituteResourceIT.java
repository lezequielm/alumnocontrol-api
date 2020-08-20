package com.softstonesolutions.alumnocontrol.web.rest;

import com.softstonesolutions.alumnocontrol.AlumnocontrolApp;
import com.softstonesolutions.alumnocontrol.domain.Institute;
import com.softstonesolutions.alumnocontrol.domain.ExtendedUser;
import com.softstonesolutions.alumnocontrol.domain.Student;
import com.softstonesolutions.alumnocontrol.domain.Group;
import com.softstonesolutions.alumnocontrol.domain.Assistance;
import com.softstonesolutions.alumnocontrol.repository.InstituteRepository;
import com.softstonesolutions.alumnocontrol.service.InstituteService;
import com.softstonesolutions.alumnocontrol.service.dto.InstituteCriteria;
import com.softstonesolutions.alumnocontrol.service.InstituteQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link InstituteResource} REST controller.
 */
@SpringBootTest(classes = AlumnocontrolApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class InstituteResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ENABLED = false;
    private static final Boolean UPDATED_ENABLED = true;

    @Autowired
    private InstituteRepository instituteRepository;

    @Autowired
    private InstituteService instituteService;

    @Autowired
    private InstituteQueryService instituteQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInstituteMockMvc;

    private Institute institute;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Institute createEntity(EntityManager em) {
        Institute institute = new Institute()
            .name(DEFAULT_NAME)
            .enabled(DEFAULT_ENABLED);
        return institute;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Institute createUpdatedEntity(EntityManager em) {
        Institute institute = new Institute()
            .name(UPDATED_NAME)
            .enabled(UPDATED_ENABLED);
        return institute;
    }

    @BeforeEach
    public void initTest() {
        institute = createEntity(em);
    }

    @Test
    @Transactional
    public void createInstitute() throws Exception {
        int databaseSizeBeforeCreate = instituteRepository.findAll().size();
        // Create the Institute
        restInstituteMockMvc.perform(post("/api/institutes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(institute)))
            .andExpect(status().isCreated());

        // Validate the Institute in the database
        List<Institute> instituteList = instituteRepository.findAll();
        assertThat(instituteList).hasSize(databaseSizeBeforeCreate + 1);
        Institute testInstitute = instituteList.get(instituteList.size() - 1);
        assertThat(testInstitute.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testInstitute.isEnabled()).isEqualTo(DEFAULT_ENABLED);
    }

    @Test
    @Transactional
    public void createInstituteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = instituteRepository.findAll().size();

        // Create the Institute with an existing ID
        institute.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInstituteMockMvc.perform(post("/api/institutes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(institute)))
            .andExpect(status().isBadRequest());

        // Validate the Institute in the database
        List<Institute> instituteList = instituteRepository.findAll();
        assertThat(instituteList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = instituteRepository.findAll().size();
        // set the field null
        institute.setName(null);

        // Create the Institute, which fails.


        restInstituteMockMvc.perform(post("/api/institutes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(institute)))
            .andExpect(status().isBadRequest());

        List<Institute> instituteList = instituteRepository.findAll();
        assertThat(instituteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEnabledIsRequired() throws Exception {
        int databaseSizeBeforeTest = instituteRepository.findAll().size();
        // set the field null
        institute.setEnabled(null);

        // Create the Institute, which fails.


        restInstituteMockMvc.perform(post("/api/institutes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(institute)))
            .andExpect(status().isBadRequest());

        List<Institute> instituteList = instituteRepository.findAll();
        assertThat(instituteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInstitutes() throws Exception {
        // Initialize the database
        instituteRepository.saveAndFlush(institute);

        // Get all the instituteList
        restInstituteMockMvc.perform(get("/api/institutes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(institute.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getInstitute() throws Exception {
        // Initialize the database
        instituteRepository.saveAndFlush(institute);

        // Get the institute
        restInstituteMockMvc.perform(get("/api/institutes/{id}", institute.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(institute.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED.booleanValue()));
    }


    @Test
    @Transactional
    public void getInstitutesByIdFiltering() throws Exception {
        // Initialize the database
        instituteRepository.saveAndFlush(institute);

        Long id = institute.getId();

        defaultInstituteShouldBeFound("id.equals=" + id);
        defaultInstituteShouldNotBeFound("id.notEquals=" + id);

        defaultInstituteShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultInstituteShouldNotBeFound("id.greaterThan=" + id);

        defaultInstituteShouldBeFound("id.lessThanOrEqual=" + id);
        defaultInstituteShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllInstitutesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        instituteRepository.saveAndFlush(institute);

        // Get all the instituteList where name equals to DEFAULT_NAME
        defaultInstituteShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the instituteList where name equals to UPDATED_NAME
        defaultInstituteShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllInstitutesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        instituteRepository.saveAndFlush(institute);

        // Get all the instituteList where name not equals to DEFAULT_NAME
        defaultInstituteShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the instituteList where name not equals to UPDATED_NAME
        defaultInstituteShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllInstitutesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        instituteRepository.saveAndFlush(institute);

        // Get all the instituteList where name in DEFAULT_NAME or UPDATED_NAME
        defaultInstituteShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the instituteList where name equals to UPDATED_NAME
        defaultInstituteShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllInstitutesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        instituteRepository.saveAndFlush(institute);

        // Get all the instituteList where name is not null
        defaultInstituteShouldBeFound("name.specified=true");

        // Get all the instituteList where name is null
        defaultInstituteShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllInstitutesByNameContainsSomething() throws Exception {
        // Initialize the database
        instituteRepository.saveAndFlush(institute);

        // Get all the instituteList where name contains DEFAULT_NAME
        defaultInstituteShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the instituteList where name contains UPDATED_NAME
        defaultInstituteShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllInstitutesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        instituteRepository.saveAndFlush(institute);

        // Get all the instituteList where name does not contain DEFAULT_NAME
        defaultInstituteShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the instituteList where name does not contain UPDATED_NAME
        defaultInstituteShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllInstitutesByEnabledIsEqualToSomething() throws Exception {
        // Initialize the database
        instituteRepository.saveAndFlush(institute);

        // Get all the instituteList where enabled equals to DEFAULT_ENABLED
        defaultInstituteShouldBeFound("enabled.equals=" + DEFAULT_ENABLED);

        // Get all the instituteList where enabled equals to UPDATED_ENABLED
        defaultInstituteShouldNotBeFound("enabled.equals=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    public void getAllInstitutesByEnabledIsNotEqualToSomething() throws Exception {
        // Initialize the database
        instituteRepository.saveAndFlush(institute);

        // Get all the instituteList where enabled not equals to DEFAULT_ENABLED
        defaultInstituteShouldNotBeFound("enabled.notEquals=" + DEFAULT_ENABLED);

        // Get all the instituteList where enabled not equals to UPDATED_ENABLED
        defaultInstituteShouldBeFound("enabled.notEquals=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    public void getAllInstitutesByEnabledIsInShouldWork() throws Exception {
        // Initialize the database
        instituteRepository.saveAndFlush(institute);

        // Get all the instituteList where enabled in DEFAULT_ENABLED or UPDATED_ENABLED
        defaultInstituteShouldBeFound("enabled.in=" + DEFAULT_ENABLED + "," + UPDATED_ENABLED);

        // Get all the instituteList where enabled equals to UPDATED_ENABLED
        defaultInstituteShouldNotBeFound("enabled.in=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    public void getAllInstitutesByEnabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        instituteRepository.saveAndFlush(institute);

        // Get all the instituteList where enabled is not null
        defaultInstituteShouldBeFound("enabled.specified=true");

        // Get all the instituteList where enabled is null
        defaultInstituteShouldNotBeFound("enabled.specified=false");
    }

    @Test
    @Transactional
    public void getAllInstitutesByUsersIsEqualToSomething() throws Exception {
        // Initialize the database
        instituteRepository.saveAndFlush(institute);
        ExtendedUser users = ExtendedUserResourceIT.createEntity(em);
        em.persist(users);
        em.flush();
        institute.addUsers(users);
        instituteRepository.saveAndFlush(institute);
        Long usersId = users.getId();

        // Get all the instituteList where users equals to usersId
        defaultInstituteShouldBeFound("usersId.equals=" + usersId);

        // Get all the instituteList where users equals to usersId + 1
        defaultInstituteShouldNotBeFound("usersId.equals=" + (usersId + 1));
    }


    @Test
    @Transactional
    public void getAllInstitutesByStudentsIsEqualToSomething() throws Exception {
        // Initialize the database
        instituteRepository.saveAndFlush(institute);
        Student students = StudentResourceIT.createEntity(em);
        em.persist(students);
        em.flush();
        institute.addStudents(students);
        instituteRepository.saveAndFlush(institute);
        Long studentsId = students.getId();

        // Get all the instituteList where students equals to studentsId
        defaultInstituteShouldBeFound("studentsId.equals=" + studentsId);

        // Get all the instituteList where students equals to studentsId + 1
        defaultInstituteShouldNotBeFound("studentsId.equals=" + (studentsId + 1));
    }


    @Test
    @Transactional
    public void getAllInstitutesByGroupsIsEqualToSomething() throws Exception {
        // Initialize the database
        instituteRepository.saveAndFlush(institute);
        Group groups = GroupResourceIT.createEntity(em);
        em.persist(groups);
        em.flush();
        institute.addGroups(groups);
        instituteRepository.saveAndFlush(institute);
        Long groupsId = groups.getId();

        // Get all the instituteList where groups equals to groupsId
        defaultInstituteShouldBeFound("groupsId.equals=" + groupsId);

        // Get all the instituteList where groups equals to groupsId + 1
        defaultInstituteShouldNotBeFound("groupsId.equals=" + (groupsId + 1));
    }


    @Test
    @Transactional
    public void getAllInstitutesByAssistanceIsEqualToSomething() throws Exception {
        // Initialize the database
        instituteRepository.saveAndFlush(institute);
        Assistance assistance = AssistanceResourceIT.createEntity(em);
        em.persist(assistance);
        em.flush();
        institute.addAssistance(assistance);
        instituteRepository.saveAndFlush(institute);
        Long assistanceId = assistance.getId();

        // Get all the instituteList where assistance equals to assistanceId
        defaultInstituteShouldBeFound("assistanceId.equals=" + assistanceId);

        // Get all the instituteList where assistance equals to assistanceId + 1
        defaultInstituteShouldNotBeFound("assistanceId.equals=" + (assistanceId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInstituteShouldBeFound(String filter) throws Exception {
        restInstituteMockMvc.perform(get("/api/institutes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(institute.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())));

        // Check, that the count call also returns 1
        restInstituteMockMvc.perform(get("/api/institutes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInstituteShouldNotBeFound(String filter) throws Exception {
        restInstituteMockMvc.perform(get("/api/institutes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInstituteMockMvc.perform(get("/api/institutes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingInstitute() throws Exception {
        // Get the institute
        restInstituteMockMvc.perform(get("/api/institutes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInstitute() throws Exception {
        // Initialize the database
        instituteService.save(institute);

        int databaseSizeBeforeUpdate = instituteRepository.findAll().size();

        // Update the institute
        Institute updatedInstitute = instituteRepository.findById(institute.getId()).get();
        // Disconnect from session so that the updates on updatedInstitute are not directly saved in db
        em.detach(updatedInstitute);
        updatedInstitute
            .name(UPDATED_NAME)
            .enabled(UPDATED_ENABLED);

        restInstituteMockMvc.perform(put("/api/institutes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedInstitute)))
            .andExpect(status().isOk());

        // Validate the Institute in the database
        List<Institute> instituteList = instituteRepository.findAll();
        assertThat(instituteList).hasSize(databaseSizeBeforeUpdate);
        Institute testInstitute = instituteList.get(instituteList.size() - 1);
        assertThat(testInstitute.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testInstitute.isEnabled()).isEqualTo(UPDATED_ENABLED);
    }

    @Test
    @Transactional
    public void updateNonExistingInstitute() throws Exception {
        int databaseSizeBeforeUpdate = instituteRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInstituteMockMvc.perform(put("/api/institutes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(institute)))
            .andExpect(status().isBadRequest());

        // Validate the Institute in the database
        List<Institute> instituteList = instituteRepository.findAll();
        assertThat(instituteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteInstitute() throws Exception {
        // Initialize the database
        instituteService.save(institute);

        int databaseSizeBeforeDelete = instituteRepository.findAll().size();

        // Delete the institute
        restInstituteMockMvc.perform(delete("/api/institutes/{id}", institute.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Institute> instituteList = instituteRepository.findAll();
        assertThat(instituteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
