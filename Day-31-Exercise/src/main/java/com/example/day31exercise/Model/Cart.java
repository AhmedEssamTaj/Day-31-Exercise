package com.example.day31exercise.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cart_id;

    @Column(columnDefinition = "int not null")
    private Integer user_id;

    @Column(columnDefinition = "int not null")
    private Integer product_id;
}
