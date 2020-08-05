package com.softstonesolutions.alumnocontrol.domain;


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
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(mappedBy = "classMeeting")
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

    public Set<Asistence> getAsistences() {
        return asistences;
    }

    public ClassMeeting asistences(Set<Asistence> asistences) {
        this.asistences = asistences;
        return this;
    }

    public ClassMeeting addAsistence(Asistence asistence) {
        this.asistences.add(asistence);
        asistence.setClassMeeting(this);
        return this;
    }

    public ClassMeeting removeAsistence(Asistence asistence) {
        this.asistences.remove(asistence);
        asistence.setClassMeeting(null);
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
