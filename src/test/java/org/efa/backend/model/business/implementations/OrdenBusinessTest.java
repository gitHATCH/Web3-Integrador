package org.efa.backend.model.business.implementations;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.efa.backend.model.*;
import org.efa.backend.model.business.exceptions.BusinessException;
import org.efa.backend.model.business.exceptions.FoundException;
import org.efa.backend.model.business.exceptions.NotFoundException;
import org.efa.backend.model.persistence.OrdenRepository;
import org.springframework.dao.DuplicateKeyException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class OrdenBusinessTest {

    @InjectMocks
    private OrdenBusiness ordenBusiness;

    @Mock
    private OrdenRepository ordenRepository;

    @Mock
    private CamionBusiness camionBusiness;
    @Mock
    private ChoferBusiness choferBusiness;
    @Mock
    private ClienteBusiness clienteBusiness;
    @Mock
    private ProductoBusiness productoBusiness;
    @Mock
    private CisternadoBusiness cisternadoBusiness;

//    @Test
//    public void checkout_OK() throws BusinessException, NotFoundException, JsonProcessingException {
//        //given
//        Orden orden = Orden.builder().numeroOrden(1L).build();
//        given(ordenBusiness.load(any(Long.class))).willReturn(orden);
//        given(ordenRepository.save(any(Orden.class))).willReturn(Orden.builder().numeroOrden(1L).build());
//
//        // when
//        String result = ordenBusiness.checkOut("test123", 1L);
//
//        // then
//        then(result).isNotNull();
//    }

//    @Test
//    public void conciliacion_OK() throws BusinessException, NotFoundException, JsonProcessingException {
//        //given
//        long numeroOrden = 1;
//        Orden orden = Orden.builder().numeroOrden(numeroOrden).build();
//        given(ordenBusiness.load(numeroOrden)).willReturn(orden);
//
//        //when
//        String result = ordenBusiness.conciliacion(1);
//        //then
//        then(result).isNotNull();
//    }

    @Test
    public void load_OK() throws BusinessException, NotFoundException {
        //given
        long numeroOrden = 1;
        Orden orden = Orden.builder().numeroOrden(numeroOrden).build();
        given(ordenRepository.findByNumeroOrden(numeroOrden)).willReturn(Optional.ofNullable(orden));

        //when
        Orden result = ordenBusiness.load(orden.getNumeroOrden());
        //then
        then(result).isNotNull();
    }

    @Test
    public void load_ErrorNoOrden() {
        //given
        long numeroOrden = 1;
        Orden orden = Orden.builder().numeroOrden(numeroOrden).build();
        given(ordenRepository.findByNumeroOrden(numeroOrden)).willReturn(Optional.empty());

        //When
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> ordenBusiness.load(orden.getNumeroOrden()));
        //Then
        then(thrown.getMessage()).isEqualTo("No se encuentra la orden con id " + numeroOrden);
    }

    @Test
    public void load_BusinessException() {
        //given
        given(ordenRepository.findByNumeroOrden(any(Long.class))).willThrow(RuntimeException.class);

        //When
        BusinessException thrown = assertThrows(BusinessException.class, () -> ordenBusiness.load(1));
        //Then
        then(thrown.getMessage());
    }

    @Test
    public void list_OK() throws BusinessException {
        //given
        given(ordenRepository.findAll()).willReturn(List.of());

        //when
        List<Orden> result = ordenBusiness.list();
        //then
        then(result).isNotNull();
    }

    @Test
    public void list_EmptyList() throws BusinessException {
        //given
        given(ordenRepository.findAll()).willReturn(Collections.emptyList());

        //when
        List<Orden> result = ordenBusiness.list();
        //then
        then(result).isNotNull().isEmpty();
    }

    @Test
    public void list_Error() {
        // given
        given(ordenRepository.findAll()).willThrow(DuplicateKeyException.class);

        //When
        BusinessException thrown = assertThrows(BusinessException.class, () -> ordenBusiness.list());
        //Then
        then(thrown.getMessage()).isEqualTo("Error al traer lista de las Ordenes.");
    }

    @Test
    public void add_OK() throws BusinessException, NotFoundException, FoundException {
        //given
        long numeroOrden = 1;
        Orden orden = Orden.builder().numeroOrden(numeroOrden).build();
//        given(ordenRepository.findByNumeroOrden(any(Long.class))).willReturn(Optional.ofNullable(Orden.builder().build()));
        given(camionBusiness.exists(any(String.class))).willReturn(true);

        Camion camion = Camion.builder().patente("test").code("test").build();
        given(camionBusiness.add(any(Camion.class))).willReturn(camion);
        orden.setCamion(camion);

        Chofer chofer = Chofer.builder().dni(123).code("test").nombre("test").apellido("test").build();
        given(choferBusiness.add(any(Chofer.class))).willReturn(chofer);
        orden.setChofer(chofer);

        Cliente cliente = Cliente.builder().razonSocial(1).code("test").build();
        given(clienteBusiness.add(any(Cliente.class))).willReturn(cliente);
        orden.setCliente(cliente);

        Producto producto = Producto.builder().id(1).code("test").build();
        given(productoBusiness.add(any(Producto.class))).willReturn(producto);
        orden.setProducto(producto);

        orden.setEstado(1);
        given(ordenRepository.save(any(Orden.class))).willReturn(Orden.builder().build());

        //when
        Orden result = ordenBusiness.add(orden);
        //then
        then(result).isNotNull();
    }

    //    @Test
//    public void add_OK_Mutation() throws BusinessException, NotFoundException, FoundException {
//        //given
//        long numeroOrden = 1;
//        Orden orden = Orden.builder().numeroOrden(numeroOrden).build();
//        given(ordenRepository.findByNumeroOrden(any(Long.class))).willReturn(Optional.ofNullable(Orden.builder().build()));
//        given(camionBusiness.exists(any(String.class))).willReturn(false);
//
//        Camion camion = Camion.builder().patente("test").code("test").build();
//        given(camionBusiness.add(any(Camion.class))).willReturn(camion);
//        Cisternado cisternado = Cisternado.builder().id_cisterna(1).build();
//        cisternado.setCamion(camion);
//        cisternadoBusiness.add(cisternado);
//
//        Chofer chofer = Chofer.builder().dni(123).code("test").nombre("test").apellido("test").build();
//        given(choferBusiness.add(any(Chofer.class))).willReturn(chofer);
//        orden.setChofer(chofer);
//
//        Cliente cliente = Cliente.builder().razonSocial(1).code("test").build();
//        given(clienteBusiness.add(any(Cliente.class))).willReturn(cliente);
//        orden.setCliente(cliente);
//
//        Producto producto = Producto.builder().id(1).code("test").build();
//        given(productoBusiness.add(any(Producto.class))).willReturn(producto);
//        orden.setProducto(producto);
//
//        given(ordenRepository.save(any(Orden.class))).willReturn(Orden.builder().build());
//
//        //when
//        Orden result = ordenBusiness.add(orden);
//        //then
//        then(result).isNotNull();
//    }
    @Test
    public void add_ErrorOrdenExistente() {
        //given
        long numeroOrden = 1;
        Orden orden = Orden.builder().numeroOrden(numeroOrden).build();
        given(ordenRepository.findByNumeroOrden(any(Long.class))).willReturn(Optional.ofNullable(Orden.builder().build()));

        //When
        FoundException thrown = assertThrows(FoundException.class, () -> ordenBusiness.add(orden));
        //Then
        then(thrown.getMessage()).isEqualTo("Ya hay una orden con el nro " + orden.getNumeroOrden());
    }

    @Test
    public void add_ErrorCamion() {
        //given
        long numeroOrden = 1;
        Orden orden = Orden.builder().numeroOrden(numeroOrden).build();
//        given(ordenRepository.findByNumeroOrden(any(Long.class))).willReturn(Optional.ofNullable(Orden.builder().build()));

        //When
        BusinessException thrown = assertThrows(BusinessException.class, () -> ordenBusiness.add(orden));
        //Then
        then(thrown.getMessage()).isEqualTo("Error al crear el camion, en orden.");
    }

    @Test
    public void add_ErrorChofer() throws FoundException, BusinessException, NotFoundException {
        //given
        long numeroOrden = 1;
        Orden orden = Orden.builder().numeroOrden(numeroOrden).build();
//        given(ordenRepository.findByNumeroOrden(any(Long.class))).willReturn(Optional.ofNullable(Orden.builder().build()));
        given(camionBusiness.exists(any(String.class))).willReturn(true);

        Camion camion = Camion.builder().patente("test").code("test").build();
        given(camionBusiness.add(any(Camion.class))).willReturn(camion);
        orden.setCamion(camion);

        Chofer chofer = Chofer.builder().dni(123).code("test").nombre("test").apellido("test").build();
        given(choferBusiness.add(any(Chofer.class))).willThrow(RuntimeException.class);
        orden.setChofer(chofer);
        //When
        BusinessException thrown = assertThrows(BusinessException.class, () -> ordenBusiness.add(orden));
        //Then
        then(thrown.getMessage()).isEqualTo("Error al crear el chofer, en orden.");
    }

    @Test
    public void add_ErrorCliente() throws FoundException, BusinessException, NotFoundException {
        //given
        long numeroOrden = 1;
        Orden orden = Orden.builder().numeroOrden(numeroOrden).build();
//        given(ordenRepository.findByNumeroOrden(any(Long.class))).willReturn(Optional.ofNullable(Orden.builder().build()));
        given(camionBusiness.exists(any(String.class))).willReturn(true);

        Camion camion = Camion.builder().patente("test").code("test").build();
        given(camionBusiness.add(any(Camion.class))).willReturn(camion);
        orden.setCamion(camion);

        Chofer chofer = Chofer.builder().dni(123).code("test").nombre("test").apellido("test").build();
        given(choferBusiness.add(any(Chofer.class))).willReturn(chofer);
        orden.setChofer(chofer);

        Cliente cliente = Cliente.builder().razonSocial(1).code("test").build();
        given(clienteBusiness.add(any(Cliente.class))).willThrow(RuntimeException.class);
        orden.setCliente(cliente);
        //When
        BusinessException thrown = assertThrows(BusinessException.class, () -> ordenBusiness.add(orden));
        //Then
        then(thrown.getMessage()).isEqualTo("Error al crear el cliente, en orden.");
    }

    @Test
    public void add_ErrorProducto() throws FoundException, BusinessException, NotFoundException {
        //given
        long numeroOrden = 1;
        Orden orden = Orden.builder().numeroOrden(numeroOrden).build();
//        given(ordenRepository.findByNumeroOrden(any(Long.class))).willReturn(Optional.ofNullable(Orden.builder().build()));
        given(camionBusiness.exists(any(String.class))).willReturn(true);

        Camion camion = Camion.builder().patente("test").code("test").build();
        given(camionBusiness.add(any(Camion.class))).willReturn(camion);
        orden.setCamion(camion);

        Chofer chofer = Chofer.builder().dni(123).code("test").nombre("test").apellido("test").build();
        given(choferBusiness.add(any(Chofer.class))).willReturn(chofer);
        orden.setChofer(chofer);

        Cliente cliente = Cliente.builder().razonSocial(1).code("test").build();
        given(clienteBusiness.add(any(Cliente.class))).willReturn(cliente);
        orden.setCliente(cliente);

        Producto producto = Producto.builder().id(1).code("test").build();
        given(productoBusiness.add(any(Producto.class))).willThrow(RuntimeException.class);
        orden.setProducto(producto);
        //When
        BusinessException thrown = assertThrows(BusinessException.class, () -> ordenBusiness.add(orden));
        //Then
        then(thrown.getMessage()).isEqualTo("Error al crear el producto, en orden.");
    }

    @Test
    public void add_ErrorOrden() throws FoundException, BusinessException, NotFoundException {
        //given
        long numeroOrden = 1;
        Orden orden = Orden.builder().numeroOrden(numeroOrden).build();
//        given(ordenRepository.findByNumeroOrden(any(Long.class))).willReturn(Optional.ofNullable(Orden.builder().build()));
        given(camionBusiness.exists(any(String.class))).willReturn(true);

        Camion camion = Camion.builder().patente("test").code("test").build();
        given(camionBusiness.add(any(Camion.class))).willReturn(camion);
        orden.setCamion(camion);

        Chofer chofer = Chofer.builder().dni(123).code("test").nombre("test").apellido("test").build();
        given(choferBusiness.add(any(Chofer.class))).willReturn(chofer);
        orden.setChofer(chofer);

        Cliente cliente = Cliente.builder().razonSocial(1).code("test").build();
        given(clienteBusiness.add(any(Cliente.class))).willReturn(cliente);
        orden.setCliente(cliente);

        Producto producto = Producto.builder().id(1).code("test").build();
        given(productoBusiness.add(any(Producto.class))).willReturn(producto);
        orden.setProducto(producto);

        orden.setEstado(1);
        given(ordenRepository.save(any(Orden.class))).willThrow(RuntimeException.class);
        //When
        BusinessException thrown = assertThrows(BusinessException.class, () -> ordenBusiness.add(orden));
        //Then
        then(thrown.getMessage()).isEqualTo("Error en creacion de la orden.");
    }

//    @Test
//    public void aceptarAlarma_OK() throws BusinessException, NotFoundException {
//        //given
//        long numeroOrden = 1;
//        Orden orden = Orden.builder().numeroOrden(numeroOrden).build();
//        given(ordenBusinessMock.load(numeroOrden)).willReturn(orden);
//        orden.setAlarma(false);
//
//        given(ordenRepository.save(any(Orden.class))).willReturn(orden);
//        //when
//        ordenBusiness.aceptarAlarma(numeroOrden);
//        //then
//    }
}

