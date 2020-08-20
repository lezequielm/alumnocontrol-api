package com.softstonesolutions.alumnocontrol.web.rest;

import com.softstonesolutions.alumnocontrol.AlumnocontrolApp;
import com.softstonesolutions.alumnocontrol.domain.ExtendedUser;
import com.softstonesolutions.alumnocontrol.domain.User;
import com.softstonesolutions.alumnocontrol.domain.Contact;
import com.softstonesolutions.alumnocontrol.domain.Address;
import com.softstonesolutions.alumnocontrol.domain.Institute;
import com.softstonesolutions.alumnocontrol.domain.Group;
import com.softstonesolutions.alumnocontrol.repository.ExtendedUserRepository;
import com.softstonesolutions.alumnocontrol.service.ExtendedUserService;
import com.softstonesolutions.alumnocontrol.service.dto.ExtendedUserCriteria;
import com.softstonesolutions.alumnocontrol.service.ExtendedUserQueryService;

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
 * Integration tests for the {@link ExtendedUserResource} REST controller.
 */
@SpringBootTest(classes = AlumnocontrolApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ExtendedUserResourceIT {

    private static final String DEFAULT_PHOTO_URL = "AAAAAAAAAA";
    private static final String UPDATED_PHOTO_URL = "BBBBBBBBBB";

    @Autowired
    private ExtendedUserRepository extendedUserRepository;

    @Autowired
    private ExtendedUserService extendedUserService;

    @Autowired
    private ExtendedUserQueryService extendedUserQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExtendedUserMockMvc;

    private ExtendedUser extendedUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExtendedUser createEntity(EntityManager em) {
        ExtendedUser extendedUser = new ExtendedUser()
            .photoUrl(DEFAULT_PHOTO_URL);
        return extendedUser;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExtendedUser createUpdatedEntity(EntityManager em) {
        ExtendedUser extendedUser = new ExtendedUser()
            .photoUrl(UPDATED_PHOTO_URL);
        return extendedUser;
    }

    @BeforeEach
    public void initTest() {
        extendedUser = createEntity(em);
    }

    @Test
    @Transactional
    public void createExtendedUser() throws Exception {
        int databaseSizeBeforeCreate = extendedUserRepository.findAll().size();
        // Create the ExtendedUser
        restExtendedUserMockMvc.perform(post("/api/extended-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(extendedUser)))
            .andExpect(status().isCreated());

        // Validate the ExtendedUser in the database
        List<ExtendedUser> extendedUserList = extendedUserRepository.findAll();
        assertThat(extendedUserList).hasSize(databaseSizeBeforeCreate + 1);
        ExtendedUser testExtendedUser = extendedUserList.get(extendedUserList.size() - 1);
        assertThat(testExtendedUser.getPhotoUrl()).isEqualTo(DEFAULT_PHOTO_URL);
    }

    @Test
    @Transactional
    public void createExtendedUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = extendedUserRepository.findAll().size();

        // Create the ExtendedUser with an existing ID
        extendedUser.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restExtendedUserMockMvc.perform(post("/api/extended-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(extendedUser)))
            .andExpect(status().isBadRequest());

        // Validate the ExtendedUser in the database
        List<ExtendedUser> extendedUserList = extendedUserRepository.findAll();
        assertThat(extendedUserList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllExtendedUsers() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList
        restExtendedUserMockMvc.perform(get("/api/extended-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(extendedUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].photoUrl").value(hasItem(DEFAULT_PHOTO_URL)));
    }
    
    @Test
    @Transactional
    public void getExtendedUser() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get the extendedUser
        restExtendedUserMockMvc.perform(get("/api/extended-users/{id}", extendedUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(extendedUser.getId().intValue()))
            .andExpect(jsonPath("$.photoUrl").value(DEFAULT_PHOTO_URL));
    }


    @Test
    @Transactional
    public void getExtendedUsersByIdFiltering() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        Long id = extendedUser.getId();

        defaultExtendedUserShouldBeFound("id.equals=" + id);
        defaultExtendedUserShouldNotBeFound("id.notEquals=" + id);

        defaultExtendedUserShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultExtendedUserShouldNotBeFound("id.greaterThan=" + id);

        defaultExtendedUserShouldBeFound("id.lessThanOrEqual=" + id);
        defaultExtendedUserShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllExtendedUsersByPhotoUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList where photoUrl equals to DEFAULT_PHOTO_URL
        defaultExtendedUserShouldBeFound("photoUrl.equals=" + DEFAULT_PHOTO_URL);

        // Get all the extendedUserList where photoUrl equals to UPDATED_PHOTO_URL
        defaultExtendedUserShouldNotBeFound("photoUrl.equals=" + UPDATED_PHOTO_URL);
    }

    @Test
    @Transactional
    public void getAllExtendedUsersByPhotoUrlIsNotEqualToSomething() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList where photoUrl not equals to DEFAULT_PHOTO_URL
        defaultExtendedUserShouldNotBeFound("photoUrl.notEquals=" + DEFAULT_PHOTO_URL);

        // Get all the extendedUserList where photoUrl not equals to UPDATED_PHOTO_URL
        defaultExtendedUserShouldBeFound("photoUrl.notEquals=" + UPDATED_PHOTO_URL);
    }

    @Test
    @Transactional
    public void getAllExtendedUsersByPhotoUrlIsInShouldWork() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList where photoUrl in DEFAULT_PHOTO_URL or UPDATED_PHOTO_URL
        defaultExtendedUserShouldBeFound("photoUrl.in=" + DEFAULT_PHOTO_URL + "," + UPDATED_PHOTO_URL);

        // Get all the extendedUserList where photoUrl equals to UPDATED_PHOTO_URL
        defaultExtendedUserShouldNotBeFound("photoUrl.in=" + UPDATED_PHOTO_URL);
    }

    @Test
    @Transactional
    public void getAllExtendedUsersByPhotoUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList where photoUrl is not null
        defaultExtendedUserShouldBeFound("photoUrl.specified=true");

        // Get all the extendedUserList where photoUrl is null
        defaultExtendedUserShouldNotBeFound("photoUrl.specified=false");
    }
                @Test
    @Transactional
    public void getAllExtendedUsersByPhotoUrlContainsSomething() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList where photoUrl contains DEFAULT_PHOTO_URL
        defaultExtendedUserShouldBeFound("photoUrl.contains=" + DEFAULT_PHOTO_URL);

        // Get all the extendedUserList where photoUrl contains UPDATED_PHOTO_URL
        defaultExtendedUserShouldNotBeFound("photoUrl.contains=" + UPDATED_PHOTO_URL);
    }

    @Test
    @Transactional
    public void getAllExtendedUsersByPhotoUrlNotContainsSomething() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList where photoUrl does not contain DEFAULT_PHOTO_URL
        defaultExtendedUserShouldNotBeFound("photoUrl.doesNotContain=" + DEFAULT_PHOTO_URL);

        // Get all the extendedUserList where photoUrl does not contain UPDATED_PHOTO_URL
        defaultExtendedUserShouldBeFound("photoUrl.doesNotContain=" + UPDATED_PHOTO_URL);
    }


    @Test
    @Transactional
    public void getAllExtendedUsersByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        extendedUser.setUser(user);
        extendedUserRepository.saveAndFlush(extendedUser);
        Long userId = user.getId();

        // Get all the extendedUserList where user equals to userId
        defaultExtendedUserShouldBeFound("userId.equals=" + userId);

        // Get all the extendedUserList where user equals to userId + 1
        defaultExtendedUserShouldNotBeFound("userId.equals=" + (userId + 1));
    }


    @Test
    @Transactional
    public void getAllExtendedUsersByContactIsEqualToSomething() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);
        Contact contact = ContactResourceIT.createEntity(em);
        em.persist(contact);
        em.flush();
        extendedUser.addContact(contact);
        extendedUserRepository.saveAndFlush(extendedUser);
        Long contactId = contact.getId();

        // Get all the extendedUserList where contact equals to contactId
        defaultExtendedUserShouldBeFound("contactId.equals=" + contactId);

        // Get all the extendedUserList where contact equals to contactId + 1
        defaultExtendedUserShouldNotBeFound("contactId.equals=" + (contactId + 1));
    }


    @Test
    @Transactional
    public void getAllExtendedUsersByAddressesIsEqualToSomething() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);
        Address addresses = AddressResourceIT.createEntity(em);
        em.persist(addresses);
        em.flush();
        extendedUser.addAddresses(addresses);
        extendedUserRepository.saveAndFlush(extendedUser);
        Long addressesId = addresses.getId();

        // Get all the extendedUserList where addresses equals to addressesId
        defaultExtendedUserShouldBeFound("addressesId.equals=" + addressesId);

        // Get all the extendedUserList where addresses equals to addressesId + 1
        defaultExtendedUserShouldNotBeFound("addressesId.equals=" + (addressesId + 1));
    }


    @Test
    @Transactional
    public void getAllExtendedUsersByInstituteIsEqualToSomething() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);
        Institute institute = InstituteResourceIT.createEntity(em);
        em.persist(institute);
        em.flush();
        extendedUser.setInstitute(institute);
        extendedUserRepository.saveAndFlush(extendedUser);
        Long instituteId = institute.getId();

        // Get all the extendedUserList where institute equals to instituteId
        defaultExtendedUserShouldBeFound("instituteId.equals=" + instituteId);

        // Get all the extendedUserList where institute equals to instituteId + 1
        defaultExtendedUserShouldNotBeFound("instituteId.equals=" + (instituteId + 1));
    }


    @Test
    @Transactional
    public void getAllExtendedUsersByGroupIsEqualToSomething() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);
        Group group = GroupResourceIT.createEntity(em);
        em.persist(group);
        em.flush();
        extendedUser.setGroup(group);
        extendedUserRepository.saveAndFlush(extendedUser);
        Long groupId = group.getId();

        // Get all the extendedUserList where group equals to groupId
        defaultExtendedUserShouldBeFound("groupId.equals=" + groupId);

        // Get all the extendedUserList where group equals to groupId + 1
        defaultExtendedUserShouldNotBeFound("groupId.equals=" + (groupId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultExtendedUserShouldBeFound(String filter) throws Exception {
        restExtendedUserMockMvc.perform(get("/api/extended-users?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(extendedUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].photoUrl").value(hasItem(DEFAULT_PHOTO_URL)));

        // Check, that the count call also returns 1
        restExtendedUserMockMvc.perform(get("/api/extended-users/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultExtendedUserShouldNotBeFound(String filter) throws Exception {
        restExtendedUserMockMvc.perform(get("/api/extended-users?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restExtendedUserMockMvc.perform(get("/api/extended-users/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingExtendedUser() throws Exception {
        // Get the extendedUser
        restExtendedUserMockMvc.perform(get("/api/extended-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExtendedUser() throws Exception {
        // Initialize the database
        extendedUserService.save(extendedUser);

        int databaseSizeBeforeUpdate = extendedUserRepository.findAll().size();

        // Update the extendedUser
        ExtendedUser updatedExtendedUser = extendedUserRepository.findById(extendedUser.getId()).get();
        // Disconnect from session so that the updates on updatedExtendedUser are not directly saved in db
        em.detach(updatedExtendedUser);
        updatedExtendedUser
            .photoUrl(UPDATED_PHOTO_URL);

        restExtendedUserMockMvc.perform(put("/api/extended-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedExtendedUser)))
            .andExpect(status().isOk());

        // Validate the ExtendedUser in the database
        List<ExtendedUser> extendedUserList = extendedUserRepository.findAll();
        assertThat(extendedUserList).hasSize(databaseSizeBeforeUpdate);
        ExtendedUser testExtendedUser = extendedUserList.get(extendedUserList.size() - 1);
        assertThat(testExtendedUser.getPhotoUrl()).isEqualTo(UPDATED_PHOTO_URL);
    }

    @Test
    @Transactional
    public void updateNonExistingExtendedUser() throws Exception {
        int databaseSizeBeforeUpdate = extendedUserRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExtendedUserMockMvc.perform(put("/api/extended-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(extendedUser)))
            .andExpect(status().isBadRequest());

        // Validate the ExtendedUser in the database
        List<ExtendedUser> extendedUserList = extendedUserRepository.findAll();
        assertThat(extendedUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteExtendedUser() throws Exception {
        // Initialize the database
        extendedUserService.save(extendedUser);

        int databaseSizeBeforeDelete = extendedUserRepository.findAll().size();

        // Delete the extendedUser
        restExtendedUserMockMvc.perform(delete("/api/extended-users/{id}", extendedUser.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ExtendedUser> extendedUserList = extendedUserRepository.findAll();
        assertThat(extendedUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
