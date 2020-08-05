package com.softstonesolutions.alumnocontrol.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A Asistence.
 */
@Entity
@Table(name = "asistence")
public class Asistence implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "present", nullable = false)
    private Boolean present;

    @Column(name = "jhi_delayed")
    private Boolean delayed;

    @Column(name = "justified")
    private Boolean justified;

    @Lob
    @Column(name = "justification")
    private String justification;

    @ManyToOne
    @JsonIgnoreProperties(value = "asistences", allowSetters = true)
    private Student student;

    @ManyToOne
    @JsonIgnoreProperties(value = "asistences", allowSetters = true)
    private Institute institute;

    @ManyToOne
    @JsonIgnoreProperties(value = "asistences", allowSetters = true)
    private ClassMeeting classMeeting;

    @ManyToOne
    @JsonIgnoreProperties(value = "asistences", allowSetters = true)
    private Group group;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isPresent() {
        return present;
    }

    public Asistence present(Boolean present) {
        this.present = present;
        return this;
    }

    public void setPresent(Boolean present) {
        this.present = present;
    }

    public Boolean isDelayed() {
        return delayed;
    }

    public Asistence delayed(Boolean delayed) {
        this.delayed = delayed;
        return this;
    }

    public void setDelayed(Boolean delayed) {
        this.delayed = delayed;
    }

    public Boolean isJustified() {
        return justified;
    }

    public Asistence justified(Boolean justified) {
        this.justified = justified;
        return this;
    }

    public void setJustified(Boolean justified) {
        this.justified = justified;
    }

    public String getJustification() {
        return justification;
    }

    public Asistence justification(String justification) {
        this.justification = justification;
        return this;
    }

    public void setJustification(String justification) {
        this.justification = justification;
    }

    public Student getStudent() {
        return student;
    }

    public Asistence student(Student student) {
        this.student = student;
        return this;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Institute getInstitute() {
        return institute;
    }

    public Asistence institute(Institute institute) {
        this.institute = institute;
        return this;
    }

    public void setInstitute(Institute institute) {
        this.institute = institute;
    }

    public ClassMeeting getClassMeeting() {
        return classMeeting;
    }

    public Asistence classMeeting(ClassMeeting classMeeting) {
        this.classMeeting = classMeeting;
        return this;
    }

    public void setClassMeeting(ClassMeeting classMeeting) {
        this.classMeeting = classMeeting;
    }

    public Group getGroup() {
        return group;
    }

    public Asistence group(Group group) {
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
        if (!(o instanceof Asistence)) {
            return false;
        }
        return id != null && id.equals(((Asistence) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Asistence{" +
            "id=" + getId() +
            ", present='" + isPresent() + "'" +
            ", delayed='" + isDelayed() + "'" +
            ", justified='" + isJustified() + "'" +
            ", justification='" + getJustification() + "'" +
            "}";
    }
}
