package com.softstonesolutions.alumnocontrol.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

import com.softstonesolutions.alumnocontrol.domain.enumeration.ContactType;

/**
 * A Contact.
 */
@Entity
@Table(name = "contact")
public class Contact implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "contact_type", nullable = false)
    private ContactType contactType;

    @NotNull
    @Size(min = 2, max = 60)
    @Column(name = "data", length = 60, nullable = false)
    private String data;

    @NotNull
    @Column(name = "jhi_order", nullable = false)
    private Long order;

    @ManyToOne
    @JsonIgnoreProperties(value = "contacts", allowSetters = true)
    private Student student;

    @ManyToOne
    @JsonIgnoreProperties(value = "contacts", allowSetters = true)
    private ExtendedUser user;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ContactType getContactType() {
        return contactType;
    }

    public Contact contactType(ContactType contactType) {
        this.contactType = contactType;
        return this;
    }

    public void setContactType(ContactType contactType) {
        this.contactType = contactType;
    }

    public String getData() {
        return data;
    }

    public Contact data(String data) {
        this.data = data;
        return this;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Long getOrder() {
        return order;
    }

    public Contact order(Long order) {
        this.order = order;
        return this;
    }

    public void setOrder(Long order) {
        this.order = order;
    }

    public Student getStudent() {
        return student;
    }

    public Contact student(Student student) {
        this.student = student;
        return this;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public ExtendedUser getUser() {
        return user;
    }

    public Contact user(ExtendedUser extendedUser) {
        this.user = extendedUser;
        return this;
    }

    public void setUser(ExtendedUser extendedUser) {
        this.user = extendedUser;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Contact)) {
            return false;
        }
        return id != null && id.equals(((Contact) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Contact{" +
            "id=" + getId() +
            ", contactType='" + getContactType() + "'" +
            ", data='" + getData() + "'" +
            ", order=" + getOrder() +
            "}";
    }
}
