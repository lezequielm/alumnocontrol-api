package com.softstonesolutions.alumnocontrol.web.rest;

import com.softstonesolutions.alumnocontrol.domain.ClassMeeting;
import com.softstonesolutions.alumnocontrol.service.ClassMeetingService;
import com.softstonesolutions.alumnocontrol.web.rest.errors.BadRequestAlertException;
import com.softstonesolutions.alumnocontrol.service.dto.ClassMeetingCriteria;
import com.softstonesolutions.alumnocontrol.service.ClassMeetingQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.softstonesolutions.alumnocontrol.domain.ClassMeeting}.
 */
@RestController
@RequestMapping("/api")
public class ClassMeetingResource {

    private final Logger log = LoggerFactory.getLogger(ClassMeetingResource.class);

    private static final String ENTITY_NAME = "classMeeting";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClassMeetingService classMeetingService;

    private final ClassMeetingQueryService classMeetingQueryService;

    public ClassMeetingResource(ClassMeetingService classMeetingService, ClassMeetingQueryService classMeetingQueryService) {
        this.classMeetingService = classMeetingService;
        this.classMeetingQueryService = classMeetingQueryService;
    }

    /**
     * {@code POST  /class-meetings} : Create a new classMeeting.
     *
     * @param classMeeting the classMeeting to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new classMeeting, or with status {@code 400 (Bad Request)} if the classMeeting has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/class-meetings")
    public ResponseEntity<ClassMeeting> createClassMeeting(@Valid @RequestBody ClassMeeting classMeeting) throws URISyntaxException {
        log.debug("REST request to save ClassMeeting : {}", classMeeting);
        if (classMeeting.getId() != null) {
            throw new BadRequestAlertException("A new classMeeting cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ClassMeeting result = classMeetingService.save(classMeeting);
        return ResponseEntity.created(new URI("/api/class-meetings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /class-meetings} : Updates an existing classMeeting.
     *
     * @param classMeeting the classMeeting to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated classMeeting,
     * or with status {@code 400 (Bad Request)} if the classMeeting is not valid,
     * or with status {@code 500 (Internal Server Error)} if the classMeeting couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/class-meetings")
    public ResponseEntity<ClassMeeting> updateClassMeeting(@Valid @RequestBody ClassMeeting classMeeting) throws URISyntaxException {
        log.debug("REST request to update ClassMeeting : {}", classMeeting);
        if (classMeeting.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ClassMeeting result = classMeetingService.save(classMeeting);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, classMeeting.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /class-meetings} : get all the classMeetings.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of classMeetings in body.
     */
    @GetMapping("/class-meetings")
    public ResponseEntity<List<ClassMeeting>> getAllClassMeetings(ClassMeetingCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ClassMeetings by criteria: {}", criteria);
        Page<ClassMeeting> page = classMeetingQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /class-meetings/count} : count all the classMeetings.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/class-meetings/count")
    public ResponseEntity<Long> countClassMeetings(ClassMeetingCriteria criteria) {
        log.debug("REST request to count ClassMeetings by criteria: {}", criteria);
        return ResponseEntity.ok().body(classMeetingQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /class-meetings/:id} : get the "id" classMeeting.
     *
     * @param id the id of the classMeeting to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the classMeeting, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/class-meetings/{id}")
    public ResponseEntity<ClassMeeting> getClassMeeting(@PathVariable Long id) {
        log.debug("REST request to get ClassMeeting : {}", id);
        Optional<ClassMeeting> classMeeting = classMeetingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(classMeeting);
    }

    /**
     * {@code DELETE  /class-meetings/:id} : delete the "id" classMeeting.
     *
     * @param id the id of the classMeeting to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/class-meetings/{id}")
    public ResponseEntity<Void> deleteClassMeeting(@PathVariable Long id) {
        log.debug("REST request to delete ClassMeeting : {}", id);
        classMeetingService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
