package com.softstonesolutions.alumnocontrol.repository;

import com.softstonesolutions.alumnocontrol.domain.Student;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Student entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
}
