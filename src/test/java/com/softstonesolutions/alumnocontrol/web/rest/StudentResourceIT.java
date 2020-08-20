package com.softstonesolutions.alumnocontrol.web.rest;

import com.softstonesolutions.alumnocontrol.AlumnocontrolApp;
import com.softstonesolutions.alumnocontrol.domain.Student;
import com.softstonesolutions.alumnocontrol.domain.Contact;
import com.softstonesolutions.alumnocontrol.domain.Document;
import com.softstonesolutions.alumnocontrol.domain.Assistance;
import com.softstonesolutions.alumnocontrol.domain.Address;
import com.softstonesolutions.alumnocontrol.domain.Institute;
import com.softstonesolutions.alumnocontrol.domain.Group;
import com.softstonesolutions.alumnocontrol.repository.StudentRepository;
import com.softstonesolutions.alumnocontrol.service.StudentService;
import com.softstonesolutions.alumnocontrol.service.dto.StudentCriteria;
import com.softstonesolutions.alumnocontrol.service.StudentQueryService;

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
 * Integration tests for the {@link StudentResource} REST controller.
 */
@SpringBootTest(classes = AlumnocontrolApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class StudentResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ID_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ID_NUMBER = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_BIRTH_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_BIRTH_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_BIRTH_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Boolean DEFAULT_ENABLED = false;
    private static final Boolean UPDATED_ENABLED = true;

    private static final String DEFAULT_PHOTO_URL = "AAAAAAAAAA";
    private static final String UPDATED_PHOTO_URL = "BBBBBBBBBB";

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentQueryService studentQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStudentMockMvc;

    private Student student;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Student createEntity(EntityManager em) {
        Student student = new Student()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .idNumber(DEFAULT_ID_NUMBER)
            .birthDate(DEFAULT_BIRTH_DATE)
            .enabled(DEFAULT_ENABLED)
            .photoUrl(DEFAULT_PHOTO_URL);
        return student;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Student createUpdatedEntity(EntityManager em) {
        Student student = new Student()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .idNumber(UPDATED_ID_NUMBER)
            .birthDate(UPDATED_BIRTH_DATE)
            .enabled(UPDATED_ENABLED)
            .photoUrl(UPDATED_PHOTO_URL);
        return student;
    }

    @BeforeEach
    public void initTest() {
        student = createEntity(em);
    }

    @Test
    @Transactional
    public void createStudent() throws Exception {
        int databaseSizeBeforeCreate = studentRepository.findAll().size();
        // Create the Student
        restStudentMockMvc.perform(post("/api/students")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(student)))
            .andExpect(status().isCreated());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeCreate + 1);
        Student testStudent = studentList.get(studentList.size() - 1);
        assertThat(testStudent.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testStudent.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testStudent.getIdNumber()).isEqualTo(DEFAULT_ID_NUMBER);
        assertThat(testStudent.getBirthDate()).isEqualTo(DEFAULT_BIRTH_DATE);
        assertThat(testStudent.isEnabled()).isEqualTo(DEFAULT_ENABLED);
        assertThat(testStudent.getPhotoUrl()).isEqualTo(DEFAULT_PHOTO_URL);
    }

    @Test
    @Transactional
    public void createStudentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = studentRepository.findAll().size();

        // Create the Student with an existing ID
        student.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStudentMockMvc.perform(post("/api/students")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(student)))
            .andExpect(status().isBadRequest());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = studentRepository.findAll().size();
        // set the field null
        student.setFirstName(null);

        // Create the Student, which fails.


        restStudentMockMvc.perform(post("/api/students")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(student)))
            .andExpect(status().isBadRequest());

        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = studentRepository.findAll().size();
        // set the field null
        student.setLastName(null);

        // Create the Student, which fails.


        restStudentMockMvc.perform(post("/api/students")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(student)))
            .andExpect(status().isBadRequest());

        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEnabledIsRequired() throws Exception {
        int databaseSizeBeforeTest = studentRepository.findAll().size();
        // set the field null
        student.setEnabled(null);

        // Create the Student, which fails.


        restStudentMockMvc.perform(post("/api/students")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(student)))
            .andExpect(status().isBadRequest());

        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStudents() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList
        restStudentMockMvc.perform(get("/api/students?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(student.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].idNumber").value(hasItem(DEFAULT_ID_NUMBER)))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(sameInstant(DEFAULT_BIRTH_DATE))))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].photoUrl").value(hasItem(DEFAULT_PHOTO_URL)));
    }
    
    @Test
    @Transactional
    public void getStudent() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get the student
        restStudentMockMvc.perform(get("/api/students/{id}", student.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(student.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.idNumber").value(DEFAULT_ID_NUMBER))
            .andExpect(jsonPath("$.birthDate").value(sameInstant(DEFAULT_BIRTH_DATE)))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED.booleanValue()))
            .andExpect(jsonPath("$.photoUrl").value(DEFAULT_PHOTO_URL));
    }


    @Test
    @Transactional
    public void getStudentsByIdFiltering() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        Long id = student.getId();

        defaultStudentShouldBeFound("id.equals=" + id);
        defaultStudentShouldNotBeFound("id.notEquals=" + id);

        defaultStudentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultStudentShouldNotBeFound("id.greaterThan=" + id);

        defaultStudentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultStudentShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllStudentsByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where firstName equals to DEFAULT_FIRST_NAME
        defaultStudentShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the studentList where firstName equals to UPDATED_FIRST_NAME
        defaultStudentShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllStudentsByFirstNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where firstName not equals to DEFAULT_FIRST_NAME
        defaultStudentShouldNotBeFound("firstName.notEquals=" + DEFAULT_FIRST_NAME);

        // Get all the studentList where firstName not equals to UPDATED_FIRST_NAME
        defaultStudentShouldBeFound("firstName.notEquals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllStudentsByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultStudentShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the studentList where firstName equals to UPDATED_FIRST_NAME
        defaultStudentShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllStudentsByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where firstName is not null
        defaultStudentShouldBeFound("firstName.specified=true");

        // Get all the studentList where firstName is null
        defaultStudentShouldNotBeFound("firstName.specified=false");
    }
                @Test
    @Transactional
    public void getAllStudentsByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where firstName contains DEFAULT_FIRST_NAME
        defaultStudentShouldBeFound("firstName.contains=" + DEFAULT_FIRST_NAME);

        // Get all the studentList where firstName contains UPDATED_FIRST_NAME
        defaultStudentShouldNotBeFound("firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllStudentsByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where firstName does not contain DEFAULT_FIRST_NAME
        defaultStudentShouldNotBeFound("firstName.doesNotContain=" + DEFAULT_FIRST_NAME);

        // Get all the studentList where firstName does not contain UPDATED_FIRST_NAME
        defaultStudentShouldBeFound("firstName.doesNotContain=" + UPDATED_FIRST_NAME);
    }


    @Test
    @Transactional
    public void getAllStudentsByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where lastName equals to DEFAULT_LAST_NAME
        defaultStudentShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the studentList where lastName equals to UPDATED_LAST_NAME
        defaultStudentShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllStudentsByLastNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where lastName not equals to DEFAULT_LAST_NAME
        defaultStudentShouldNotBeFound("lastName.notEquals=" + DEFAULT_LAST_NAME);

        // Get all the studentList where lastName not equals to UPDATED_LAST_NAME
        defaultStudentShouldBeFound("lastName.notEquals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllStudentsByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultStudentShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the studentList where lastName equals to UPDATED_LAST_NAME
        defaultStudentShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllStudentsByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where lastName is not null
        defaultStudentShouldBeFound("lastName.specified=true");

        // Get all the studentList where lastName is null
        defaultStudentShouldNotBeFound("lastName.specified=false");
    }
                @Test
    @Transactional
    public void getAllStudentsByLastNameContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where lastName contains DEFAULT_LAST_NAME
        defaultStudentShouldBeFound("lastName.contains=" + DEFAULT_LAST_NAME);

        // Get all the studentList where lastName contains UPDATED_LAST_NAME
        defaultStudentShouldNotBeFound("lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllStudentsByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where lastName does not contain DEFAULT_LAST_NAME
        defaultStudentShouldNotBeFound("lastName.doesNotContain=" + DEFAULT_LAST_NAME);

        // Get all the studentList where lastName does not contain UPDATED_LAST_NAME
        defaultStudentShouldBeFound("lastName.doesNotContain=" + UPDATED_LAST_NAME);
    }


    @Test
    @Transactional
    public void getAllStudentsByIdNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where idNumber equals to DEFAULT_ID_NUMBER
        defaultStudentShouldBeFound("idNumber.equals=" + DEFAULT_ID_NUMBER);

        // Get all the studentList where idNumber equals to UPDATED_ID_NUMBER
        defaultStudentShouldNotBeFound("idNumber.equals=" + UPDATED_ID_NUMBER);
    }

    @Test
    @Transactional
    public void getAllStudentsByIdNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where idNumber not equals to DEFAULT_ID_NUMBER
        defaultStudentShouldNotBeFound("idNumber.notEquals=" + DEFAULT_ID_NUMBER);

        // Get all the studentList where idNumber not equals to UPDATED_ID_NUMBER
        defaultStudentShouldBeFound("idNumber.notEquals=" + UPDATED_ID_NUMBER);
    }

    @Test
    @Transactional
    public void getAllStudentsByIdNumberIsInShouldWork() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where idNumber in DEFAULT_ID_NUMBER or UPDATED_ID_NUMBER
        defaultStudentShouldBeFound("idNumber.in=" + DEFAULT_ID_NUMBER + "," + UPDATED_ID_NUMBER);

        // Get all the studentList where idNumber equals to UPDATED_ID_NUMBER
        defaultStudentShouldNotBeFound("idNumber.in=" + UPDATED_ID_NUMBER);
    }

    @Test
    @Transactional
    public void getAllStudentsByIdNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where idNumber is not null
        defaultStudentShouldBeFound("idNumber.specified=true");

        // Get all the studentList where idNumber is null
        defaultStudentShouldNotBeFound("idNumber.specified=false");
    }
                @Test
    @Transactional
    public void getAllStudentsByIdNumberContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where idNumber contains DEFAULT_ID_NUMBER
        defaultStudentShouldBeFound("idNumber.contains=" + DEFAULT_ID_NUMBER);

        // Get all the studentList where idNumber contains UPDATED_ID_NUMBER
        defaultStudentShouldNotBeFound("idNumber.contains=" + UPDATED_ID_NUMBER);
    }

    @Test
    @Transactional
    public void getAllStudentsByIdNumberNotContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where idNumber does not contain DEFAULT_ID_NUMBER
        defaultStudentShouldNotBeFound("idNumber.doesNotContain=" + DEFAULT_ID_NUMBER);

        // Get all the studentList where idNumber does not contain UPDATED_ID_NUMBER
        defaultStudentShouldBeFound("idNumber.doesNotContain=" + UPDATED_ID_NUMBER);
    }


    @Test
    @Transactional
    public void getAllStudentsByBirthDateIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where birthDate equals to DEFAULT_BIRTH_DATE
        defaultStudentShouldBeFound("birthDate.equals=" + DEFAULT_BIRTH_DATE);

        // Get all the studentList where birthDate equals to UPDATED_BIRTH_DATE
        defaultStudentShouldNotBeFound("birthDate.equals=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    public void getAllStudentsByBirthDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where birthDate not equals to DEFAULT_BIRTH_DATE
        defaultStudentShouldNotBeFound("birthDate.notEquals=" + DEFAULT_BIRTH_DATE);

        // Get all the studentList where birthDate not equals to UPDATED_BIRTH_DATE
        defaultStudentShouldBeFound("birthDate.notEquals=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    public void getAllStudentsByBirthDateIsInShouldWork() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where birthDate in DEFAULT_BIRTH_DATE or UPDATED_BIRTH_DATE
        defaultStudentShouldBeFound("birthDate.in=" + DEFAULT_BIRTH_DATE + "," + UPDATED_BIRTH_DATE);

        // Get all the studentList where birthDate equals to UPDATED_BIRTH_DATE
        defaultStudentShouldNotBeFound("birthDate.in=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    public void getAllStudentsByBirthDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where birthDate is not null
        defaultStudentShouldBeFound("birthDate.specified=true");

        // Get all the studentList where birthDate is null
        defaultStudentShouldNotBeFound("birthDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllStudentsByBirthDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where birthDate is greater than or equal to DEFAULT_BIRTH_DATE
        defaultStudentShouldBeFound("birthDate.greaterThanOrEqual=" + DEFAULT_BIRTH_DATE);

        // Get all the studentList where birthDate is greater than or equal to UPDATED_BIRTH_DATE
        defaultStudentShouldNotBeFound("birthDate.greaterThanOrEqual=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    public void getAllStudentsByBirthDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where birthDate is less than or equal to DEFAULT_BIRTH_DATE
        defaultStudentShouldBeFound("birthDate.lessThanOrEqual=" + DEFAULT_BIRTH_DATE);

        // Get all the studentList where birthDate is less than or equal to SMALLER_BIRTH_DATE
        defaultStudentShouldNotBeFound("birthDate.lessThanOrEqual=" + SMALLER_BIRTH_DATE);
    }

    @Test
    @Transactional
    public void getAllStudentsByBirthDateIsLessThanSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where birthDate is less than DEFAULT_BIRTH_DATE
        defaultStudentShouldNotBeFound("birthDate.lessThan=" + DEFAULT_BIRTH_DATE);

        // Get all the studentList where birthDate is less than UPDATED_BIRTH_DATE
        defaultStudentShouldBeFound("birthDate.lessThan=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    public void getAllStudentsByBirthDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where birthDate is greater than DEFAULT_BIRTH_DATE
        defaultStudentShouldNotBeFound("birthDate.greaterThan=" + DEFAULT_BIRTH_DATE);

        // Get all the studentList where birthDate is greater than SMALLER_BIRTH_DATE
        defaultStudentShouldBeFound("birthDate.greaterThan=" + SMALLER_BIRTH_DATE);
    }


    @Test
    @Transactional
    public void getAllStudentsByEnabledIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where enabled equals to DEFAULT_ENABLED
        defaultStudentShouldBeFound("enabled.equals=" + DEFAULT_ENABLED);

        // Get all the studentList where enabled equals to UPDATED_ENABLED
        defaultStudentShouldNotBeFound("enabled.equals=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    public void getAllStudentsByEnabledIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where enabled not equals to DEFAULT_ENABLED
        defaultStudentShouldNotBeFound("enabled.notEquals=" + DEFAULT_ENABLED);

        // Get all the studentList where enabled not equals to UPDATED_ENABLED
        defaultStudentShouldBeFound("enabled.notEquals=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    public void getAllStudentsByEnabledIsInShouldWork() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where enabled in DEFAULT_ENABLED or UPDATED_ENABLED
        defaultStudentShouldBeFound("enabled.in=" + DEFAULT_ENABLED + "," + UPDATED_ENABLED);

        // Get all the studentList where enabled equals to UPDATED_ENABLED
        defaultStudentShouldNotBeFound("enabled.in=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    public void getAllStudentsByEnabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where enabled is not null
        defaultStudentShouldBeFound("enabled.specified=true");

        // Get all the studentList where enabled is null
        defaultStudentShouldNotBeFound("enabled.specified=false");
    }

    @Test
    @Transactional
    public void getAllStudentsByPhotoUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where photoUrl equals to DEFAULT_PHOTO_URL
        defaultStudentShouldBeFound("photoUrl.equals=" + DEFAULT_PHOTO_URL);

        // Get all the studentList where photoUrl equals to UPDATED_PHOTO_URL
        defaultStudentShouldNotBeFound("photoUrl.equals=" + UPDATED_PHOTO_URL);
    }

    @Test
    @Transactional
    public void getAllStudentsByPhotoUrlIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where photoUrl not equals to DEFAULT_PHOTO_URL
        defaultStudentShouldNotBeFound("photoUrl.notEquals=" + DEFAULT_PHOTO_URL);

        // Get all the studentList where photoUrl not equals to UPDATED_PHOTO_URL
        defaultStudentShouldBeFound("photoUrl.notEquals=" + UPDATED_PHOTO_URL);
    }

    @Test
    @Transactional
    public void getAllStudentsByPhotoUrlIsInShouldWork() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where photoUrl in DEFAULT_PHOTO_URL or UPDATED_PHOTO_URL
        defaultStudentShouldBeFound("photoUrl.in=" + DEFAULT_PHOTO_URL + "," + UPDATED_PHOTO_URL);

        // Get all the studentList where photoUrl equals to UPDATED_PHOTO_URL
        defaultStudentShouldNotBeFound("photoUrl.in=" + UPDATED_PHOTO_URL);
    }

    @Test
    @Transactional
    public void getAllStudentsByPhotoUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where photoUrl is not null
        defaultStudentShouldBeFound("photoUrl.specified=true");

        // Get all the studentList where photoUrl is null
        defaultStudentShouldNotBeFound("photoUrl.specified=false");
    }
                @Test
    @Transactional
    public void getAllStudentsByPhotoUrlContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where photoUrl contains DEFAULT_PHOTO_URL
        defaultStudentShouldBeFound("photoUrl.contains=" + DEFAULT_PHOTO_URL);

        // Get all the studentList where photoUrl contains UPDATED_PHOTO_URL
        defaultStudentShouldNotBeFound("photoUrl.contains=" + UPDATED_PHOTO_URL);
    }

    @Test
    @Transactional
    public void getAllStudentsByPhotoUrlNotContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where photoUrl does not contain DEFAULT_PHOTO_URL
        defaultStudentShouldNotBeFound("photoUrl.doesNotContain=" + DEFAULT_PHOTO_URL);

        // Get all the studentList where photoUrl does not contain UPDATED_PHOTO_URL
        defaultStudentShouldBeFound("photoUrl.doesNotContain=" + UPDATED_PHOTO_URL);
    }


    @Test
    @Transactional
    public void getAllStudentsByContactsIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);
        Contact contacts = ContactResourceIT.createEntity(em);
        em.persist(contacts);
        em.flush();
        student.addContacts(contacts);
        studentRepository.saveAndFlush(student);
        Long contactsId = contacts.getId();

        // Get all the studentList where contacts equals to contactsId
        defaultStudentShouldBeFound("contactsId.equals=" + contactsId);

        // Get all the studentList where contacts equals to contactsId + 1
        defaultStudentShouldNotBeFound("contactsId.equals=" + (contactsId + 1));
    }


    @Test
    @Transactional
    public void getAllStudentsByDocumentsIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);
        Document documents = DocumentResourceIT.createEntity(em);
        em.persist(documents);
        em.flush();
        student.addDocuments(documents);
        studentRepository.saveAndFlush(student);
        Long documentsId = documents.getId();

        // Get all the studentList where documents equals to documentsId
        defaultStudentShouldBeFound("documentsId.equals=" + documentsId);

        // Get all the studentList where documents equals to documentsId + 1
        defaultStudentShouldNotBeFound("documentsId.equals=" + (documentsId + 1));
    }


    @Test
    @Transactional
    public void getAllStudentsByAssistanceIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);
        Assistance assistance = AssistanceResourceIT.createEntity(em);
        em.persist(assistance);
        em.flush();
        student.addAssistance(assistance);
        studentRepository.saveAndFlush(student);
        Long assistanceId = assistance.getId();

        // Get all the studentList where assistance equals to assistanceId
        defaultStudentShouldBeFound("assistanceId.equals=" + assistanceId);

        // Get all the studentList where assistance equals to assistanceId + 1
        defaultStudentShouldNotBeFound("assistanceId.equals=" + (assistanceId + 1));
    }


    @Test
    @Transactional
    public void getAllStudentsByAddressesIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);
        Address addresses = AddressResourceIT.createEntity(em);
        em.persist(addresses);
        em.flush();
        student.addAddresses(addresses);
        studentRepository.saveAndFlush(student);
        Long addressesId = addresses.getId();

        // Get all the studentList where addresses equals to addressesId
        defaultStudentShouldBeFound("addressesId.equals=" + addressesId);

        // Get all the studentList where addresses equals to addressesId + 1
        defaultStudentShouldNotBeFound("addressesId.equals=" + (addressesId + 1));
    }


    @Test
    @Transactional
    public void getAllStudentsByInstituteIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);
        Institute institute = InstituteResourceIT.createEntity(em);
        em.persist(institute);
        em.flush();
        student.setInstitute(institute);
        studentRepository.saveAndFlush(student);
        Long instituteId = institute.getId();

        // Get all the studentList where institute equals to instituteId
        defaultStudentShouldBeFound("instituteId.equals=" + instituteId);

        // Get all the studentList where institute equals to instituteId + 1
        defaultStudentShouldNotBeFound("instituteId.equals=" + (instituteId + 1));
    }


    @Test
    @Transactional
    public void getAllStudentsByGroupIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);
        Group group = GroupResourceIT.createEntity(em);
        em.persist(group);
        em.flush();
        student.setGroup(group);
        studentRepository.saveAndFlush(student);
        Long groupId = group.getId();

        // Get all the studentList where group equals to groupId
        defaultStudentShouldBeFound("groupId.equals=" + groupId);

        // Get all the studentList where group equals to groupId + 1
        defaultStudentShouldNotBeFound("groupId.equals=" + (groupId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultStudentShouldBeFound(String filter) throws Exception {
        restStudentMockMvc.perform(get("/api/students?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(student.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].idNumber").value(hasItem(DEFAULT_ID_NUMBER)))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(sameInstant(DEFAULT_BIRTH_DATE))))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].photoUrl").value(hasItem(DEFAULT_PHOTO_URL)));

        // Check, that the count call also returns 1
        restStudentMockMvc.perform(get("/api/students/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultStudentShouldNotBeFound(String filter) throws Exception {
        restStudentMockMvc.perform(get("/api/students?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restStudentMockMvc.perform(get("/api/students/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingStudent() throws Exception {
        // Get the student
        restStudentMockMvc.perform(get("/api/students/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStudent() throws Exception {
        // Initialize the database
        studentService.save(student);

        int databaseSizeBeforeUpdate = studentRepository.findAll().size();

        // Update the student
        Student updatedStudent = studentRepository.findById(student.getId()).get();
        // Disconnect from session so that the updates on updatedStudent are not directly saved in db
        em.detach(updatedStudent);
        updatedStudent
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .idNumber(UPDATED_ID_NUMBER)
            .birthDate(UPDATED_BIRTH_DATE)
            .enabled(UPDATED_ENABLED)
            .photoUrl(UPDATED_PHOTO_URL);

        restStudentMockMvc.perform(put("/api/students")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedStudent)))
            .andExpect(status().isOk());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeUpdate);
        Student testStudent = studentList.get(studentList.size() - 1);
        assertThat(testStudent.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testStudent.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testStudent.getIdNumber()).isEqualTo(UPDATED_ID_NUMBER);
        assertThat(testStudent.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
        assertThat(testStudent.isEnabled()).isEqualTo(UPDATED_ENABLED);
        assertThat(testStudent.getPhotoUrl()).isEqualTo(UPDATED_PHOTO_URL);
    }

    @Test
    @Transactional
    public void updateNonExistingStudent() throws Exception {
        int databaseSizeBeforeUpdate = studentRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudentMockMvc.perform(put("/api/students")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(student)))
            .andExpect(status().isBadRequest());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteStudent() throws Exception {
        // Initialize the database
        studentService.save(student);

        int databaseSizeBeforeDelete = studentRepository.findAll().size();

        // Delete the student
        restStudentMockMvc.perform(delete("/api/students/{id}", student.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
