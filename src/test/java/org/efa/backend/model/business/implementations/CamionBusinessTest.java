package org.efa.backend.model.business.implementations;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.efa.backend.model.business.implementations.CamionBusiness;
import org.efa.backend.model.persistence.CamionRepository;

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
    public void get_OK(){
        // Given
        String code = "codigo";

        given(camionRepository.existsByCode(any(String.class))).willReturn(false);

        // When
        Boolean result = camionBusiness.exists(code);

        // Then
        then(result).isNotNull();
    }

}
