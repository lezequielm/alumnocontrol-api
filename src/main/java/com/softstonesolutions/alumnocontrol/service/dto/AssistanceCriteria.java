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
 * Criteria class for the {@link com.softstonesolutions.alumnocontrol.domain.Assistance} entity. This class is used
 * in {@link com.softstonesolutions.alumnocontrol.web.rest.AssistanceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /assistances?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AssistanceCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BooleanFilter present;

    private BooleanFilter delayed;

    private BooleanFilter justified;

    private LongFilter studentId;

    private LongFilter instituteId;

    private LongFilter classMeetingId;

    private LongFilter groupId;

    public AssistanceCriteria() {
    }

    public AssistanceCriteria(AssistanceCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.present = other.present == null ? null : other.present.copy();
        this.delayed = other.delayed == null ? null : other.delayed.copy();
        this.justified = other.justified == null ? null : other.justified.copy();
        this.studentId = other.studentId == null ? null : other.studentId.copy();
        this.instituteId = other.instituteId == null ? null : other.instituteId.copy();
        this.classMeetingId = other.classMeetingId == null ? null : other.classMeetingId.copy();
        this.groupId = other.groupId == null ? null : other.groupId.copy();
    }

    @Override
    public AssistanceCriteria copy() {
        return new AssistanceCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public BooleanFilter getPresent() {
        return present;
    }

    public void setPresent(BooleanFilter present) {
        this.present = present;
    }

    public BooleanFilter getDelayed() {
        return delayed;
    }

    public void setDelayed(BooleanFilter delayed) {
        this.delayed = delayed;
    }

    public BooleanFilter getJustified() {
        return justified;
    }

    public void setJustified(BooleanFilter justified) {
        this.justified = justified;
    }

    public LongFilter getStudentId() {
        return studentId;
    }

    public void setStudentId(LongFilter studentId) {
        this.studentId = studentId;
    }

    public LongFilter getInstituteId() {
        return instituteId;
    }

    public void setInstituteId(LongFilter instituteId) {
        this.instituteId = instituteId;
    }

    public LongFilter getClassMeetingId() {
        return classMeetingId;
    }

    public void setClassMeetingId(LongFilter classMeetingId) {
        this.classMeetingId = classMeetingId;
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
        final AssistanceCriteria that = (AssistanceCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(present, that.present) &&
            Objects.equals(delayed, that.delayed) &&
            Objects.equals(justified, that.justified) &&
            Objects.equals(studentId, that.studentId) &&
            Objects.equals(instituteId, that.instituteId) &&
            Objects.equals(classMeetingId, that.classMeetingId) &&
            Objects.equals(groupId, that.groupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        present,
        delayed,
        justified,
        studentId,
        instituteId,
        classMeetingId,
        groupId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssistanceCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (present != null ? "present=" + present + ", " : "") +
                (delayed != null ? "delayed=" + delayed + ", " : "") +
                (justified != null ? "justified=" + justified + ", " : "") +
                (studentId != null ? "studentId=" + studentId + ", " : "") +
                (instituteId != null ? "instituteId=" + instituteId + ", " : "") +
                (classMeetingId != null ? "classMeetingId=" + classMeetingId + ", " : "") +
                (groupId != null ? "groupId=" + groupId + ", " : "") +
            "}";
    }

}
