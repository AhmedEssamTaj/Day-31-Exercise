package com.example.day31exercise.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Check(constraints = "(LENGTH(merchant_name) > 3)")
public class Merchant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer merchant_id;

    @NotEmpty(message = "name cannot be null")
    @Size(min = 4, message = "name must be more then 3 length long")
    @Column(columnDefinition = "varchar(30) not null unique")
    private String merchant_name;
}
