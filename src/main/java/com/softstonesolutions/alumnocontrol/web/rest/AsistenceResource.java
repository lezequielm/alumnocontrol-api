package com.softstonesolutions.alumnocontrol.web.rest;

import com.softstonesolutions.alumnocontrol.domain.Asistence;
import com.softstonesolutions.alumnocontrol.service.AsistenceService;
import com.softstonesolutions.alumnocontrol.web.rest.errors.BadRequestAlertException;

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
 * REST controller for managing {@link com.softstonesolutions.alumnocontrol.domain.Asistence}.
 */
@RestController
@RequestMapping("/api")
public class AsistenceResource {

    private final Logger log = LoggerFactory.getLogger(AsistenceResource.class);

    private static final String ENTITY_NAME = "asistence";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AsistenceService asistenceService;

    public AsistenceResource(AsistenceService asistenceService) {
        this.asistenceService = asistenceService;
    }

    /**
     * {@code POST  /asistences} : Create a new asistence.
     *
     * @param asistence the asistence to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new asistence, or with status {@code 400 (Bad Request)} if the asistence has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/asistences")
    public ResponseEntity<Asistence> createAsistence(@Valid @RequestBody Asistence asistence) throws URISyntaxException {
        log.debug("REST request to save Asistence : {}", asistence);
        if (asistence.getId() != null) {
            throw new BadRequestAlertException("A new asistence cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Asistence result = asistenceService.save(asistence);
        return ResponseEntity.created(new URI("/api/asistences/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /asistences} : Updates an existing asistence.
     *
     * @param asistence the asistence to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated asistence,
     * or with status {@code 400 (Bad Request)} if the asistence is not valid,
     * or with status {@code 500 (Internal Server Error)} if the asistence couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/asistences")
    public ResponseEntity<Asistence> updateAsistence(@Valid @RequestBody Asistence asistence) throws URISyntaxException {
        log.debug("REST request to update Asistence : {}", asistence);
        if (asistence.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Asistence result = asistenceService.save(asistence);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, asistence.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /asistences} : get all the asistences.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of asistences in body.
     */
    @GetMapping("/asistences")
    public ResponseEntity<List<Asistence>> getAllAsistences(Pageable pageable) {
        log.debug("REST request to get a page of Asistences");
        Page<Asistence> page = asistenceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /asistences/:id} : get the "id" asistence.
     *
     * @param id the id of the asistence to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the asistence, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/asistences/{id}")
    public ResponseEntity<Asistence> getAsistence(@PathVariable Long id) {
        log.debug("REST request to get Asistence : {}", id);
        Optional<Asistence> asistence = asistenceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(asistence);
    }

    /**
     * {@code DELETE  /asistences/:id} : delete the "id" asistence.
     *
     * @param id the id of the asistence to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/asistences/{id}")
    public ResponseEntity<Void> deleteAsistence(@PathVariable Long id) {
        log.debug("REST request to delete Asistence : {}", id);
        asistenceService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
