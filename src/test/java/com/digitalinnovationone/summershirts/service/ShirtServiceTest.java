package com.digitalinnovationone.summershirts.service;

import com.digitalinnovationone.summershirts.builder.ShirtDTOBuilder;
import com.digitalinnovationone.summershirts.dto.ShirtDTO;
import com.digitalinnovationone.summershirts.entity.Shirt;
import com.digitalinnovationone.summershirts.exception.ShirtNotFoundException;
import com.digitalinnovationone.summershirts.exception.ShirtStockExceededException;
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
import static java.util.Optional.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ShirtServiceTest {

    private static final long INVALID_SHIRT_ID = 1L;

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

        when(shirtRepository.findByModel(shirtDTO.getModel())).thenReturn(Optional.empty()); //testing if the exception is being throwing
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

    @Test
    void whenDeleteIsCalledWithValidIdThenAShirtShouldBeDeleted() throws ShirtNotFoundException {
        ShirtDTO expectedDeletedShirtDTO = shirtDTO;
        Shirt expectedDeletedShirt = shirt;

        when(shirtRepository.findById(expectedDeletedShirtDTO.getId())).thenReturn(Optional.of(expectedDeletedShirt));
        doNothing().when(shirtRepository).deleteById(expectedDeletedShirt.getId());

        shirtService.deleteById(expectedDeletedShirtDTO.getId());

        verify(shirtRepository, times(1)).findById(expectedDeletedShirtDTO.getId()); // it verifies how many times the method was called
        verify(shirtRepository, times(1)).deleteById(expectedDeletedShirt.getId()); // it verifies how many times the method was called
    }

    @Test
    void whenDeleteIsCalledWithInvalidIdThenExceptionShouldBeThrown() {
        when(shirtRepository.findById(INVALID_SHIRT_ID)).thenReturn(Optional.empty());

        assertThrows(ShirtNotFoundException.class, () -> shirtService.deleteById(INVALID_SHIRT_ID));
    }

    @Test
    void whenIncrementWithValidIdIsCalledThenIncrementShirtStock() throws ShirtNotFoundException, ShirtStockExceededException {
        ShirtDTO expectedShirtDTO = shirtDTO;
        Shirt expectedShirt = shirt;

        when(shirtRepository.findById(expectedShirtDTO.getId())).thenReturn(Optional.of(expectedShirt));

        int quantityToIncrement = 90;
        int expectedQuantityAfterIncrement = expectedShirtDTO.getQuantity() + quantityToIncrement;
        ShirtDTO incrementedShirDTO = shirtService.increment(expectedShirtDTO.getId(), quantityToIncrement);

        assertThat(expectedQuantityAfterIncrement, equalTo(incrementedShirDTO.getQuantity())); // with hamcrest here I'm testing if quantity from A is equal quantity from B
        assertThat(expectedQuantityAfterIncrement, lessThanOrEqualTo(expectedShirtDTO.getMax())); // here is testing if max quantity from entity is equal or less increment quantity
    }

    @Test
    void whenIncrementIsGreaterThanMaxThenThrowException() {
        ShirtDTO expectedShirtDTO = shirtDTO;
        Shirt expectedShirt = shirt;

        when(shirtRepository.findById(expectedShirtDTO.getId())).thenReturn(Optional.of(expectedShirt));

        int quantityToIncrement = 101;
        assertThrows(ShirtStockExceededException.class, () -> shirtService.increment(expectedShirtDTO.getId(), quantityToIncrement));
    }

    @Test
    void whenIncrementIsCalledWithInvalidIdThenThrowException() {
        int quantityToIncrement = 10;

        when(shirtRepository.findById(INVALID_SHIRT_ID)).thenReturn(Optional.empty());

        assertThrows(ShirtNotFoundException.class, () -> shirtService.increment(INVALID_SHIRT_ID, quantityToIncrement));
    }
}
