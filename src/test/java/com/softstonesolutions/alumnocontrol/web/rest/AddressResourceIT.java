package com.softstonesolutions.alumnocontrol.web.rest;

import com.softstonesolutions.alumnocontrol.AlumnocontrolApp;
import com.softstonesolutions.alumnocontrol.domain.Address;
import com.softstonesolutions.alumnocontrol.domain.ExtendedUser;
import com.softstonesolutions.alumnocontrol.domain.Student;
import com.softstonesolutions.alumnocontrol.repository.AddressRepository;
import com.softstonesolutions.alumnocontrol.service.AddressService;
import com.softstonesolutions.alumnocontrol.service.dto.AddressCriteria;
import com.softstonesolutions.alumnocontrol.service.AddressQueryService;

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
 * Integration tests for the {@link AddressResource} REST controller.
 */
@SpringBootTest(classes = AlumnocontrolApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class AddressResourceIT {

    private static final String DEFAULT_STREET = "AAAAAAAAAA";
    private static final String UPDATED_STREET = "BBBBBBBBBB";

    private static final String DEFAULT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_NUMBER = "BBBBBBBBBB";

    private static final Integer DEFAULT_FLAT = 1;
    private static final Integer UPDATED_FLAT = 2;
    private static final Integer SMALLER_FLAT = 1 - 1;

    private static final String DEFAULT_DEPARTMENT = "AAAAAAAAAA";
    private static final String UPDATED_DEPARTMENT = "BBBBBBBBBB";

    private static final String DEFAULT_POSTAL_CODE = "AAAAAAAAAA";
    private static final String UPDATED_POSTAL_CODE = "BBBBBBBBBB";

    private static final Long DEFAULT_ORDER = 1L;
    private static final Long UPDATED_ORDER = 2L;
    private static final Long SMALLER_ORDER = 1L - 1L;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private AddressService addressService;

    @Autowired
    private AddressQueryService addressQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAddressMockMvc;

    private Address address;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Address createEntity(EntityManager em) {
        Address address = new Address()
            .street(DEFAULT_STREET)
            .number(DEFAULT_NUMBER)
            .flat(DEFAULT_FLAT)
            .department(DEFAULT_DEPARTMENT)
            .postalCode(DEFAULT_POSTAL_CODE)
            .order(DEFAULT_ORDER);
        return address;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Address createUpdatedEntity(EntityManager em) {
        Address address = new Address()
            .street(UPDATED_STREET)
            .number(UPDATED_NUMBER)
            .flat(UPDATED_FLAT)
            .department(UPDATED_DEPARTMENT)
            .postalCode(UPDATED_POSTAL_CODE)
            .order(UPDATED_ORDER);
        return address;
    }

    @BeforeEach
    public void initTest() {
        address = createEntity(em);
    }

    @Test
    @Transactional
    public void createAddress() throws Exception {
        int databaseSizeBeforeCreate = addressRepository.findAll().size();
        // Create the Address
        restAddressMockMvc.perform(post("/api/addresses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(address)))
            .andExpect(status().isCreated());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeCreate + 1);
        Address testAddress = addressList.get(addressList.size() - 1);
        assertThat(testAddress.getStreet()).isEqualTo(DEFAULT_STREET);
        assertThat(testAddress.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testAddress.getFlat()).isEqualTo(DEFAULT_FLAT);
        assertThat(testAddress.getDepartment()).isEqualTo(DEFAULT_DEPARTMENT);
        assertThat(testAddress.getPostalCode()).isEqualTo(DEFAULT_POSTAL_CODE);
        assertThat(testAddress.getOrder()).isEqualTo(DEFAULT_ORDER);
    }

    @Test
    @Transactional
    public void createAddressWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = addressRepository.findAll().size();

        // Create the Address with an existing ID
        address.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAddressMockMvc.perform(post("/api/addresses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(address)))
            .andExpect(status().isBadRequest());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkStreetIsRequired() throws Exception {
        int databaseSizeBeforeTest = addressRepository.findAll().size();
        // set the field null
        address.setStreet(null);

        // Create the Address, which fails.


        restAddressMockMvc.perform(post("/api/addresses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(address)))
            .andExpect(status().isBadRequest());

        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = addressRepository.findAll().size();
        // set the field null
        address.setNumber(null);

        // Create the Address, which fails.


        restAddressMockMvc.perform(post("/api/addresses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(address)))
            .andExpect(status().isBadRequest());

        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOrderIsRequired() throws Exception {
        int databaseSizeBeforeTest = addressRepository.findAll().size();
        // set the field null
        address.setOrder(null);

        // Create the Address, which fails.


        restAddressMockMvc.perform(post("/api/addresses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(address)))
            .andExpect(status().isBadRequest());

        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAddresses() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList
        restAddressMockMvc.perform(get("/api/addresses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(address.getId().intValue())))
            .andExpect(jsonPath("$.[*].street").value(hasItem(DEFAULT_STREET)))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].flat").value(hasItem(DEFAULT_FLAT)))
            .andExpect(jsonPath("$.[*].department").value(hasItem(DEFAULT_DEPARTMENT)))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE)))
            .andExpect(jsonPath("$.[*].order").value(hasItem(DEFAULT_ORDER.intValue())));
    }
    
    @Test
    @Transactional
    public void getAddress() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get the address
        restAddressMockMvc.perform(get("/api/addresses/{id}", address.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(address.getId().intValue()))
            .andExpect(jsonPath("$.street").value(DEFAULT_STREET))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER))
            .andExpect(jsonPath("$.flat").value(DEFAULT_FLAT))
            .andExpect(jsonPath("$.department").value(DEFAULT_DEPARTMENT))
            .andExpect(jsonPath("$.postalCode").value(DEFAULT_POSTAL_CODE))
            .andExpect(jsonPath("$.order").value(DEFAULT_ORDER.intValue()));
    }


    @Test
    @Transactional
    public void getAddressesByIdFiltering() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        Long id = address.getId();

        defaultAddressShouldBeFound("id.equals=" + id);
        defaultAddressShouldNotBeFound("id.notEquals=" + id);

        defaultAddressShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAddressShouldNotBeFound("id.greaterThan=" + id);

        defaultAddressShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAddressShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllAddressesByStreetIsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where street equals to DEFAULT_STREET
        defaultAddressShouldBeFound("street.equals=" + DEFAULT_STREET);

        // Get all the addressList where street equals to UPDATED_STREET
        defaultAddressShouldNotBeFound("street.equals=" + UPDATED_STREET);
    }

    @Test
    @Transactional
    public void getAllAddressesByStreetIsNotEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where street not equals to DEFAULT_STREET
        defaultAddressShouldNotBeFound("street.notEquals=" + DEFAULT_STREET);

        // Get all the addressList where street not equals to UPDATED_STREET
        defaultAddressShouldBeFound("street.notEquals=" + UPDATED_STREET);
    }

    @Test
    @Transactional
    public void getAllAddressesByStreetIsInShouldWork() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where street in DEFAULT_STREET or UPDATED_STREET
        defaultAddressShouldBeFound("street.in=" + DEFAULT_STREET + "," + UPDATED_STREET);

        // Get all the addressList where street equals to UPDATED_STREET
        defaultAddressShouldNotBeFound("street.in=" + UPDATED_STREET);
    }

    @Test
    @Transactional
    public void getAllAddressesByStreetIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where street is not null
        defaultAddressShouldBeFound("street.specified=true");

        // Get all the addressList where street is null
        defaultAddressShouldNotBeFound("street.specified=false");
    }
                @Test
    @Transactional
    public void getAllAddressesByStreetContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where street contains DEFAULT_STREET
        defaultAddressShouldBeFound("street.contains=" + DEFAULT_STREET);

        // Get all the addressList where street contains UPDATED_STREET
        defaultAddressShouldNotBeFound("street.contains=" + UPDATED_STREET);
    }

    @Test
    @Transactional
    public void getAllAddressesByStreetNotContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where street does not contain DEFAULT_STREET
        defaultAddressShouldNotBeFound("street.doesNotContain=" + DEFAULT_STREET);

        // Get all the addressList where street does not contain UPDATED_STREET
        defaultAddressShouldBeFound("street.doesNotContain=" + UPDATED_STREET);
    }


    @Test
    @Transactional
    public void getAllAddressesByNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where number equals to DEFAULT_NUMBER
        defaultAddressShouldBeFound("number.equals=" + DEFAULT_NUMBER);

        // Get all the addressList where number equals to UPDATED_NUMBER
        defaultAddressShouldNotBeFound("number.equals=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    public void getAllAddressesByNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where number not equals to DEFAULT_NUMBER
        defaultAddressShouldNotBeFound("number.notEquals=" + DEFAULT_NUMBER);

        // Get all the addressList where number not equals to UPDATED_NUMBER
        defaultAddressShouldBeFound("number.notEquals=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    public void getAllAddressesByNumberIsInShouldWork() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where number in DEFAULT_NUMBER or UPDATED_NUMBER
        defaultAddressShouldBeFound("number.in=" + DEFAULT_NUMBER + "," + UPDATED_NUMBER);

        // Get all the addressList where number equals to UPDATED_NUMBER
        defaultAddressShouldNotBeFound("number.in=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    public void getAllAddressesByNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where number is not null
        defaultAddressShouldBeFound("number.specified=true");

        // Get all the addressList where number is null
        defaultAddressShouldNotBeFound("number.specified=false");
    }
                @Test
    @Transactional
    public void getAllAddressesByNumberContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where number contains DEFAULT_NUMBER
        defaultAddressShouldBeFound("number.contains=" + DEFAULT_NUMBER);

        // Get all the addressList where number contains UPDATED_NUMBER
        defaultAddressShouldNotBeFound("number.contains=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    public void getAllAddressesByNumberNotContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where number does not contain DEFAULT_NUMBER
        defaultAddressShouldNotBeFound("number.doesNotContain=" + DEFAULT_NUMBER);

        // Get all the addressList where number does not contain UPDATED_NUMBER
        defaultAddressShouldBeFound("number.doesNotContain=" + UPDATED_NUMBER);
    }


    @Test
    @Transactional
    public void getAllAddressesByFlatIsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where flat equals to DEFAULT_FLAT
        defaultAddressShouldBeFound("flat.equals=" + DEFAULT_FLAT);

        // Get all the addressList where flat equals to UPDATED_FLAT
        defaultAddressShouldNotBeFound("flat.equals=" + UPDATED_FLAT);
    }

    @Test
    @Transactional
    public void getAllAddressesByFlatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where flat not equals to DEFAULT_FLAT
        defaultAddressShouldNotBeFound("flat.notEquals=" + DEFAULT_FLAT);

        // Get all the addressList where flat not equals to UPDATED_FLAT
        defaultAddressShouldBeFound("flat.notEquals=" + UPDATED_FLAT);
    }

    @Test
    @Transactional
    public void getAllAddressesByFlatIsInShouldWork() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where flat in DEFAULT_FLAT or UPDATED_FLAT
        defaultAddressShouldBeFound("flat.in=" + DEFAULT_FLAT + "," + UPDATED_FLAT);

        // Get all the addressList where flat equals to UPDATED_FLAT
        defaultAddressShouldNotBeFound("flat.in=" + UPDATED_FLAT);
    }

    @Test
    @Transactional
    public void getAllAddressesByFlatIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where flat is not null
        defaultAddressShouldBeFound("flat.specified=true");

        // Get all the addressList where flat is null
        defaultAddressShouldNotBeFound("flat.specified=false");
    }

    @Test
    @Transactional
    public void getAllAddressesByFlatIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where flat is greater than or equal to DEFAULT_FLAT
        defaultAddressShouldBeFound("flat.greaterThanOrEqual=" + DEFAULT_FLAT);

        // Get all the addressList where flat is greater than or equal to UPDATED_FLAT
        defaultAddressShouldNotBeFound("flat.greaterThanOrEqual=" + UPDATED_FLAT);
    }

    @Test
    @Transactional
    public void getAllAddressesByFlatIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where flat is less than or equal to DEFAULT_FLAT
        defaultAddressShouldBeFound("flat.lessThanOrEqual=" + DEFAULT_FLAT);

        // Get all the addressList where flat is less than or equal to SMALLER_FLAT
        defaultAddressShouldNotBeFound("flat.lessThanOrEqual=" + SMALLER_FLAT);
    }

    @Test
    @Transactional
    public void getAllAddressesByFlatIsLessThanSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where flat is less than DEFAULT_FLAT
        defaultAddressShouldNotBeFound("flat.lessThan=" + DEFAULT_FLAT);

        // Get all the addressList where flat is less than UPDATED_FLAT
        defaultAddressShouldBeFound("flat.lessThan=" + UPDATED_FLAT);
    }

    @Test
    @Transactional
    public void getAllAddressesByFlatIsGreaterThanSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where flat is greater than DEFAULT_FLAT
        defaultAddressShouldNotBeFound("flat.greaterThan=" + DEFAULT_FLAT);

        // Get all the addressList where flat is greater than SMALLER_FLAT
        defaultAddressShouldBeFound("flat.greaterThan=" + SMALLER_FLAT);
    }


    @Test
    @Transactional
    public void getAllAddressesByDepartmentIsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where department equals to DEFAULT_DEPARTMENT
        defaultAddressShouldBeFound("department.equals=" + DEFAULT_DEPARTMENT);

        // Get all the addressList where department equals to UPDATED_DEPARTMENT
        defaultAddressShouldNotBeFound("department.equals=" + UPDATED_DEPARTMENT);
    }

    @Test
    @Transactional
    public void getAllAddressesByDepartmentIsNotEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where department not equals to DEFAULT_DEPARTMENT
        defaultAddressShouldNotBeFound("department.notEquals=" + DEFAULT_DEPARTMENT);

        // Get all the addressList where department not equals to UPDATED_DEPARTMENT
        defaultAddressShouldBeFound("department.notEquals=" + UPDATED_DEPARTMENT);
    }

    @Test
    @Transactional
    public void getAllAddressesByDepartmentIsInShouldWork() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where department in DEFAULT_DEPARTMENT or UPDATED_DEPARTMENT
        defaultAddressShouldBeFound("department.in=" + DEFAULT_DEPARTMENT + "," + UPDATED_DEPARTMENT);

        // Get all the addressList where department equals to UPDATED_DEPARTMENT
        defaultAddressShouldNotBeFound("department.in=" + UPDATED_DEPARTMENT);
    }

    @Test
    @Transactional
    public void getAllAddressesByDepartmentIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where department is not null
        defaultAddressShouldBeFound("department.specified=true");

        // Get all the addressList where department is null
        defaultAddressShouldNotBeFound("department.specified=false");
    }
                @Test
    @Transactional
    public void getAllAddressesByDepartmentContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where department contains DEFAULT_DEPARTMENT
        defaultAddressShouldBeFound("department.contains=" + DEFAULT_DEPARTMENT);

        // Get all the addressList where department contains UPDATED_DEPARTMENT
        defaultAddressShouldNotBeFound("department.contains=" + UPDATED_DEPARTMENT);
    }

    @Test
    @Transactional
    public void getAllAddressesByDepartmentNotContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where department does not contain DEFAULT_DEPARTMENT
        defaultAddressShouldNotBeFound("department.doesNotContain=" + DEFAULT_DEPARTMENT);

        // Get all the addressList where department does not contain UPDATED_DEPARTMENT
        defaultAddressShouldBeFound("department.doesNotContain=" + UPDATED_DEPARTMENT);
    }


    @Test
    @Transactional
    public void getAllAddressesByPostalCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where postalCode equals to DEFAULT_POSTAL_CODE
        defaultAddressShouldBeFound("postalCode.equals=" + DEFAULT_POSTAL_CODE);

        // Get all the addressList where postalCode equals to UPDATED_POSTAL_CODE
        defaultAddressShouldNotBeFound("postalCode.equals=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    public void getAllAddressesByPostalCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where postalCode not equals to DEFAULT_POSTAL_CODE
        defaultAddressShouldNotBeFound("postalCode.notEquals=" + DEFAULT_POSTAL_CODE);

        // Get all the addressList where postalCode not equals to UPDATED_POSTAL_CODE
        defaultAddressShouldBeFound("postalCode.notEquals=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    public void getAllAddressesByPostalCodeIsInShouldWork() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where postalCode in DEFAULT_POSTAL_CODE or UPDATED_POSTAL_CODE
        defaultAddressShouldBeFound("postalCode.in=" + DEFAULT_POSTAL_CODE + "," + UPDATED_POSTAL_CODE);

        // Get all the addressList where postalCode equals to UPDATED_POSTAL_CODE
        defaultAddressShouldNotBeFound("postalCode.in=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    public void getAllAddressesByPostalCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where postalCode is not null
        defaultAddressShouldBeFound("postalCode.specified=true");

        // Get all the addressList where postalCode is null
        defaultAddressShouldNotBeFound("postalCode.specified=false");
    }
                @Test
    @Transactional
    public void getAllAddressesByPostalCodeContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where postalCode contains DEFAULT_POSTAL_CODE
        defaultAddressShouldBeFound("postalCode.contains=" + DEFAULT_POSTAL_CODE);

        // Get all the addressList where postalCode contains UPDATED_POSTAL_CODE
        defaultAddressShouldNotBeFound("postalCode.contains=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    public void getAllAddressesByPostalCodeNotContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where postalCode does not contain DEFAULT_POSTAL_CODE
        defaultAddressShouldNotBeFound("postalCode.doesNotContain=" + DEFAULT_POSTAL_CODE);

        // Get all the addressList where postalCode does not contain UPDATED_POSTAL_CODE
        defaultAddressShouldBeFound("postalCode.doesNotContain=" + UPDATED_POSTAL_CODE);
    }


    @Test
    @Transactional
    public void getAllAddressesByOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where order equals to DEFAULT_ORDER
        defaultAddressShouldBeFound("order.equals=" + DEFAULT_ORDER);

        // Get all the addressList where order equals to UPDATED_ORDER
        defaultAddressShouldNotBeFound("order.equals=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    public void getAllAddressesByOrderIsNotEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where order not equals to DEFAULT_ORDER
        defaultAddressShouldNotBeFound("order.notEquals=" + DEFAULT_ORDER);

        // Get all the addressList where order not equals to UPDATED_ORDER
        defaultAddressShouldBeFound("order.notEquals=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    public void getAllAddressesByOrderIsInShouldWork() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where order in DEFAULT_ORDER or UPDATED_ORDER
        defaultAddressShouldBeFound("order.in=" + DEFAULT_ORDER + "," + UPDATED_ORDER);

        // Get all the addressList where order equals to UPDATED_ORDER
        defaultAddressShouldNotBeFound("order.in=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    public void getAllAddressesByOrderIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where order is not null
        defaultAddressShouldBeFound("order.specified=true");

        // Get all the addressList where order is null
        defaultAddressShouldNotBeFound("order.specified=false");
    }

    @Test
    @Transactional
    public void getAllAddressesByOrderIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where order is greater than or equal to DEFAULT_ORDER
        defaultAddressShouldBeFound("order.greaterThanOrEqual=" + DEFAULT_ORDER);

        // Get all the addressList where order is greater than or equal to UPDATED_ORDER
        defaultAddressShouldNotBeFound("order.greaterThanOrEqual=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    public void getAllAddressesByOrderIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where order is less than or equal to DEFAULT_ORDER
        defaultAddressShouldBeFound("order.lessThanOrEqual=" + DEFAULT_ORDER);

        // Get all the addressList where order is less than or equal to SMALLER_ORDER
        defaultAddressShouldNotBeFound("order.lessThanOrEqual=" + SMALLER_ORDER);
    }

    @Test
    @Transactional
    public void getAllAddressesByOrderIsLessThanSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where order is less than DEFAULT_ORDER
        defaultAddressShouldNotBeFound("order.lessThan=" + DEFAULT_ORDER);

        // Get all the addressList where order is less than UPDATED_ORDER
        defaultAddressShouldBeFound("order.lessThan=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    public void getAllAddressesByOrderIsGreaterThanSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where order is greater than DEFAULT_ORDER
        defaultAddressShouldNotBeFound("order.greaterThan=" + DEFAULT_ORDER);

        // Get all the addressList where order is greater than SMALLER_ORDER
        defaultAddressShouldBeFound("order.greaterThan=" + SMALLER_ORDER);
    }


    @Test
    @Transactional
    public void getAllAddressesByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);
        ExtendedUser user = ExtendedUserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        address.setUser(user);
        addressRepository.saveAndFlush(address);
        Long userId = user.getId();

        // Get all the addressList where user equals to userId
        defaultAddressShouldBeFound("userId.equals=" + userId);

        // Get all the addressList where user equals to userId + 1
        defaultAddressShouldNotBeFound("userId.equals=" + (userId + 1));
    }


    @Test
    @Transactional
    public void getAllAddressesByStudentIsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);
        Student student = StudentResourceIT.createEntity(em);
        em.persist(student);
        em.flush();
        address.setStudent(student);
        addressRepository.saveAndFlush(address);
        Long studentId = student.getId();

        // Get all the addressList where student equals to studentId
        defaultAddressShouldBeFound("studentId.equals=" + studentId);

        // Get all the addressList where student equals to studentId + 1
        defaultAddressShouldNotBeFound("studentId.equals=" + (studentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAddressShouldBeFound(String filter) throws Exception {
        restAddressMockMvc.perform(get("/api/addresses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(address.getId().intValue())))
            .andExpect(jsonPath("$.[*].street").value(hasItem(DEFAULT_STREET)))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].flat").value(hasItem(DEFAULT_FLAT)))
            .andExpect(jsonPath("$.[*].department").value(hasItem(DEFAULT_DEPARTMENT)))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE)))
            .andExpect(jsonPath("$.[*].order").value(hasItem(DEFAULT_ORDER.intValue())));

        // Check, that the count call also returns 1
        restAddressMockMvc.perform(get("/api/addresses/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAddressShouldNotBeFound(String filter) throws Exception {
        restAddressMockMvc.perform(get("/api/addresses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAddressMockMvc.perform(get("/api/addresses/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingAddress() throws Exception {
        // Get the address
        restAddressMockMvc.perform(get("/api/addresses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAddress() throws Exception {
        // Initialize the database
        addressService.save(address);

        int databaseSizeBeforeUpdate = addressRepository.findAll().size();

        // Update the address
        Address updatedAddress = addressRepository.findById(address.getId()).get();
        // Disconnect from session so that the updates on updatedAddress are not directly saved in db
        em.detach(updatedAddress);
        updatedAddress
            .street(UPDATED_STREET)
            .number(UPDATED_NUMBER)
            .flat(UPDATED_FLAT)
            .department(UPDATED_DEPARTMENT)
            .postalCode(UPDATED_POSTAL_CODE)
            .order(UPDATED_ORDER);

        restAddressMockMvc.perform(put("/api/addresses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedAddress)))
            .andExpect(status().isOk());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
        Address testAddress = addressList.get(addressList.size() - 1);
        assertThat(testAddress.getStreet()).isEqualTo(UPDATED_STREET);
        assertThat(testAddress.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testAddress.getFlat()).isEqualTo(UPDATED_FLAT);
        assertThat(testAddress.getDepartment()).isEqualTo(UPDATED_DEPARTMENT);
        assertThat(testAddress.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
        assertThat(testAddress.getOrder()).isEqualTo(UPDATED_ORDER);
    }

    @Test
    @Transactional
    public void updateNonExistingAddress() throws Exception {
        int databaseSizeBeforeUpdate = addressRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAddressMockMvc.perform(put("/api/addresses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(address)))
            .andExpect(status().isBadRequest());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAddress() throws Exception {
        // Initialize the database
        addressService.save(address);

        int databaseSizeBeforeDelete = addressRepository.findAll().size();

        // Delete the address
        restAddressMockMvc.perform(delete("/api/addresses/{id}", address.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
