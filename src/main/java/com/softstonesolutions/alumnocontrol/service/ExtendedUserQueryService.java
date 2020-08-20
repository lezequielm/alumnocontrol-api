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

import com.softstonesolutions.alumnocontrol.domain.ExtendedUser;
import com.softstonesolutions.alumnocontrol.domain.*; // for static metamodels
import com.softstonesolutions.alumnocontrol.repository.ExtendedUserRepository;
import com.softstonesolutions.alumnocontrol.service.dto.ExtendedUserCriteria;

/**
 * Service for executing complex queries for {@link ExtendedUser} entities in the database.
 * The main input is a {@link ExtendedUserCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ExtendedUser} or a {@link Page} of {@link ExtendedUser} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ExtendedUserQueryService extends QueryService<ExtendedUser> {

    private final Logger log = LoggerFactory.getLogger(ExtendedUserQueryService.class);

    private final ExtendedUserRepository extendedUserRepository;

    public ExtendedUserQueryService(ExtendedUserRepository extendedUserRepository) {
        this.extendedUserRepository = extendedUserRepository;
    }

    /**
     * Return a {@link List} of {@link ExtendedUser} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ExtendedUser> findByCriteria(ExtendedUserCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ExtendedUser> specification = createSpecification(criteria);
        return extendedUserRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ExtendedUser} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ExtendedUser> findByCriteria(ExtendedUserCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ExtendedUser> specification = createSpecification(criteria);
        return extendedUserRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ExtendedUserCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ExtendedUser> specification = createSpecification(criteria);
        return extendedUserRepository.count(specification);
    }

    /**
     * Function to convert {@link ExtendedUserCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ExtendedUser> createSpecification(ExtendedUserCriteria criteria) {
        Specification<ExtendedUser> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ExtendedUser_.id));
            }
            if (criteria.getPhotoUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhotoUrl(), ExtendedUser_.photoUrl));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserId(),
                    root -> root.join(ExtendedUser_.user, JoinType.LEFT).get(User_.id)));
            }
            if (criteria.getContactId() != null) {
                specification = specification.and(buildSpecification(criteria.getContactId(),
                    root -> root.join(ExtendedUser_.contacts, JoinType.LEFT).get(Contact_.id)));
            }
            if (criteria.getAddressesId() != null) {
                specification = specification.and(buildSpecification(criteria.getAddressesId(),
                    root -> root.join(ExtendedUser_.addresses, JoinType.LEFT).get(Address_.id)));
            }
            if (criteria.getInstituteId() != null) {
                specification = specification.and(buildSpecification(criteria.getInstituteId(),
                    root -> root.join(ExtendedUser_.institute, JoinType.LEFT).get(Institute_.id)));
            }
            if (criteria.getGroupId() != null) {
                specification = specification.and(buildSpecification(criteria.getGroupId(),
                    root -> root.join(ExtendedUser_.group, JoinType.LEFT).get(Group_.id)));
            }
        }
        return specification;
    }
}
