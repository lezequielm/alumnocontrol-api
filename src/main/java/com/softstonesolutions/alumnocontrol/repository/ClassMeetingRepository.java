package com.softstonesolutions.alumnocontrol.repository;

import com.softstonesolutions.alumnocontrol.domain.ClassMeeting;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ClassMeeting entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClassMeetingRepository extends JpaRepository<ClassMeeting, Long> {
}
