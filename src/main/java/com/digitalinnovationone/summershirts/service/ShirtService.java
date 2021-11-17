package com.digitalinnovationone.summershirts.service;

import com.digitalinnovationone.summershirts.dto.ShirtDTO;
import com.digitalinnovationone.summershirts.entity.Shirt;
import com.digitalinnovationone.summershirts.enums.ShirtModel;
import com.digitalinnovationone.summershirts.exception.ShirtNotFoundException;
import com.digitalinnovationone.summershirts.exception.ShirtStockExceededException;
import com.digitalinnovationone.summershirts.exception.ShirtWithThisModelAlreadyRegisteredException;
import com.digitalinnovationone.summershirts.repository.ShirtRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ShirtService {

    private ShirtRepository shirtRepository;

    public ShirtDTO createShirt(ShirtDTO shirtDTO) throws ShirtWithThisModelAlreadyRegisteredException {
        verifyIfIsAlreadyRegistered(shirtDTO);
        Shirt shirt = toModel(shirtDTO);
        shirtRepository.save(shirt);
        ShirtDTO shirtDTO1 = toDTO(shirt);

        return shirtDTO1;
    }

    public ShirtDTO findByModel(ShirtModel model) throws ShirtNotFoundException {
        Shirt foundShirt = shirtRepository.findByModel(model)
                .orElseThrow(() -> new ShirtNotFoundException(model));
        return toDTO(foundShirt);

    }

    public List<ShirtDTO> listAll() {
        return shirtRepository.findAll()
                .stream()
                .map(ShirtService::toDTO)
                .collect(Collectors.toList());
    }

    public void deleteById(Long id) throws ShirtNotFoundException {
        verifyIfExist(id);
        shirtRepository.deleteById(id);
    }

    public ShirtDTO increment(Long id, int quantityToIncrement) throws ShirtNotFoundException, ShirtStockExceededException {
        Shirt shirtToIncrement = verifyIfExist(id);
        int quantityAfterIncrement = shirtToIncrement.getQuantity() + quantityToIncrement;
        if (quantityAfterIncrement <= shirtToIncrement.getMax()) {
            shirtToIncrement.setQuantity(shirtToIncrement.getQuantity() + quantityToIncrement);
            Shirt incrementedShirtStock = shirtRepository.save(shirtToIncrement);
            return toDTO(incrementedShirtStock);
        } else {
            throw new ShirtStockExceededException(id, quantityToIncrement);
        }
    }

    // it's simplification of the method -instance a dto from an entity-
    // I had to make it static because I've been need to use a reference method in the 'listAll' method
    private static ShirtDTO toDTO(Shirt shirt) {
        ShirtDTO shirtDTO = new ShirtDTO(
                shirt.getId(),
                shirt.getBrand(),
                shirt.getMax(),
                shirt.getQuantity(),
                shirt.getModel());
        return shirtDTO;
    }

    // it's simplification of the method -instance an entity from a dto-
    private Shirt toModel(ShirtDTO shirtDTO) {
        Shirt shirt = new Shirt(
                shirtDTO.getId(),
                shirtDTO.getBrand(),
                shirtDTO.getMax(),
                shirtDTO.getQuantity(),
                shirtDTO.getModel());
        return shirt;
    }

    // It's simplification of the method 'findByModel()'
    private void verifyIfIsAlreadyRegistered(ShirtDTO shirtDTO) throws ShirtWithThisModelAlreadyRegisteredException {
        Optional<Shirt> optSavedShirtModel = shirtRepository.findByModel(shirtDTO.getModel());
        if (optSavedShirtModel.isPresent()) {
            throw new ShirtWithThisModelAlreadyRegisteredException(shirtDTO.getModel().getDescription());
        }
    }

    // Finding by id method
    private Shirt verifyIfExist(Long id) throws ShirtNotFoundException {
        return shirtRepository.findById(id).orElseThrow(() -> new ShirtNotFoundException(id));
    }
}