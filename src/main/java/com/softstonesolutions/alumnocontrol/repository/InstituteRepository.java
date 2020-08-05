package com.softstonesolutions.alumnocontrol.repository;

import com.softstonesolutions.alumnocontrol.domain.Institute;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Institute entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InstituteRepository extends JpaRepository<Institute, Long> {
}
