package com.digitalinnovationone.summershirts.entity;

import com.digitalinnovationone.summershirts.enums.ShirtModel;
import com.digitalinnovationone.summershirts.enums.ShirtSize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Shirt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int max;

    @Column(nullable = false)
    private int quantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private ShirtModel model;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ShirtSize size;
}
