package com.softstonesolutions.alumnocontrol.repository;

import com.softstonesolutions.alumnocontrol.domain.Assistance;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Assistance entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AssistanceRepository extends JpaRepository<Assistance, Long> {
}
