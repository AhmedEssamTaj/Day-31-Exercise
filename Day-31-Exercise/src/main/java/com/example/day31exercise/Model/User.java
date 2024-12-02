package com.example.day31exercise.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity

//@Check(constraints =
//        "LENGTH(user_name) >= 5 AND " +
//                "LENGTH(password) > 6 AND " +
//                "password REGEXP '^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]+$' AND " +
//                "email LIKE '%@%.%' AND " +
//                "role IN ('Admin', 'Customer') AND " +
//                "balance > 0"
//)

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer user_id;

    @NotEmpty(message = "username cannot be null")
    @Size(min = 5,message = "username must be at least 5 characters")
    @Column(columnDefinition = "varchar(25) not null unique")
    private String user_name;

    @NotEmpty(message = "password cannot be empty")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]+$")
    @Size(min = 7, message = "password length must be more then 6 ")
    @Column(columnDefinition = "varchar(25) not null")
    private String password;

    @NotEmpty(message = "email cannot be null")
    @Email(message = "Email must follow a valid email format")
    @Column(columnDefinition = "varchar(25) not null unique")
    private String email;

    @NotEmpty(message = "role cannot be empty")
    @Pattern(regexp = "^(Admin|Customer)$",message = "role can only be either Customer or Admin")
    @Column(columnDefinition = "varchar(8) not null")
    private String role;

    @NotNull(message = "balance cannot be null")
    @Positive(message = "balance must be positive!")
    @Column(columnDefinition = "double not null")
    private Double balance;

}
