package org.efa.backend.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.efa.backend.exceptions.custom.BusinessException;
import org.efa.backend.exceptions.custom.FoundException;
import org.efa.backend.exceptions.custom.NotFoundException;
import org.efa.backend.miscellaneous.Paths;
import org.efa.backend.model.DetalleCarga;
import org.efa.backend.model.Orden;
import org.efa.backend.model.serializers.OrdenEstado2JSONSerializer;
import org.efa.backend.model.business.IOrdenBusiness;
import org.efa.backend.model.views.IConciliacionSlimView;
import org.efa.backend.utils.IStandardResponseBusiness;
import org.efa.backend.utils.JsonUtiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Paths.URL_ORDENES)
public class OrdenRestController {

    @Autowired
    private IOrdenBusiness ordenBusiness;

    @Autowired
    private IStandardResponseBusiness response;

    @PostMapping(value = "/crear")
    public ResponseEntity<?> load(@RequestBody Orden orden) {
        try {
            Orden response = ordenBusiness.add(orden);
            HttpHeaders responseHeaders=new HttpHeaders();
            responseHeaders.set("location",Paths.URL_ORDENES+"/"+response.getId());
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
            return new ResponseEntity<>(ordenBusiness.loadAll(), HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<>(response.build(HttpStatus.INTERNAL_SERVER_ERROR, e, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> loadById(@PathVariable("id") Long id) {
        try {
            return new ResponseEntity<>(ordenBusiness.loadById(id), HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<>(response.build(HttpStatus.INTERNAL_SERVER_ERROR, e, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(response.build(HttpStatus.NOT_FOUND, e, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/{numero}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> load(@PathVariable("numero") long numero) {
        try {
            return new ResponseEntity<>(ordenBusiness.load(numero), HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<>(response.build(HttpStatus.INTERNAL_SERVER_ERROR, e, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(response.build(HttpStatus.NOT_FOUND, e, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/modificar")
    public ResponseEntity<?> update(@RequestBody Orden orden) {
        try {
            ordenBusiness.update(orden);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<>(response.build(HttpStatus.INTERNAL_SERVER_ERROR, e, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(response.build(HttpStatus.NOT_FOUND, e, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @Transactional
    @DeleteMapping(value="/eliminar/{numero}")
    public ResponseEntity<?> delete(@PathVariable("numero") long numero){
        try {
            ordenBusiness.delete(numero);
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
            ordenBusiness.deleteById(id);
            return new ResponseEntity<>( HttpStatus.OK);
        } catch (BusinessException | NotFoundException e) {
            return new ResponseEntity<>(response.build(HttpStatus.INTERNAL_SERVER_ERROR, e, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/tara")
    public ResponseEntity<?> setTara(@RequestBody Orden orden) {
        StdSerializer<Orden> serializer = new OrdenEstado2JSONSerializer(Orden.class, false);
        try {
//            Integer password = ordenBusiness.addTara(orden);

            String result = JsonUtiles.getObjectMapper(Orden.class,serializer, null).writeValueAsString(ordenBusiness.addTara(orden));

            return new ResponseEntity<>(result, HttpStatus.OK);
//            return new ResponseEntity<>("Password: " + password, HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<>(response.build(HttpStatus.INTERNAL_SERVER_ERROR, e, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(response.build(HttpStatus.NOT_FOUND, e, e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
            return new ResponseEntity<>(response.build(HttpStatus.INTERNAL_SERVER_ERROR, e, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/encenderBomba")
    public ResponseEntity<?> turnOnBomb(@RequestBody Orden orden) {
        try {
            Orden ordenNueva = ordenBusiness.turnOnBomb(orden);
            return new ResponseEntity<>("Bomba encendida para la orden numero "+ordenNueva.getNumero()+" y camión de patente "+ordenNueva.getCamion().getPatente(),HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<>(response.build(HttpStatus.INTERNAL_SERVER_ERROR, e, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(response.build(HttpStatus.NOT_FOUND, e, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/obtenerCargaActual/{numero}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> loadCargaActual(@PathVariable("numero") long numero) {
        try {
            return new ResponseEntity<>(ordenBusiness.getCargaActual(numero), HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<>(response.build(HttpStatus.INTERNAL_SERVER_ERROR, e, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(response.build(HttpStatus.NOT_FOUND, e, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/cargar/{numero}")
    public ResponseEntity<?> loadCamion(@PathVariable("numero") long numero, @RequestBody DetalleCarga detalleCarga) {
        try {
            ordenBusiness.cargarCamion(numero,detalleCarga);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<>(response.build(HttpStatus.INTERNAL_SERVER_ERROR, e, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(response.build(HttpStatus.NOT_FOUND, e, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/apagarBomba")
    public ResponseEntity<?> turnOffBomb(@RequestParam(name = "numero") Long numero,
                                         @RequestParam(name = "password") int password
                                         ) {
        try {
            Orden ordenNueva = ordenBusiness.turnOffBomb(numero, password);
            return new ResponseEntity<>(ordenNueva,HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<>(response.build(HttpStatus.INTERNAL_SERVER_ERROR, e, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(response.build(HttpStatus.NOT_FOUND, e, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/cerrarOrden")
    public ResponseEntity<?> cerrarOrden(@RequestParam(name = "numero") Long numero ) {
        try {
            IConciliacionSlimView conciliacion = ordenBusiness.cerrarOrden(numero);
            return new ResponseEntity<>(conciliacion,HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<>(response.build(HttpStatus.INTERNAL_SERVER_ERROR, e, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(response.build(HttpStatus.NOT_FOUND, e, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/conciliacion/{numero}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getConciliacion(@PathVariable("numero") long numero) {
        try {
            return new ResponseEntity<>(ordenBusiness.concilacion(numero), HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<>(response.build(HttpStatus.INTERNAL_SERVER_ERROR, e, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(response.build(HttpStatus.NOT_FOUND, e, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/b2b")
    public ResponseEntity<?> addExternal(HttpEntity<String> httpEntity) {
        try {
            Orden response = ordenBusiness.addExternal(httpEntity.getBody());
            HttpHeaders responseHeaders = new HttpHeaders();
            //responseHeaders.set("location", Constants.URL_INTEGRATION_CLI1 + "/products/" + response.getCodCli1());
            return new ResponseEntity<>(responseHeaders, HttpStatus.CREATED);
        } catch (BusinessException e) {
            return new ResponseEntity<>(response.build(HttpStatus.INTERNAL_SERVER_ERROR, e, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (FoundException e) {
            return new ResponseEntity<>(response.build(HttpStatus.FOUND, e, e.getMessage()), HttpStatus.FOUND);
        }
    }

}
