package com.softstonesolutions.alumnocontrol.service;

import com.softstonesolutions.alumnocontrol.domain.Asistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Asistence}.
 */
public interface AsistenceService {

    /**
     * Save a asistence.
     *
     * @param asistence the entity to save.
     * @return the persisted entity.
     */
    Asistence save(Asistence asistence);

    /**
     * Get all the asistences.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Asistence> findAll(Pageable pageable);


    /**
     * Get the "id" asistence.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Asistence> findOne(Long id);

    /**
     * Delete the "id" asistence.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
