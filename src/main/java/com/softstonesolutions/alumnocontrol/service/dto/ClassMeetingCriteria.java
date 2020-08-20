package com.softstonesolutions.alumnocontrol.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import com.softstonesolutions.alumnocontrol.domain.enumeration.ClassType;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link com.softstonesolutions.alumnocontrol.domain.ClassMeeting} entity. This class is used
 * in {@link com.softstonesolutions.alumnocontrol.web.rest.ClassMeetingResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /class-meetings?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ClassMeetingCriteria implements Serializable, Criteria {
    /**
     * Class for filtering ClassType
     */
    public static class ClassTypeFilter extends Filter<ClassType> {

        public ClassTypeFilter() {
        }

        public ClassTypeFilter(ClassTypeFilter filter) {
            super(filter);
        }

        @Override
        public ClassTypeFilter copy() {
            return new ClassTypeFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private ClassTypeFilter classType;

    private ZonedDateTimeFilter date;

    private LongFilter commentsId;

    private LongFilter assistanceId;

    public ClassMeetingCriteria() {
    }

    public ClassMeetingCriteria(ClassMeetingCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.classType = other.classType == null ? null : other.classType.copy();
        this.date = other.date == null ? null : other.date.copy();
        this.commentsId = other.commentsId == null ? null : other.commentsId.copy();
        this.assistanceId = other.assistanceId == null ? null : other.assistanceId.copy();
    }

    @Override
    public ClassMeetingCriteria copy() {
        return new ClassMeetingCriteria(this);
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

    public ClassTypeFilter getClassType() {
        return classType;
    }

    public void setClassType(ClassTypeFilter classType) {
        this.classType = classType;
    }

    public ZonedDateTimeFilter getDate() {
        return date;
    }

    public void setDate(ZonedDateTimeFilter date) {
        this.date = date;
    }

    public LongFilter getCommentsId() {
        return commentsId;
    }

    public void setCommentsId(LongFilter commentsId) {
        this.commentsId = commentsId;
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
        final ClassMeetingCriteria that = (ClassMeetingCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(classType, that.classType) &&
            Objects.equals(date, that.date) &&
            Objects.equals(commentsId, that.commentsId) &&
            Objects.equals(assistanceId, that.assistanceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        classType,
        date,
        commentsId,
        assistanceId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClassMeetingCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (classType != null ? "classType=" + classType + ", " : "") +
                (date != null ? "date=" + date + ", " : "") +
                (commentsId != null ? "commentsId=" + commentsId + ", " : "") +
                (assistanceId != null ? "assistanceId=" + assistanceId + ", " : "") +
            "}";
    }

}
