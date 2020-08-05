package com.softstonesolutions.alumnocontrol.repository;

import com.softstonesolutions.alumnocontrol.domain.Asistence;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Asistence entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AsistenceRepository extends JpaRepository<Asistence, Long> {
}
