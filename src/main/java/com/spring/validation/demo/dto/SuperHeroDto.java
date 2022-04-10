package com.spring.validation.demo.dto;

//import javax.validation.constraints.Email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

//import javax.validation.constraints.NotNull;
//import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuperHeroDto implements Serializable {

    private int id;
    @NotBlank(message = "Name shouldn't be empty or null")
    private String name;
    private String superName;
    @NotNull(message = "Profession shouldn't be null")
    private String profession;
    @Min(18)
    @Max(60)
    private int age;
    private boolean canFly;

//    @Email - for email validation
//    @NotNull - for not null validation
//    @Pattern(regexp = "^\\d{10}$", message = "Invalid mobile number") - for mobile no validation

}
