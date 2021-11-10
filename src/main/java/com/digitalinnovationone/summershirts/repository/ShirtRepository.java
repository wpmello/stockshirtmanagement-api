package com.digitalinnovationone.summershirts.repository;

import com.digitalinnovationone.summershirts.entity.Shirt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShirtRepository extends JpaRepository<Shirt, Long> {

    Shirt findByModel(String model);
}
