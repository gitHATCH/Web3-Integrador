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
    // detalle.async : 30 se establece una variable string con un valor de 30 que el timeUnit establece en segundos
    // por ende se ejecutara cada 30 segundos con un delay de 1 segundo
    @Async
    @Scheduled(fixedDelayString = "${detalle.async:30}", initialDelay = 1, timeUnit = TimeUnit.SECONDS)
    public void detalles() throws BusinessException, NotFoundException {
        log.info("Guardando los detalles...");

        // accedemos al Map que tiene <orden , detalle recientes>
        Map<Long, DetalleReciente> ordenes = detalleBusiness.getOrdenes();
        for (Map.Entry<Long, DetalleReciente> entry : ordenes.entrySet()) {
            Long ordenId = entry.getKey();
            DetalleReciente detalle = entry.getValue();

            detalleBusiness.add(detalle.getDetalleReciente(), ordenId);
            Orden orden = ordenBusiness.load(ordenId);

            // Verificamos si la alarma no se encuentra activada 'false'

            if (!orden.isAlarma()){

                // Verificamos si el ultimo detalle de la temperatura supera al umbral establecido

                if (detalle.getDetalleReciente().getTemperatura() > orden.getTemperaturaUmbral()) {
                    //mandamos mail estableciendo cual fue la temperatura
                    mailBusiness.sendSimpleMessageToAll(detalle.getDetalleReciente().getTemperatura());
                    // Activa la alarma
                    orden.setAlarma(true);
                    ordenRepository.save(orden);
                }

            }

        }

        detalleBusiness.borrarMapaOrdenes();
    }
}
