package com.example.day31exercise.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer product_id;

    @NotEmpty(message = "name cannot be null")
    @Size(min = 4, message = "name must be more then 3 length long")
    @Column(columnDefinition = "varchar(25) not null")
    private String product_name;

    @NotNull(message = "price cannot be null")
    @Positive(message = "price must be a positive number")
    @Column(columnDefinition = "double not null")
    private Double product_price;

    @NotNull(message = "category cannot be null")
    @Column(columnDefinition = "int not null")
    private Integer category_id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY) // Read-only in JSON
    @Column(columnDefinition = "double not null")
    private Double averageRating = 0.0;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY) // Read-only in JSON
    @Column(columnDefinition = "int not null")
    private Integer ratingCount = 0;

}
