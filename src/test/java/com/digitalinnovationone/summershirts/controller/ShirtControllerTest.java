package com.digitalinnovationone.summershirts.controller;

import com.digitalinnovationone.summershirts.builder.ShirtDTOBuilder;
import com.digitalinnovationone.summershirts.dto.QuantityDTO;
import com.digitalinnovationone.summershirts.dto.ShirtDTO;
import com.digitalinnovationone.summershirts.exception.ShirtNotFoundException;
import com.digitalinnovationone.summershirts.exception.ShirtStockDecrementExceededException;
import com.digitalinnovationone.summershirts.exception.ShirtStockIncrementExceededException;
import com.digitalinnovationone.summershirts.service.ShirtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Collections;

import static com.digitalinnovationone.summershirts.utils.JsonConvertion.asJsonString;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ShirtControllerTest {

    private static final String SHIRT_API_URL_PATH = "/api/v1/shirts";
    private static final long VALID_SHIRT_ID = 1L;
    private static final long INVALID_SHIRT_ID = 2L;
    private static final String SHIRT_API_SUBPATH_INCREMENT_URL = "/increment";
    private static final String SHIRT_API_SUBPATH_DECREMENT_URL = "/decrement";


    // It was made this way because I can't use 'MapStruct' or 'ModelMapper' for the conversion of the objects
    ShirtDTO shirtDTO = ShirtDTOBuilder.builder().build().toShirtDTO();
    QuantityDTO quantityDTO = QuantityDTO.builder().quantity(10).build();

    private MockMvc mockMvc;

    @Mock
    private ShirtService shirtService;

    @InjectMocks
    private ShirtController shirtController;

    //It's made to configure the 'mockMvc' passing a class as a target then we say our 'mockMvc' accept pageable objects
    // and the visual return will be a Json object
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(shirtController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((viewName, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void whenPOSTIsCalledThenReturnAShirtIsCreated() throws Exception {
        ShirtDTO shirt = shirtDTO;

        when(shirtService.createShirt(shirt)).thenReturn(shirt);

        mockMvc.perform(post(SHIRT_API_URL_PATH) // it's indicate the path
                        .contentType(MediaType.APPLICATION_JSON) // type of the media
                        .content(asJsonString(shirt))) // the object already formatted
                .andExpect(status().isCreated()) // expected result
                .andExpect(jsonPath("$.brand", is(shirt.getBrand()))); // similar between the parameters
    }

    @Test
    void whenPOSTIsCalledWithoutRequiredFieldThenAnErrorIsReturned() throws Exception {
        ShirtDTO expectedShitDTO = shirtDTO;
        expectedShitDTO.setBrand(null);

        mockMvc.perform(post(SHIRT_API_URL_PATH) // it's indicate the path
                        .contentType(MediaType.APPLICATION_JSON) // type of the media
                        .content(asJsonString(expectedShitDTO))) // the object already formatted
                .andExpect(status().isBadRequest()); //expected result
    }

    @Test
    void whenGETIsCalledWithValidModelThenOkStatusIsReturned() throws Exception {
        when(shirtService.findByModel(shirtDTO.getModel())).thenReturn(shirtDTO);

        mockMvc.perform(get(SHIRT_API_URL_PATH + "/" + shirtDTO.getModel())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect((status().isOk()))
                .andExpect(jsonPath("$.brand", is(shirtDTO.getBrand())))
                .andExpect(jsonPath("$.model", is(shirtDTO.getModel().toString())));
    }

    @Test
    void whenGETIsCalledWithNotRegisteredModelThenNotFoundStatusIsReturned() throws Exception {
        when(shirtService.findByModel(shirtDTO.getModel())).thenThrow(ShirtNotFoundException.class);

        mockMvc.perform(get(SHIRT_API_URL_PATH + "/" + shirtDTO.getModel())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGETListWithShirtsIsCalledThenOkStatusIsReturned() throws Exception {
        when(shirtService.listAll()).thenReturn(Collections.singletonList(shirtDTO));

        mockMvc.perform(get(SHIRT_API_URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].brand", is(shirtDTO.getBrand())))
                .andExpect(jsonPath("$[0].model", is(shirtDTO.getModel().toString())));
    }

    @Test
    void whenGETListWithoutShirtsIsCalledThenStatusOkIsReturned() throws Exception {
        when(shirtService.listAll()).thenReturn(Collections.EMPTY_LIST);

        mockMvc.perform(get(SHIRT_API_URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenDELETEIsCalledWithValidIdThenNoContentStatusIsReturned() throws Exception {
        doNothing().when(shirtService).deleteById(VALID_SHIRT_ID);

        mockMvc.perform(delete(SHIRT_API_URL_PATH + "/" + VALID_SHIRT_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(shirtService, times(1)).deleteById(VALID_SHIRT_ID);
    }

    @Test
    void whenDELETEIsCalledWithInvalidIdThenExceptionIsThrown() throws Exception {
        doThrow(ShirtNotFoundException.class).when(shirtService).deleteById(INVALID_SHIRT_ID);

        mockMvc.perform(delete(SHIRT_API_URL_PATH + "/" + INVALID_SHIRT_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    void whenPATCHIsCalledToIncrementDiscountThenOkStatusIsReturned() throws Exception, ShirtStockIncrementExceededException {
        shirtDTO.setQuantity(shirtDTO.getQuantity() + quantityDTO.getQuantity());

        when(shirtService.increment(VALID_SHIRT_ID, quantityDTO.getQuantity())).thenReturn(shirtDTO);

        mockMvc.perform(patch(SHIRT_API_URL_PATH + "/" + VALID_SHIRT_ID + SHIRT_API_SUBPATH_INCREMENT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(quantityDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brand", is(shirtDTO.getBrand())))
                .andExpect(jsonPath("$.quantity", is(shirtDTO.getQuantity())));
    }

    @Test
    void whenPATCHIsCalledToIncrementGreaterThanMaxThenBadRequestStatusIsReturned() throws Exception {
        when(shirtService.increment(shirtDTO.getId(), quantityDTO.getQuantity())).thenThrow(ShirtStockIncrementExceededException.class);

        mockMvc.perform(patch(SHIRT_API_URL_PATH + "/" + VALID_SHIRT_ID + SHIRT_API_SUBPATH_INCREMENT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(quantityDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenPATCHIsCalledToIncrementWithInvalidThenNotFoundIsReturned() throws Exception {
        when(shirtService.increment(INVALID_SHIRT_ID, quantityDTO.getQuantity())).thenThrow(ShirtNotFoundException.class);

        mockMvc.perform(patch(SHIRT_API_URL_PATH + "/" + INVALID_SHIRT_ID + SHIRT_API_SUBPATH_INCREMENT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(quantityDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenPATCHIsCalledToDecrementDiscountThenOkStatusIsReturned() throws Exception {
        when(shirtService.decrement(VALID_SHIRT_ID, quantityDTO.getQuantity())).thenReturn(shirtDTO);

        mockMvc.perform(patch(SHIRT_API_URL_PATH + "/" + VALID_SHIRT_ID + SHIRT_API_SUBPATH_DECREMENT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(quantityDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.model", is(shirtDTO.getModel().toString())));
    }

    @Test
    void whenPATCHIsCalledToDecrementLowerThanZeroThenBadRequestIsReturned() throws Exception {
        when(shirtService.decrement(VALID_SHIRT_ID, shirtDTO.getQuantity())).thenThrow(ShirtStockDecrementExceededException.class);

        mockMvc.perform(patch(SHIRT_API_URL_PATH + "/" + VALID_SHIRT_ID + SHIRT_API_SUBPATH_DECREMENT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(quantityDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenPATCHIsCalledWithInvalidIdToDecrementTheNotFoundStatusIsReturned() throws Exception {
        when(shirtService.decrement(INVALID_SHIRT_ID, shirtDTO.getQuantity())).thenThrow(ShirtNotFoundException.class);

        mockMvc.perform(patch(SHIRT_API_URL_PATH + "/" + INVALID_SHIRT_ID + SHIRT_API_SUBPATH_DECREMENT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(quantityDTO)))
                .andExpect(status().isNotFound());
    }
}