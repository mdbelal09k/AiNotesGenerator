package com.AiNotesGenerator.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank
    @Email(message = "Invalid email")
    private String email;

    @NotBlank
    @Size(min = 6, message = "Min 6 characters")
    private String password;

	public String getEmail() {
		// TODO Auto-generated method stub
		return email;
	}

	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}
	public String getPassword() {
		return password;
	}
}