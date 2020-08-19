package com.softstonesolutions.alumnocontrol.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Group.
 */
@Entity
@Table(name = "jhi_group")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Group implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 2, max = 60)
    @Column(name = "name", length = 60, nullable = false)
    private String name;

    @NotNull
    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    @OneToMany(mappedBy = "group")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Document> requestedDocuments = new HashSet<>();

    @OneToMany(mappedBy = "group")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Student> students = new HashSet<>();

    @OneToMany(mappedBy = "group")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Assistance> assistances = new HashSet<>();

    @OneToMany(mappedBy = "group")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<ExtendedUser> users = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "groups", allowSetters = true)
    private Institute institute;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Group name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public Group enabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Set<Document> getRequestedDocuments() {
        return requestedDocuments;
    }

    public Group requestedDocuments(Set<Document> documents) {
        this.requestedDocuments = documents;
        return this;
    }

    public Group addRequestedDocuments(Document document) {
        this.requestedDocuments.add(document);
        document.setGroup(this);
        return this;
    }

    public Group removeRequestedDocuments(Document document) {
        this.requestedDocuments.remove(document);
        document.setGroup(null);
        return this;
    }

    public void setRequestedDocuments(Set<Document> documents) {
        this.requestedDocuments = documents;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public Group students(Set<Student> students) {
        this.students = students;
        return this;
    }

    public Group addStudents(Student student) {
        this.students.add(student);
        student.setGroup(this);
        return this;
    }

    public Group removeStudents(Student student) {
        this.students.remove(student);
        student.setGroup(null);
        return this;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    public Set<Assistance> getAssistances() {
        return assistances;
    }

    public Group assistances(Set<Assistance> assistances) {
        this.assistances = assistances;
        return this;
    }

    public Group addAssistance(Assistance assistance) {
        this.assistances.add(assistance);
        assistance.setGroup(this);
        return this;
    }

    public Group removeAssistance(Assistance assistance) {
        this.assistances.remove(assistance);
        assistance.setGroup(null);
        return this;
    }

    public void setAssistances(Set<Assistance> assistances) {
        this.assistances = assistances;
    }

    public Set<ExtendedUser> getUsers() {
        return users;
    }

    public Group users(Set<ExtendedUser> extendedUsers) {
        this.users = extendedUsers;
        return this;
    }

    public Group addUsers(ExtendedUser extendedUser) {
        this.users.add(extendedUser);
        extendedUser.setGroup(this);
        return this;
    }

    public Group removeUsers(ExtendedUser extendedUser) {
        this.users.remove(extendedUser);
        extendedUser.setGroup(null);
        return this;
    }

    public void setUsers(Set<ExtendedUser> extendedUsers) {
        this.users = extendedUsers;
    }

    public Institute getInstitute() {
        return institute;
    }

    public Group institute(Institute institute) {
        this.institute = institute;
        return this;
    }

    public void setInstitute(Institute institute) {
        this.institute = institute;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Group)) {
            return false;
        }
        return id != null && id.equals(((Group) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Group{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", enabled='" + isEnabled() + "'" +
            "}";
    }
}
