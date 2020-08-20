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
 * Criteria class for the {@link com.softstonesolutions.alumnocontrol.domain.ExtendedUser} entity. This class is used
 * in {@link com.softstonesolutions.alumnocontrol.web.rest.ExtendedUserResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /extended-users?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ExtendedUserCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter photoUrl;

    private LongFilter userId;

    private LongFilter contactId;

    private LongFilter addressesId;

    private LongFilter instituteId;

    private LongFilter groupId;

    public ExtendedUserCriteria() {
    }

    public ExtendedUserCriteria(ExtendedUserCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.photoUrl = other.photoUrl == null ? null : other.photoUrl.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.contactId = other.contactId == null ? null : other.contactId.copy();
        this.addressesId = other.addressesId == null ? null : other.addressesId.copy();
        this.instituteId = other.instituteId == null ? null : other.instituteId.copy();
        this.groupId = other.groupId == null ? null : other.groupId.copy();
    }

    @Override
    public ExtendedUserCriteria copy() {
        return new ExtendedUserCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(StringFilter photoUrl) {
        this.photoUrl = photoUrl;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getContactId() {
        return contactId;
    }

    public void setContactId(LongFilter contactId) {
        this.contactId = contactId;
    }

    public LongFilter getAddressesId() {
        return addressesId;
    }

    public void setAddressesId(LongFilter addressesId) {
        this.addressesId = addressesId;
    }

    public LongFilter getInstituteId() {
        return instituteId;
    }

    public void setInstituteId(LongFilter instituteId) {
        this.instituteId = instituteId;
    }

    public LongFilter getGroupId() {
        return groupId;
    }

    public void setGroupId(LongFilter groupId) {
        this.groupId = groupId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ExtendedUserCriteria that = (ExtendedUserCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(photoUrl, that.photoUrl) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(contactId, that.contactId) &&
            Objects.equals(addressesId, that.addressesId) &&
            Objects.equals(instituteId, that.instituteId) &&
            Objects.equals(groupId, that.groupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        photoUrl,
        userId,
        contactId,
        addressesId,
        instituteId,
        groupId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExtendedUserCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (photoUrl != null ? "photoUrl=" + photoUrl + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
                (contactId != null ? "contactId=" + contactId + ", " : "") +
                (addressesId != null ? "addressesId=" + addressesId + ", " : "") +
                (instituteId != null ? "instituteId=" + instituteId + ", " : "") +
                (groupId != null ? "groupId=" + groupId + ", " : "") +
            "}";
    }

}
