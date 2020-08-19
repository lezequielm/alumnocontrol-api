package com.softstonesolutions.alumnocontrol.service;

import com.softstonesolutions.alumnocontrol.domain.Assistance;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Assistance}.
 */
public interface AssistanceService {

    /**
     * Save a assistance.
     *
     * @param assistance the entity to save.
     * @return the persisted entity.
     */
    Assistance save(Assistance assistance);

    /**
     * Get all the assistances.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Assistance> findAll(Pageable pageable);


    /**
     * Get the "id" assistance.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Assistance> findOne(Long id);

    /**
     * Delete the "id" assistance.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
