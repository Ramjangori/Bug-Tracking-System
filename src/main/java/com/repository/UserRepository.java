package com.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.entity.User;
import com.enums.Role;

public interface UserRepository extends JpaRepository<User,Long> {

	Optional<User> findByEmail(String email);
	List<User> findByRoleIn(List<Role> roles);
}
