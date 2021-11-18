package com.digitalinnovationone.summershirts.controller;

import com.digitalinnovationone.summershirts.dto.QuantityDTO;
import com.digitalinnovationone.summershirts.dto.ShirtDTO;
import com.digitalinnovationone.summershirts.enums.ShirtModel;
import com.digitalinnovationone.summershirts.exception.ShirtNotFoundException;
import com.digitalinnovationone.summershirts.exception.ShirtStockDecrementExceededException;
import com.digitalinnovationone.summershirts.exception.ShirtStockIncrementExceededException;
import com.digitalinnovationone.summershirts.exception.ShirtWithThisModelAlreadyRegisteredException;
import com.digitalinnovationone.summershirts.service.ShirtService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/shirts")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ShirtController implements ShirtControllerDocs{

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

    @GetMapping
    public List<ShirtDTO> listAll() {
        return shirtService.listAll();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) throws ShirtNotFoundException {
        shirtService.deleteById(id);
    }

    @PatchMapping("/{id}/increment")
    public ShirtDTO increment(@PathVariable Long id, @RequestBody @Valid QuantityDTO quantityDTO) throws ShirtNotFoundException, ShirtStockIncrementExceededException {
        return shirtService.increment(id, quantityDTO.getQuantity());
    }

    @PatchMapping("/{id}/decrement")
    public ShirtDTO decrement(@PathVariable Long id, @RequestBody @Valid QuantityDTO quantityDTO) throws ShirtNotFoundException, ShirtStockDecrementExceededException {
        return shirtService.decrement(id, quantityDTO.getQuantity());
    }
}
