package com.friday;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


public record User(
                   String prefix,
                   @NotBlank(message = "First name is mandatory") String firstName,
                   String middleName,
                   @NotBlank(message = "Last name is mandatory") String lastName,
                   String suffix,
                   @NotBlank(message = "Email is mandatory") @Email(message = "Invalid email address") String email,
                   @NotBlank(message = "Phone number is mandatory") String phone) {

}