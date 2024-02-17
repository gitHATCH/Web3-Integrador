package org.efa.backend.auth;

import lombok.extern.slf4j.Slf4j;
import org.efa.backend.model.business.exceptions.BusinessException;
import org.efa.backend.model.business.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class RoleBusiness implements IRoleBusiness{
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role add(Role role) throws NotFoundException, BusinessException {
        try {
            Role existingRole = load(role.getName());
            return existingRole;
        } catch (NotFoundException e) {
            // el rol no existe, se crea uno nuevo
            return roleRepository.save(role);
        } catch (Exception e) {
            throw BusinessException.builder().message("Error creación de rol.").build();
        }
    }

    @Override
    public Role load(String name) throws NotFoundException, BusinessException {
        Optional<Role> ou;
        try {
            ou = roleRepository.findOneByName(name);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
        if (ou.isEmpty()) {
            throw NotFoundException.builder().message("No se encuentra el rol con name: " + name)
                    .build();
        }
        return ou.get();
    }
}
