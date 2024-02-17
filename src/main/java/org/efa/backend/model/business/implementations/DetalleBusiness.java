package org.efa.backend.model.business.implementations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.efa.backend.model.Detalle;
import org.efa.backend.model.DetalleReciente;
import org.efa.backend.model.Orden;
import org.efa.backend.model.business.exceptions.BusinessException;
import org.efa.backend.model.business.exceptions.NotAuthorizedException;
import org.efa.backend.model.business.exceptions.NotFoundException;
import org.efa.backend.model.business.interfaces.IDetalleBusiness;
import org.efa.backend.model.persistence.DetalleRepository;
import org.efa.backend.model.persistence.OrdenRepository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class DetalleBusiness implements IDetalleBusiness {

    private final DetalleRepository detalleRepository;
    private final OrdenRepository ordenRepository;

    private Map<Long, DetalleReciente> ordenes = new HashMap<>();

    @Override
    public Detalle load(long rs) throws BusinessException, NotFoundException {
        Optional<Detalle> detalle;
        try {
            detalle = detalleRepository.findById(rs);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }

        if (detalle.isEmpty()) {
            throw NotFoundException.builder().message("No se encontro el detalle: " + rs).build();
        }

        return detalle.get();

    }

    @Override
    public List<Detalle> list() throws BusinessException {
        try {
            return detalleRepository.findAll();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
    }

    @Override
    public List<Detalle> listByNumeroOrden(long numeroOrden) throws BusinessException {
        try {
            return detalleRepository.findAllById_NumeroOrden(numeroOrden);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
    }


    @Override
    public void add(Detalle detalle, long numeroOrden) throws BusinessException {

        Optional<Orden> orden = ordenRepository.findByNumeroOrden(numeroOrden);

        try {
            if (!detalleRepository.existsDetalleByOrden_numeroOrden(numeroOrden)) {
                orden.get().setFechaDetalleInicial(OffsetDateTime.now());
            }
            if (detalle.getMasa() <= orden.get().getPreset()) {
                orden.get().setFechaDetalleFinal(OffsetDateTime.now());
                //Asignamos la fecha de detalle y la orden
                detalle.setFechaDetalle(OffsetDateTime.now());
                orden.get().setUltimaMasa(detalle.getMasa());
                orden.get().setUltimaDensidad(detalle.getDensidad());
                orden.get().setUltimaTemperatura(detalle.getTemperatura());
                orden.get().setUltimoCaudal(detalle.getCaudal());
                detalle.setOrden(orden.get());

            }
            ordenRepository.save(orden.get());
            detalleRepository.save(detalle);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().message("Error creacion de Detalle.").build();
        }
    }

    @Override
    public void procesarDetalle(Detalle detalle, long numeroOrden, int password) throws NotFoundException, BusinessException, NotAuthorizedException {

        Optional<Orden> orden = ordenRepository.findByNumeroOrden(numeroOrden);
        if (orden.isEmpty()) {
            throw NotFoundException.builder().message("No se encontro la orden a cargar: " + numeroOrden).build();
        }
        if (orden.get().getEstado() != 2) {
            throw BusinessException.builder().message("Error, orden no disponible para la carga.").build();
        }
        if (orden.get().getPassword() != password) {
            throw NotAuthorizedException.builder().message("Password Incorrecta.").build();
        }
        if (detalle.getCaudal() <= 0) {
            throw BusinessException.builder().message("Valor de Caudal no valido.").build();
        }
        if (Float.compare(detalle.getMasa(), orden.get().getUltimaMasa()) <= 0) {
            throw BusinessException.builder().message("Error, valor de masa inferior al ultimo cargado.").build();
        }
        if (detalle.getMasa() > orden.get().getPreset()) {
            throw BusinessException.builder().message("Error, valor de masa superior al Preset.").build();
        }

        DetalleReciente detalleReciente = ordenes.get(numeroOrden);
        if (detalleReciente == null) {
            detalleReciente = new DetalleReciente();
            ordenes.put(numeroOrden, detalleReciente);
        }

        detalleReciente.setDetalleReciente(detalle);
    }

    @Override
    public Map<Long, DetalleReciente> getOrdenes() {
        // devuelve una copia del map que tiene los detalles recientes.
        return new HashMap<>(ordenes);
    }

    @Override
    public void borrarMapaOrdenes() {
        ordenes.clear();
    }
}
