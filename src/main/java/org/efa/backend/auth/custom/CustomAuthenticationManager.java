package org.efa.backend.auth.custom;

import org.efa.backend.auth.IUserBusiness;
import org.efa.backend.auth.User;
import org.efa.backend.model.business.exceptions.BusinessException;
import org.efa.backend.model.business.exceptions.NotFoundException;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;

public class CustomAuthenticationManager implements AuthenticationManager {

    public CustomAuthenticationManager(PasswordEncoder pEncoder, IUserBusiness userBusiness) {
        this.pEncoder = pEncoder;
        this.userBusiness = userBusiness;
    }

    private IUserBusiness userBusiness;

    private PasswordEncoder pEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        User user = null;
        try {
            user = userBusiness.load(username);
        } catch (NotFoundException e) {
            throw new BadCredentialsException("No user registered with this details");
        } catch (BusinessException e) {
            e.printStackTrace();
            throw new AuthenticationServiceException(e.getMessage());
        }
        String validation = user.validate();
        if (validation.equals(User.VALIDATION_ACCOUNT_EXPIRED))
            throw new AccountExpiredException(User.VALIDATION_ACCOUNT_EXPIRED);
        if (validation.equals(User.VALIDATION_CREDENTIALS_EXPIRED))
            throw new CredentialsExpiredException(User.VALIDATION_CREDENTIALS_EXPIRED);
        if (validation.equals(User.VALIDATION_DISABLED))
            throw new DisabledException(User.VALIDATION_DISABLED);
        if (validation.equals(User.VALIDATION_LOCKED))
            throw new LockedException(User.VALIDATION_LOCKED);

        if (!pEncoder.matches(password, user.getPassword()))
            throw new BadCredentialsException("Invalid password");

        return new UsernamePasswordAuthenticationToken(user, null);

    }

    public Authentication AuthWrap(String name, String pass) {
        return new Authentication() {
            @Override
            public String getName() {
                return name;
            }

            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
            }

            @Override
            public boolean isAuthenticated() {
                return false;
            }

            @Override
            public Object getPrincipal() {
                return null;
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public Object getCredentials() {
                return pass;
            }

            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }
        };
    }

}