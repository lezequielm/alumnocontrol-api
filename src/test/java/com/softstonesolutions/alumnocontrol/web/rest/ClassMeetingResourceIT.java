package com.softstonesolutions.alumnocontrol.web.rest;

import com.softstonesolutions.alumnocontrol.AlumnocontrolApp;
import com.softstonesolutions.alumnocontrol.domain.ClassMeeting;
import com.softstonesolutions.alumnocontrol.domain.Comment;
import com.softstonesolutions.alumnocontrol.domain.Assistance;
import com.softstonesolutions.alumnocontrol.repository.ClassMeetingRepository;
import com.softstonesolutions.alumnocontrol.service.ClassMeetingService;
import com.softstonesolutions.alumnocontrol.service.dto.ClassMeetingCriteria;
import com.softstonesolutions.alumnocontrol.service.ClassMeetingQueryService;

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
    private static final ZonedDateTime SMALLER_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    @Autowired
    private ClassMeetingRepository classMeetingRepository;

    @Autowired
    private ClassMeetingService classMeetingService;

    @Autowired
    private ClassMeetingQueryService classMeetingQueryService;

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
    public void getClassMeetingsByIdFiltering() throws Exception {
        // Initialize the database
        classMeetingRepository.saveAndFlush(classMeeting);

        Long id = classMeeting.getId();

        defaultClassMeetingShouldBeFound("id.equals=" + id);
        defaultClassMeetingShouldNotBeFound("id.notEquals=" + id);

        defaultClassMeetingShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultClassMeetingShouldNotBeFound("id.greaterThan=" + id);

        defaultClassMeetingShouldBeFound("id.lessThanOrEqual=" + id);
        defaultClassMeetingShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllClassMeetingsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        classMeetingRepository.saveAndFlush(classMeeting);

        // Get all the classMeetingList where name equals to DEFAULT_NAME
        defaultClassMeetingShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the classMeetingList where name equals to UPDATED_NAME
        defaultClassMeetingShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllClassMeetingsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classMeetingRepository.saveAndFlush(classMeeting);

        // Get all the classMeetingList where name not equals to DEFAULT_NAME
        defaultClassMeetingShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the classMeetingList where name not equals to UPDATED_NAME
        defaultClassMeetingShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllClassMeetingsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        classMeetingRepository.saveAndFlush(classMeeting);

        // Get all the classMeetingList where name in DEFAULT_NAME or UPDATED_NAME
        defaultClassMeetingShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the classMeetingList where name equals to UPDATED_NAME
        defaultClassMeetingShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllClassMeetingsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        classMeetingRepository.saveAndFlush(classMeeting);

        // Get all the classMeetingList where name is not null
        defaultClassMeetingShouldBeFound("name.specified=true");

        // Get all the classMeetingList where name is null
        defaultClassMeetingShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllClassMeetingsByNameContainsSomething() throws Exception {
        // Initialize the database
        classMeetingRepository.saveAndFlush(classMeeting);

        // Get all the classMeetingList where name contains DEFAULT_NAME
        defaultClassMeetingShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the classMeetingList where name contains UPDATED_NAME
        defaultClassMeetingShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllClassMeetingsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        classMeetingRepository.saveAndFlush(classMeeting);

        // Get all the classMeetingList where name does not contain DEFAULT_NAME
        defaultClassMeetingShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the classMeetingList where name does not contain UPDATED_NAME
        defaultClassMeetingShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllClassMeetingsByClassTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        classMeetingRepository.saveAndFlush(classMeeting);

        // Get all the classMeetingList where classType equals to DEFAULT_CLASS_TYPE
        defaultClassMeetingShouldBeFound("classType.equals=" + DEFAULT_CLASS_TYPE);

        // Get all the classMeetingList where classType equals to UPDATED_CLASS_TYPE
        defaultClassMeetingShouldNotBeFound("classType.equals=" + UPDATED_CLASS_TYPE);
    }

    @Test
    @Transactional
    public void getAllClassMeetingsByClassTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classMeetingRepository.saveAndFlush(classMeeting);

        // Get all the classMeetingList where classType not equals to DEFAULT_CLASS_TYPE
        defaultClassMeetingShouldNotBeFound("classType.notEquals=" + DEFAULT_CLASS_TYPE);

        // Get all the classMeetingList where classType not equals to UPDATED_CLASS_TYPE
        defaultClassMeetingShouldBeFound("classType.notEquals=" + UPDATED_CLASS_TYPE);
    }

    @Test
    @Transactional
    public void getAllClassMeetingsByClassTypeIsInShouldWork() throws Exception {
        // Initialize the database
        classMeetingRepository.saveAndFlush(classMeeting);

        // Get all the classMeetingList where classType in DEFAULT_CLASS_TYPE or UPDATED_CLASS_TYPE
        defaultClassMeetingShouldBeFound("classType.in=" + DEFAULT_CLASS_TYPE + "," + UPDATED_CLASS_TYPE);

        // Get all the classMeetingList where classType equals to UPDATED_CLASS_TYPE
        defaultClassMeetingShouldNotBeFound("classType.in=" + UPDATED_CLASS_TYPE);
    }

    @Test
    @Transactional
    public void getAllClassMeetingsByClassTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        classMeetingRepository.saveAndFlush(classMeeting);

        // Get all the classMeetingList where classType is not null
        defaultClassMeetingShouldBeFound("classType.specified=true");

        // Get all the classMeetingList where classType is null
        defaultClassMeetingShouldNotBeFound("classType.specified=false");
    }

    @Test
    @Transactional
    public void getAllClassMeetingsByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        classMeetingRepository.saveAndFlush(classMeeting);

        // Get all the classMeetingList where date equals to DEFAULT_DATE
        defaultClassMeetingShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the classMeetingList where date equals to UPDATED_DATE
        defaultClassMeetingShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllClassMeetingsByDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classMeetingRepository.saveAndFlush(classMeeting);

        // Get all the classMeetingList where date not equals to DEFAULT_DATE
        defaultClassMeetingShouldNotBeFound("date.notEquals=" + DEFAULT_DATE);

        // Get all the classMeetingList where date not equals to UPDATED_DATE
        defaultClassMeetingShouldBeFound("date.notEquals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllClassMeetingsByDateIsInShouldWork() throws Exception {
        // Initialize the database
        classMeetingRepository.saveAndFlush(classMeeting);

        // Get all the classMeetingList where date in DEFAULT_DATE or UPDATED_DATE
        defaultClassMeetingShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the classMeetingList where date equals to UPDATED_DATE
        defaultClassMeetingShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllClassMeetingsByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        classMeetingRepository.saveAndFlush(classMeeting);

        // Get all the classMeetingList where date is not null
        defaultClassMeetingShouldBeFound("date.specified=true");

        // Get all the classMeetingList where date is null
        defaultClassMeetingShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    public void getAllClassMeetingsByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        classMeetingRepository.saveAndFlush(classMeeting);

        // Get all the classMeetingList where date is greater than or equal to DEFAULT_DATE
        defaultClassMeetingShouldBeFound("date.greaterThanOrEqual=" + DEFAULT_DATE);

        // Get all the classMeetingList where date is greater than or equal to UPDATED_DATE
        defaultClassMeetingShouldNotBeFound("date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllClassMeetingsByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        classMeetingRepository.saveAndFlush(classMeeting);

        // Get all the classMeetingList where date is less than or equal to DEFAULT_DATE
        defaultClassMeetingShouldBeFound("date.lessThanOrEqual=" + DEFAULT_DATE);

        // Get all the classMeetingList where date is less than or equal to SMALLER_DATE
        defaultClassMeetingShouldNotBeFound("date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    public void getAllClassMeetingsByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        classMeetingRepository.saveAndFlush(classMeeting);

        // Get all the classMeetingList where date is less than DEFAULT_DATE
        defaultClassMeetingShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the classMeetingList where date is less than UPDATED_DATE
        defaultClassMeetingShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllClassMeetingsByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        classMeetingRepository.saveAndFlush(classMeeting);

        // Get all the classMeetingList where date is greater than DEFAULT_DATE
        defaultClassMeetingShouldNotBeFound("date.greaterThan=" + DEFAULT_DATE);

        // Get all the classMeetingList where date is greater than SMALLER_DATE
        defaultClassMeetingShouldBeFound("date.greaterThan=" + SMALLER_DATE);
    }


    @Test
    @Transactional
    public void getAllClassMeetingsByCommentsIsEqualToSomething() throws Exception {
        // Initialize the database
        classMeetingRepository.saveAndFlush(classMeeting);
        Comment comments = CommentResourceIT.createEntity(em);
        em.persist(comments);
        em.flush();
        classMeeting.addComments(comments);
        classMeetingRepository.saveAndFlush(classMeeting);
        Long commentsId = comments.getId();

        // Get all the classMeetingList where comments equals to commentsId
        defaultClassMeetingShouldBeFound("commentsId.equals=" + commentsId);

        // Get all the classMeetingList where comments equals to commentsId + 1
        defaultClassMeetingShouldNotBeFound("commentsId.equals=" + (commentsId + 1));
    }


    @Test
    @Transactional
    public void getAllClassMeetingsByAssistanceIsEqualToSomething() throws Exception {
        // Initialize the database
        classMeetingRepository.saveAndFlush(classMeeting);
        Assistance assistance = AssistanceResourceIT.createEntity(em);
        em.persist(assistance);
        em.flush();
        classMeeting.addAssistance(assistance);
        classMeetingRepository.saveAndFlush(classMeeting);
        Long assistanceId = assistance.getId();

        // Get all the classMeetingList where assistance equals to assistanceId
        defaultClassMeetingShouldBeFound("assistanceId.equals=" + assistanceId);

        // Get all the classMeetingList where assistance equals to assistanceId + 1
        defaultClassMeetingShouldNotBeFound("assistanceId.equals=" + (assistanceId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultClassMeetingShouldBeFound(String filter) throws Exception {
        restClassMeetingMockMvc.perform(get("/api/class-meetings?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(classMeeting.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].classType").value(hasItem(DEFAULT_CLASS_TYPE.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))));

        // Check, that the count call also returns 1
        restClassMeetingMockMvc.perform(get("/api/class-meetings/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultClassMeetingShouldNotBeFound(String filter) throws Exception {
        restClassMeetingMockMvc.perform(get("/api/class-meetings?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restClassMeetingMockMvc.perform(get("/api/class-meetings/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
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
