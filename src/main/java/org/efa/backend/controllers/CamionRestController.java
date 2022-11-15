package org.efa.backend.controllers;

import org.efa.backend.exceptions.custom.BusinessException;
import org.efa.backend.exceptions.custom.FoundException;
import org.efa.backend.exceptions.custom.NotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.efa.backend.miscellaneous.Paths;
import org.efa.backend.model.Camion;
import org.efa.backend.model.business.ICamionBusiness;
import org.efa.backend.utils.IStandardResponseBusiness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Paths.URL_CAMIONES)
public class CamionRestController {

    @Autowired
    private ICamionBusiness camionBusiness;

    @Autowired
    private IStandardResponseBusiness response;

    @PostMapping(value = "/crear")
    public ResponseEntity<?> load(@RequestBody Camion camion) {
        try {
            Camion response = camionBusiness.add(camion);
            HttpHeaders responseHeaders=new HttpHeaders();
            responseHeaders.set("location",Paths.URL_CAMIONES+"/"+response.getId());
            return new ResponseEntity<>( responseHeaders, HttpStatus.CREATED);
        } catch (FoundException e) {
            return new ResponseEntity<>(response.build(HttpStatus.NOT_FOUND, e, e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (BusinessException e) {
            return new ResponseEntity<>(response.build(HttpStatus.INTERNAL_SERVER_ERROR, e, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value="/listar", produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> loadAll() {
        try {
            return new ResponseEntity<>(camionBusiness.loadAll(), HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<>(response.build(HttpStatus.INTERNAL_SERVER_ERROR, e, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> loadById(@PathVariable("id") Long id) {
        try {
            return new ResponseEntity<>(camionBusiness.loadById(id), HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<>(response.build(HttpStatus.INTERNAL_SERVER_ERROR, e, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(response.build(HttpStatus.NOT_FOUND, e, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/{patente}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> load(@PathVariable("patente") String patente) {
        try {
            return new ResponseEntity<>(camionBusiness.load(patente), HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<>(response.build(HttpStatus.INTERNAL_SERVER_ERROR, e, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(response.build(HttpStatus.NOT_FOUND, e, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/modificar")
    public ResponseEntity<?> update(@RequestBody Camion camion) {
        try {
            camionBusiness.update(camion);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<>(response.build(HttpStatus.INTERNAL_SERVER_ERROR, e, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(response.build(HttpStatus.NOT_FOUND, e, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }
    @Transactional
    @DeleteMapping(value="/eliminar/{patente}")
    public ResponseEntity<?> delete(@PathVariable("patente") String patente){
        try {
            camionBusiness.delete(patente);
            return new ResponseEntity<>( HttpStatus.OK);
        } catch (BusinessException | NotFoundException e) {
            return new ResponseEntity<>(response.build(HttpStatus.INTERNAL_SERVER_ERROR, e, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Transactional
    @DeleteMapping(value="/eliminar/id/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") Long id){
        try {
            camionBusiness.deleteById(id);
            return new ResponseEntity<>( HttpStatus.OK);
        } catch (BusinessException | NotFoundException e) {
            return new ResponseEntity<>(response.build(HttpStatus.INTERNAL_SERVER_ERROR, e, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}