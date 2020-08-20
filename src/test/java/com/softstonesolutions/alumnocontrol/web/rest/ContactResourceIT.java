package com.softstonesolutions.alumnocontrol.web.rest;

import com.softstonesolutions.alumnocontrol.AlumnocontrolApp;
import com.softstonesolutions.alumnocontrol.domain.Contact;
import com.softstonesolutions.alumnocontrol.domain.Student;
import com.softstonesolutions.alumnocontrol.domain.ExtendedUser;
import com.softstonesolutions.alumnocontrol.repository.ContactRepository;
import com.softstonesolutions.alumnocontrol.service.ContactService;
import com.softstonesolutions.alumnocontrol.service.dto.ContactCriteria;
import com.softstonesolutions.alumnocontrol.service.ContactQueryService;

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

import com.softstonesolutions.alumnocontrol.domain.enumeration.ContactType;
/**
 * Integration tests for the {@link ContactResource} REST controller.
 */
@SpringBootTest(classes = AlumnocontrolApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ContactResourceIT {

    private static final ContactType DEFAULT_CONTACT_TYPE = ContactType.EMAIL;
    private static final ContactType UPDATED_CONTACT_TYPE = ContactType.PHONE;

    private static final String DEFAULT_DATA = "AAAAAAAAAA";
    private static final String UPDATED_DATA = "BBBBBBBBBB";

    private static final Long DEFAULT_ORDER = 1L;
    private static final Long UPDATED_ORDER = 2L;
    private static final Long SMALLER_ORDER = 1L - 1L;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ContactService contactService;

    @Autowired
    private ContactQueryService contactQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContactMockMvc;

    private Contact contact;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contact createEntity(EntityManager em) {
        Contact contact = new Contact()
            .contactType(DEFAULT_CONTACT_TYPE)
            .data(DEFAULT_DATA)
            .order(DEFAULT_ORDER);
        return contact;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contact createUpdatedEntity(EntityManager em) {
        Contact contact = new Contact()
            .contactType(UPDATED_CONTACT_TYPE)
            .data(UPDATED_DATA)
            .order(UPDATED_ORDER);
        return contact;
    }

    @BeforeEach
    public void initTest() {
        contact = createEntity(em);
    }

    @Test
    @Transactional
    public void createContact() throws Exception {
        int databaseSizeBeforeCreate = contactRepository.findAll().size();
        // Create the Contact
        restContactMockMvc.perform(post("/api/contacts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contact)))
            .andExpect(status().isCreated());

        // Validate the Contact in the database
        List<Contact> contactList = contactRepository.findAll();
        assertThat(contactList).hasSize(databaseSizeBeforeCreate + 1);
        Contact testContact = contactList.get(contactList.size() - 1);
        assertThat(testContact.getContactType()).isEqualTo(DEFAULT_CONTACT_TYPE);
        assertThat(testContact.getData()).isEqualTo(DEFAULT_DATA);
        assertThat(testContact.getOrder()).isEqualTo(DEFAULT_ORDER);
    }

    @Test
    @Transactional
    public void createContactWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = contactRepository.findAll().size();

        // Create the Contact with an existing ID
        contact.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restContactMockMvc.perform(post("/api/contacts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contact)))
            .andExpect(status().isBadRequest());

        // Validate the Contact in the database
        List<Contact> contactList = contactRepository.findAll();
        assertThat(contactList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkContactTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = contactRepository.findAll().size();
        // set the field null
        contact.setContactType(null);

        // Create the Contact, which fails.


        restContactMockMvc.perform(post("/api/contacts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contact)))
            .andExpect(status().isBadRequest());

        List<Contact> contactList = contactRepository.findAll();
        assertThat(contactList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDataIsRequired() throws Exception {
        int databaseSizeBeforeTest = contactRepository.findAll().size();
        // set the field null
        contact.setData(null);

        // Create the Contact, which fails.


        restContactMockMvc.perform(post("/api/contacts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contact)))
            .andExpect(status().isBadRequest());

        List<Contact> contactList = contactRepository.findAll();
        assertThat(contactList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOrderIsRequired() throws Exception {
        int databaseSizeBeforeTest = contactRepository.findAll().size();
        // set the field null
        contact.setOrder(null);

        // Create the Contact, which fails.


        restContactMockMvc.perform(post("/api/contacts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contact)))
            .andExpect(status().isBadRequest());

        List<Contact> contactList = contactRepository.findAll();
        assertThat(contactList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllContacts() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList
        restContactMockMvc.perform(get("/api/contacts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contact.getId().intValue())))
            .andExpect(jsonPath("$.[*].contactType").value(hasItem(DEFAULT_CONTACT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].data").value(hasItem(DEFAULT_DATA)))
            .andExpect(jsonPath("$.[*].order").value(hasItem(DEFAULT_ORDER.intValue())));
    }
    
    @Test
    @Transactional
    public void getContact() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get the contact
        restContactMockMvc.perform(get("/api/contacts/{id}", contact.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contact.getId().intValue()))
            .andExpect(jsonPath("$.contactType").value(DEFAULT_CONTACT_TYPE.toString()))
            .andExpect(jsonPath("$.data").value(DEFAULT_DATA))
            .andExpect(jsonPath("$.order").value(DEFAULT_ORDER.intValue()));
    }


    @Test
    @Transactional
    public void getContactsByIdFiltering() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        Long id = contact.getId();

        defaultContactShouldBeFound("id.equals=" + id);
        defaultContactShouldNotBeFound("id.notEquals=" + id);

        defaultContactShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultContactShouldNotBeFound("id.greaterThan=" + id);

        defaultContactShouldBeFound("id.lessThanOrEqual=" + id);
        defaultContactShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllContactsByContactTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where contactType equals to DEFAULT_CONTACT_TYPE
        defaultContactShouldBeFound("contactType.equals=" + DEFAULT_CONTACT_TYPE);

        // Get all the contactList where contactType equals to UPDATED_CONTACT_TYPE
        defaultContactShouldNotBeFound("contactType.equals=" + UPDATED_CONTACT_TYPE);
    }

    @Test
    @Transactional
    public void getAllContactsByContactTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where contactType not equals to DEFAULT_CONTACT_TYPE
        defaultContactShouldNotBeFound("contactType.notEquals=" + DEFAULT_CONTACT_TYPE);

        // Get all the contactList where contactType not equals to UPDATED_CONTACT_TYPE
        defaultContactShouldBeFound("contactType.notEquals=" + UPDATED_CONTACT_TYPE);
    }

    @Test
    @Transactional
    public void getAllContactsByContactTypeIsInShouldWork() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where contactType in DEFAULT_CONTACT_TYPE or UPDATED_CONTACT_TYPE
        defaultContactShouldBeFound("contactType.in=" + DEFAULT_CONTACT_TYPE + "," + UPDATED_CONTACT_TYPE);

        // Get all the contactList where contactType equals to UPDATED_CONTACT_TYPE
        defaultContactShouldNotBeFound("contactType.in=" + UPDATED_CONTACT_TYPE);
    }

    @Test
    @Transactional
    public void getAllContactsByContactTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where contactType is not null
        defaultContactShouldBeFound("contactType.specified=true");

        // Get all the contactList where contactType is null
        defaultContactShouldNotBeFound("contactType.specified=false");
    }

    @Test
    @Transactional
    public void getAllContactsByDataIsEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where data equals to DEFAULT_DATA
        defaultContactShouldBeFound("data.equals=" + DEFAULT_DATA);

        // Get all the contactList where data equals to UPDATED_DATA
        defaultContactShouldNotBeFound("data.equals=" + UPDATED_DATA);
    }

    @Test
    @Transactional
    public void getAllContactsByDataIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where data not equals to DEFAULT_DATA
        defaultContactShouldNotBeFound("data.notEquals=" + DEFAULT_DATA);

        // Get all the contactList where data not equals to UPDATED_DATA
        defaultContactShouldBeFound("data.notEquals=" + UPDATED_DATA);
    }

    @Test
    @Transactional
    public void getAllContactsByDataIsInShouldWork() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where data in DEFAULT_DATA or UPDATED_DATA
        defaultContactShouldBeFound("data.in=" + DEFAULT_DATA + "," + UPDATED_DATA);

        // Get all the contactList where data equals to UPDATED_DATA
        defaultContactShouldNotBeFound("data.in=" + UPDATED_DATA);
    }

    @Test
    @Transactional
    public void getAllContactsByDataIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where data is not null
        defaultContactShouldBeFound("data.specified=true");

        // Get all the contactList where data is null
        defaultContactShouldNotBeFound("data.specified=false");
    }
                @Test
    @Transactional
    public void getAllContactsByDataContainsSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where data contains DEFAULT_DATA
        defaultContactShouldBeFound("data.contains=" + DEFAULT_DATA);

        // Get all the contactList where data contains UPDATED_DATA
        defaultContactShouldNotBeFound("data.contains=" + UPDATED_DATA);
    }

    @Test
    @Transactional
    public void getAllContactsByDataNotContainsSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where data does not contain DEFAULT_DATA
        defaultContactShouldNotBeFound("data.doesNotContain=" + DEFAULT_DATA);

        // Get all the contactList where data does not contain UPDATED_DATA
        defaultContactShouldBeFound("data.doesNotContain=" + UPDATED_DATA);
    }


    @Test
    @Transactional
    public void getAllContactsByOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where order equals to DEFAULT_ORDER
        defaultContactShouldBeFound("order.equals=" + DEFAULT_ORDER);

        // Get all the contactList where order equals to UPDATED_ORDER
        defaultContactShouldNotBeFound("order.equals=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    public void getAllContactsByOrderIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where order not equals to DEFAULT_ORDER
        defaultContactShouldNotBeFound("order.notEquals=" + DEFAULT_ORDER);

        // Get all the contactList where order not equals to UPDATED_ORDER
        defaultContactShouldBeFound("order.notEquals=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    public void getAllContactsByOrderIsInShouldWork() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where order in DEFAULT_ORDER or UPDATED_ORDER
        defaultContactShouldBeFound("order.in=" + DEFAULT_ORDER + "," + UPDATED_ORDER);

        // Get all the contactList where order equals to UPDATED_ORDER
        defaultContactShouldNotBeFound("order.in=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    public void getAllContactsByOrderIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where order is not null
        defaultContactShouldBeFound("order.specified=true");

        // Get all the contactList where order is null
        defaultContactShouldNotBeFound("order.specified=false");
    }

    @Test
    @Transactional
    public void getAllContactsByOrderIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where order is greater than or equal to DEFAULT_ORDER
        defaultContactShouldBeFound("order.greaterThanOrEqual=" + DEFAULT_ORDER);

        // Get all the contactList where order is greater than or equal to UPDATED_ORDER
        defaultContactShouldNotBeFound("order.greaterThanOrEqual=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    public void getAllContactsByOrderIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where order is less than or equal to DEFAULT_ORDER
        defaultContactShouldBeFound("order.lessThanOrEqual=" + DEFAULT_ORDER);

        // Get all the contactList where order is less than or equal to SMALLER_ORDER
        defaultContactShouldNotBeFound("order.lessThanOrEqual=" + SMALLER_ORDER);
    }

    @Test
    @Transactional
    public void getAllContactsByOrderIsLessThanSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where order is less than DEFAULT_ORDER
        defaultContactShouldNotBeFound("order.lessThan=" + DEFAULT_ORDER);

        // Get all the contactList where order is less than UPDATED_ORDER
        defaultContactShouldBeFound("order.lessThan=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    public void getAllContactsByOrderIsGreaterThanSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where order is greater than DEFAULT_ORDER
        defaultContactShouldNotBeFound("order.greaterThan=" + DEFAULT_ORDER);

        // Get all the contactList where order is greater than SMALLER_ORDER
        defaultContactShouldBeFound("order.greaterThan=" + SMALLER_ORDER);
    }


    @Test
    @Transactional
    public void getAllContactsByStudentIsEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);
        Student student = StudentResourceIT.createEntity(em);
        em.persist(student);
        em.flush();
        contact.setStudent(student);
        contactRepository.saveAndFlush(contact);
        Long studentId = student.getId();

        // Get all the contactList where student equals to studentId
        defaultContactShouldBeFound("studentId.equals=" + studentId);

        // Get all the contactList where student equals to studentId + 1
        defaultContactShouldNotBeFound("studentId.equals=" + (studentId + 1));
    }


    @Test
    @Transactional
    public void getAllContactsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);
        ExtendedUser user = ExtendedUserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        contact.setUser(user);
        contactRepository.saveAndFlush(contact);
        Long userId = user.getId();

        // Get all the contactList where user equals to userId
        defaultContactShouldBeFound("userId.equals=" + userId);

        // Get all the contactList where user equals to userId + 1
        defaultContactShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultContactShouldBeFound(String filter) throws Exception {
        restContactMockMvc.perform(get("/api/contacts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contact.getId().intValue())))
            .andExpect(jsonPath("$.[*].contactType").value(hasItem(DEFAULT_CONTACT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].data").value(hasItem(DEFAULT_DATA)))
            .andExpect(jsonPath("$.[*].order").value(hasItem(DEFAULT_ORDER.intValue())));

        // Check, that the count call also returns 1
        restContactMockMvc.perform(get("/api/contacts/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultContactShouldNotBeFound(String filter) throws Exception {
        restContactMockMvc.perform(get("/api/contacts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restContactMockMvc.perform(get("/api/contacts/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingContact() throws Exception {
        // Get the contact
        restContactMockMvc.perform(get("/api/contacts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateContact() throws Exception {
        // Initialize the database
        contactService.save(contact);

        int databaseSizeBeforeUpdate = contactRepository.findAll().size();

        // Update the contact
        Contact updatedContact = contactRepository.findById(contact.getId()).get();
        // Disconnect from session so that the updates on updatedContact are not directly saved in db
        em.detach(updatedContact);
        updatedContact
            .contactType(UPDATED_CONTACT_TYPE)
            .data(UPDATED_DATA)
            .order(UPDATED_ORDER);

        restContactMockMvc.perform(put("/api/contacts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedContact)))
            .andExpect(status().isOk());

        // Validate the Contact in the database
        List<Contact> contactList = contactRepository.findAll();
        assertThat(contactList).hasSize(databaseSizeBeforeUpdate);
        Contact testContact = contactList.get(contactList.size() - 1);
        assertThat(testContact.getContactType()).isEqualTo(UPDATED_CONTACT_TYPE);
        assertThat(testContact.getData()).isEqualTo(UPDATED_DATA);
        assertThat(testContact.getOrder()).isEqualTo(UPDATED_ORDER);
    }

    @Test
    @Transactional
    public void updateNonExistingContact() throws Exception {
        int databaseSizeBeforeUpdate = contactRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContactMockMvc.perform(put("/api/contacts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contact)))
            .andExpect(status().isBadRequest());

        // Validate the Contact in the database
        List<Contact> contactList = contactRepository.findAll();
        assertThat(contactList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteContact() throws Exception {
        // Initialize the database
        contactService.save(contact);

        int databaseSizeBeforeDelete = contactRepository.findAll().size();

        // Delete the contact
        restContactMockMvc.perform(delete("/api/contacts/{id}", contact.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Contact> contactList = contactRepository.findAll();
        assertThat(contactList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
