package com.springboot.springsocialapp.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springboot.springsocialapp.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	
	Optional<User>findByEmail(String email);
	
	Boolean existsByEmail(String email);
}
