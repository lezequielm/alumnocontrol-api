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

import com.softstonesolutions.alumnocontrol.domain.Assistance;
import com.softstonesolutions.alumnocontrol.domain.*; // for static metamodels
import com.softstonesolutions.alumnocontrol.repository.AssistanceRepository;
import com.softstonesolutions.alumnocontrol.service.dto.AssistanceCriteria;

/**
 * Service for executing complex queries for {@link Assistance} entities in the database.
 * The main input is a {@link AssistanceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Assistance} or a {@link Page} of {@link Assistance} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AssistanceQueryService extends QueryService<Assistance> {

    private final Logger log = LoggerFactory.getLogger(AssistanceQueryService.class);

    private final AssistanceRepository assistanceRepository;

    public AssistanceQueryService(AssistanceRepository assistanceRepository) {
        this.assistanceRepository = assistanceRepository;
    }

    /**
     * Return a {@link List} of {@link Assistance} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Assistance> findByCriteria(AssistanceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Assistance> specification = createSpecification(criteria);
        return assistanceRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Assistance} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Assistance> findByCriteria(AssistanceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Assistance> specification = createSpecification(criteria);
        return assistanceRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AssistanceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Assistance> specification = createSpecification(criteria);
        return assistanceRepository.count(specification);
    }

    /**
     * Function to convert {@link AssistanceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Assistance> createSpecification(AssistanceCriteria criteria) {
        Specification<Assistance> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Assistance_.id));
            }
            if (criteria.getPresent() != null) {
                specification = specification.and(buildSpecification(criteria.getPresent(), Assistance_.present));
            }
            if (criteria.getDelayed() != null) {
                specification = specification.and(buildSpecification(criteria.getDelayed(), Assistance_.delayed));
            }
            if (criteria.getJustified() != null) {
                specification = specification.and(buildSpecification(criteria.getJustified(), Assistance_.justified));
            }
            if (criteria.getStudentId() != null) {
                specification = specification.and(buildSpecification(criteria.getStudentId(),
                    root -> root.join(Assistance_.student, JoinType.LEFT).get(Student_.id)));
            }
            if (criteria.getInstituteId() != null) {
                specification = specification.and(buildSpecification(criteria.getInstituteId(),
                    root -> root.join(Assistance_.institute, JoinType.LEFT).get(Institute_.id)));
            }
            if (criteria.getClassMeetingId() != null) {
                specification = specification.and(buildSpecification(criteria.getClassMeetingId(),
                    root -> root.join(Assistance_.classMeeting, JoinType.LEFT).get(ClassMeeting_.id)));
            }
            if (criteria.getGroupId() != null) {
                specification = specification.and(buildSpecification(criteria.getGroupId(),
                    root -> root.join(Assistance_.group, JoinType.LEFT).get(Group_.id)));
            }
        }
        return specification;
    }
}
