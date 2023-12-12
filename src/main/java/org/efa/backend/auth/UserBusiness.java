package org.efa.backend.auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import org.efa.backend.exceptions.custom.BusinessException;
import org.efa.backend.exceptions.custom.NotFoundException;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserBusiness implements IUserBusiness{

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

    // primer metodo carga una cuenta por el username o email

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

    // intentamos cargar el user, y luego usamos el password encoder para ver si coincide la password vieja con la provista
    //el metodo encode del codificador del password que genera el hash
    // aqui se podria complicar la generacion del password con las reglas que nosotros quisieramos

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

}
