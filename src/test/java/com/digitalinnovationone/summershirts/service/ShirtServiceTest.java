package com.digitalinnovationone.summershirts.service;

import com.digitalinnovationone.summershirts.builder.ShirtDTOBuilder;
import com.digitalinnovationone.summershirts.dto.ShirtDTO;
import com.digitalinnovationone.summershirts.entity.Shirt;
import com.digitalinnovationone.summershirts.exception.ShirtNotFoundException;
import com.digitalinnovationone.summershirts.exception.ShirtWithThisModelAlreadyRegisteredException;
import com.digitalinnovationone.summershirts.repository.ShirtRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static java.util.Optional.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ShirtServiceTest {

    @Mock
    private ShirtRepository shirtRepository;

    @InjectMocks
    private ShirtService shirtService;

    // It was made this way because I can't use 'MapStruct' or 'ModelMapper' for the conversion of the objects
    ShirtDTO shirtDTO = ShirtDTOBuilder.builder().build().toShirtDTO();
    Shirt shirt = new Shirt(shirtDTO.getId(), shirtDTO.getBrand(), shirtDTO.getMax(), shirtDTO.getQuantity(), shirtDTO.getModel());

    @Test
    void whenNewShirtInformedThenShouldBeCreated() throws ShirtWithThisModelAlreadyRegisteredException {
        Shirt expectedSavedShirt = shirt;

        when(shirtRepository.findByModel(shirtDTO.getModel())).thenReturn(empty()); //testing if the exception is being throwing
        when(shirtRepository.save(expectedSavedShirt)).thenReturn(expectedSavedShirt); // testing if the object is being saved

        ShirtDTO createdShirtDTO = shirtService.createShirt(shirtDTO);

        assertEquals(shirtDTO.getId(), createdShirtDTO.getId()); // confirming if the objects is equals
    }

    @Test
    void whenAlreadyRegisteredShirtInformedThenReturnAnExceptionShouldBeThrown() {
        Shirt duplicatedShirt = shirt;

        when(shirtRepository.findByModel(shirtDTO.getModel())).thenReturn(of(duplicatedShirt)); // testing to throw exception

        assertThrows(ShirtWithThisModelAlreadyRegisteredException.class, () -> shirtService.createShirt(shirtDTO)); // confirming the exception when trying to create
    }

    @Test
    void whenValidShirtModelIsGivenThenReturnAShirt() throws ShirtNotFoundException {
        ShirtDTO expectedShirtDTO = shirtDTO;
        Shirt expectedFoundShirt = shirt;

        when(shirtRepository.findByModel(expectedShirtDTO.getModel())).thenReturn(Optional.of(expectedFoundShirt)); // return a shirt when you to find for a model

        ShirtDTO foundShirtDTO = shirtService.findByModel(expectedShirtDTO.getModel());

        assertEquals(expectedShirtDTO, foundShirtDTO); // confirming if the objects is equals
    }

    @Test
    void whenNotRegisteredShirtModelIsGivenThenThrowAnException() {
        ShirtDTO expectedShirDTO = shirtDTO;

        when(shirtRepository.findByModel(expectedShirDTO.getModel())).thenReturn(Optional.empty()); // return an empty search

        assertThrows(ShirtNotFoundException.class, () -> shirtService.findByModel(expectedShirDTO.getModel())); // verifying if the exception is being throwing
    }

    @Test
    void whenListShirtIsCalledThenReturnAListOfShirts() {
        ShirtDTO expectedShirtDTO = shirtDTO; // You also can use the variables that be in the start of the code if you don't want to instance it again like I do
        Shirt expectedFoundShirt = shirt;

        when(shirtRepository.findAll()).thenReturn(singletonList(expectedFoundShirt));

        List<ShirtDTO> foundsShirtDTO = shirtService.listAll();

        assertFalse(foundsShirtDTO.isEmpty());
        assertEquals(expectedShirtDTO, foundsShirtDTO.get(0));
    }

    @Test
    void whenListIsCalledThenReturnAnEmptyList() {
        when(shirtRepository.findAll()).thenReturn(Collections.EMPTY_LIST);

        List<ShirtDTO> listDTO = shirtService.listAll();

        assertTrue(listDTO.isEmpty());
    }
}
