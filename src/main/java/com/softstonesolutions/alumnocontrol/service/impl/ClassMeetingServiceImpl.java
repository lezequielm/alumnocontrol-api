package com.softstonesolutions.alumnocontrol.service.impl;

import com.softstonesolutions.alumnocontrol.service.ClassMeetingService;
import com.softstonesolutions.alumnocontrol.domain.ClassMeeting;
import com.softstonesolutions.alumnocontrol.repository.ClassMeetingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link ClassMeeting}.
 */
@Service
@Transactional
public class ClassMeetingServiceImpl implements ClassMeetingService {

    private final Logger log = LoggerFactory.getLogger(ClassMeetingServiceImpl.class);

    private final ClassMeetingRepository classMeetingRepository;

    public ClassMeetingServiceImpl(ClassMeetingRepository classMeetingRepository) {
        this.classMeetingRepository = classMeetingRepository;
    }

    @Override
    public ClassMeeting save(ClassMeeting classMeeting) {
        log.debug("Request to save ClassMeeting : {}", classMeeting);
        return classMeetingRepository.save(classMeeting);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ClassMeeting> findAll(Pageable pageable) {
        log.debug("Request to get all ClassMeetings");
        return classMeetingRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<ClassMeeting> findOne(Long id) {
        log.debug("Request to get ClassMeeting : {}", id);
        return classMeetingRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ClassMeeting : {}", id);
        classMeetingRepository.deleteById(id);
    }
}
