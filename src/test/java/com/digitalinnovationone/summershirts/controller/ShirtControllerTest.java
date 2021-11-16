package com.digitalinnovationone.summershirts.controller;

import com.digitalinnovationone.summershirts.builder.ShirtDTOBuilder;
import com.digitalinnovationone.summershirts.dto.ShirtDTO;
import com.digitalinnovationone.summershirts.exception.ShirtNotFoundException;
import com.digitalinnovationone.summershirts.exception.ShirtWithThisModelAlreadyRegisteredException;
import com.digitalinnovationone.summershirts.service.ShirtService;
import com.digitalinnovationone.summershirts.utils.JsonConvertion;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import static com.digitalinnovationone.summershirts.utils.JsonConvertion.asJsonString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ShirtControllerTest {

    private static final String SHIRT_API_URL_PATH = "/api/v1/shirts";

    // It was made this way because I can't use 'MapStruct' or 'ModelMapper' for the conversion of the objects
    ShirtDTO shirtDTO = ShirtDTOBuilder.builder().build().toShirtDTO();

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
}