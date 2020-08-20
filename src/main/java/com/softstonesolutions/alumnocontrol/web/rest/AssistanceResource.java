package com.softstonesolutions.alumnocontrol.web.rest;

import com.softstonesolutions.alumnocontrol.domain.Assistance;
import com.softstonesolutions.alumnocontrol.service.AssistanceService;
import com.softstonesolutions.alumnocontrol.web.rest.errors.BadRequestAlertException;
import com.softstonesolutions.alumnocontrol.service.dto.AssistanceCriteria;
import com.softstonesolutions.alumnocontrol.service.AssistanceQueryService;

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
 * REST controller for managing {@link com.softstonesolutions.alumnocontrol.domain.Assistance}.
 */
@RestController
@RequestMapping("/api")
public class AssistanceResource {

    private final Logger log = LoggerFactory.getLogger(AssistanceResource.class);

    private static final String ENTITY_NAME = "assistance";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AssistanceService assistanceService;

    private final AssistanceQueryService assistanceQueryService;

    public AssistanceResource(AssistanceService assistanceService, AssistanceQueryService assistanceQueryService) {
        this.assistanceService = assistanceService;
        this.assistanceQueryService = assistanceQueryService;
    }

    /**
     * {@code POST  /assistances} : Create a new assistance.
     *
     * @param assistance the assistance to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new assistance, or with status {@code 400 (Bad Request)} if the assistance has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/assistances")
    public ResponseEntity<Assistance> createAssistance(@Valid @RequestBody Assistance assistance) throws URISyntaxException {
        log.debug("REST request to save Assistance : {}", assistance);
        if (assistance.getId() != null) {
            throw new BadRequestAlertException("A new assistance cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Assistance result = assistanceService.save(assistance);
        return ResponseEntity.created(new URI("/api/assistances/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /assistances} : Updates an existing assistance.
     *
     * @param assistance the assistance to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assistance,
     * or with status {@code 400 (Bad Request)} if the assistance is not valid,
     * or with status {@code 500 (Internal Server Error)} if the assistance couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/assistances")
    public ResponseEntity<Assistance> updateAssistance(@Valid @RequestBody Assistance assistance) throws URISyntaxException {
        log.debug("REST request to update Assistance : {}", assistance);
        if (assistance.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Assistance result = assistanceService.save(assistance);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, assistance.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /assistances} : get all the assistances.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of assistances in body.
     */
    @GetMapping("/assistances")
    public ResponseEntity<List<Assistance>> getAllAssistances(AssistanceCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Assistances by criteria: {}", criteria);
        Page<Assistance> page = assistanceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /assistances/count} : count all the assistances.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/assistances/count")
    public ResponseEntity<Long> countAssistances(AssistanceCriteria criteria) {
        log.debug("REST request to count Assistances by criteria: {}", criteria);
        return ResponseEntity.ok().body(assistanceQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /assistances/:id} : get the "id" assistance.
     *
     * @param id the id of the assistance to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the assistance, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/assistances/{id}")
    public ResponseEntity<Assistance> getAssistance(@PathVariable Long id) {
        log.debug("REST request to get Assistance : {}", id);
        Optional<Assistance> assistance = assistanceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(assistance);
    }

    /**
     * {@code DELETE  /assistances/:id} : delete the "id" assistance.
     *
     * @param id the id of the assistance to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/assistances/{id}")
    public ResponseEntity<Void> deleteAssistance(@PathVariable Long id) {
        log.debug("REST request to delete Assistance : {}", id);
        assistanceService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
