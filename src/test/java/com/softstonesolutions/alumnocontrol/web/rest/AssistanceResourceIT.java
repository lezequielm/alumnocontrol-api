package com.softstonesolutions.alumnocontrol.web.rest;

import com.softstonesolutions.alumnocontrol.AlumnocontrolApp;
import com.softstonesolutions.alumnocontrol.domain.Assistance;
import com.softstonesolutions.alumnocontrol.domain.Student;
import com.softstonesolutions.alumnocontrol.domain.Institute;
import com.softstonesolutions.alumnocontrol.domain.ClassMeeting;
import com.softstonesolutions.alumnocontrol.domain.Group;
import com.softstonesolutions.alumnocontrol.repository.AssistanceRepository;
import com.softstonesolutions.alumnocontrol.service.AssistanceService;
import com.softstonesolutions.alumnocontrol.service.dto.AssistanceCriteria;
import com.softstonesolutions.alumnocontrol.service.AssistanceQueryService;

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
    private AssistanceQueryService assistanceQueryService;

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
    public void getAssistancesByIdFiltering() throws Exception {
        // Initialize the database
        assistanceRepository.saveAndFlush(assistance);

        Long id = assistance.getId();

        defaultAssistanceShouldBeFound("id.equals=" + id);
        defaultAssistanceShouldNotBeFound("id.notEquals=" + id);

        defaultAssistanceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAssistanceShouldNotBeFound("id.greaterThan=" + id);

        defaultAssistanceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAssistanceShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllAssistancesByPresentIsEqualToSomething() throws Exception {
        // Initialize the database
        assistanceRepository.saveAndFlush(assistance);

        // Get all the assistanceList where present equals to DEFAULT_PRESENT
        defaultAssistanceShouldBeFound("present.equals=" + DEFAULT_PRESENT);

        // Get all the assistanceList where present equals to UPDATED_PRESENT
        defaultAssistanceShouldNotBeFound("present.equals=" + UPDATED_PRESENT);
    }

    @Test
    @Transactional
    public void getAllAssistancesByPresentIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assistanceRepository.saveAndFlush(assistance);

        // Get all the assistanceList where present not equals to DEFAULT_PRESENT
        defaultAssistanceShouldNotBeFound("present.notEquals=" + DEFAULT_PRESENT);

        // Get all the assistanceList where present not equals to UPDATED_PRESENT
        defaultAssistanceShouldBeFound("present.notEquals=" + UPDATED_PRESENT);
    }

    @Test
    @Transactional
    public void getAllAssistancesByPresentIsInShouldWork() throws Exception {
        // Initialize the database
        assistanceRepository.saveAndFlush(assistance);

        // Get all the assistanceList where present in DEFAULT_PRESENT or UPDATED_PRESENT
        defaultAssistanceShouldBeFound("present.in=" + DEFAULT_PRESENT + "," + UPDATED_PRESENT);

        // Get all the assistanceList where present equals to UPDATED_PRESENT
        defaultAssistanceShouldNotBeFound("present.in=" + UPDATED_PRESENT);
    }

    @Test
    @Transactional
    public void getAllAssistancesByPresentIsNullOrNotNull() throws Exception {
        // Initialize the database
        assistanceRepository.saveAndFlush(assistance);

        // Get all the assistanceList where present is not null
        defaultAssistanceShouldBeFound("present.specified=true");

        // Get all the assistanceList where present is null
        defaultAssistanceShouldNotBeFound("present.specified=false");
    }

    @Test
    @Transactional
    public void getAllAssistancesByDelayedIsEqualToSomething() throws Exception {
        // Initialize the database
        assistanceRepository.saveAndFlush(assistance);

        // Get all the assistanceList where delayed equals to DEFAULT_DELAYED
        defaultAssistanceShouldBeFound("delayed.equals=" + DEFAULT_DELAYED);

        // Get all the assistanceList where delayed equals to UPDATED_DELAYED
        defaultAssistanceShouldNotBeFound("delayed.equals=" + UPDATED_DELAYED);
    }

    @Test
    @Transactional
    public void getAllAssistancesByDelayedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assistanceRepository.saveAndFlush(assistance);

        // Get all the assistanceList where delayed not equals to DEFAULT_DELAYED
        defaultAssistanceShouldNotBeFound("delayed.notEquals=" + DEFAULT_DELAYED);

        // Get all the assistanceList where delayed not equals to UPDATED_DELAYED
        defaultAssistanceShouldBeFound("delayed.notEquals=" + UPDATED_DELAYED);
    }

    @Test
    @Transactional
    public void getAllAssistancesByDelayedIsInShouldWork() throws Exception {
        // Initialize the database
        assistanceRepository.saveAndFlush(assistance);

        // Get all the assistanceList where delayed in DEFAULT_DELAYED or UPDATED_DELAYED
        defaultAssistanceShouldBeFound("delayed.in=" + DEFAULT_DELAYED + "," + UPDATED_DELAYED);

        // Get all the assistanceList where delayed equals to UPDATED_DELAYED
        defaultAssistanceShouldNotBeFound("delayed.in=" + UPDATED_DELAYED);
    }

    @Test
    @Transactional
    public void getAllAssistancesByDelayedIsNullOrNotNull() throws Exception {
        // Initialize the database
        assistanceRepository.saveAndFlush(assistance);

        // Get all the assistanceList where delayed is not null
        defaultAssistanceShouldBeFound("delayed.specified=true");

        // Get all the assistanceList where delayed is null
        defaultAssistanceShouldNotBeFound("delayed.specified=false");
    }

    @Test
    @Transactional
    public void getAllAssistancesByJustifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        assistanceRepository.saveAndFlush(assistance);

        // Get all the assistanceList where justified equals to DEFAULT_JUSTIFIED
        defaultAssistanceShouldBeFound("justified.equals=" + DEFAULT_JUSTIFIED);

        // Get all the assistanceList where justified equals to UPDATED_JUSTIFIED
        defaultAssistanceShouldNotBeFound("justified.equals=" + UPDATED_JUSTIFIED);
    }

    @Test
    @Transactional
    public void getAllAssistancesByJustifiedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assistanceRepository.saveAndFlush(assistance);

        // Get all the assistanceList where justified not equals to DEFAULT_JUSTIFIED
        defaultAssistanceShouldNotBeFound("justified.notEquals=" + DEFAULT_JUSTIFIED);

        // Get all the assistanceList where justified not equals to UPDATED_JUSTIFIED
        defaultAssistanceShouldBeFound("justified.notEquals=" + UPDATED_JUSTIFIED);
    }

    @Test
    @Transactional
    public void getAllAssistancesByJustifiedIsInShouldWork() throws Exception {
        // Initialize the database
        assistanceRepository.saveAndFlush(assistance);

        // Get all the assistanceList where justified in DEFAULT_JUSTIFIED or UPDATED_JUSTIFIED
        defaultAssistanceShouldBeFound("justified.in=" + DEFAULT_JUSTIFIED + "," + UPDATED_JUSTIFIED);

        // Get all the assistanceList where justified equals to UPDATED_JUSTIFIED
        defaultAssistanceShouldNotBeFound("justified.in=" + UPDATED_JUSTIFIED);
    }

    @Test
    @Transactional
    public void getAllAssistancesByJustifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        assistanceRepository.saveAndFlush(assistance);

        // Get all the assistanceList where justified is not null
        defaultAssistanceShouldBeFound("justified.specified=true");

        // Get all the assistanceList where justified is null
        defaultAssistanceShouldNotBeFound("justified.specified=false");
    }

    @Test
    @Transactional
    public void getAllAssistancesByStudentIsEqualToSomething() throws Exception {
        // Initialize the database
        assistanceRepository.saveAndFlush(assistance);
        Student student = StudentResourceIT.createEntity(em);
        em.persist(student);
        em.flush();
        assistance.setStudent(student);
        assistanceRepository.saveAndFlush(assistance);
        Long studentId = student.getId();

        // Get all the assistanceList where student equals to studentId
        defaultAssistanceShouldBeFound("studentId.equals=" + studentId);

        // Get all the assistanceList where student equals to studentId + 1
        defaultAssistanceShouldNotBeFound("studentId.equals=" + (studentId + 1));
    }


    @Test
    @Transactional
    public void getAllAssistancesByInstituteIsEqualToSomething() throws Exception {
        // Initialize the database
        assistanceRepository.saveAndFlush(assistance);
        Institute institute = InstituteResourceIT.createEntity(em);
        em.persist(institute);
        em.flush();
        assistance.setInstitute(institute);
        assistanceRepository.saveAndFlush(assistance);
        Long instituteId = institute.getId();

        // Get all the assistanceList where institute equals to instituteId
        defaultAssistanceShouldBeFound("instituteId.equals=" + instituteId);

        // Get all the assistanceList where institute equals to instituteId + 1
        defaultAssistanceShouldNotBeFound("instituteId.equals=" + (instituteId + 1));
    }


    @Test
    @Transactional
    public void getAllAssistancesByClassMeetingIsEqualToSomething() throws Exception {
        // Initialize the database
        assistanceRepository.saveAndFlush(assistance);
        ClassMeeting classMeeting = ClassMeetingResourceIT.createEntity(em);
        em.persist(classMeeting);
        em.flush();
        assistance.setClassMeeting(classMeeting);
        assistanceRepository.saveAndFlush(assistance);
        Long classMeetingId = classMeeting.getId();

        // Get all the assistanceList where classMeeting equals to classMeetingId
        defaultAssistanceShouldBeFound("classMeetingId.equals=" + classMeetingId);

        // Get all the assistanceList where classMeeting equals to classMeetingId + 1
        defaultAssistanceShouldNotBeFound("classMeetingId.equals=" + (classMeetingId + 1));
    }


    @Test
    @Transactional
    public void getAllAssistancesByGroupIsEqualToSomething() throws Exception {
        // Initialize the database
        assistanceRepository.saveAndFlush(assistance);
        Group group = GroupResourceIT.createEntity(em);
        em.persist(group);
        em.flush();
        assistance.setGroup(group);
        assistanceRepository.saveAndFlush(assistance);
        Long groupId = group.getId();

        // Get all the assistanceList where group equals to groupId
        defaultAssistanceShouldBeFound("groupId.equals=" + groupId);

        // Get all the assistanceList where group equals to groupId + 1
        defaultAssistanceShouldNotBeFound("groupId.equals=" + (groupId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAssistanceShouldBeFound(String filter) throws Exception {
        restAssistanceMockMvc.perform(get("/api/assistances?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assistance.getId().intValue())))
            .andExpect(jsonPath("$.[*].present").value(hasItem(DEFAULT_PRESENT.booleanValue())))
            .andExpect(jsonPath("$.[*].delayed").value(hasItem(DEFAULT_DELAYED.booleanValue())))
            .andExpect(jsonPath("$.[*].justified").value(hasItem(DEFAULT_JUSTIFIED.booleanValue())))
            .andExpect(jsonPath("$.[*].justification").value(hasItem(DEFAULT_JUSTIFICATION.toString())));

        // Check, that the count call also returns 1
        restAssistanceMockMvc.perform(get("/api/assistances/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAssistanceShouldNotBeFound(String filter) throws Exception {
        restAssistanceMockMvc.perform(get("/api/assistances?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAssistanceMockMvc.perform(get("/api/assistances/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
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
