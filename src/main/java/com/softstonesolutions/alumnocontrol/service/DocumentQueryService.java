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

import com.softstonesolutions.alumnocontrol.domain.Document;
import com.softstonesolutions.alumnocontrol.domain.*; // for static metamodels
import com.softstonesolutions.alumnocontrol.repository.DocumentRepository;
import com.softstonesolutions.alumnocontrol.service.dto.DocumentCriteria;

/**
 * Service for executing complex queries for {@link Document} entities in the database.
 * The main input is a {@link DocumentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Document} or a {@link Page} of {@link Document} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DocumentQueryService extends QueryService<Document> {

    private final Logger log = LoggerFactory.getLogger(DocumentQueryService.class);

    private final DocumentRepository documentRepository;

    public DocumentQueryService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    /**
     * Return a {@link List} of {@link Document} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Document> findByCriteria(DocumentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Document> specification = createSpecification(criteria);
        return documentRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Document} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Document> findByCriteria(DocumentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Document> specification = createSpecification(criteria);
        return documentRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DocumentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Document> specification = createSpecification(criteria);
        return documentRepository.count(specification);
    }

    /**
     * Function to convert {@link DocumentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Document> createSpecification(DocumentCriteria criteria) {
        Specification<Document> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Document_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Document_.name));
            }
            if (criteria.getRequestDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRequestDate(), Document_.requestDate));
            }
            if (criteria.getUploadDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUploadDate(), Document_.uploadDate));
            }
            if (criteria.getFileUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFileUrl(), Document_.fileUrl));
            }
            if (criteria.getSent() != null) {
                specification = specification.and(buildSpecification(criteria.getSent(), Document_.sent));
            }
            if (criteria.getGroupId() != null) {
                specification = specification.and(buildSpecification(criteria.getGroupId(),
                    root -> root.join(Document_.group, JoinType.LEFT).get(Group_.id)));
            }
            if (criteria.getStudentId() != null) {
                specification = specification.and(buildSpecification(criteria.getStudentId(),
                    root -> root.join(Document_.student, JoinType.LEFT).get(Student_.id)));
            }
        }
        return specification;
    }
}
