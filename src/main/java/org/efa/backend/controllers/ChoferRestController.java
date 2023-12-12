package org.efa.backend.controllers;

import org.efa.backend.exceptions.custom.BusinessException;
import org.efa.backend.exceptions.custom.FoundException;
import org.efa.backend.exceptions.custom.NotFoundException;
import org.efa.backend.miscellaneous.Paths;
import org.efa.backend.model.Chofer;
import org.efa.backend.model.business.IChoferBusiness;
import org.efa.backend.utils.IStandardResponseBusiness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Paths.URL_CHOFERES)
public class ChoferRestController {
	
	@Autowired
    private IStandardResponseBusiness response;
	
	@Autowired
    private IChoferBusiness choferBusiness;
	
	@PostMapping(value = "/crear")
    public ResponseEntity<?> load(@RequestBody Chofer chofer) {
        try {
            Chofer response = choferBusiness.add(chofer);
            HttpHeaders responseHeaders=new HttpHeaders();
            responseHeaders.set("location",Paths.URL_CHOFERES+"/"+response.getId());
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
            return new ResponseEntity<>(choferBusiness.loadAll(), HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<>(response.build(HttpStatus.INTERNAL_SERVER_ERROR, e, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
	
	@GetMapping(value = "/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> loadById(@PathVariable("id") Long id) {
        try {
            return new ResponseEntity<>(choferBusiness.loadById(id), HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<>(response.build(HttpStatus.INTERNAL_SERVER_ERROR, e, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(response.build(HttpStatus.NOT_FOUND, e, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }


	@GetMapping(value = "/{codigo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> load(@PathVariable("codigo") String codigo) {
        try {
            return new ResponseEntity<>(choferBusiness.load(codigo), HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<>(response.build(HttpStatus.INTERNAL_SERVER_ERROR, e, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(response.build(HttpStatus.NOT_FOUND, e, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }
	
	@PutMapping(value = "/modificar")
    public ResponseEntity<?> update(@RequestBody Chofer chofer) {
        try {
            choferBusiness.update(chofer);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<>(response.build(HttpStatus.INTERNAL_SERVER_ERROR, e, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(response.build(HttpStatus.NOT_FOUND, e, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }
	
	@Transactional
	@DeleteMapping(value="/eliminar/{codigo}")
    public ResponseEntity<?> delete(@PathVariable("codigo") String codigo){
        try {
            choferBusiness.delete(codigo);
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
            choferBusiness.deleteById(id);
            return new ResponseEntity<>( HttpStatus.OK);
        } catch (BusinessException | NotFoundException e) {
            return new ResponseEntity<>(response.build(HttpStatus.INTERNAL_SERVER_ERROR, e, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}