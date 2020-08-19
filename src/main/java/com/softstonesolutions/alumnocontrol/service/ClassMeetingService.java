package com.softstonesolutions.alumnocontrol.service;

import com.softstonesolutions.alumnocontrol.domain.ClassMeeting;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link ClassMeeting}.
 */
public interface ClassMeetingService {

    /**
     * Save a classMeeting.
     *
     * @param classMeeting the entity to save.
     * @return the persisted entity.
     */
    ClassMeeting save(ClassMeeting classMeeting);

    /**
     * Get all the classMeetings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ClassMeeting> findAll(Pageable pageable);


    /**
     * Get the "id" classMeeting.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ClassMeeting> findOne(Long id);

    /**
     * Delete the "id" classMeeting.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
