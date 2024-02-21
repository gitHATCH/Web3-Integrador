package org.efa.backend.model.business.implementations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.efa.backend.auth.UserBusiness;
import org.efa.backend.model.Alarma;
import org.efa.backend.model.Orden;
import org.efa.backend.model.business.exceptions.BusinessException;
import org.efa.backend.model.business.exceptions.FoundException;
import org.efa.backend.model.business.exceptions.NotFoundException;
import org.efa.backend.model.business.interfaces.IAlarmaBusiness;
import org.efa.backend.model.persistence.AlarmaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class AlarmaBusiness implements IAlarmaBusiness {

    private final AlarmaRepository alarmaRepository;
    @Autowired
    private OrdenBusiness ordenBusiness;

    @Autowired
    private UserBusiness userBusiness;

    @Override
    public Alarma add(long numeroOrden, String nombreUsuario) throws FoundException, BusinessException, NotFoundException {

//        Orden orden = ordenBusiness.load(numeroOrden);

        Alarma alarma = Alarma.builder()
                .fecha(OffsetDateTime.now())
                .numeroOrden(numeroOrden)
                .usuario(userBusiness.load(nombreUsuario))
                .build();

        return alarmaRepository.save(alarma);
    }


}
