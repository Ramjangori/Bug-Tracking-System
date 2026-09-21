package com.dto;

import java.time.LocalDateTime;

import com.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

	private Long id;
	private String firstName;
	private String lastName;
	private String email;
	private Role role;
	private Boolean active;
	private LocalDateTime createdAt;
}
