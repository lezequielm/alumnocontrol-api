package com.softstonesolutions.alumnocontrol.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A Document.
 */
@Entity
@Table(name = "document")
public class Document implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "request_date")
    private ZonedDateTime requestDate;

    @Column(name = "upload_date")
    private ZonedDateTime uploadDate;

    @NotNull
    @Column(name = "file_url", nullable = false)
    private String fileUrl;

    @NotNull
    @Column(name = "sent", nullable = false)
    private Boolean sent;

    @ManyToOne
    @JsonIgnoreProperties(value = "requestedDocuments", allowSetters = true)
    private Group group;

    @ManyToOne
    @JsonIgnoreProperties(value = "documents", allowSetters = true)
    private Student student;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Document name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ZonedDateTime getRequestDate() {
        return requestDate;
    }

    public Document requestDate(ZonedDateTime requestDate) {
        this.requestDate = requestDate;
        return this;
    }

    public void setRequestDate(ZonedDateTime requestDate) {
        this.requestDate = requestDate;
    }

    public ZonedDateTime getUploadDate() {
        return uploadDate;
    }

    public Document uploadDate(ZonedDateTime uploadDate) {
        this.uploadDate = uploadDate;
        return this;
    }

    public void setUploadDate(ZonedDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public Document fileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
        return this;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public Boolean isSent() {
        return sent;
    }

    public Document sent(Boolean sent) {
        this.sent = sent;
        return this;
    }

    public void setSent(Boolean sent) {
        this.sent = sent;
    }

    public Group getGroup() {
        return group;
    }

    public Document group(Group group) {
        this.group = group;
        return this;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Student getStudent() {
        return student;
    }

    public Document student(Student student) {
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
        if (!(o instanceof Document)) {
            return false;
        }
        return id != null && id.equals(((Document) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Document{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", requestDate='" + getRequestDate() + "'" +
            ", uploadDate='" + getUploadDate() + "'" +
            ", fileUrl='" + getFileUrl() + "'" +
            ", sent='" + isSent() + "'" +
            "}";
    }
}
