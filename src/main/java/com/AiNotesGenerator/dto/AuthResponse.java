package com.AiNotesGenerator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    public AuthResponse(String token, String email, String name) {
		// TODO Auto-generated constructor stub
    	this.token=token;
    	this.email=email;
    	this.name=name;
	}
	private String token;
    private String email;
    private String name;
}