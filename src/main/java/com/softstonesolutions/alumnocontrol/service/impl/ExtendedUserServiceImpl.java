package com.softstonesolutions.alumnocontrol.service.impl;

import com.softstonesolutions.alumnocontrol.service.ExtendedUserService;
import com.softstonesolutions.alumnocontrol.domain.ExtendedUser;
import com.softstonesolutions.alumnocontrol.repository.ExtendedUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link ExtendedUser}.
 */
@Service
@Transactional
public class ExtendedUserServiceImpl implements ExtendedUserService {

    private final Logger log = LoggerFactory.getLogger(ExtendedUserServiceImpl.class);

    private final ExtendedUserRepository extendedUserRepository;

    public ExtendedUserServiceImpl(ExtendedUserRepository extendedUserRepository) {
        this.extendedUserRepository = extendedUserRepository;
    }

    @Override
    public ExtendedUser save(ExtendedUser extendedUser) {
        log.debug("Request to save ExtendedUser : {}", extendedUser);
        return extendedUserRepository.save(extendedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExtendedUser> findAll(Pageable pageable) {
        log.debug("Request to get all ExtendedUsers");
        return extendedUserRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<ExtendedUser> findOne(Long id) {
        log.debug("Request to get ExtendedUser : {}", id);
        return extendedUserRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ExtendedUser : {}", id);
        extendedUserRepository.deleteById(id);
    }
}
