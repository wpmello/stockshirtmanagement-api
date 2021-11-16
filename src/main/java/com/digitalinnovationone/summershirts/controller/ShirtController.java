package com.digitalinnovationone.summershirts.controller;

import com.digitalinnovationone.summershirts.dto.ShirtDTO;
import com.digitalinnovationone.summershirts.enums.ShirtModel;
import com.digitalinnovationone.summershirts.exception.ShirtNotFoundException;
import com.digitalinnovationone.summershirts.exception.ShirtWithThisModelAlreadyRegisteredException;
import com.digitalinnovationone.summershirts.service.ShirtService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/v1/shirts")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ShirtController {

    private ShirtService shirtService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ShirtDTO createShirt(@RequestBody @Valid ShirtDTO shirtDTO) throws ShirtWithThisModelAlreadyRegisteredException {
        return shirtService.createShirt(shirtDTO);
    }

    @GetMapping("/{model}")
    public ShirtDTO findByModel(@PathVariable ShirtModel model) throws ShirtNotFoundException {
        return shirtService.findByModel(model);
    }

}
