package com.softstonesolutions.alumnocontrol.service;

import com.softstonesolutions.alumnocontrol.domain.ExtendedUser;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link ExtendedUser}.
 */
public interface ExtendedUserService {

    /**
     * Save a extendedUser.
     *
     * @param extendedUser the entity to save.
     * @return the persisted entity.
     */
    ExtendedUser save(ExtendedUser extendedUser);

    /**
     * Get all the extendedUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ExtendedUser> findAll(Pageable pageable);


    /**
     * Get the "id" extendedUser.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ExtendedUser> findOne(Long id);

    /**
     * Delete the "id" extendedUser.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
