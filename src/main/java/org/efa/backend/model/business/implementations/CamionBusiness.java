package org.efa.backend.model.business.implementations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.efa.backend.model.Camion;
import org.efa.backend.model.Cisternado;
import org.efa.backend.model.business.exceptions.BusinessException;
import org.efa.backend.model.business.exceptions.FoundException;
import org.efa.backend.model.business.exceptions.NotFoundException;
import org.efa.backend.model.business.interfaces.ICamionBusiness;
import org.efa.backend.model.persistence.CamionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CamionBusiness implements ICamionBusiness {


    private final CamionRepository camionRepository;

    private final CisternadoBusiness cisternadoBusiness;

    /**
     * Este método se utiliza para obtener un objeto Camion basado en el código proporcionado.
     *
     * @param code Este es el identificador único para el objeto Camion.
     * @return Camion Este devuelve el objeto Camion asociado con el código proporcionado.
     * @throws NotFoundException Esta excepción se lanza cuando no se encuentra un objeto Camion con el código proporcionado.
     * @throws BusinessException Esta excepción se lanza cuando hay un error en la lógica de negocio o en el acceso a la base de datos.
     */
    @Override
    public Camion load(String code) throws NotFoundException, BusinessException {
        Optional<Camion> camion;
        try {
            camion = camionRepository.findByCode(code);
        } catch (Exception e) {
            throw BusinessException.builder().ex(e).build();
        }
        if (camion.isEmpty()) {
            throw NotFoundException.builder().message("No se encuentra el camion con CODIGO: " + code).build();
        }
        return camion.get();
    }

    @Override
    public Boolean exists(String code) {
        return camionRepository.existsByCode(code);
    }

    @Override
    public List<Camion> list() throws BusinessException {
        try {
            return camionRepository.findAll();
        } catch (Exception e) {
            throw BusinessException.builder().message("Error al traer todos los Camiones.").build();
        }
    }

    @Override
    public Camion add(Camion camion) throws FoundException, BusinessException, NotFoundException {
        if (camionRepository.existsByCode(camion.getCode())) {
//                return load(camion.getCode());
            throw FoundException.builder().message("Ya existe un camion con el CODIGO: " + camion.getCode()).build();
        }
        try {
            Camion result = camionRepository.save(camion);
            for (Cisternado cisternado : camion.getDatosCisterna()){
                cisternado.setCamion(result);
                cisternadoBusiness.add(cisternado);
            }
            return result;
        } catch (Exception e) {
            throw BusinessException.builder().message("Error creacion de camion").build();
        }
    }
}
