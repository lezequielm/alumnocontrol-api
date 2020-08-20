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
import io.github.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link com.softstonesolutions.alumnocontrol.domain.Student} entity. This class is used
 * in {@link com.softstonesolutions.alumnocontrol.web.rest.StudentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /students?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class StudentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter firstName;

    private StringFilter lastName;

    private StringFilter idNumber;

    private ZonedDateTimeFilter birthDate;

    private BooleanFilter enabled;

    private StringFilter photoUrl;

    private LongFilter contactsId;

    private LongFilter documentsId;

    private LongFilter assistanceId;

    private LongFilter addressesId;

    private LongFilter instituteId;

    private LongFilter groupId;

    public StudentCriteria() {
    }

    public StudentCriteria(StudentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.firstName = other.firstName == null ? null : other.firstName.copy();
        this.lastName = other.lastName == null ? null : other.lastName.copy();
        this.idNumber = other.idNumber == null ? null : other.idNumber.copy();
        this.birthDate = other.birthDate == null ? null : other.birthDate.copy();
        this.enabled = other.enabled == null ? null : other.enabled.copy();
        this.photoUrl = other.photoUrl == null ? null : other.photoUrl.copy();
        this.contactsId = other.contactsId == null ? null : other.contactsId.copy();
        this.documentsId = other.documentsId == null ? null : other.documentsId.copy();
        this.assistanceId = other.assistanceId == null ? null : other.assistanceId.copy();
        this.addressesId = other.addressesId == null ? null : other.addressesId.copy();
        this.instituteId = other.instituteId == null ? null : other.instituteId.copy();
        this.groupId = other.groupId == null ? null : other.groupId.copy();
    }

    @Override
    public StudentCriteria copy() {
        return new StudentCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getFirstName() {
        return firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public StringFilter getLastName() {
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public StringFilter getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(StringFilter idNumber) {
        this.idNumber = idNumber;
    }

    public ZonedDateTimeFilter getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(ZonedDateTimeFilter birthDate) {
        this.birthDate = birthDate;
    }

    public BooleanFilter getEnabled() {
        return enabled;
    }

    public void setEnabled(BooleanFilter enabled) {
        this.enabled = enabled;
    }

    public StringFilter getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(StringFilter photoUrl) {
        this.photoUrl = photoUrl;
    }

    public LongFilter getContactsId() {
        return contactsId;
    }

    public void setContactsId(LongFilter contactsId) {
        this.contactsId = contactsId;
    }

    public LongFilter getDocumentsId() {
        return documentsId;
    }

    public void setDocumentsId(LongFilter documentsId) {
        this.documentsId = documentsId;
    }

    public LongFilter getAssistanceId() {
        return assistanceId;
    }

    public void setAssistanceId(LongFilter assistanceId) {
        this.assistanceId = assistanceId;
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
        final StudentCriteria that = (StudentCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(idNumber, that.idNumber) &&
            Objects.equals(birthDate, that.birthDate) &&
            Objects.equals(enabled, that.enabled) &&
            Objects.equals(photoUrl, that.photoUrl) &&
            Objects.equals(contactsId, that.contactsId) &&
            Objects.equals(documentsId, that.documentsId) &&
            Objects.equals(assistanceId, that.assistanceId) &&
            Objects.equals(addressesId, that.addressesId) &&
            Objects.equals(instituteId, that.instituteId) &&
            Objects.equals(groupId, that.groupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        firstName,
        lastName,
        idNumber,
        birthDate,
        enabled,
        photoUrl,
        contactsId,
        documentsId,
        assistanceId,
        addressesId,
        instituteId,
        groupId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StudentCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (firstName != null ? "firstName=" + firstName + ", " : "") +
                (lastName != null ? "lastName=" + lastName + ", " : "") +
                (idNumber != null ? "idNumber=" + idNumber + ", " : "") +
                (birthDate != null ? "birthDate=" + birthDate + ", " : "") +
                (enabled != null ? "enabled=" + enabled + ", " : "") +
                (photoUrl != null ? "photoUrl=" + photoUrl + ", " : "") +
                (contactsId != null ? "contactsId=" + contactsId + ", " : "") +
                (documentsId != null ? "documentsId=" + documentsId + ", " : "") +
                (assistanceId != null ? "assistanceId=" + assistanceId + ", " : "") +
                (addressesId != null ? "addressesId=" + addressesId + ", " : "") +
                (instituteId != null ? "instituteId=" + instituteId + ", " : "") +
                (groupId != null ? "groupId=" + groupId + ", " : "") +
            "}";
    }

}
