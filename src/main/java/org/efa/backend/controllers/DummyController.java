package org.efa.backend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.efa.backend.controllers.constants.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.URL_DUMMY)
@SecurityRequirement(name = "Bearer Authentication")
@Tag(description = "API Servicios Dummy. No necesita ROLES para ser consumido.", name = "Dummy")
@RequiredArgsConstructor
public class DummyController extends BaseRestController {

    @SneakyThrows
    @Operation(operationId = "dummy", summary = "Este servicio comprueba el servicio de login.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authorized successful"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @GetMapping(value = "", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> get() {

        return new ResponseEntity<>("Dummy Response", HttpStatus.OK);
    }
}
