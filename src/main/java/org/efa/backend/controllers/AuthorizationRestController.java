package org.efa.backend.controllers;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.SneakyThrows;
import org.efa.backend.auth.IRoleBusiness;
import org.efa.backend.auth.IUserBusiness;
import org.efa.backend.auth.Role;
import org.efa.backend.auth.User;
import org.efa.backend.controllers.constants.Constants;
import org.efa.backend.model.business.exceptions.BusinessException;
import org.efa.backend.model.business.exceptions.NotFoundException;
import org.efa.backend.utils.StandartResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
@Tag(description = "API Servicios de Authorizacion.", name = "Authorization")
@RequestMapping(Constants.URL_AUTHORIZATION)
public class AuthorizationRestController extends BaseRestController {

    @Hidden
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<String> onlyAdmin() {
        return new ResponseEntity<String>("Servicio admin", HttpStatus.OK);
    }

    @Hidden
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/user")
    public ResponseEntity<String> onlyUser() {
        return new ResponseEntity<String>("Servicio user", HttpStatus.OK);
    }

    @Hidden
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/user-or-admin")
    public ResponseEntity<String> rolUserOArdmin() {
        return new ResponseEntity<String>("Servicio user or admin", HttpStatus.OK);
    }

    @Operation(operationId = "my-rols", summary = "Este servicio retorna los roles que posee el usuario.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna un arreglo con los roles."),
            @ApiResponse(responseCode = "403", description = "Forbidden")})
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/my-rols")
    public ResponseEntity<String> myRols() {
        return new ResponseEntity<String>(getUserLogged().getAuthorities().toString(), HttpStatus.OK);
    }

    @Hidden
    @GetMapping("/variable")
    public ResponseEntity<String> variable(HttpServletRequest request) {
        if (request.isUserInRole("ROLE_ADMIN")) {
            return new ResponseEntity<String>("Tenés rol admin", HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("No tenés rol admin", HttpStatus.OK);
        }
    }

    //Deuelve toda la data de ese usuario.
    //TODO: ************************** este endpoint deberia estar!! ***************************************
    @Operation(operationId = "full-data", summary = "Este servicio retorna las caracteristicas del usuario logueado.")
    @Parameter(in = ParameterIn.QUERY, name = "username", schema = @Schema(type = "string"), required = true, description = "Nombre del usuario.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna un objeto User.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PostAuthorize("returnObject.username == #username")
    @GetMapping("/full-data")
    public User fullData(@RequestParam("username") String username) {
        return getUserLogged();
    }

    @Autowired
    private IUserBusiness userBusiness;

    //El user actual no figura en la lista
    @Hidden
    @PostFilter("filterObject != authentication.principal.username")
    @GetMapping("/self-filter")
    public List<String> selfFilter() {
        List<String> r = null;
        try {
            r = userBusiness.list().stream().map(u -> u.getUsername()).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return r;
    }

    @Autowired
    private IRoleBusiness roleBusiness;

    @SneakyThrows
    @Operation(operationId = "add-role", summary = "Este servicio agrega un rol al usuario logueado.")
    @Parameter(in = ParameterIn.QUERY, name = "username", schema = @Schema(type = "string"), required = true, description = "Nombre del usuario.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna un objeto User.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "409", description = "Conflict", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = StandartResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Not found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = StandartResponse.class))}),
            @ApiResponse(responseCode = "500", description = "Error interno", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = StandartResponse.class))})
    })
    @PreAuthorize("#username == authentication.principal.username")
    @PostMapping(value = "/add-role", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addRole(@RequestParam String username, @RequestBody Role role) {
        try {
            User user = userBusiness.load(username);
            Role rol = roleBusiness.add(role);
            user.getRoles().add(rol);
            userBusiness.add(user);

            HttpHeaders responseHeaders = new HttpHeaders();
            return new ResponseEntity<>(user, responseHeaders, HttpStatus.CREATED);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (BusinessException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }
}
