package com.softstonesolutions.alumnocontrol.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A ExtendedUser.
 */
@Entity
@Table(name = "extended_user")
public class ExtendedUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "photo_url")
    private String photoUrl;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "user")
    private Set<Contact> contacts = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "users", allowSetters = true)
    private Institute institute;

    @ManyToOne
    @JsonIgnoreProperties(value = "users", allowSetters = true)
    private Group group;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public ExtendedUser photoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
        return this;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public User getUser() {
        return user;
    }

    public ExtendedUser user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Contact> getContacts() {
        return contacts;
    }

    public ExtendedUser contacts(Set<Contact> contacts) {
        this.contacts = contacts;
        return this;
    }

    public ExtendedUser addContact(Contact contact) {
        this.contacts.add(contact);
        contact.setUser(this);
        return this;
    }

    public ExtendedUser removeContact(Contact contact) {
        this.contacts.remove(contact);
        contact.setUser(null);
        return this;
    }

    public void setContacts(Set<Contact> contacts) {
        this.contacts = contacts;
    }

    public Institute getInstitute() {
        return institute;
    }

    public ExtendedUser institute(Institute institute) {
        this.institute = institute;
        return this;
    }

    public void setInstitute(Institute institute) {
        this.institute = institute;
    }

    public Group getGroup() {
        return group;
    }

    public ExtendedUser group(Group group) {
        this.group = group;
        return this;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExtendedUser)) {
            return false;
        }
        return id != null && id.equals(((ExtendedUser) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExtendedUser{" +
            "id=" + getId() +
            ", photoUrl='" + getPhotoUrl() + "'" +
            "}";
    }
}
