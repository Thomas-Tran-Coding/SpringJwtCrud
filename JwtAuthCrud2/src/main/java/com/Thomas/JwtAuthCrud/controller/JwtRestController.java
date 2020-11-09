package com.Thomas.JwtAuthCrud.controller;

import java.util.Collection;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
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
	//

	@GetMapping()
	public List<AppUser> getAllUsers() {
		return userService.getAllUsers();
	}

//	//CREATE createUser
	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@RequestBody AppUser signUpRequest) {

		if (userService.findByLogin(signUpRequest.getLogin()) != null) {
			return new ResponseEntity<String>("ERROR: Login " + signUpRequest.getLogin() + " is already in use!",
					HttpStatus.BAD_REQUEST);
		}

		if (userService.findByEmail(signUpRequest.getEmail()) != null) {
			return new ResponseEntity<String>("ERROR: User email" + signUpRequest.getEmail() + " is already taken.",
					HttpStatus.BAD_REQUEST);
		}

		// Create new user's account
		AppUser user = new AppUser(signUpRequest.getId(), signUpRequest.getLogin(),
				encoder.encode(signUpRequest.getPassword()), signUpRequest.getFname(), signUpRequest.getLname(),
				signUpRequest.getEmail());

		userService.saveUser(user);

		return new ResponseEntity<String>("User registered successfully!", HttpStatus.OK);

	}

	// GET findById
	@GetMapping("/{id}")
	public ResponseEntity<?> getUser(@PathVariable("id") Integer id) {
		AppUser user = userService.findById(id);
		if (user == null) {
			return new ResponseEntity<String>("ERROR: User with Id: " + id + " could not be found!",
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<AppUser>(user, HttpStatus.OK);
	}

	// UPDATE updateUser
	@PutMapping("/{id}")
	public ResponseEntity<?> updateUser(@RequestBody AppUser updateRequest) {

		if (userService.findById(updateRequest.getId()) == null) {
			return new ResponseEntity<String>("ERROR: User with Id: " + updateRequest.getId() + " could not found.",
					HttpStatus.NOT_FOUND);
		}

		AppUser updatedUser = new AppUser(updateRequest.getId(), updateRequest.getLogin(),
				encoder.encode(updateRequest.getPassword()), updateRequest.getFname(), updateRequest.getLname(),
				updateRequest.getEmail());

		userService.updateUser(updatedUser);
		return new ResponseEntity<String>("User update successfully!", HttpStatus.OK);
	}

	// DELETE deleteById
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable("id") Integer id) {
		AppUser user = userService.findById(id);
		if (user == null) {
			return new ResponseEntity<String>("ERROR: Could not delete user with the id " + id, HttpStatus.NOT_FOUND);
		}
		userService.deleteById(id);
		return new ResponseEntity<String>("User deleted successfully!", HttpStatus.OK);
	}

	@GetMapping("/findByLogin/{login}")
	public ResponseEntity<?> findLoginUser(@PathVariable("login") String login) {
		AppUser user = userService.findByLogin(login);
		if (user == null) {
			return new ResponseEntity<String>("ERROR: Could not find user with the login " + login,
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<AppUser>(user, HttpStatus.OK);
	}

	@GetMapping("/getRolesById/{id}")
	public ResponseEntity<?> getRolesById(@PathVariable("id") Integer id) {
		AppUser user = userService.findById(id);
		Collection<GrantedAuthority> grantedAuthorities = userService.getAuthorityRolesById(id);
		if (user == null) {
			return new ResponseEntity<String>("ERROR: Could not find authority roles for user with the id " + id,
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Collection>(grantedAuthorities, HttpStatus.OK);
	}

}
