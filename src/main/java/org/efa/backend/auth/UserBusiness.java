package org.efa.backend.auth;

import lombok.extern.slf4j.Slf4j;
import org.efa.backend.model.business.exceptions.BusinessException;
import org.efa.backend.model.business.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserBusiness implements IUserBusiness {

    @Autowired
    private UserRepository userDAO;

    @Override
    public User load(String usernameOrEmail) throws NotFoundException, BusinessException {
        Optional<User> ou;
        try {
            ou = userDAO.findOneByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
        if (ou.isEmpty()) {
            throw NotFoundException.builder().message("No se encuentra el usuari@ email o nombre =" + usernameOrEmail)
                    .build();
        }
        return ou.get();
    }

    @Override
    public void changePassword(String usernameOrEmail, String oldPassword, String newPassword, PasswordEncoder pEncoder)
            throws BadPasswordException, NotFoundException, BusinessException {
        User user = load(usernameOrEmail);
        if (!pEncoder.matches(oldPassword, user.getPassword())) {
            throw BadPasswordException.builder().build();
        }
        user.setPassword(pEncoder.encode(newPassword));
        try {
            userDAO.save(user);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
    }

    public static void getPassword(PasswordEncoder pEncoder) {
        User user = new User();
        user.setPassword(pEncoder.encode("admin"));
        System.out.println(user.getPassword());
    }

    @Override
    public void disable(String usernameOrEmail) throws NotFoundException, BusinessException {
        setDisable(usernameOrEmail, false);
    }

    @Override
    public void enable(String usernameOrEmail) throws NotFoundException, BusinessException {
        setDisable(usernameOrEmail, true);
    }

    private void setDisable(String usernameOrEmail, boolean enable) throws NotFoundException, BusinessException {
        User user = load(usernameOrEmail);
        user.setEnabled(enable);
        try {
            userDAO.save(user);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
    }

    @Override
    public List<User> list() throws BusinessException {
        try {
            return userDAO.findAll();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
    }

    @Override
    public User add(User user) throws NotFoundException, BusinessException {
        try{
            return userDAO.save(user);
        } catch (Exception e){
            throw BusinessException.builder().message("Error crecion de usuario").build();
        }
    }
}
