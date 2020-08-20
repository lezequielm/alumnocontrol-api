package com.softstonesolutions.alumnocontrol.web.rest;

import com.softstonesolutions.alumnocontrol.AlumnocontrolApp;
import com.softstonesolutions.alumnocontrol.domain.Group;
import com.softstonesolutions.alumnocontrol.domain.Document;
import com.softstonesolutions.alumnocontrol.domain.Student;
import com.softstonesolutions.alumnocontrol.domain.Assistance;
import com.softstonesolutions.alumnocontrol.domain.ExtendedUser;
import com.softstonesolutions.alumnocontrol.domain.Institute;
import com.softstonesolutions.alumnocontrol.repository.GroupRepository;
import com.softstonesolutions.alumnocontrol.service.GroupService;
import com.softstonesolutions.alumnocontrol.service.dto.GroupCriteria;
import com.softstonesolutions.alumnocontrol.service.GroupQueryService;

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
 * Integration tests for the {@link GroupResource} REST controller.
 */
@SpringBootTest(classes = AlumnocontrolApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class GroupResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ENABLED = false;
    private static final Boolean UPDATED_ENABLED = true;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GroupService groupService;

    @Autowired
    private GroupQueryService groupQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGroupMockMvc;

    private Group group;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Group createEntity(EntityManager em) {
        Group group = new Group()
            .name(DEFAULT_NAME)
            .enabled(DEFAULT_ENABLED);
        return group;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Group createUpdatedEntity(EntityManager em) {
        Group group = new Group()
            .name(UPDATED_NAME)
            .enabled(UPDATED_ENABLED);
        return group;
    }

    @BeforeEach
    public void initTest() {
        group = createEntity(em);
    }

    @Test
    @Transactional
    public void createGroup() throws Exception {
        int databaseSizeBeforeCreate = groupRepository.findAll().size();
        // Create the Group
        restGroupMockMvc.perform(post("/api/groups")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(group)))
            .andExpect(status().isCreated());

        // Validate the Group in the database
        List<Group> groupList = groupRepository.findAll();
        assertThat(groupList).hasSize(databaseSizeBeforeCreate + 1);
        Group testGroup = groupList.get(groupList.size() - 1);
        assertThat(testGroup.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testGroup.isEnabled()).isEqualTo(DEFAULT_ENABLED);
    }

    @Test
    @Transactional
    public void createGroupWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = groupRepository.findAll().size();

        // Create the Group with an existing ID
        group.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGroupMockMvc.perform(post("/api/groups")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(group)))
            .andExpect(status().isBadRequest());

        // Validate the Group in the database
        List<Group> groupList = groupRepository.findAll();
        assertThat(groupList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = groupRepository.findAll().size();
        // set the field null
        group.setName(null);

        // Create the Group, which fails.


        restGroupMockMvc.perform(post("/api/groups")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(group)))
            .andExpect(status().isBadRequest());

        List<Group> groupList = groupRepository.findAll();
        assertThat(groupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEnabledIsRequired() throws Exception {
        int databaseSizeBeforeTest = groupRepository.findAll().size();
        // set the field null
        group.setEnabled(null);

        // Create the Group, which fails.


        restGroupMockMvc.perform(post("/api/groups")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(group)))
            .andExpect(status().isBadRequest());

        List<Group> groupList = groupRepository.findAll();
        assertThat(groupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllGroups() throws Exception {
        // Initialize the database
        groupRepository.saveAndFlush(group);

        // Get all the groupList
        restGroupMockMvc.perform(get("/api/groups?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(group.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getGroup() throws Exception {
        // Initialize the database
        groupRepository.saveAndFlush(group);

        // Get the group
        restGroupMockMvc.perform(get("/api/groups/{id}", group.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(group.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED.booleanValue()));
    }


    @Test
    @Transactional
    public void getGroupsByIdFiltering() throws Exception {
        // Initialize the database
        groupRepository.saveAndFlush(group);

        Long id = group.getId();

        defaultGroupShouldBeFound("id.equals=" + id);
        defaultGroupShouldNotBeFound("id.notEquals=" + id);

        defaultGroupShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultGroupShouldNotBeFound("id.greaterThan=" + id);

        defaultGroupShouldBeFound("id.lessThanOrEqual=" + id);
        defaultGroupShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllGroupsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        groupRepository.saveAndFlush(group);

        // Get all the groupList where name equals to DEFAULT_NAME
        defaultGroupShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the groupList where name equals to UPDATED_NAME
        defaultGroupShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllGroupsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        groupRepository.saveAndFlush(group);

        // Get all the groupList where name not equals to DEFAULT_NAME
        defaultGroupShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the groupList where name not equals to UPDATED_NAME
        defaultGroupShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllGroupsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        groupRepository.saveAndFlush(group);

        // Get all the groupList where name in DEFAULT_NAME or UPDATED_NAME
        defaultGroupShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the groupList where name equals to UPDATED_NAME
        defaultGroupShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllGroupsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        groupRepository.saveAndFlush(group);

        // Get all the groupList where name is not null
        defaultGroupShouldBeFound("name.specified=true");

        // Get all the groupList where name is null
        defaultGroupShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllGroupsByNameContainsSomething() throws Exception {
        // Initialize the database
        groupRepository.saveAndFlush(group);

        // Get all the groupList where name contains DEFAULT_NAME
        defaultGroupShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the groupList where name contains UPDATED_NAME
        defaultGroupShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllGroupsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        groupRepository.saveAndFlush(group);

        // Get all the groupList where name does not contain DEFAULT_NAME
        defaultGroupShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the groupList where name does not contain UPDATED_NAME
        defaultGroupShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllGroupsByEnabledIsEqualToSomething() throws Exception {
        // Initialize the database
        groupRepository.saveAndFlush(group);

        // Get all the groupList where enabled equals to DEFAULT_ENABLED
        defaultGroupShouldBeFound("enabled.equals=" + DEFAULT_ENABLED);

        // Get all the groupList where enabled equals to UPDATED_ENABLED
        defaultGroupShouldNotBeFound("enabled.equals=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    public void getAllGroupsByEnabledIsNotEqualToSomething() throws Exception {
        // Initialize the database
        groupRepository.saveAndFlush(group);

        // Get all the groupList where enabled not equals to DEFAULT_ENABLED
        defaultGroupShouldNotBeFound("enabled.notEquals=" + DEFAULT_ENABLED);

        // Get all the groupList where enabled not equals to UPDATED_ENABLED
        defaultGroupShouldBeFound("enabled.notEquals=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    public void getAllGroupsByEnabledIsInShouldWork() throws Exception {
        // Initialize the database
        groupRepository.saveAndFlush(group);

        // Get all the groupList where enabled in DEFAULT_ENABLED or UPDATED_ENABLED
        defaultGroupShouldBeFound("enabled.in=" + DEFAULT_ENABLED + "," + UPDATED_ENABLED);

        // Get all the groupList where enabled equals to UPDATED_ENABLED
        defaultGroupShouldNotBeFound("enabled.in=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    public void getAllGroupsByEnabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        groupRepository.saveAndFlush(group);

        // Get all the groupList where enabled is not null
        defaultGroupShouldBeFound("enabled.specified=true");

        // Get all the groupList where enabled is null
        defaultGroupShouldNotBeFound("enabled.specified=false");
    }

    @Test
    @Transactional
    public void getAllGroupsByRequestedDocumentsIsEqualToSomething() throws Exception {
        // Initialize the database
        groupRepository.saveAndFlush(group);
        Document requestedDocuments = DocumentResourceIT.createEntity(em);
        em.persist(requestedDocuments);
        em.flush();
        group.addRequestedDocuments(requestedDocuments);
        groupRepository.saveAndFlush(group);
        Long requestedDocumentsId = requestedDocuments.getId();

        // Get all the groupList where requestedDocuments equals to requestedDocumentsId
        defaultGroupShouldBeFound("requestedDocumentsId.equals=" + requestedDocumentsId);

        // Get all the groupList where requestedDocuments equals to requestedDocumentsId + 1
        defaultGroupShouldNotBeFound("requestedDocumentsId.equals=" + (requestedDocumentsId + 1));
    }


    @Test
    @Transactional
    public void getAllGroupsByStudentsIsEqualToSomething() throws Exception {
        // Initialize the database
        groupRepository.saveAndFlush(group);
        Student students = StudentResourceIT.createEntity(em);
        em.persist(students);
        em.flush();
        group.addStudents(students);
        groupRepository.saveAndFlush(group);
        Long studentsId = students.getId();

        // Get all the groupList where students equals to studentsId
        defaultGroupShouldBeFound("studentsId.equals=" + studentsId);

        // Get all the groupList where students equals to studentsId + 1
        defaultGroupShouldNotBeFound("studentsId.equals=" + (studentsId + 1));
    }


    @Test
    @Transactional
    public void getAllGroupsByAssistanceIsEqualToSomething() throws Exception {
        // Initialize the database
        groupRepository.saveAndFlush(group);
        Assistance assistance = AssistanceResourceIT.createEntity(em);
        em.persist(assistance);
        em.flush();
        group.addAssistance(assistance);
        groupRepository.saveAndFlush(group);
        Long assistanceId = assistance.getId();

        // Get all the groupList where assistance equals to assistanceId
        defaultGroupShouldBeFound("assistanceId.equals=" + assistanceId);

        // Get all the groupList where assistance equals to assistanceId + 1
        defaultGroupShouldNotBeFound("assistanceId.equals=" + (assistanceId + 1));
    }


    @Test
    @Transactional
    public void getAllGroupsByUsersIsEqualToSomething() throws Exception {
        // Initialize the database
        groupRepository.saveAndFlush(group);
        ExtendedUser users = ExtendedUserResourceIT.createEntity(em);
        em.persist(users);
        em.flush();
        group.addUsers(users);
        groupRepository.saveAndFlush(group);
        Long usersId = users.getId();

        // Get all the groupList where users equals to usersId
        defaultGroupShouldBeFound("usersId.equals=" + usersId);

        // Get all the groupList where users equals to usersId + 1
        defaultGroupShouldNotBeFound("usersId.equals=" + (usersId + 1));
    }


    @Test
    @Transactional
    public void getAllGroupsByInstituteIsEqualToSomething() throws Exception {
        // Initialize the database
        groupRepository.saveAndFlush(group);
        Institute institute = InstituteResourceIT.createEntity(em);
        em.persist(institute);
        em.flush();
        group.setInstitute(institute);
        groupRepository.saveAndFlush(group);
        Long instituteId = institute.getId();

        // Get all the groupList where institute equals to instituteId
        defaultGroupShouldBeFound("instituteId.equals=" + instituteId);

        // Get all the groupList where institute equals to instituteId + 1
        defaultGroupShouldNotBeFound("instituteId.equals=" + (instituteId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultGroupShouldBeFound(String filter) throws Exception {
        restGroupMockMvc.perform(get("/api/groups?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(group.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())));

        // Check, that the count call also returns 1
        restGroupMockMvc.perform(get("/api/groups/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultGroupShouldNotBeFound(String filter) throws Exception {
        restGroupMockMvc.perform(get("/api/groups?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGroupMockMvc.perform(get("/api/groups/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingGroup() throws Exception {
        // Get the group
        restGroupMockMvc.perform(get("/api/groups/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGroup() throws Exception {
        // Initialize the database
        groupService.save(group);

        int databaseSizeBeforeUpdate = groupRepository.findAll().size();

        // Update the group
        Group updatedGroup = groupRepository.findById(group.getId()).get();
        // Disconnect from session so that the updates on updatedGroup are not directly saved in db
        em.detach(updatedGroup);
        updatedGroup
            .name(UPDATED_NAME)
            .enabled(UPDATED_ENABLED);

        restGroupMockMvc.perform(put("/api/groups")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedGroup)))
            .andExpect(status().isOk());

        // Validate the Group in the database
        List<Group> groupList = groupRepository.findAll();
        assertThat(groupList).hasSize(databaseSizeBeforeUpdate);
        Group testGroup = groupList.get(groupList.size() - 1);
        assertThat(testGroup.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testGroup.isEnabled()).isEqualTo(UPDATED_ENABLED);
    }

    @Test
    @Transactional
    public void updateNonExistingGroup() throws Exception {
        int databaseSizeBeforeUpdate = groupRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGroupMockMvc.perform(put("/api/groups")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(group)))
            .andExpect(status().isBadRequest());

        // Validate the Group in the database
        List<Group> groupList = groupRepository.findAll();
        assertThat(groupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteGroup() throws Exception {
        // Initialize the database
        groupService.save(group);

        int databaseSizeBeforeDelete = groupRepository.findAll().size();

        // Delete the group
        restGroupMockMvc.perform(delete("/api/groups/{id}", group.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Group> groupList = groupRepository.findAll();
        assertThat(groupList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
