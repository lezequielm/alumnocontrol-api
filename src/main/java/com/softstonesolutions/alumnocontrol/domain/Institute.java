package com.softstonesolutions.alumnocontrol.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Institute.
 */
@Entity
@Table(name = "institute")
public class Institute implements Serializable {

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

    @OneToMany(mappedBy = "institute")
    private Set<ExtendedUser> users = new HashSet<>();

    @OneToMany(mappedBy = "institute")
    private Set<Student> students = new HashSet<>();

    @OneToMany(mappedBy = "institute")
    private Set<Group> groups = new HashSet<>();

    @OneToMany(mappedBy = "institute")
    private Set<Asistence> asistences = new HashSet<>();

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

    public Institute name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public Institute enabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Set<ExtendedUser> getUsers() {
        return users;
    }

    public Institute users(Set<ExtendedUser> extendedUsers) {
        this.users = extendedUsers;
        return this;
    }

    public Institute addUsers(ExtendedUser extendedUser) {
        this.users.add(extendedUser);
        extendedUser.setInstitute(this);
        return this;
    }

    public Institute removeUsers(ExtendedUser extendedUser) {
        this.users.remove(extendedUser);
        extendedUser.setInstitute(null);
        return this;
    }

    public void setUsers(Set<ExtendedUser> extendedUsers) {
        this.users = extendedUsers;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public Institute students(Set<Student> students) {
        this.students = students;
        return this;
    }

    public Institute addStudents(Student student) {
        this.students.add(student);
        student.setInstitute(this);
        return this;
    }

    public Institute removeStudents(Student student) {
        this.students.remove(student);
        student.setInstitute(null);
        return this;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    public Set<Group> getGroups() {
        return groups;
    }

    public Institute groups(Set<Group> groups) {
        this.groups = groups;
        return this;
    }

    public Institute addGroups(Group group) {
        this.groups.add(group);
        group.setInstitute(this);
        return this;
    }

    public Institute removeGroups(Group group) {
        this.groups.remove(group);
        group.setInstitute(null);
        return this;
    }

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }

    public Set<Asistence> getAsistences() {
        return asistences;
    }

    public Institute asistences(Set<Asistence> asistences) {
        this.asistences = asistences;
        return this;
    }

    public Institute addAsistence(Asistence asistence) {
        this.asistences.add(asistence);
        asistence.setInstitute(this);
        return this;
    }

    public Institute removeAsistence(Asistence asistence) {
        this.asistences.remove(asistence);
        asistence.setInstitute(null);
        return this;
    }

    public void setAsistences(Set<Asistence> asistences) {
        this.asistences = asistences;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Institute)) {
            return false;
        }
        return id != null && id.equals(((Institute) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Institute{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", enabled='" + isEnabled() + "'" +
            "}";
    }
}
