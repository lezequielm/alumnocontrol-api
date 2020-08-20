package com.softstonesolutions.alumnocontrol.web.rest;

import com.softstonesolutions.alumnocontrol.AlumnocontrolApp;
import com.softstonesolutions.alumnocontrol.domain.Document;
import com.softstonesolutions.alumnocontrol.domain.Group;
import com.softstonesolutions.alumnocontrol.domain.Student;
import com.softstonesolutions.alumnocontrol.repository.DocumentRepository;
import com.softstonesolutions.alumnocontrol.service.DocumentService;
import com.softstonesolutions.alumnocontrol.service.dto.DocumentCriteria;
import com.softstonesolutions.alumnocontrol.service.DocumentQueryService;

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

/**
 * Integration tests for the {@link DocumentResource} REST controller.
 */
@SpringBootTest(classes = AlumnocontrolApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class DocumentResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_REQUEST_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_REQUEST_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_REQUEST_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_UPLOAD_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPLOAD_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_UPLOAD_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_FILE_URL = "AAAAAAAAAA";
    private static final String UPDATED_FILE_URL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_SENT = false;
    private static final Boolean UPDATED_SENT = true;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private DocumentQueryService documentQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocumentMockMvc;

    private Document document;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Document createEntity(EntityManager em) {
        Document document = new Document()
            .name(DEFAULT_NAME)
            .requestDate(DEFAULT_REQUEST_DATE)
            .uploadDate(DEFAULT_UPLOAD_DATE)
            .fileUrl(DEFAULT_FILE_URL)
            .sent(DEFAULT_SENT);
        return document;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Document createUpdatedEntity(EntityManager em) {
        Document document = new Document()
            .name(UPDATED_NAME)
            .requestDate(UPDATED_REQUEST_DATE)
            .uploadDate(UPDATED_UPLOAD_DATE)
            .fileUrl(UPDATED_FILE_URL)
            .sent(UPDATED_SENT);
        return document;
    }

    @BeforeEach
    public void initTest() {
        document = createEntity(em);
    }

    @Test
    @Transactional
    public void createDocument() throws Exception {
        int databaseSizeBeforeCreate = documentRepository.findAll().size();
        // Create the Document
        restDocumentMockMvc.perform(post("/api/documents")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(document)))
            .andExpect(status().isCreated());

        // Validate the Document in the database
        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeCreate + 1);
        Document testDocument = documentList.get(documentList.size() - 1);
        assertThat(testDocument.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDocument.getRequestDate()).isEqualTo(DEFAULT_REQUEST_DATE);
        assertThat(testDocument.getUploadDate()).isEqualTo(DEFAULT_UPLOAD_DATE);
        assertThat(testDocument.getFileUrl()).isEqualTo(DEFAULT_FILE_URL);
        assertThat(testDocument.isSent()).isEqualTo(DEFAULT_SENT);
    }

    @Test
    @Transactional
    public void createDocumentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = documentRepository.findAll().size();

        // Create the Document with an existing ID
        document.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentMockMvc.perform(post("/api/documents")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(document)))
            .andExpect(status().isBadRequest());

        // Validate the Document in the database
        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = documentRepository.findAll().size();
        // set the field null
        document.setName(null);

        // Create the Document, which fails.


        restDocumentMockMvc.perform(post("/api/documents")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(document)))
            .andExpect(status().isBadRequest());

        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFileUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = documentRepository.findAll().size();
        // set the field null
        document.setFileUrl(null);

        // Create the Document, which fails.


        restDocumentMockMvc.perform(post("/api/documents")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(document)))
            .andExpect(status().isBadRequest());

        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSentIsRequired() throws Exception {
        int databaseSizeBeforeTest = documentRepository.findAll().size();
        // set the field null
        document.setSent(null);

        // Create the Document, which fails.


        restDocumentMockMvc.perform(post("/api/documents")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(document)))
            .andExpect(status().isBadRequest());

        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDocuments() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList
        restDocumentMockMvc.perform(get("/api/documents?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(document.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].requestDate").value(hasItem(sameInstant(DEFAULT_REQUEST_DATE))))
            .andExpect(jsonPath("$.[*].uploadDate").value(hasItem(sameInstant(DEFAULT_UPLOAD_DATE))))
            .andExpect(jsonPath("$.[*].fileUrl").value(hasItem(DEFAULT_FILE_URL)))
            .andExpect(jsonPath("$.[*].sent").value(hasItem(DEFAULT_SENT.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getDocument() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get the document
        restDocumentMockMvc.perform(get("/api/documents/{id}", document.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(document.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.requestDate").value(sameInstant(DEFAULT_REQUEST_DATE)))
            .andExpect(jsonPath("$.uploadDate").value(sameInstant(DEFAULT_UPLOAD_DATE)))
            .andExpect(jsonPath("$.fileUrl").value(DEFAULT_FILE_URL))
            .andExpect(jsonPath("$.sent").value(DEFAULT_SENT.booleanValue()));
    }


    @Test
    @Transactional
    public void getDocumentsByIdFiltering() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        Long id = document.getId();

        defaultDocumentShouldBeFound("id.equals=" + id);
        defaultDocumentShouldNotBeFound("id.notEquals=" + id);

        defaultDocumentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDocumentShouldNotBeFound("id.greaterThan=" + id);

        defaultDocumentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDocumentShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllDocumentsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where name equals to DEFAULT_NAME
        defaultDocumentShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the documentList where name equals to UPDATED_NAME
        defaultDocumentShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllDocumentsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where name not equals to DEFAULT_NAME
        defaultDocumentShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the documentList where name not equals to UPDATED_NAME
        defaultDocumentShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllDocumentsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where name in DEFAULT_NAME or UPDATED_NAME
        defaultDocumentShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the documentList where name equals to UPDATED_NAME
        defaultDocumentShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllDocumentsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where name is not null
        defaultDocumentShouldBeFound("name.specified=true");

        // Get all the documentList where name is null
        defaultDocumentShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllDocumentsByNameContainsSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where name contains DEFAULT_NAME
        defaultDocumentShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the documentList where name contains UPDATED_NAME
        defaultDocumentShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllDocumentsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where name does not contain DEFAULT_NAME
        defaultDocumentShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the documentList where name does not contain UPDATED_NAME
        defaultDocumentShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllDocumentsByRequestDateIsEqualToSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where requestDate equals to DEFAULT_REQUEST_DATE
        defaultDocumentShouldBeFound("requestDate.equals=" + DEFAULT_REQUEST_DATE);

        // Get all the documentList where requestDate equals to UPDATED_REQUEST_DATE
        defaultDocumentShouldNotBeFound("requestDate.equals=" + UPDATED_REQUEST_DATE);
    }

    @Test
    @Transactional
    public void getAllDocumentsByRequestDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where requestDate not equals to DEFAULT_REQUEST_DATE
        defaultDocumentShouldNotBeFound("requestDate.notEquals=" + DEFAULT_REQUEST_DATE);

        // Get all the documentList where requestDate not equals to UPDATED_REQUEST_DATE
        defaultDocumentShouldBeFound("requestDate.notEquals=" + UPDATED_REQUEST_DATE);
    }

    @Test
    @Transactional
    public void getAllDocumentsByRequestDateIsInShouldWork() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where requestDate in DEFAULT_REQUEST_DATE or UPDATED_REQUEST_DATE
        defaultDocumentShouldBeFound("requestDate.in=" + DEFAULT_REQUEST_DATE + "," + UPDATED_REQUEST_DATE);

        // Get all the documentList where requestDate equals to UPDATED_REQUEST_DATE
        defaultDocumentShouldNotBeFound("requestDate.in=" + UPDATED_REQUEST_DATE);
    }

    @Test
    @Transactional
    public void getAllDocumentsByRequestDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where requestDate is not null
        defaultDocumentShouldBeFound("requestDate.specified=true");

        // Get all the documentList where requestDate is null
        defaultDocumentShouldNotBeFound("requestDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllDocumentsByRequestDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where requestDate is greater than or equal to DEFAULT_REQUEST_DATE
        defaultDocumentShouldBeFound("requestDate.greaterThanOrEqual=" + DEFAULT_REQUEST_DATE);

        // Get all the documentList where requestDate is greater than or equal to UPDATED_REQUEST_DATE
        defaultDocumentShouldNotBeFound("requestDate.greaterThanOrEqual=" + UPDATED_REQUEST_DATE);
    }

    @Test
    @Transactional
    public void getAllDocumentsByRequestDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where requestDate is less than or equal to DEFAULT_REQUEST_DATE
        defaultDocumentShouldBeFound("requestDate.lessThanOrEqual=" + DEFAULT_REQUEST_DATE);

        // Get all the documentList where requestDate is less than or equal to SMALLER_REQUEST_DATE
        defaultDocumentShouldNotBeFound("requestDate.lessThanOrEqual=" + SMALLER_REQUEST_DATE);
    }

    @Test
    @Transactional
    public void getAllDocumentsByRequestDateIsLessThanSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where requestDate is less than DEFAULT_REQUEST_DATE
        defaultDocumentShouldNotBeFound("requestDate.lessThan=" + DEFAULT_REQUEST_DATE);

        // Get all the documentList where requestDate is less than UPDATED_REQUEST_DATE
        defaultDocumentShouldBeFound("requestDate.lessThan=" + UPDATED_REQUEST_DATE);
    }

    @Test
    @Transactional
    public void getAllDocumentsByRequestDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where requestDate is greater than DEFAULT_REQUEST_DATE
        defaultDocumentShouldNotBeFound("requestDate.greaterThan=" + DEFAULT_REQUEST_DATE);

        // Get all the documentList where requestDate is greater than SMALLER_REQUEST_DATE
        defaultDocumentShouldBeFound("requestDate.greaterThan=" + SMALLER_REQUEST_DATE);
    }


    @Test
    @Transactional
    public void getAllDocumentsByUploadDateIsEqualToSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where uploadDate equals to DEFAULT_UPLOAD_DATE
        defaultDocumentShouldBeFound("uploadDate.equals=" + DEFAULT_UPLOAD_DATE);

        // Get all the documentList where uploadDate equals to UPDATED_UPLOAD_DATE
        defaultDocumentShouldNotBeFound("uploadDate.equals=" + UPDATED_UPLOAD_DATE);
    }

    @Test
    @Transactional
    public void getAllDocumentsByUploadDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where uploadDate not equals to DEFAULT_UPLOAD_DATE
        defaultDocumentShouldNotBeFound("uploadDate.notEquals=" + DEFAULT_UPLOAD_DATE);

        // Get all the documentList where uploadDate not equals to UPDATED_UPLOAD_DATE
        defaultDocumentShouldBeFound("uploadDate.notEquals=" + UPDATED_UPLOAD_DATE);
    }

    @Test
    @Transactional
    public void getAllDocumentsByUploadDateIsInShouldWork() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where uploadDate in DEFAULT_UPLOAD_DATE or UPDATED_UPLOAD_DATE
        defaultDocumentShouldBeFound("uploadDate.in=" + DEFAULT_UPLOAD_DATE + "," + UPDATED_UPLOAD_DATE);

        // Get all the documentList where uploadDate equals to UPDATED_UPLOAD_DATE
        defaultDocumentShouldNotBeFound("uploadDate.in=" + UPDATED_UPLOAD_DATE);
    }

    @Test
    @Transactional
    public void getAllDocumentsByUploadDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where uploadDate is not null
        defaultDocumentShouldBeFound("uploadDate.specified=true");

        // Get all the documentList where uploadDate is null
        defaultDocumentShouldNotBeFound("uploadDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllDocumentsByUploadDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where uploadDate is greater than or equal to DEFAULT_UPLOAD_DATE
        defaultDocumentShouldBeFound("uploadDate.greaterThanOrEqual=" + DEFAULT_UPLOAD_DATE);

        // Get all the documentList where uploadDate is greater than or equal to UPDATED_UPLOAD_DATE
        defaultDocumentShouldNotBeFound("uploadDate.greaterThanOrEqual=" + UPDATED_UPLOAD_DATE);
    }

    @Test
    @Transactional
    public void getAllDocumentsByUploadDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where uploadDate is less than or equal to DEFAULT_UPLOAD_DATE
        defaultDocumentShouldBeFound("uploadDate.lessThanOrEqual=" + DEFAULT_UPLOAD_DATE);

        // Get all the documentList where uploadDate is less than or equal to SMALLER_UPLOAD_DATE
        defaultDocumentShouldNotBeFound("uploadDate.lessThanOrEqual=" + SMALLER_UPLOAD_DATE);
    }

    @Test
    @Transactional
    public void getAllDocumentsByUploadDateIsLessThanSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where uploadDate is less than DEFAULT_UPLOAD_DATE
        defaultDocumentShouldNotBeFound("uploadDate.lessThan=" + DEFAULT_UPLOAD_DATE);

        // Get all the documentList where uploadDate is less than UPDATED_UPLOAD_DATE
        defaultDocumentShouldBeFound("uploadDate.lessThan=" + UPDATED_UPLOAD_DATE);
    }

    @Test
    @Transactional
    public void getAllDocumentsByUploadDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where uploadDate is greater than DEFAULT_UPLOAD_DATE
        defaultDocumentShouldNotBeFound("uploadDate.greaterThan=" + DEFAULT_UPLOAD_DATE);

        // Get all the documentList where uploadDate is greater than SMALLER_UPLOAD_DATE
        defaultDocumentShouldBeFound("uploadDate.greaterThan=" + SMALLER_UPLOAD_DATE);
    }


    @Test
    @Transactional
    public void getAllDocumentsByFileUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where fileUrl equals to DEFAULT_FILE_URL
        defaultDocumentShouldBeFound("fileUrl.equals=" + DEFAULT_FILE_URL);

        // Get all the documentList where fileUrl equals to UPDATED_FILE_URL
        defaultDocumentShouldNotBeFound("fileUrl.equals=" + UPDATED_FILE_URL);
    }

    @Test
    @Transactional
    public void getAllDocumentsByFileUrlIsNotEqualToSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where fileUrl not equals to DEFAULT_FILE_URL
        defaultDocumentShouldNotBeFound("fileUrl.notEquals=" + DEFAULT_FILE_URL);

        // Get all the documentList where fileUrl not equals to UPDATED_FILE_URL
        defaultDocumentShouldBeFound("fileUrl.notEquals=" + UPDATED_FILE_URL);
    }

    @Test
    @Transactional
    public void getAllDocumentsByFileUrlIsInShouldWork() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where fileUrl in DEFAULT_FILE_URL or UPDATED_FILE_URL
        defaultDocumentShouldBeFound("fileUrl.in=" + DEFAULT_FILE_URL + "," + UPDATED_FILE_URL);

        // Get all the documentList where fileUrl equals to UPDATED_FILE_URL
        defaultDocumentShouldNotBeFound("fileUrl.in=" + UPDATED_FILE_URL);
    }

    @Test
    @Transactional
    public void getAllDocumentsByFileUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where fileUrl is not null
        defaultDocumentShouldBeFound("fileUrl.specified=true");

        // Get all the documentList where fileUrl is null
        defaultDocumentShouldNotBeFound("fileUrl.specified=false");
    }
                @Test
    @Transactional
    public void getAllDocumentsByFileUrlContainsSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where fileUrl contains DEFAULT_FILE_URL
        defaultDocumentShouldBeFound("fileUrl.contains=" + DEFAULT_FILE_URL);

        // Get all the documentList where fileUrl contains UPDATED_FILE_URL
        defaultDocumentShouldNotBeFound("fileUrl.contains=" + UPDATED_FILE_URL);
    }

    @Test
    @Transactional
    public void getAllDocumentsByFileUrlNotContainsSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where fileUrl does not contain DEFAULT_FILE_URL
        defaultDocumentShouldNotBeFound("fileUrl.doesNotContain=" + DEFAULT_FILE_URL);

        // Get all the documentList where fileUrl does not contain UPDATED_FILE_URL
        defaultDocumentShouldBeFound("fileUrl.doesNotContain=" + UPDATED_FILE_URL);
    }


    @Test
    @Transactional
    public void getAllDocumentsBySentIsEqualToSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where sent equals to DEFAULT_SENT
        defaultDocumentShouldBeFound("sent.equals=" + DEFAULT_SENT);

        // Get all the documentList where sent equals to UPDATED_SENT
        defaultDocumentShouldNotBeFound("sent.equals=" + UPDATED_SENT);
    }

    @Test
    @Transactional
    public void getAllDocumentsBySentIsNotEqualToSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where sent not equals to DEFAULT_SENT
        defaultDocumentShouldNotBeFound("sent.notEquals=" + DEFAULT_SENT);

        // Get all the documentList where sent not equals to UPDATED_SENT
        defaultDocumentShouldBeFound("sent.notEquals=" + UPDATED_SENT);
    }

    @Test
    @Transactional
    public void getAllDocumentsBySentIsInShouldWork() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where sent in DEFAULT_SENT or UPDATED_SENT
        defaultDocumentShouldBeFound("sent.in=" + DEFAULT_SENT + "," + UPDATED_SENT);

        // Get all the documentList where sent equals to UPDATED_SENT
        defaultDocumentShouldNotBeFound("sent.in=" + UPDATED_SENT);
    }

    @Test
    @Transactional
    public void getAllDocumentsBySentIsNullOrNotNull() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where sent is not null
        defaultDocumentShouldBeFound("sent.specified=true");

        // Get all the documentList where sent is null
        defaultDocumentShouldNotBeFound("sent.specified=false");
    }

    @Test
    @Transactional
    public void getAllDocumentsByGroupIsEqualToSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);
        Group group = GroupResourceIT.createEntity(em);
        em.persist(group);
        em.flush();
        document.setGroup(group);
        documentRepository.saveAndFlush(document);
        Long groupId = group.getId();

        // Get all the documentList where group equals to groupId
        defaultDocumentShouldBeFound("groupId.equals=" + groupId);

        // Get all the documentList where group equals to groupId + 1
        defaultDocumentShouldNotBeFound("groupId.equals=" + (groupId + 1));
    }


    @Test
    @Transactional
    public void getAllDocumentsByStudentIsEqualToSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);
        Student student = StudentResourceIT.createEntity(em);
        em.persist(student);
        em.flush();
        document.setStudent(student);
        documentRepository.saveAndFlush(document);
        Long studentId = student.getId();

        // Get all the documentList where student equals to studentId
        defaultDocumentShouldBeFound("studentId.equals=" + studentId);

        // Get all the documentList where student equals to studentId + 1
        defaultDocumentShouldNotBeFound("studentId.equals=" + (studentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDocumentShouldBeFound(String filter) throws Exception {
        restDocumentMockMvc.perform(get("/api/documents?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(document.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].requestDate").value(hasItem(sameInstant(DEFAULT_REQUEST_DATE))))
            .andExpect(jsonPath("$.[*].uploadDate").value(hasItem(sameInstant(DEFAULT_UPLOAD_DATE))))
            .andExpect(jsonPath("$.[*].fileUrl").value(hasItem(DEFAULT_FILE_URL)))
            .andExpect(jsonPath("$.[*].sent").value(hasItem(DEFAULT_SENT.booleanValue())));

        // Check, that the count call also returns 1
        restDocumentMockMvc.perform(get("/api/documents/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDocumentShouldNotBeFound(String filter) throws Exception {
        restDocumentMockMvc.perform(get("/api/documents?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDocumentMockMvc.perform(get("/api/documents/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingDocument() throws Exception {
        // Get the document
        restDocumentMockMvc.perform(get("/api/documents/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDocument() throws Exception {
        // Initialize the database
        documentService.save(document);

        int databaseSizeBeforeUpdate = documentRepository.findAll().size();

        // Update the document
        Document updatedDocument = documentRepository.findById(document.getId()).get();
        // Disconnect from session so that the updates on updatedDocument are not directly saved in db
        em.detach(updatedDocument);
        updatedDocument
            .name(UPDATED_NAME)
            .requestDate(UPDATED_REQUEST_DATE)
            .uploadDate(UPDATED_UPLOAD_DATE)
            .fileUrl(UPDATED_FILE_URL)
            .sent(UPDATED_SENT);

        restDocumentMockMvc.perform(put("/api/documents")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedDocument)))
            .andExpect(status().isOk());

        // Validate the Document in the database
        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeUpdate);
        Document testDocument = documentList.get(documentList.size() - 1);
        assertThat(testDocument.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDocument.getRequestDate()).isEqualTo(UPDATED_REQUEST_DATE);
        assertThat(testDocument.getUploadDate()).isEqualTo(UPDATED_UPLOAD_DATE);
        assertThat(testDocument.getFileUrl()).isEqualTo(UPDATED_FILE_URL);
        assertThat(testDocument.isSent()).isEqualTo(UPDATED_SENT);
    }

    @Test
    @Transactional
    public void updateNonExistingDocument() throws Exception {
        int databaseSizeBeforeUpdate = documentRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentMockMvc.perform(put("/api/documents")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(document)))
            .andExpect(status().isBadRequest());

        // Validate the Document in the database
        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDocument() throws Exception {
        // Initialize the database
        documentService.save(document);

        int databaseSizeBeforeDelete = documentRepository.findAll().size();

        // Delete the document
        restDocumentMockMvc.perform(delete("/api/documents/{id}", document.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
