package org.efa.backend.controllers;

import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.efa.backend.controllers.constants.Constants;
import org.efa.backend.model.Detalle;
import org.efa.backend.model.Orden;
import org.efa.backend.model.business.exceptions.BusinessException;
import org.efa.backend.model.business.exceptions.FoundException;
import org.efa.backend.model.business.exceptions.NotAuthorizedException;
import org.efa.backend.model.business.exceptions.NotFoundException;
import org.efa.backend.model.business.interfaces.IDetalleBusiness;
import org.efa.backend.model.business.interfaces.IAlarmaBusiness;
import org.efa.backend.model.business.interfaces.IOrdenBusiness;
import org.efa.backend.model.serializer.*;
import org.efa.backend.utils.JsonUtiles;
import org.efa.backend.utils.StandartResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constants.URL_ALARMA)
@SecurityRequirement(name = "Bearer Authentication")
@Tag(description = "API Servicios de la entidad Alarma.", name = "Alarma")
@RequiredArgsConstructor
public class AlarmaController extends BaseRestController {

    @Autowired
    private IOrdenBusiness ordenBusiness;

    @Autowired
    private IAlarmaBusiness alarmaBusiness;

    @SneakyThrows
    @Operation(operationId = "aceptar-alarma", summary = "Este servicio acepta la alarma de envio de mails.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alarma aceptada corectamente para una orden."),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = StandartResponse.class))})
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/aceptar-alarma", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> aceptarAlarma(@RequestHeader(name = "NumeroOrden") long numeroOrden) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String nombreUsuario = authentication.getName();
        try {
            ordenBusiness.aceptarAlarma(numeroOrden);
            alarmaBusiness.add(numeroOrden,nombreUsuario);

            return new ResponseEntity<>("Alarma aceptada para orden con numero: " + numeroOrden, HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}