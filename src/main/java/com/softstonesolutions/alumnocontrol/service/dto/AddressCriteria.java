package com.softstonesolutions.alumnocontrol.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.softstonesolutions.alumnocontrol.domain.Address} entity. This class is used
 * in {@link com.softstonesolutions.alumnocontrol.web.rest.AddressResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /addresses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AddressCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter street;

    private StringFilter number;

    private IntegerFilter flat;

    private StringFilter department;

    private StringFilter postalCode;

    private LongFilter order;

    private LongFilter userId;

    private LongFilter studentId;

    public AddressCriteria() {
    }

    public AddressCriteria(AddressCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.street = other.street == null ? null : other.street.copy();
        this.number = other.number == null ? null : other.number.copy();
        this.flat = other.flat == null ? null : other.flat.copy();
        this.department = other.department == null ? null : other.department.copy();
        this.postalCode = other.postalCode == null ? null : other.postalCode.copy();
        this.order = other.order == null ? null : other.order.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.studentId = other.studentId == null ? null : other.studentId.copy();
    }

    @Override
    public AddressCriteria copy() {
        return new AddressCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getStreet() {
        return street;
    }

    public void setStreet(StringFilter street) {
        this.street = street;
    }

    public StringFilter getNumber() {
        return number;
    }

    public void setNumber(StringFilter number) {
        this.number = number;
    }

    public IntegerFilter getFlat() {
        return flat;
    }

    public void setFlat(IntegerFilter flat) {
        this.flat = flat;
    }

    public StringFilter getDepartment() {
        return department;
    }

    public void setDepartment(StringFilter department) {
        this.department = department;
    }

    public StringFilter getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(StringFilter postalCode) {
        this.postalCode = postalCode;
    }

    public LongFilter getOrder() {
        return order;
    }

    public void setOrder(LongFilter order) {
        this.order = order;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getStudentId() {
        return studentId;
    }

    public void setStudentId(LongFilter studentId) {
        this.studentId = studentId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AddressCriteria that = (AddressCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(street, that.street) &&
            Objects.equals(number, that.number) &&
            Objects.equals(flat, that.flat) &&
            Objects.equals(department, that.department) &&
            Objects.equals(postalCode, that.postalCode) &&
            Objects.equals(order, that.order) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(studentId, that.studentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        street,
        number,
        flat,
        department,
        postalCode,
        order,
        userId,
        studentId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AddressCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (street != null ? "street=" + street + ", " : "") +
                (number != null ? "number=" + number + ", " : "") +
                (flat != null ? "flat=" + flat + ", " : "") +
                (department != null ? "department=" + department + ", " : "") +
                (postalCode != null ? "postalCode=" + postalCode + ", " : "") +
                (order != null ? "order=" + order + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
                (studentId != null ? "studentId=" + studentId + ", " : "") +
            "}";
    }

}
