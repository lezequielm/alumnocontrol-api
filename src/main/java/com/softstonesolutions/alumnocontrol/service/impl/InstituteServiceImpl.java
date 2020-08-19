package com.softstonesolutions.alumnocontrol.service.impl;

import com.softstonesolutions.alumnocontrol.service.InstituteService;
import com.softstonesolutions.alumnocontrol.domain.Institute;
import com.softstonesolutions.alumnocontrol.repository.InstituteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Institute}.
 */
@Service
@Transactional
public class InstituteServiceImpl implements InstituteService {

    private final Logger log = LoggerFactory.getLogger(InstituteServiceImpl.class);

    private final InstituteRepository instituteRepository;

    public InstituteServiceImpl(InstituteRepository instituteRepository) {
        this.instituteRepository = instituteRepository;
    }

    @Override
    public Institute save(Institute institute) {
        log.debug("Request to save Institute : {}", institute);
        return instituteRepository.save(institute);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Institute> findAll(Pageable pageable) {
        log.debug("Request to get all Institutes");
        return instituteRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<Institute> findOne(Long id) {
        log.debug("Request to get Institute : {}", id);
        return instituteRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Institute : {}", id);
        instituteRepository.deleteById(id);
    }
}
