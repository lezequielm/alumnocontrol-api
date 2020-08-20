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
 * Criteria class for the {@link com.softstonesolutions.alumnocontrol.domain.Document} entity. This class is used
 * in {@link com.softstonesolutions.alumnocontrol.web.rest.DocumentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /documents?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DocumentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private ZonedDateTimeFilter requestDate;

    private ZonedDateTimeFilter uploadDate;

    private StringFilter fileUrl;

    private BooleanFilter sent;

    private LongFilter groupId;

    private LongFilter studentId;

    public DocumentCriteria() {
    }

    public DocumentCriteria(DocumentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.requestDate = other.requestDate == null ? null : other.requestDate.copy();
        this.uploadDate = other.uploadDate == null ? null : other.uploadDate.copy();
        this.fileUrl = other.fileUrl == null ? null : other.fileUrl.copy();
        this.sent = other.sent == null ? null : other.sent.copy();
        this.groupId = other.groupId == null ? null : other.groupId.copy();
        this.studentId = other.studentId == null ? null : other.studentId.copy();
    }

    @Override
    public DocumentCriteria copy() {
        return new DocumentCriteria(this);
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

    public ZonedDateTimeFilter getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(ZonedDateTimeFilter requestDate) {
        this.requestDate = requestDate;
    }

    public ZonedDateTimeFilter getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(ZonedDateTimeFilter uploadDate) {
        this.uploadDate = uploadDate;
    }

    public StringFilter getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(StringFilter fileUrl) {
        this.fileUrl = fileUrl;
    }

    public BooleanFilter getSent() {
        return sent;
    }

    public void setSent(BooleanFilter sent) {
        this.sent = sent;
    }

    public LongFilter getGroupId() {
        return groupId;
    }

    public void setGroupId(LongFilter groupId) {
        this.groupId = groupId;
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
        final DocumentCriteria that = (DocumentCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(requestDate, that.requestDate) &&
            Objects.equals(uploadDate, that.uploadDate) &&
            Objects.equals(fileUrl, that.fileUrl) &&
            Objects.equals(sent, that.sent) &&
            Objects.equals(groupId, that.groupId) &&
            Objects.equals(studentId, that.studentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        requestDate,
        uploadDate,
        fileUrl,
        sent,
        groupId,
        studentId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (requestDate != null ? "requestDate=" + requestDate + ", " : "") +
                (uploadDate != null ? "uploadDate=" + uploadDate + ", " : "") +
                (fileUrl != null ? "fileUrl=" + fileUrl + ", " : "") +
                (sent != null ? "sent=" + sent + ", " : "") +
                (groupId != null ? "groupId=" + groupId + ", " : "") +
                (studentId != null ? "studentId=" + studentId + ", " : "") +
            "}";
    }

}
