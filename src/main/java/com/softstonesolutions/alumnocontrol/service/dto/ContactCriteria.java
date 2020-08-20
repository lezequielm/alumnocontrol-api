package com.softstonesolutions.alumnocontrol.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import com.softstonesolutions.alumnocontrol.domain.enumeration.ContactType;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.softstonesolutions.alumnocontrol.domain.Contact} entity. This class is used
 * in {@link com.softstonesolutions.alumnocontrol.web.rest.ContactResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /contacts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ContactCriteria implements Serializable, Criteria {
    /**
     * Class for filtering ContactType
     */
    public static class ContactTypeFilter extends Filter<ContactType> {

        public ContactTypeFilter() {
        }

        public ContactTypeFilter(ContactTypeFilter filter) {
            super(filter);
        }

        @Override
        public ContactTypeFilter copy() {
            return new ContactTypeFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private ContactTypeFilter contactType;

    private StringFilter data;

    private LongFilter order;

    private LongFilter studentId;

    private LongFilter userId;

    public ContactCriteria() {
    }

    public ContactCriteria(ContactCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.contactType = other.contactType == null ? null : other.contactType.copy();
        this.data = other.data == null ? null : other.data.copy();
        this.order = other.order == null ? null : other.order.copy();
        this.studentId = other.studentId == null ? null : other.studentId.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
    }

    @Override
    public ContactCriteria copy() {
        return new ContactCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public ContactTypeFilter getContactType() {
        return contactType;
    }

    public void setContactType(ContactTypeFilter contactType) {
        this.contactType = contactType;
    }

    public StringFilter getData() {
        return data;
    }

    public void setData(StringFilter data) {
        this.data = data;
    }

    public LongFilter getOrder() {
        return order;
    }

    public void setOrder(LongFilter order) {
        this.order = order;
    }

    public LongFilter getStudentId() {
        return studentId;
    }

    public void setStudentId(LongFilter studentId) {
        this.studentId = studentId;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ContactCriteria that = (ContactCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(contactType, that.contactType) &&
            Objects.equals(data, that.data) &&
            Objects.equals(order, that.order) &&
            Objects.equals(studentId, that.studentId) &&
            Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        contactType,
        data,
        order,
        studentId,
        userId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContactCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (contactType != null ? "contactType=" + contactType + ", " : "") +
                (data != null ? "data=" + data + ", " : "") +
                (order != null ? "order=" + order + ", " : "") +
                (studentId != null ? "studentId=" + studentId + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
            "}";
    }

}
