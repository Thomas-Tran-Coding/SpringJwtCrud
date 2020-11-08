package com.Thomas.JwtAuthCrud.controller;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Thomas.JwtAuthCrud.dao.UserDao;
import com.Thomas.JwtAuthCrud.model.Role;
import com.Thomas.JwtAuthCrud.security.services.UserService;
import com.Thomas.JwtAuthCrud.model.AppUser;


@RestController
@RequestMapping("/user")
public class JwtRestController {

	@Autowired
	UserService userService;

	@GetMapping("/test")
	public String test() {
		return "It works!";
	}

	@GetMapping("/all")
	public List<AppUser> getAllUsers() {
		return userService.getAllUsers();
	}
	
	@PostMapping
	public ResponseEntity<String> createUser(@RequestBody AppUser user) throws SQLIntegrityConstraintViolationException {
		if(userService.findById(user.getId()) != null) {
			return new ResponseEntity<String>("User with the Id: " + user.getId() +" is being already used! ", HttpStatus.IM_USED);
		}
		userService.saveUser(user);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getUser(@PathVariable("id") Integer id) {
		AppUser user = userService.findById(id);
		if (user == null) {
			return new ResponseEntity<String>("Could not find user with the id " + id, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<AppUser>(user, HttpStatus.OK);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> updateUser(@RequestBody AppUser user) {

		if(userService.findById(user.getId()) == null) {
			return new ResponseEntity<String>("Unable to update as user id: " + user.getId() + " is not found.", HttpStatus.NOT_FOUND);
		}
		userService.updateUser(user);
		return new ResponseEntity<String>("User update successfully.", HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable("id") Integer id) {
		AppUser user = userService.findById(id);
		if (user == null) {
			return new ResponseEntity<String>("Could not delete user with the id " + id, HttpStatus.NOT_FOUND);
		} 
		userService.deleteById(id);
		return new ResponseEntity<String>("User deleted successfully.", HttpStatus.OK);
	}
	
	@GetMapping("/role/{id}")
	public ResponseEntity<?> findWholeUser(@PathVariable("id") Integer id) {
		Role role = userService.findUserRole(id);
		if(role == null ) {
			return new ResponseEntity<String>("Could not find user with the id " + id, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Role>(role, HttpStatus.OK);
	}
	
	@GetMapping("/hans/{login}")
	public ResponseEntity<?> findLoginUser(@PathVariable("login") String login) {
		AppUser user = userService.findByLogin(login);
		if(user == null ) {
			return new ResponseEntity<String>("Could not find user with the login " + login, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<AppUser>(user, HttpStatus.OK);
	}
	
	
}
