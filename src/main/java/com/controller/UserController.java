package com.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dto.UserRequest;
import com.dto.UserResponse;
import com.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {

		this.userService = userService;
	}

// User Registeration Api 
	@PostMapping
	public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserRequest request) {

		UserResponse user = userService.registerUser(request);

		return ResponseEntity.status(HttpStatus.CREATED).body(user);
	}

	// Find User By Id Api
	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
	@GetMapping("/{id}")
	public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {

		UserResponse user = userService.getUserById(id);

		return ResponseEntity.status(HttpStatus.OK).body(user);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping
	public ResponseEntity<List<UserResponse>> getAllUsers(){
		
		List<UserResponse> userList = userService.getAllUsers();
		
		return ResponseEntity.status(HttpStatus.OK).body(userList); 
			
	}

	@PreAuthorize("isAuthenticated()")
	@PutMapping("/{id}")
	public ResponseEntity<UserResponse> updateUser(
	        @PathVariable Long id,
	        @Valid @RequestBody UserRequest request) {

	    UserResponse response = userService.updateUser(id, request);

	    return ResponseEntity.ok(response);
	}


	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteUser(@PathVariable Long id) {
		
		userService.deleteUser(id);
		
		return ResponseEntity.ok("User Delete Successfully ..");
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/me")
	public ResponseEntity<UserResponse> getMyProfile() {
	    return ResponseEntity.ok(userService.getMyProfile());
	}
	
}
