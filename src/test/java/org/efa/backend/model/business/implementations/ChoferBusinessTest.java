package org.efa.backend.model.business.implementations;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.efa.backend.model.Chofer;
import org.efa.backend.model.business.exceptions.BusinessException;
import org.efa.backend.model.business.exceptions.FoundException;
import org.efa.backend.model.business.exceptions.NotFoundException;
import org.efa.backend.model.persistence.ChoferRepository;
import org.springframework.dao.DuplicateKeyException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ChoferBusinessTest {

    @InjectMocks
    private ChoferBusiness choferBusiness;

    @Mock
    private ChoferRepository choferRepository;

    @Test
    public void add_OK() throws FoundException, BusinessException, NotFoundException {
        // given
        Chofer chofer = Chofer.builder().dni(1234).code("test123").build();
        given(choferRepository.existsByCode(any(String.class))).willReturn(false);
//        given(choferRepository.existsByCode(any(String.class))).willReturn(false);
        given(choferRepository.save(any(Chofer.class))).willReturn(Chofer.builder().dni(4223423).build());


        // when
        Chofer result = choferBusiness.add(chofer);

        // then
        then(result).isNotNull();
    }

    @Test
    public void add_OK_True() throws FoundException, BusinessException, NotFoundException {
        // given
        Chofer chofer = Chofer.builder().dni(1234).code("test123").build();
//        given(choferRepository.existsByCode(any(String.class))).willReturn(true);
        given(choferRepository.existsByCode(any(String.class))).willReturn(false);
        given(choferRepository.save(any(Chofer.class))).willReturn(Chofer.builder().dni(4223423).build());

        // when
        Chofer result = choferBusiness.add(chofer);
        // then
        then(result).isNotNull();
    }

    @Test
    public void add_Error_NotExistsCode() {
        // given
        Chofer chofer = Chofer.builder().dni(1234).code("test123").build();
        given(choferRepository.existsByCode(any(String.class))).willReturn(false);
        given(choferRepository.save(any(Chofer.class))).willThrow(RuntimeException.class);
        //When
        BusinessException thrown = assertThrows(BusinessException.class, () -> choferBusiness.add(chofer));
        //Then
        then(thrown.getMessage()).isEqualTo("Error de creacion de chofer");
    }

    @Test
    public void load_OK() throws BusinessException, NotFoundException {
        // Given
        String code = "codigo";
        given(choferRepository.findByCode(any(String.class))).willReturn(Optional.ofNullable(Chofer.builder().dni(123).code("test").build()));

        // When
        Chofer result = choferBusiness.load(code);

        // Then
        then(result).isNotNull();
    }

    @Test
    public void load_Error() {
        // Given
        String code = "codigo";
        given(choferRepository.findByCode(any(String.class))).willThrow(DuplicateKeyException.class);

        //When
        BusinessException thrown = assertThrows(BusinessException.class, () -> choferBusiness.load(code));
        //Then
        then(thrown.getMessage());
    }

    @Test
    public void list_OK() throws BusinessException {
        // given
        given(choferRepository.findAll()).willReturn(List.of());

        // when
        List<Chofer> result = choferBusiness.list();
        // then
        then(result).isNotNull();
    }

    @Test
    public void list_EmptyList() throws BusinessException{
        // given
        given(choferRepository.findAll()).willReturn(Collections.emptyList());

        // when
//        List<Chofer> result = choferBusiness.list();
        // then
//        then(result.equals(Collections.emptyList()));
        assertEquals(Collections.emptyList(), choferBusiness.list());
    }

    @Test
    public void list_Error() {
        // given
        given(choferRepository.findAll()).willThrow(DuplicateKeyException.class);

        //When
        BusinessException thrown = assertThrows(BusinessException.class, () -> choferBusiness.list());
        //Then
        then(thrown.getMessage());
    }
}
