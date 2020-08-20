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

import com.softstonesolutions.alumnocontrol.domain.Institute;
import com.softstonesolutions.alumnocontrol.domain.*; // for static metamodels
import com.softstonesolutions.alumnocontrol.repository.InstituteRepository;
import com.softstonesolutions.alumnocontrol.service.dto.InstituteCriteria;

/**
 * Service for executing complex queries for {@link Institute} entities in the database.
 * The main input is a {@link InstituteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Institute} or a {@link Page} of {@link Institute} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InstituteQueryService extends QueryService<Institute> {

    private final Logger log = LoggerFactory.getLogger(InstituteQueryService.class);

    private final InstituteRepository instituteRepository;

    public InstituteQueryService(InstituteRepository instituteRepository) {
        this.instituteRepository = instituteRepository;
    }

    /**
     * Return a {@link List} of {@link Institute} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Institute> findByCriteria(InstituteCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Institute> specification = createSpecification(criteria);
        return instituteRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Institute} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Institute> findByCriteria(InstituteCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Institute> specification = createSpecification(criteria);
        return instituteRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InstituteCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Institute> specification = createSpecification(criteria);
        return instituteRepository.count(specification);
    }

    /**
     * Function to convert {@link InstituteCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Institute> createSpecification(InstituteCriteria criteria) {
        Specification<Institute> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Institute_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Institute_.name));
            }
            if (criteria.getEnabled() != null) {
                specification = specification.and(buildSpecification(criteria.getEnabled(), Institute_.enabled));
            }
            if (criteria.getUsersId() != null) {
                specification = specification.and(buildSpecification(criteria.getUsersId(),
                    root -> root.join(Institute_.users, JoinType.LEFT).get(ExtendedUser_.id)));
            }
            if (criteria.getStudentsId() != null) {
                specification = specification.and(buildSpecification(criteria.getStudentsId(),
                    root -> root.join(Institute_.students, JoinType.LEFT).get(Student_.id)));
            }
            if (criteria.getGroupsId() != null) {
                specification = specification.and(buildSpecification(criteria.getGroupsId(),
                    root -> root.join(Institute_.groups, JoinType.LEFT).get(Group_.id)));
            }
            if (criteria.getAssistanceId() != null) {
                specification = specification.and(buildSpecification(criteria.getAssistanceId(),
                    root -> root.join(Institute_.assistances, JoinType.LEFT).get(Assistance_.id)));
            }
        }
        return specification;
    }
}
