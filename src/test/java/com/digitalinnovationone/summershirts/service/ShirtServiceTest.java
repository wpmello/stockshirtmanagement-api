package com.digitalinnovationone.summershirts.service;

import com.digitalinnovationone.summershirts.builder.ShirtDTOBuilder;
import com.digitalinnovationone.summershirts.dto.ShirtDTO;
import com.digitalinnovationone.summershirts.entity.Shirt;
import com.digitalinnovationone.summershirts.exception.ShirtWithThisModelAlreadyRegisteredException;
import com.digitalinnovationone.summershirts.repository.ShirtRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ShirtServiceTest {

    @Mock
    private ShirtRepository shirtRepository;

    @InjectMocks
    private ShirtService shirtService;

    // It was made this way because I can't use 'MapStruct' or 'ModelMapper' to do the conversion of the objects
    private Shirt shirt;
    private ShirtDTO shirtDTO;

    @Test
    void whenNewShirtInformedThenShouldBeCreated() throws ShirtWithThisModelAlreadyRegisteredException {
        ShirtDTO shirtDTO = ShirtDTOBuilder.builder().build().toShirtDTO();
        shirt = new Shirt(shirtDTO.getId(), shirtDTO.getBrand(), shirtDTO.getMax(), shirtDTO.getQuantity(), shirtDTO.getModel());

        when(shirtRepository.findByModel(shirtDTO.getModel())).thenReturn(Optional.empty()); //testing if the exception is being throwing
        when(shirtRepository.save(shirt)).thenReturn(shirt); // testing if the object is being saved

        ShirtDTO createdShirtDTO = shirtService.createShirt(shirtDTO);

        assertEquals(shirtDTO.getId(), createdShirtDTO.getId()); // confirming if the objects is equals
    }

    @Test
    void whenAlreadyRegisteredShirtInformedThenReturnAnExceptionShouldBeThrown() {
        shirtDTO = ShirtDTOBuilder.builder().build().toShirtDTO();
        shirt = new Shirt(shirtDTO.getId(), shirtDTO.getBrand(), shirtDTO.getMax(), shirtDTO.getQuantity(), shirtDTO.getModel());

        when(shirtRepository.findByModel(shirtDTO.getModel())).thenReturn(Optional.of(shirt)); // testing to throw exception

        assertThrows(ShirtWithThisModelAlreadyRegisteredException.class, () -> shirtService.createShirt(shirtDTO)); // confirming the exception when trying to create
    }
}
