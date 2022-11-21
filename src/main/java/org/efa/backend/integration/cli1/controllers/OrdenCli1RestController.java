package org.efa.backend.integration.cli1.controllers;

import org.efa.backend.exceptions.custom.BusinessException;
import org.efa.backend.exceptions.custom.FoundException;
import org.efa.backend.exceptions.custom.NotFoundException;
import org.efa.backend.integration.cli1.model.OrdenCli1;
import org.efa.backend.integration.cli1.model.business.IOrdenCli1Business;
import org.efa.backend.miscellaneous.Paths;
import org.efa.backend.utils.IStandardResponseBusiness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Paths.URL_ORDENES_CLI1)
public class OrdenCli1RestController {

    @Autowired
    private IOrdenCli1Business ordenCli1Business;

    @Autowired
    private IStandardResponseBusiness response;

    @GetMapping(value = "/{codigo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> load(@PathVariable("codigo") String codigo) {
        try {
            return new ResponseEntity<>(ordenCli1Business.load(codigo), HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<>(response.build(HttpStatus.INTERNAL_SERVER_ERROR, e, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(response.build(HttpStatus.NOT_FOUND, e, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/listar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> list() {

        try {
            return new ResponseEntity<>(ordenCli1Business.list(), HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<>(response.build(HttpStatus.INTERNAL_SERVER_ERROR, e, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/crear")
    public ResponseEntity<?> add(@RequestBody OrdenCli1 orden) {

        try {

            OrdenCli1 response = ordenCli1Business.add(orden);
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("location", Paths.URL_ORDENES_CLI1 + "/" + response.getId());
            return new ResponseEntity<>(responseHeaders, HttpStatus.CREATED);

        } catch (FoundException e) {
            return new ResponseEntity<>(response.build(HttpStatus.FOUND, e, e.getMessage()), HttpStatus.FOUND);

        } catch (BusinessException e) {
            return new ResponseEntity<>(response.build(HttpStatus.INTERNAL_SERVER_ERROR, e, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping(value = "/b2b")
    public ResponseEntity<?> addExternal(HttpEntity<String> httpEntity) {
        try {
            OrdenCli1 response = ordenCli1Business.addExternal(httpEntity.getBody());
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("location", Paths.URL_ORDENES + "/codigo/" + response.getCodigo());
            return new ResponseEntity<>(responseHeaders, HttpStatus.CREATED);
        } catch (BusinessException e) {
            return new ResponseEntity<>(response.build(HttpStatus.INTERNAL_SERVER_ERROR, e, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (FoundException e) {
            return new ResponseEntity<>(response.build(HttpStatus.FOUND, e, e.getMessage()), HttpStatus.FOUND);
        }
    }

}
