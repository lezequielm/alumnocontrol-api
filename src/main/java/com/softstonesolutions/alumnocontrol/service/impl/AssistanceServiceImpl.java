package com.softstonesolutions.alumnocontrol.service.impl;

import com.softstonesolutions.alumnocontrol.service.AssistanceService;
import com.softstonesolutions.alumnocontrol.domain.Assistance;
import com.softstonesolutions.alumnocontrol.repository.AssistanceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Assistance}.
 */
@Service
@Transactional
public class AssistanceServiceImpl implements AssistanceService {

    private final Logger log = LoggerFactory.getLogger(AssistanceServiceImpl.class);

    private final AssistanceRepository assistanceRepository;

    public AssistanceServiceImpl(AssistanceRepository assistanceRepository) {
        this.assistanceRepository = assistanceRepository;
    }

    @Override
    public Assistance save(Assistance assistance) {
        log.debug("Request to save Assistance : {}", assistance);
        return assistanceRepository.save(assistance);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Assistance> findAll(Pageable pageable) {
        log.debug("Request to get all Assistances");
        return assistanceRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<Assistance> findOne(Long id) {
        log.debug("Request to get Assistance : {}", id);
        return assistanceRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Assistance : {}", id);
        assistanceRepository.deleteById(id);
    }
}
