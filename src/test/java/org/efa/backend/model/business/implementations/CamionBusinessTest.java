package org.efa.backend.model.business.implementations;

import org.efa.backend.model.Camion;
import org.efa.backend.model.business.exceptions.BusinessException;
import org.efa.backend.model.business.exceptions.FoundException;
import org.efa.backend.model.business.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.efa.backend.model.business.implementations.CamionBusiness;
import org.efa.backend.model.persistence.CamionRepository;

import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
//@SpringBootTest
public class CamionBusinessTest {

    @InjectMocks
    private CamionBusiness camionBusiness;

    @Mock
    private CamionRepository camionRepository;

    @Test
    public void not_exist(){
        // Given
        String code = "codigo";

        given(camionRepository.existsByCode(any(String.class))).willReturn(false);

        // When
        Boolean result = camionBusiness.exists(code);

        // Then
        then(result).isFalse();
    }

    @Test
    public void exist(){
        // Given
        String code = "codigo";

        given(camionRepository.existsByCode(any(String.class))).willReturn(true);

        // When
        Boolean result = camionBusiness.exists(code);

        // Then
        then(result).isTrue();
    }

    @Test
    public void get_OK() throws BusinessException, NotFoundException {
        // Given
        Camion camion = Camion.builder().code("code").patente("AAA123").build();
        given(camionRepository.findByCode(any(String.class))).willReturn(Optional.ofNullable(camion));

        // When
        Camion result = camionBusiness.load("code");
        // Then
        then(result).isNotNull();
    }

    @Test
    public void get_NotFound() {
        // Given
        given(camionRepository.findByCode(any(String.class))).willReturn(Optional.empty());
        //When
        NotFoundException thrown = org.junit.jupiter.api.Assertions.assertThrows(NotFoundException.class, () -> camionBusiness.load("code"));
        //Then
        then(thrown.getMessage()).isEqualTo("No se encuentra el camion con CODIGO: code");
    }

//    @Test
//    public void get_BusinessException() {
//        // Given
//        given(camionRepository.findByCode(any(String.class))).willThrow(new BusinessException());
//        //When
//        BusinessException thrown = org.junit.jupiter.api.Assertions.assertThrows(BusinessException.class, () -> camionBusiness.load("code"));
//        //Then
//        then(thrown.getMessage());
//    }

//    @Test
//    public void add_OK() throws BusinessException, NotFoundException, FoundException {
//        // given
//        Camion camion = Camion.builder().code("code").patente("AAA123").build();
//        given(camionRepository.existsByCode(any(String.class))).willReturn(false);
//        given(camionRepository.save(any(Camion.class))).willReturn(Camion.builder().code("code").patente("AAA123").build());
//
//        // when
//        Camion result = camionBusiness.add(camion);
//
//        // then
//        then(result).isNotNull();
//    }

}
