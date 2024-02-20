package org.efa.backend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.efa.backend.controllers.constants.Constants;
import org.efa.backend.model.Camion;
import org.efa.backend.model.business.exceptions.BusinessException;
import org.efa.backend.model.business.exceptions.FoundException;
import org.efa.backend.model.business.exceptions.NotFoundException;
import org.efa.backend.model.business.interfaces.ICamionBusiness;
import org.efa.backend.utils.StandartResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constants.URL_CAMION)
@SecurityRequirement(name = "Bearer Authentication")
@Tag(description = "API Servicios de la entidad Camion. Es necesario tener ROLE_ADMIN.", name = "Camion")
@RequiredArgsConstructor
public class CamionController extends BaseRestController {

    @Autowired
    private ICamionBusiness camionBusiness;

    @SneakyThrows
    @Operation(operationId = "camion-get", summary = "Este servicio busca un camion a partir de su codigo externo.")
    @Parameter(in = ParameterIn.PATH, name = "code", schema = @Schema(type = "string"), required = true, description = "Codigo externo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se encontro el camion.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Camion.class))}),
            @ApiResponse(responseCode = "409", description = "Conflict", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = StandartResponse.class))}),
            @ApiResponse(responseCode = "404", description = "No se encuentra el camion para el identificador informado", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = StandartResponse.class))}),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping(value = "/buscar/{code}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> get(@PathVariable("code") String code) {
        try {
            return new ResponseEntity<>(camionBusiness.load(code), HttpStatus.OK);
        } catch (FoundException | BusinessException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @SneakyThrows
    @Operation(operationId = "camion-delete", summary = "Este servicio elimina un camion a partir de su codigo externo.")
    @Parameter(in = ParameterIn.PATH, name = "code", schema = @Schema(type = "string"), required = true, description = "Codigo externo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se elimino el camion."),
            @ApiResponse(responseCode = "404", description = "No se encuentra el camion para el identificador informado", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = StandartResponse.class))}),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/eliminar/{code}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete(@PathVariable("code") String code) {
        try {
            camionBusiness.delete(code);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @SneakyThrows
    @Operation(operationId = "camion-post", summary = "Este servicio crea un camion.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Camion creado correctamente."),
            @ApiResponse(responseCode = "409", description = "Conflict", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = StandartResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Not found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = StandartResponse.class))}),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(@RequestBody Camion camion) {
        try {
            Camion result = camionBusiness.add(camion);
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("location", Constants.URL_CAMION + "/buscar/" + result.getCode());
            return new ResponseEntity<>(responseHeaders, HttpStatus.CREATED);
        } catch (FoundException | BusinessException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @SneakyThrows
    @Operation(operationId = "find-all", summary = "Este servicio devuelve una lista de todos los camiones.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de camiones retornada correctamente."),
            @ApiResponse(responseCode = "404", description = "Not Found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = StandartResponse.class))}),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping(value = "/find-all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findAll() {
        try {
            return new ResponseEntity<>(camionBusiness.list(), HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}