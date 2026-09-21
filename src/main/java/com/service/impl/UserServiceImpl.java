package com.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import com.exception.DeleteUserException;
import com.repository.ProjectRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dto.UserRequest;
import com.dto.UserResponse;
import com.entity.User;
import com.exception.EmailAlreadyExists;
import com.exception.UserNotFoundException;
import com.mapper.UserMapper;
import com.repository.UserRepository;
import com.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final UserMapper userMapper;
	private final PasswordEncoder passwordEncoder;
	private final ProjectRepository projectRepository;

	public UserServiceImpl(UserRepository userRepository, UserMapper userMapper ,PasswordEncoder passwordEncoder , ProjectRepository projectRepository ) {
		this.userRepository = userRepository;
		this.userMapper = userMapper;
		this.passwordEncoder = passwordEncoder;
		this.projectRepository=projectRepository;
	}

	// Register User
	@Override
	public UserResponse registerUser(UserRequest user) {

		if (userRepository.findByEmail(user.getEmail()).isPresent()) {
			throw new EmailAlreadyExists("Email '" + user.getEmail() + "' already exists.");
		}

		User u = userMapper.toEntity(user);
		u.setCreatedAt(LocalDateTime.now());
		u.setUpdatedAt(LocalDateTime.now());
		u.setActive(true);
		u.setPassword(passwordEncoder.encode(user.getPassword()));
		User savedUser = userRepository.save(u);
		UserResponse userResponse = userMapper.toResponse(savedUser);

		return userResponse;

	}

	// Get User By Id
	@Override
	public UserResponse getUserById(Long id) {

		User user = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found."));

		UserResponse userResponse = userMapper.toResponse(user);

		return userResponse;
	}

	// Get All Users
	@Override
	public List<UserResponse> getAllUsers() {

		List<User> users = userRepository.findAll();

		return userMapper.toResponse(users);

	}

	// Update User
	@Override
	public UserResponse updateUser(Long id, UserRequest request) {

	    User existingUser = userRepository.findById(id)
	            .orElseThrow(() ->
	                    new UserNotFoundException("User with id " + id + " not found."));

		String email = existingUser.getEmail();
		String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();

		if(!email.equals(currentUserEmail)){

			throw new AccessDeniedException("You can Not Update Other User Information ..");
		}

	    userRepository.findByEmail(request.getEmail()).ifPresent(existing -> {
	        if (!existing.getId().equals(id)) {
	            throw new EmailAlreadyExists(
	                    "Email '" + request.getEmail() + "' already exists.");
	        }
	    });

	    existingUser.setFirstName(request.getFirstName());
	    existingUser.setLastName(request.getLastName());
	    existingUser.setEmail(request.getEmail());
	    existingUser.setUpdatedAt(LocalDateTime.now());
		if (request.getPassword() != null && !request.getPassword().isBlank()) {
			existingUser.setPassword(passwordEncoder.encode(request.getPassword()));
		}
	    User updatedUser = userRepository.save(existingUser);

	    return userMapper.toResponse(updatedUser);
	}
	

	// Delete User
	@Override
	public void deleteUser(Long id) {

		User existingUser = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found."));

		if(projectRepository.existsByCreatedBy(existingUser)){

			throw new DeleteUserException("User Involved in Project Can Not be Delete .. First Delete Project");
		}



		userRepository.delete(existingUser);
	}
	
	// get User Profile 
	
	@Override
	public UserResponse getMyProfile() {

	    String email = SecurityContextHolder.getContext()
	            .getAuthentication()
	            .getName();

	    User user = userRepository.findByEmail(email)
	            .orElseThrow(() ->
	                    new UserNotFoundException(
	                            "User with email " + email + " not found."));

	    return userMapper.toResponse(user);
	}

}