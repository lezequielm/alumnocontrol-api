package com.softstonesolutions.alumnocontrol.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

import com.softstonesolutions.alumnocontrol.domain.enumeration.ClassType;

/**
 * A ClassMeeting.
 */
@Entity
@Table(name = "class_meeting")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ClassMeeting implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 2, max = 60)
    @Column(name = "name", length = 60)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "class_type", nullable = false)
    private ClassType classType;

    @NotNull
    @Column(name = "date", nullable = false)
    private ZonedDateTime date;

    @OneToMany(mappedBy = "classMeeting")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(mappedBy = "classMeeting")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Assistance> assistances = new HashSet<>();

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

    public ClassMeeting name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ClassType getClassType() {
        return classType;
    }

    public ClassMeeting classType(ClassType classType) {
        this.classType = classType;
        return this;
    }

    public void setClassType(ClassType classType) {
        this.classType = classType;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public ClassMeeting date(ZonedDateTime date) {
        this.date = date;
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public ClassMeeting comments(Set<Comment> comments) {
        this.comments = comments;
        return this;
    }

    public ClassMeeting addComments(Comment comment) {
        this.comments.add(comment);
        comment.setClassMeeting(this);
        return this;
    }

    public ClassMeeting removeComments(Comment comment) {
        this.comments.remove(comment);
        comment.setClassMeeting(null);
        return this;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Set<Assistance> getAssistances() {
        return assistances;
    }

    public ClassMeeting assistances(Set<Assistance> assistances) {
        this.assistances = assistances;
        return this;
    }

    public ClassMeeting addAssistance(Assistance assistance) {
        this.assistances.add(assistance);
        assistance.setClassMeeting(this);
        return this;
    }

    public ClassMeeting removeAssistance(Assistance assistance) {
        this.assistances.remove(assistance);
        assistance.setClassMeeting(null);
        return this;
    }

    public void setAssistances(Set<Assistance> assistances) {
        this.assistances = assistances;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClassMeeting)) {
            return false;
        }
        return id != null && id.equals(((ClassMeeting) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClassMeeting{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", classType='" + getClassType() + "'" +
            ", date='" + getDate() + "'" +
            "}";
    }
}
