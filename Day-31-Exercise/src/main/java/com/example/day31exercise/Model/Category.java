package com.example.day31exercise.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Check(constraints = "(LENGTH(category_name) > 3)")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer category_id;

    @NotEmpty(message = "name cannot be null")
    @Size(min = 4, message = "name must be more then 3 length long")
    @Column(columnDefinition = "varchar(25) not null unique")
    private String category_name;
}
