package com.friday;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserLocator(@NotBlank(message = "Email is mandatory") @Email(message = "Invalid email address") String email) {
}
