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
 * Criteria class for the {@link com.softstonesolutions.alumnocontrol.domain.Group} entity. This class is used
 * in {@link com.softstonesolutions.alumnocontrol.web.rest.GroupResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /groups?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class GroupCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private BooleanFilter enabled;

    private LongFilter requestedDocumentsId;

    private LongFilter studentsId;

    private LongFilter assistanceId;

    private LongFilter usersId;

    private LongFilter instituteId;

    public GroupCriteria() {
    }

    public GroupCriteria(GroupCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.enabled = other.enabled == null ? null : other.enabled.copy();
        this.requestedDocumentsId = other.requestedDocumentsId == null ? null : other.requestedDocumentsId.copy();
        this.studentsId = other.studentsId == null ? null : other.studentsId.copy();
        this.assistanceId = other.assistanceId == null ? null : other.assistanceId.copy();
        this.usersId = other.usersId == null ? null : other.usersId.copy();
        this.instituteId = other.instituteId == null ? null : other.instituteId.copy();
    }

    @Override
    public GroupCriteria copy() {
        return new GroupCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public BooleanFilter getEnabled() {
        return enabled;
    }

    public void setEnabled(BooleanFilter enabled) {
        this.enabled = enabled;
    }

    public LongFilter getRequestedDocumentsId() {
        return requestedDocumentsId;
    }

    public void setRequestedDocumentsId(LongFilter requestedDocumentsId) {
        this.requestedDocumentsId = requestedDocumentsId;
    }

    public LongFilter getStudentsId() {
        return studentsId;
    }

    public void setStudentsId(LongFilter studentsId) {
        this.studentsId = studentsId;
    }

    public LongFilter getAssistanceId() {
        return assistanceId;
    }

    public void setAssistanceId(LongFilter assistanceId) {
        this.assistanceId = assistanceId;
    }

    public LongFilter getUsersId() {
        return usersId;
    }

    public void setUsersId(LongFilter usersId) {
        this.usersId = usersId;
    }

    public LongFilter getInstituteId() {
        return instituteId;
    }

    public void setInstituteId(LongFilter instituteId) {
        this.instituteId = instituteId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final GroupCriteria that = (GroupCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(enabled, that.enabled) &&
            Objects.equals(requestedDocumentsId, that.requestedDocumentsId) &&
            Objects.equals(studentsId, that.studentsId) &&
            Objects.equals(assistanceId, that.assistanceId) &&
            Objects.equals(usersId, that.usersId) &&
            Objects.equals(instituteId, that.instituteId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        enabled,
        requestedDocumentsId,
        studentsId,
        assistanceId,
        usersId,
        instituteId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GroupCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (enabled != null ? "enabled=" + enabled + ", " : "") +
                (requestedDocumentsId != null ? "requestedDocumentsId=" + requestedDocumentsId + ", " : "") +
                (studentsId != null ? "studentsId=" + studentsId + ", " : "") +
                (assistanceId != null ? "assistanceId=" + assistanceId + ", " : "") +
                (usersId != null ? "usersId=" + usersId + ", " : "") +
                (instituteId != null ? "instituteId=" + instituteId + ", " : "") +
            "}";
    }

}
