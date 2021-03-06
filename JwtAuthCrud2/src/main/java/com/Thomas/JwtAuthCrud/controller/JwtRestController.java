package com.Thomas.JwtAuthCrud.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Thomas.JwtAuthCrud.security.jwt.JwtUtil;
import com.Thomas.JwtAuthCrud.security.services.UserService;

import com.Thomas.JwtAuthCrud.model.AppUser;

@RestController
@RequestMapping("/user")
public class JwtRestController {

	// changed password length in database from VAR(50) to VAR(70) to ensure
	// additional security with bcrypt
	@Autowired
	PasswordEncoder encoder;

	@Autowired
	UserService userService;

	@Autowired
	JwtUtil jwtUtil;

	// get all users
	@GetMapping("/all")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public List<AppUser> getAllUsers() {
		return userService.getAllUsers();
	}

//	//create a new user
	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@RequestBody AppUser signUpRequest) {

		if (userService.findById(signUpRequest.getId()) != null) {
			return new ResponseEntity<String>("ERROR: ID: " + signUpRequest.getId() + " is already in use!",
					HttpStatus.BAD_REQUEST);
		}
		
		if (userService.findByLogin(signUpRequest.getLogin()) != null) {
			return new ResponseEntity<String>("ERROR: Login: " + signUpRequest.getLogin() + " is already in use!",
					HttpStatus.BAD_REQUEST);
		}

		if (userService.findByEmail(signUpRequest.getEmail()) != null) {
			return new ResponseEntity<String>("ERROR: User email: " + signUpRequest.getEmail() + " is already taken.",
					HttpStatus.BAD_REQUEST);
		}

		// Create new user's account
		AppUser user = new AppUser(	signUpRequest.getId(), 
									signUpRequest.getLogin(),
									encoder.encode(signUpRequest.getPassword()), 
									signUpRequest.getFname(), 
									signUpRequest.getLname(),
									signUpRequest.getEmail(), 
									signUpRequest.getRole());

		userService.saveUser(user);

		return new ResponseEntity<String>("User registered successfully!", HttpStatus.OK);

	}

	// find user by ID
	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> getUser(@PathVariable("id") Integer id) {
		AppUser user = userService.findById(id);
		if (user == null) {
			return new ResponseEntity<String>("ERROR: Could not find user with the id " + id, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<AppUser>(user, HttpStatus.OK);
	}

	// update user attributes
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping("/{id}")
	public ResponseEntity<?> updateUser(@PathVariable("id") Integer id, @RequestBody AppUser updateRequest) {

		if (userService.findById(id) == null) {
			return new ResponseEntity<String>("ERROR: User with Id: " + updateRequest.getId() + " could not found.",
					HttpStatus.NOT_FOUND);
		}
		// update user
		AppUser updatedUser = new AppUser(	updateRequest.getId(), 
											updateRequest.getLogin(),
											encoder.encode(updateRequest.getPassword()), 
											updateRequest.getFname(), 
											updateRequest.getLname(),
											updateRequest.getEmail(), 
											updateRequest.getRole());

		userService.updateUser(updatedUser);
		return new ResponseEntity<String>("User update successfully!", HttpStatus.OK);
	}

	// delete user by ID
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> deleteUser(@PathVariable("id") Integer id, HttpServletRequest request) {
		final String authorizationHeader = request.getHeader("Authorization");
		String jwt = authorizationHeader.substring(7);
		String username = jwtUtil.extractUsername(jwt);
		
		AppUser user = userService.findById(id);
		if (user == null) {
			return new ResponseEntity<String>("ERROR: Could not delete user with the id " + id, HttpStatus.NOT_FOUND);
		}
		
		if(username.equals(user.getLogin())) {
			return new ResponseEntity<String>("ERROR: Self deletion is not allowed!", HttpStatus.BAD_REQUEST);
		} else {

			userService.deleteById(id);
			return new ResponseEntity<String>("User deleted successfully!", HttpStatus.OK);
		}
		
	}

}
