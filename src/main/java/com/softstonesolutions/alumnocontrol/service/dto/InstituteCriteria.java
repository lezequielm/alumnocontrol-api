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
 * Criteria class for the {@link com.softstonesolutions.alumnocontrol.domain.Institute} entity. This class is used
 * in {@link com.softstonesolutions.alumnocontrol.web.rest.InstituteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /institutes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class InstituteCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private BooleanFilter enabled;

    private LongFilter usersId;

    private LongFilter studentsId;

    private LongFilter groupsId;

    private LongFilter assistanceId;

    public InstituteCriteria() {
    }

    public InstituteCriteria(InstituteCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.enabled = other.enabled == null ? null : other.enabled.copy();
        this.usersId = other.usersId == null ? null : other.usersId.copy();
        this.studentsId = other.studentsId == null ? null : other.studentsId.copy();
        this.groupsId = other.groupsId == null ? null : other.groupsId.copy();
        this.assistanceId = other.assistanceId == null ? null : other.assistanceId.copy();
    }

    @Override
    public InstituteCriteria copy() {
        return new InstituteCriteria(this);
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

    public LongFilter getUsersId() {
        return usersId;
    }

    public void setUsersId(LongFilter usersId) {
        this.usersId = usersId;
    }

    public LongFilter getStudentsId() {
        return studentsId;
    }

    public void setStudentsId(LongFilter studentsId) {
        this.studentsId = studentsId;
    }

    public LongFilter getGroupsId() {
        return groupsId;
    }

    public void setGroupsId(LongFilter groupsId) {
        this.groupsId = groupsId;
    }

    public LongFilter getAssistanceId() {
        return assistanceId;
    }

    public void setAssistanceId(LongFilter assistanceId) {
        this.assistanceId = assistanceId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final InstituteCriteria that = (InstituteCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(enabled, that.enabled) &&
            Objects.equals(usersId, that.usersId) &&
            Objects.equals(studentsId, that.studentsId) &&
            Objects.equals(groupsId, that.groupsId) &&
            Objects.equals(assistanceId, that.assistanceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        enabled,
        usersId,
        studentsId,
        groupsId,
        assistanceId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InstituteCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (enabled != null ? "enabled=" + enabled + ", " : "") +
                (usersId != null ? "usersId=" + usersId + ", " : "") +
                (studentsId != null ? "studentsId=" + studentsId + ", " : "") +
                (groupsId != null ? "groupsId=" + groupsId + ", " : "") +
                (assistanceId != null ? "assistanceId=" + assistanceId + ", " : "") +
            "}";
    }

}
