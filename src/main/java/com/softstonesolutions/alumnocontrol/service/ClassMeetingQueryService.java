package com.softstonesolutions.alumnocontrol.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.softstonesolutions.alumnocontrol.domain.ClassMeeting;
import com.softstonesolutions.alumnocontrol.domain.*; // for static metamodels
import com.softstonesolutions.alumnocontrol.repository.ClassMeetingRepository;
import com.softstonesolutions.alumnocontrol.service.dto.ClassMeetingCriteria;

/**
 * Service for executing complex queries for {@link ClassMeeting} entities in the database.
 * The main input is a {@link ClassMeetingCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ClassMeeting} or a {@link Page} of {@link ClassMeeting} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ClassMeetingQueryService extends QueryService<ClassMeeting> {

    private final Logger log = LoggerFactory.getLogger(ClassMeetingQueryService.class);

    private final ClassMeetingRepository classMeetingRepository;

    public ClassMeetingQueryService(ClassMeetingRepository classMeetingRepository) {
        this.classMeetingRepository = classMeetingRepository;
    }

    /**
     * Return a {@link List} of {@link ClassMeeting} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ClassMeeting> findByCriteria(ClassMeetingCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ClassMeeting> specification = createSpecification(criteria);
        return classMeetingRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ClassMeeting} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ClassMeeting> findByCriteria(ClassMeetingCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ClassMeeting> specification = createSpecification(criteria);
        return classMeetingRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ClassMeetingCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ClassMeeting> specification = createSpecification(criteria);
        return classMeetingRepository.count(specification);
    }

    /**
     * Function to convert {@link ClassMeetingCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ClassMeeting> createSpecification(ClassMeetingCriteria criteria) {
        Specification<ClassMeeting> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ClassMeeting_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), ClassMeeting_.name));
            }
            if (criteria.getClassType() != null) {
                specification = specification.and(buildSpecification(criteria.getClassType(), ClassMeeting_.classType));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), ClassMeeting_.date));
            }
            if (criteria.getCommentsId() != null) {
                specification = specification.and(buildSpecification(criteria.getCommentsId(),
                    root -> root.join(ClassMeeting_.comments, JoinType.LEFT).get(Comment_.id)));
            }
            if (criteria.getAssistanceId() != null) {
                specification = specification.and(buildSpecification(criteria.getAssistanceId(),
                    root -> root.join(ClassMeeting_.assistances, JoinType.LEFT).get(Assistance_.id)));
            }
        }
        return specification;
    }
}
