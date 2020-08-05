package com.softstonesolutions.alumnocontrol.service.impl;

import com.softstonesolutions.alumnocontrol.service.AsistenceService;
import com.softstonesolutions.alumnocontrol.domain.Asistence;
import com.softstonesolutions.alumnocontrol.repository.AsistenceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Asistence}.
 */
@Service
@Transactional
public class AsistenceServiceImpl implements AsistenceService {

    private final Logger log = LoggerFactory.getLogger(AsistenceServiceImpl.class);

    private final AsistenceRepository asistenceRepository;

    public AsistenceServiceImpl(AsistenceRepository asistenceRepository) {
        this.asistenceRepository = asistenceRepository;
    }

    @Override
    public Asistence save(Asistence asistence) {
        log.debug("Request to save Asistence : {}", asistence);
        return asistenceRepository.save(asistence);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Asistence> findAll(Pageable pageable) {
        log.debug("Request to get all Asistences");
        return asistenceRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<Asistence> findOne(Long id) {
        log.debug("Request to get Asistence : {}", id);
        return asistenceRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Asistence : {}", id);
        asistenceRepository.deleteById(id);
    }
}
