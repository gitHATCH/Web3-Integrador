package org.efa.backend.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.SneakyThrows;
import org.efa.backend.auth.IUserBusiness;
import org.efa.backend.auth.User;
import org.efa.backend.auth.UserJsonSerializer;
import org.efa.backend.auth.custom.CustomAuthenticationManager;
import org.efa.backend.auth.filter.AuthConstants;
import org.efa.backend.controllers.constants.Constants;
import org.efa.backend.model.business.exceptions.BusinessException;
import org.efa.backend.model.business.exceptions.NotFoundException;
import org.efa.backend.utils.JsonUtiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

@RestController
@Tag(description = "API Servicios de Authentication. No necesita ROLES para ser consumido.", name = "Auth")
public class AuthRestController extends BaseRestController {
    @Autowired
    private IUserBusiness userBusiness;

    @Autowired
    private AuthenticationManager authManager;

    @Operation(operationId = "auth-login", summary = "Este servicio autentica un usuario.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario logueado correctamente. Retorna un token JWT."),
            @ApiResponse(responseCode = "401", description = "No autorizado."),
    })
    @PostMapping(value = Constants.URL_LOGIN, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> loginExternalOnlyToken(@Parameter(description = "username or email") @RequestParam(value = "username") String username,
                                                    @RequestParam(value = "password") String password,
                                                    @Parameter(hidden = true)
                                                    //parametro oculto, no me acuerdo para q era!
                                                    @RequestParam(value = "json", defaultValue = "false") Boolean json) {
        Authentication auth = null;
        StdSerializer<User> ser = null;
        String result = "";
        try {
            auth = authManager.authenticate(((CustomAuthenticationManager) authManager).AuthWrap(username, password));
        } catch (AuthenticationServiceException e0) {
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (AuthenticationException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }

        User user = (User) auth.getPrincipal();
        String token = JWT.create().withSubject(user.getUsername())
                .withClaim("internalId", user.getIdUser())
                .withClaim("roles", new ArrayList<String>(user.getAuthoritiesStr()))
                .withClaim("email", user.getEmail())
                .withClaim("version", "1.0.0")
                .withExpiresAt(new Date(System.currentTimeMillis() + AuthConstants.EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(AuthConstants.SECRET.getBytes()));
        if (json) {
            ser = new UserJsonSerializer(User.class, false, token);

            try {
                result = JsonUtiles.getObjectMapper(User.class, ser, null).writeValueAsString(user);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            return new ResponseEntity<String>(result, HttpStatus.OK);
        }
        return new ResponseEntity<String>(token, HttpStatus.OK);
    }

    @SneakyThrows
    @Operation(operationId = "auth-register", summary = "Este servicio registra un usuario.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario registrado correctamente."),
            @ApiResponse(responseCode = "409", description = "Conflict"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @PostMapping(value = Constants.URL_REGISTER, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(@RequestBody User user) {
        try {
            user.setRoles(new HashSet<>());
            User result = userBusiness.add(user);
            HttpHeaders responseHeaders = new HttpHeaders();
            return new ResponseEntity<>(responseHeaders, HttpStatus.CREATED);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (BusinessException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }


}
