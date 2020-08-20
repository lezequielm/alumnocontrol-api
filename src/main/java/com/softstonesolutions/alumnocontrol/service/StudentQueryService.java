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

import com.softstonesolutions.alumnocontrol.domain.Student;
import com.softstonesolutions.alumnocontrol.domain.*; // for static metamodels
import com.softstonesolutions.alumnocontrol.repository.StudentRepository;
import com.softstonesolutions.alumnocontrol.service.dto.StudentCriteria;

/**
 * Service for executing complex queries for {@link Student} entities in the database.
 * The main input is a {@link StudentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Student} or a {@link Page} of {@link Student} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class StudentQueryService extends QueryService<Student> {

    private final Logger log = LoggerFactory.getLogger(StudentQueryService.class);

    private final StudentRepository studentRepository;

    public StudentQueryService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    /**
     * Return a {@link List} of {@link Student} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Student> findByCriteria(StudentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Student> specification = createSpecification(criteria);
        return studentRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Student} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Student> findByCriteria(StudentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Student> specification = createSpecification(criteria);
        return studentRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(StudentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Student> specification = createSpecification(criteria);
        return studentRepository.count(specification);
    }

    /**
     * Function to convert {@link StudentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Student> createSpecification(StudentCriteria criteria) {
        Specification<Student> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Student_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), Student_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), Student_.lastName));
            }
            if (criteria.getIdNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIdNumber(), Student_.idNumber));
            }
            if (criteria.getBirthDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBirthDate(), Student_.birthDate));
            }
            if (criteria.getEnabled() != null) {
                specification = specification.and(buildSpecification(criteria.getEnabled(), Student_.enabled));
            }
            if (criteria.getPhotoUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhotoUrl(), Student_.photoUrl));
            }
            if (criteria.getContactsId() != null) {
                specification = specification.and(buildSpecification(criteria.getContactsId(),
                    root -> root.join(Student_.contacts, JoinType.LEFT).get(Contact_.id)));
            }
            if (criteria.getDocumentsId() != null) {
                specification = specification.and(buildSpecification(criteria.getDocumentsId(),
                    root -> root.join(Student_.documents, JoinType.LEFT).get(Document_.id)));
            }
            if (criteria.getAssistanceId() != null) {
                specification = specification.and(buildSpecification(criteria.getAssistanceId(),
                    root -> root.join(Student_.assistances, JoinType.LEFT).get(Assistance_.id)));
            }
            if (criteria.getAddressesId() != null) {
                specification = specification.and(buildSpecification(criteria.getAddressesId(),
                    root -> root.join(Student_.addresses, JoinType.LEFT).get(Address_.id)));
            }
            if (criteria.getInstituteId() != null) {
                specification = specification.and(buildSpecification(criteria.getInstituteId(),
                    root -> root.join(Student_.institute, JoinType.LEFT).get(Institute_.id)));
            }
            if (criteria.getGroupId() != null) {
                specification = specification.and(buildSpecification(criteria.getGroupId(),
                    root -> root.join(Student_.group, JoinType.LEFT).get(Group_.id)));
            }
        }
        return specification;
    }
}
