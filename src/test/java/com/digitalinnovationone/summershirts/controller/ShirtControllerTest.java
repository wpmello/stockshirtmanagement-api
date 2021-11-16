package com.digitalinnovationone.summershirts.controller;

import com.digitalinnovationone.summershirts.builder.ShirtDTOBuilder;
import com.digitalinnovationone.summershirts.dto.ShirtDTO;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ShirtControllerTest {

    private static final String SHIRT_API_URL_PATH = "/api/v1/shirts";

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
        ShirtDTO shirtDTO = ShirtDTOBuilder.builder().build().toShirtDTO();

        when(shirtService.createShirt(shirtDTO)).thenReturn(shirtDTO);

        mockMvc.perform(post(SHIRT_API_URL_PATH) // it's indicate the path
                .contentType(MediaType.APPLICATION_JSON) // type of the media
                .content(asJsonString(shirtDTO))) // the object already formatted
                .andExpect(status().isCreated()) // expected result
                .andExpect(jsonPath("$.brand", is(shirtDTO.getBrand()))); // similar between the parameters
    }

    @Test
    void whenPOSTIsCalledWithoutRequiredFieldThenAnErrorIsReturned() throws Exception {
        ShirtDTO shirtDTO = ShirtDTOBuilder.builder().build().toShirtDTO();
        shirtDTO.setBrand(null);

        mockMvc.perform(post(SHIRT_API_URL_PATH) // it's indicate the path
                .contentType(MediaType.APPLICATION_JSON) // type of the media
                .content(asJsonString(shirtDTO))) // the object already formatted
                .andExpect(status().isBadRequest()); //expected result
    }
}