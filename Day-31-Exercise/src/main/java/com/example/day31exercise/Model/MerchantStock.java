package com.example.day31exercise.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MerchantStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer merchant_stock_id;

    @NotNull(message = "product id cannot be null")
    @Column(columnDefinition = "int not null")
    private Integer product_id;

    @NotNull(message = "merchant id cannot be null")
    @Column(columnDefinition = "int not null")
    private Integer merchant_id;

    @NotNull(message = "stock cannot be null")
    @Min(value = 11, message = "stock must be more then 10")
    @Column(columnDefinition = "int not null")
    private Integer stock;

}
