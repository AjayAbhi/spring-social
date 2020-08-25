package com.springboot.springsocialapp.controller;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import com.springboot.springsocialapp.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.springboot.springsocialapp.payload.SignUpRequest;
import com.springboot.springsocialapp.exception.BadRequestException;
import com.springboot.springsocialapp.payload.ApiResponse;
import com.springboot.springsocialapp.payload.AuthResponse;
import com.springboot.springsocialapp.payload.LoginRequest;
import com.springboot.springsocialapp.repo.UserRepository;
import com.springboot.springsocialapp.security.TokenProvider;
import com.springboot.springsocialapp.model.AuthProvider;

@CrossOrigin
@RestController
@RequestMapping("/auth")
public class AuthController {  
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private TokenProvider tokenProvider;
	
	@PostMapping("/login")
	public ResponseEntity<?> authenticationUser(@Valid @RequestBody LoginRequest loginRequest) {
		 
		Authentication authentication = authenticationManager.authenticate(new 
				UsernamePasswordAuthenticationToken(
						
						loginRequest.getEmail(),
						loginRequest.getPassword()
						)
				);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String token = tokenProvider.createToken(authentication);
		return ResponseEntity.ok(new AuthResponse(token));
	}
	
	@PostMapping("/signup")
	public ResponseEntity<?>registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
		if(userRepository.existsByEmail(signUpRequest.getEmail())) {
			throw new BadRequestException("Email address already in use.");
		}
		
		//Creating User's account
		User user= new User();
		user.setName(signUpRequest.getName());
		user.setEmail(signUpRequest.getEmail());
		user.setPassword(signUpRequest.getPassword());
		user.setProvider(AuthProvider.local);
		
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		User result = userRepository.save(user);
		
		URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/me")
				.buildAndExpand(result.getId()).toUri();
		
		return ResponseEntity.created(location).body(new ApiResponse(true, "User registered Successfully@"));
	}

}
