package com.softstonesolutions.alumnocontrol.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A Address.
 */
@Entity
@Table(name = "address")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "street", nullable = false)
    private String street;

    @NotNull
    @Column(name = "number", nullable = false)
    private String number;

    @Column(name = "flat")
    private Integer flat;

    @Column(name = "department")
    private String department;

    @Column(name = "postal_code")
    private String postalCode;

    @NotNull
    @Column(name = "jhi_order", nullable = false)
    private Long order;

    @ManyToOne
    @JsonIgnoreProperties(value = "addresses", allowSetters = true)
    private ExtendedUser user;

    @ManyToOne
    @JsonIgnoreProperties(value = "addresses", allowSetters = true)
    private Student student;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public Address street(String street) {
        this.street = street;
        return this;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public Address number(String number) {
        this.number = number;
        return this;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getFlat() {
        return flat;
    }

    public Address flat(Integer flat) {
        this.flat = flat;
        return this;
    }

    public void setFlat(Integer flat) {
        this.flat = flat;
    }

    public String getDepartment() {
        return department;
    }

    public Address department(String department) {
        this.department = department;
        return this;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public Address postalCode(String postalCode) {
        this.postalCode = postalCode;
        return this;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Long getOrder() {
        return order;
    }

    public Address order(Long order) {
        this.order = order;
        return this;
    }

    public void setOrder(Long order) {
        this.order = order;
    }

    public ExtendedUser getUser() {
        return user;
    }

    public Address user(ExtendedUser extendedUser) {
        this.user = extendedUser;
        return this;
    }

    public void setUser(ExtendedUser extendedUser) {
        this.user = extendedUser;
    }

    public Student getStudent() {
        return student;
    }

    public Address student(Student student) {
        this.student = student;
        return this;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Address)) {
            return false;
        }
        return id != null && id.equals(((Address) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Address{" +
            "id=" + getId() +
            ", street='" + getStreet() + "'" +
            ", number='" + getNumber() + "'" +
            ", flat=" + getFlat() +
            ", department='" + getDepartment() + "'" +
            ", postalCode='" + getPostalCode() + "'" +
            ", order=" + getOrder() +
            "}";
    }
}
