package com.softstonesolutions.alumnocontrol.service.impl;

import com.softstonesolutions.alumnocontrol.service.GroupService;
import com.softstonesolutions.alumnocontrol.domain.Group;
import com.softstonesolutions.alumnocontrol.repository.GroupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Group}.
 */
@Service
@Transactional
public class GroupServiceImpl implements GroupService {

    private final Logger log = LoggerFactory.getLogger(GroupServiceImpl.class);

    private final GroupRepository groupRepository;

    public GroupServiceImpl(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Override
    public Group save(Group group) {
        log.debug("Request to save Group : {}", group);
        return groupRepository.save(group);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Group> findAll(Pageable pageable) {
        log.debug("Request to get all Groups");
        return groupRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<Group> findOne(Long id) {
        log.debug("Request to get Group : {}", id);
        return groupRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Group : {}", id);
        groupRepository.deleteById(id);
    }
}
