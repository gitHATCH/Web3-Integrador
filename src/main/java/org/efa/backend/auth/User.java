package org.efa.backend.auth;


import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.efa.backend.model.Alarma;
import org.efa.backend.model.Cisternado;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User implements UserDetails {

    private static final long serialVersionUID = 1181473083412294679L;

    @Column(columnDefinition = "tinyint default 0")
    private boolean accountNonExpired = true;

    @Column(columnDefinition = "tinyint default 0")
    private boolean accountNonLocked = true;

    @Column(columnDefinition = "tinyint default 0")
    private boolean credentialsNonExpired = true;

    @Column(columnDefinition = "tinyint default 0")
    private boolean enabled;

    public static final String VALIDATION_OK = "OK";
    public static final String VALIDATION_ACCOUNT_EXPIRED = "ACCOUNT_EXPIRED";
    public static final String VALIDATION_CREDENTIALS_EXPIRED = "CREDENTIALS_EXPIRED";
    public static final String VALIDATION_LOCKED = "LOCKED";
    public static final String VALIDATION_DISABLED = "DISABLED";

    public String validate() {
        if (!accountNonExpired)
            return VALIDATION_ACCOUNT_EXPIRED;
        if (!credentialsNonExpired)
            return VALIDATION_CREDENTIALS_EXPIRED;
        if (!accountNonLocked)
            return VALIDATION_LOCKED;
        if (!enabled)
            return VALIDATION_DISABLED;
        return VALIDATION_OK;
    }

    @Column(length = 255, nullable = false, unique = true)
    private String email;

    @Hidden
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUser;
    @Column(length = 100, unique = true)
    private String username;
    private String password;

    @OneToMany(mappedBy = "usuario", fetch = FetchType.EAGER)
    List<Alarma> datosAlarma;

    @Hidden
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "userroles", joinColumns = {
            @JoinColumn(name = "idUser", referencedColumnName = "idUser") }, inverseJoinColumns = {
            @JoinColumn(name = "idRole", referencedColumnName = "id") })
    private Set<Role> roles;

    @Transient
    public boolean isInRole(Role role) {
        return isInRole(role.getName());
    }

    @Transient
    public boolean isInRole(String role) {
        for (Role r : getRoles())
            if (r.getName().equals(role))
                return true;
        return false;
    }

    @Hidden
    @Transient
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
        return authorities;
    }

    @Hidden
    @Transient
    public List<String> getAuthoritiesStr() {
        List<String> authorities = getRoles().stream().map(role -> role.getName()).collect(Collectors.toList());
        return authorities;
    }

}
