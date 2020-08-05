package com.softstonesolutions.alumnocontrol.service;

import com.softstonesolutions.alumnocontrol.domain.Institute;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Institute}.
 */
public interface InstituteService {

    /**
     * Save a institute.
     *
     * @param institute the entity to save.
     * @return the persisted entity.
     */
    Institute save(Institute institute);

    /**
     * Get all the institutes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Institute> findAll(Pageable pageable);


    /**
     * Get the "id" institute.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Institute> findOne(Long id);

    /**
     * Delete the "id" institute.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
