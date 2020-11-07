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
import com.Thomas.JwtAuthCrud.model.User;
import com.Thomas.JwtAuthCrud.service.UserService;


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
	public List<User> getAllUsers() {
		return userService.getAllUsers();
	}
	
	@PostMapping
	public ResponseEntity<String> createUser(@RequestBody User user) throws SQLIntegrityConstraintViolationException {
		if(userService.findById(user.getId()) != null) {
			return new ResponseEntity<String>("User with the Id: " + user.getId() +" is being already used! ", HttpStatus.IM_USED);
		}
		userService.saveUser(user);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getUser(@PathVariable("id") Integer id) {
		User user = userService.findById(id);
		if (user == null) {
			return new ResponseEntity<String>("Could not find user with the id " + id, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> updateUser(@RequestBody User user) {

		if(userService.findById(user.getId()) == null) {
			return new ResponseEntity<String>("Unable to update as user id: " + user.getId() + " is not found.", HttpStatus.NOT_FOUND);
		}
		userService.updateUser(user);
		return new ResponseEntity<String>("User update successfully.", HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable("id") Integer id) {
		User user = userService.findById(id);
		if (user == null) {
			return new ResponseEntity<String>("Could not delete user with the id " + id, HttpStatus.NOT_FOUND);
		} 
		userService.deleteById(id);
		return new ResponseEntity<String>("User deleted successfully.", HttpStatus.OK);
	}
	
}
