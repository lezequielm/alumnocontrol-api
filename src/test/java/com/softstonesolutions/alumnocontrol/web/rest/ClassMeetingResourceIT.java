package com.softstonesolutions.alumnocontrol.web.rest;

import com.softstonesolutions.alumnocontrol.AlumnocontrolApp;
import com.softstonesolutions.alumnocontrol.domain.ClassMeeting;
import com.softstonesolutions.alumnocontrol.repository.ClassMeetingRepository;
import com.softstonesolutions.alumnocontrol.service.ClassMeetingService;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.softstonesolutions.alumnocontrol.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.softstonesolutions.alumnocontrol.domain.enumeration.ClassType;
/**
 * Integration tests for the {@link ClassMeetingResource} REST controller.
 */
@SpringBootTest(classes = AlumnocontrolApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ClassMeetingResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final ClassType DEFAULT_CLASS_TYPE = ClassType.NORMAL;
    private static final ClassType UPDATED_CLASS_TYPE = ClassType.SPECIAL;

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private ClassMeetingRepository classMeetingRepository;

    @Autowired
    private ClassMeetingService classMeetingService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClassMeetingMockMvc;

    private ClassMeeting classMeeting;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClassMeeting createEntity(EntityManager em) {
        ClassMeeting classMeeting = new ClassMeeting()
            .name(DEFAULT_NAME)
            .classType(DEFAULT_CLASS_TYPE)
            .date(DEFAULT_DATE);
        return classMeeting;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClassMeeting createUpdatedEntity(EntityManager em) {
        ClassMeeting classMeeting = new ClassMeeting()
            .name(UPDATED_NAME)
            .classType(UPDATED_CLASS_TYPE)
            .date(UPDATED_DATE);
        return classMeeting;
    }

    @BeforeEach
    public void initTest() {
        classMeeting = createEntity(em);
    }

    @Test
    @Transactional
    public void createClassMeeting() throws Exception {
        int databaseSizeBeforeCreate = classMeetingRepository.findAll().size();
        // Create the ClassMeeting
        restClassMeetingMockMvc.perform(post("/api/class-meetings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(classMeeting)))
            .andExpect(status().isCreated());

        // Validate the ClassMeeting in the database
        List<ClassMeeting> classMeetingList = classMeetingRepository.findAll();
        assertThat(classMeetingList).hasSize(databaseSizeBeforeCreate + 1);
        ClassMeeting testClassMeeting = classMeetingList.get(classMeetingList.size() - 1);
        assertThat(testClassMeeting.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testClassMeeting.getClassType()).isEqualTo(DEFAULT_CLASS_TYPE);
        assertThat(testClassMeeting.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    public void createClassMeetingWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = classMeetingRepository.findAll().size();

        // Create the ClassMeeting with an existing ID
        classMeeting.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restClassMeetingMockMvc.perform(post("/api/class-meetings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(classMeeting)))
            .andExpect(status().isBadRequest());

        // Validate the ClassMeeting in the database
        List<ClassMeeting> classMeetingList = classMeetingRepository.findAll();
        assertThat(classMeetingList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkClassTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = classMeetingRepository.findAll().size();
        // set the field null
        classMeeting.setClassType(null);

        // Create the ClassMeeting, which fails.


        restClassMeetingMockMvc.perform(post("/api/class-meetings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(classMeeting)))
            .andExpect(status().isBadRequest());

        List<ClassMeeting> classMeetingList = classMeetingRepository.findAll();
        assertThat(classMeetingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = classMeetingRepository.findAll().size();
        // set the field null
        classMeeting.setDate(null);

        // Create the ClassMeeting, which fails.


        restClassMeetingMockMvc.perform(post("/api/class-meetings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(classMeeting)))
            .andExpect(status().isBadRequest());

        List<ClassMeeting> classMeetingList = classMeetingRepository.findAll();
        assertThat(classMeetingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllClassMeetings() throws Exception {
        // Initialize the database
        classMeetingRepository.saveAndFlush(classMeeting);

        // Get all the classMeetingList
        restClassMeetingMockMvc.perform(get("/api/class-meetings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(classMeeting.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].classType").value(hasItem(DEFAULT_CLASS_TYPE.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))));
    }
    
    @Test
    @Transactional
    public void getClassMeeting() throws Exception {
        // Initialize the database
        classMeetingRepository.saveAndFlush(classMeeting);

        // Get the classMeeting
        restClassMeetingMockMvc.perform(get("/api/class-meetings/{id}", classMeeting.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(classMeeting.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.classType").value(DEFAULT_CLASS_TYPE.toString()))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)));
    }
    @Test
    @Transactional
    public void getNonExistingClassMeeting() throws Exception {
        // Get the classMeeting
        restClassMeetingMockMvc.perform(get("/api/class-meetings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateClassMeeting() throws Exception {
        // Initialize the database
        classMeetingService.save(classMeeting);

        int databaseSizeBeforeUpdate = classMeetingRepository.findAll().size();

        // Update the classMeeting
        ClassMeeting updatedClassMeeting = classMeetingRepository.findById(classMeeting.getId()).get();
        // Disconnect from session so that the updates on updatedClassMeeting are not directly saved in db
        em.detach(updatedClassMeeting);
        updatedClassMeeting
            .name(UPDATED_NAME)
            .classType(UPDATED_CLASS_TYPE)
            .date(UPDATED_DATE);

        restClassMeetingMockMvc.perform(put("/api/class-meetings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedClassMeeting)))
            .andExpect(status().isOk());

        // Validate the ClassMeeting in the database
        List<ClassMeeting> classMeetingList = classMeetingRepository.findAll();
        assertThat(classMeetingList).hasSize(databaseSizeBeforeUpdate);
        ClassMeeting testClassMeeting = classMeetingList.get(classMeetingList.size() - 1);
        assertThat(testClassMeeting.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testClassMeeting.getClassType()).isEqualTo(UPDATED_CLASS_TYPE);
        assertThat(testClassMeeting.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingClassMeeting() throws Exception {
        int databaseSizeBeforeUpdate = classMeetingRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClassMeetingMockMvc.perform(put("/api/class-meetings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(classMeeting)))
            .andExpect(status().isBadRequest());

        // Validate the ClassMeeting in the database
        List<ClassMeeting> classMeetingList = classMeetingRepository.findAll();
        assertThat(classMeetingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteClassMeeting() throws Exception {
        // Initialize the database
        classMeetingService.save(classMeeting);

        int databaseSizeBeforeDelete = classMeetingRepository.findAll().size();

        // Delete the classMeeting
        restClassMeetingMockMvc.perform(delete("/api/class-meetings/{id}", classMeeting.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ClassMeeting> classMeetingList = classMeetingRepository.findAll();
        assertThat(classMeetingList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
