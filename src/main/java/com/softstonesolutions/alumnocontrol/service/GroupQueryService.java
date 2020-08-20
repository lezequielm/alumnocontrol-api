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

import com.softstonesolutions.alumnocontrol.domain.Group;
import com.softstonesolutions.alumnocontrol.domain.*; // for static metamodels
import com.softstonesolutions.alumnocontrol.repository.GroupRepository;
import com.softstonesolutions.alumnocontrol.service.dto.GroupCriteria;

/**
 * Service for executing complex queries for {@link Group} entities in the database.
 * The main input is a {@link GroupCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Group} or a {@link Page} of {@link Group} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GroupQueryService extends QueryService<Group> {

    private final Logger log = LoggerFactory.getLogger(GroupQueryService.class);

    private final GroupRepository groupRepository;

    public GroupQueryService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    /**
     * Return a {@link List} of {@link Group} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Group> findByCriteria(GroupCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Group> specification = createSpecification(criteria);
        return groupRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Group} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Group> findByCriteria(GroupCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Group> specification = createSpecification(criteria);
        return groupRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GroupCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Group> specification = createSpecification(criteria);
        return groupRepository.count(specification);
    }

    /**
     * Function to convert {@link GroupCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Group> createSpecification(GroupCriteria criteria) {
        Specification<Group> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Group_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Group_.name));
            }
            if (criteria.getEnabled() != null) {
                specification = specification.and(buildSpecification(criteria.getEnabled(), Group_.enabled));
            }
            if (criteria.getRequestedDocumentsId() != null) {
                specification = specification.and(buildSpecification(criteria.getRequestedDocumentsId(),
                    root -> root.join(Group_.requestedDocuments, JoinType.LEFT).get(Document_.id)));
            }
            if (criteria.getStudentsId() != null) {
                specification = specification.and(buildSpecification(criteria.getStudentsId(),
                    root -> root.join(Group_.students, JoinType.LEFT).get(Student_.id)));
            }
            if (criteria.getAssistanceId() != null) {
                specification = specification.and(buildSpecification(criteria.getAssistanceId(),
                    root -> root.join(Group_.assistances, JoinType.LEFT).get(Assistance_.id)));
            }
            if (criteria.getUsersId() != null) {
                specification = specification.and(buildSpecification(criteria.getUsersId(),
                    root -> root.join(Group_.users, JoinType.LEFT).get(ExtendedUser_.id)));
            }
            if (criteria.getInstituteId() != null) {
                specification = specification.and(buildSpecification(criteria.getInstituteId(),
                    root -> root.join(Group_.institute, JoinType.LEFT).get(Institute_.id)));
            }
        }
        return specification;
    }
}
