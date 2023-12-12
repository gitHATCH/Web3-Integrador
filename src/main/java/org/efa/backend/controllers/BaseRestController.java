package org.efa.backend.controllers;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.efa.backend.auth.User;

public class BaseRestController {
    protected User getUserLogged() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        return user;
    }
}
