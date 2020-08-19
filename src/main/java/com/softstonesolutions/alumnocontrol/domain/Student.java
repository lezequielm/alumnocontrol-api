package com.softstonesolutions.alumnocontrol.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * A Student.
 */
@Entity
@Table(name = "student")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 2, max = 60)
    @Column(name = "first_name", length = 60, nullable = false)
    private String firstName;

    @NotNull
    @Size(min = 2, max = 60)
    @Column(name = "last_name", length = 60, nullable = false)
    private String lastName;

    @Size(min = 6, max = 20)
    @Column(name = "id_number", length = 20)
    private String idNumber;

    @Column(name = "birth_date")
    private ZonedDateTime birthDate;

    @NotNull
    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    @Column(name = "photo_url")
    private String photoUrl;

    @OneToMany(mappedBy = "student")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Contact> contacts = new HashSet<>();

    @OneToMany(mappedBy = "student")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Document> documents = new HashSet<>();

    @OneToMany(mappedBy = "student")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Assistance> assistances = new HashSet<>();

    @OneToMany(mappedBy = "student")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Address> addresses = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "students", allowSetters = true)
    private Institute institute;

    @ManyToOne
    @JsonIgnoreProperties(value = "students", allowSetters = true)
    private Group group;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public Student firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Student lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public Student idNumber(String idNumber) {
        this.idNumber = idNumber;
        return this;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public ZonedDateTime getBirthDate() {
        return birthDate;
    }

    public Student birthDate(ZonedDateTime birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public void setBirthDate(ZonedDateTime birthDate) {
        this.birthDate = birthDate;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public Student enabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public Student photoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
        return this;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Set<Contact> getContacts() {
        return contacts;
    }

    public Student contacts(Set<Contact> contacts) {
        this.contacts = contacts;
        return this;
    }

    public Student addContacts(Contact contact) {
        this.contacts.add(contact);
        contact.setStudent(this);
        return this;
    }

    public Student removeContacts(Contact contact) {
        this.contacts.remove(contact);
        contact.setStudent(null);
        return this;
    }

    public void setContacts(Set<Contact> contacts) {
        this.contacts = contacts;
    }

    public Set<Document> getDocuments() {
        return documents;
    }

    public Student documents(Set<Document> documents) {
        this.documents = documents;
        return this;
    }

    public Student addDocuments(Document document) {
        this.documents.add(document);
        document.setStudent(this);
        return this;
    }

    public Student removeDocuments(Document document) {
        this.documents.remove(document);
        document.setStudent(null);
        return this;
    }

    public void setDocuments(Set<Document> documents) {
        this.documents = documents;
    }

    public Set<Assistance> getAssistances() {
        return assistances;
    }

    public Student assistances(Set<Assistance> assistances) {
        this.assistances = assistances;
        return this;
    }

    public Student addAssistance(Assistance assistance) {
        this.assistances.add(assistance);
        assistance.setStudent(this);
        return this;
    }

    public Student removeAssistance(Assistance assistance) {
        this.assistances.remove(assistance);
        assistance.setStudent(null);
        return this;
    }

    public void setAssistances(Set<Assistance> assistances) {
        this.assistances = assistances;
    }

    public Set<Address> getAddresses() {
        return addresses;
    }

    public Student addresses(Set<Address> addresses) {
        this.addresses = addresses;
        return this;
    }

    public Student addAddresses(Address address) {
        this.addresses.add(address);
        address.setStudent(this);
        return this;
    }

    public Student removeAddresses(Address address) {
        this.addresses.remove(address);
        address.setStudent(null);
        return this;
    }

    public void setAddresses(Set<Address> addresses) {
        this.addresses = addresses;
    }

    public Institute getInstitute() {
        return institute;
    }

    public Student institute(Institute institute) {
        this.institute = institute;
        return this;
    }

    public void setInstitute(Institute institute) {
        this.institute = institute;
    }

    public Group getGroup() {
        return group;
    }

    public Student group(Group group) {
        this.group = group;
        return this;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Student)) {
            return false;
        }
        return id != null && id.equals(((Student) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Student{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", idNumber='" + getIdNumber() + "'" +
            ", birthDate='" + getBirthDate() + "'" +
            ", enabled='" + isEnabled() + "'" +
            ", photoUrl='" + getPhotoUrl() + "'" +
            "}";
    }
}
