package org.efa.backend;

import lombok.extern.slf4j.Slf4j;
import org.efa.backend.model.DetalleReciente;
import org.efa.backend.model.Orden;
import org.efa.backend.model.business.exceptions.BusinessException;
import org.efa.backend.model.business.exceptions.NotFoundException;
import org.efa.backend.model.business.implementations.MailBusiness;
import org.efa.backend.model.business.interfaces.IDetalleBusiness;
import org.efa.backend.model.business.interfaces.IOrdenBusiness;
import org.efa.backend.model.persistence.OrdenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableScheduling
@EnableAsync
@Slf4j
@Component
public class Scheduler {

    @Autowired
    private IDetalleBusiness detalleBusiness;
    @Autowired
    private IOrdenBusiness ordenBusiness;
    @Autowired
    private OrdenRepository ordenRepository;
    @Autowired
    private MailBusiness mailBusiness;

    @Value("${temperatura.umbral}")
    private float temperaturaUmbral;

    @Async
    @Scheduled(fixedDelayString = "${detalle.async:120}", initialDelay = 30, timeUnit = TimeUnit.SECONDS)
    public void detalles() throws BusinessException, NotFoundException {
        log.info("Guardando los detalles...");

        // accedemos al Map que tiene <orden , detalle recientes>
        Map<Long, DetalleReciente> ordenes = detalleBusiness.getOrdenes();

        for (Map.Entry<Long, DetalleReciente> entry : ordenes.entrySet()) {
            Long ordenId = entry.getKey();
            DetalleReciente detalle = entry.getValue();

            detalleBusiness.add(detalle.getDetalleReciente(), ordenId);

            Orden orden = ordenBusiness.load(ordenId);
            if (!orden.isAlarma()){ // alarma no fue aceptada aun
                if (detalle.getDetalleReciente().getTemperatura() > orden.getTemperaturaUmbral()) {
                    mailBusiness.sendSimpleMessageToAll(detalle.getDetalleReciente().getTemperatura());
                }
                orden.setAlarma(true);
                ordenRepository.save(orden);
            }
        }

        detalleBusiness.borrarMapaOrdenes();
    }
}
