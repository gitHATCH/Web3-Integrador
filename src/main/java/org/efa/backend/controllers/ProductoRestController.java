package org.efa.backend.controllers;

import org.efa.backend.exceptions.custom.BusinessException;
import org.efa.backend.exceptions.custom.FoundException;
import org.efa.backend.exceptions.custom.NotFoundException;
import org.efa.backend.miscellaneous.Paths;
import org.efa.backend.model.Producto;
import org.efa.backend.model.business.IProductoBusiness;
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
@RequestMapping(Paths.URL_PRODUCTOS)
public class ProductoRestController {
	
	@Autowired
    private IStandardResponseBusiness response;
	
	@Autowired
    private IProductoBusiness productoBusiness;
	
	@PostMapping(value = "/crear")
    public ResponseEntity<?> load(@RequestBody Producto producto) {
        try {
            Producto response = productoBusiness.add(producto);
            HttpHeaders responseHeaders=new HttpHeaders();
            responseHeaders.set("location",Paths.URL_PRODUCTOS+"/"+response.getId());
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
            return new ResponseEntity<>(productoBusiness.loadAll(), HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<>(response.build(HttpStatus.INTERNAL_SERVER_ERROR, e, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
	
	@GetMapping(value = "/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> loadById(@PathVariable("id") Long id) {
        try {
            return new ResponseEntity<>(productoBusiness.loadById(id), HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<>(response.build(HttpStatus.INTERNAL_SERVER_ERROR, e, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(response.build(HttpStatus.NOT_FOUND, e, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }
	
	@GetMapping(value = "/{nombre}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> load(@PathVariable("nombre") String nombre) {
        try {
            return new ResponseEntity<>(productoBusiness.load(nombre), HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<>(response.build(HttpStatus.INTERNAL_SERVER_ERROR, e, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(response.build(HttpStatus.NOT_FOUND, e, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }
	
	@PutMapping(value = "/modificar")
    public ResponseEntity<?> update(@RequestBody Producto producto) {
        try {
            productoBusiness.update(producto);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<>(response.build(HttpStatus.INTERNAL_SERVER_ERROR, e, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(response.build(HttpStatus.NOT_FOUND, e, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }
	
	@Transactional
    @DeleteMapping(value="/eliminar/{nombre}")
    public ResponseEntity<?> delete(@PathVariable("nombre") String nombre){
        try {
            productoBusiness.delete(nombre);
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
            productoBusiness.deleteById(id);
            return new ResponseEntity<>( HttpStatus.OK);
        } catch (BusinessException | NotFoundException e) {
            return new ResponseEntity<>(response.build(HttpStatus.INTERNAL_SERVER_ERROR, e, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}