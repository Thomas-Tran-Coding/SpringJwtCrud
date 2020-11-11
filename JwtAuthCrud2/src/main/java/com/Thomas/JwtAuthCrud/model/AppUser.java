package com.Thomas.JwtAuthCrud.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AppUser implements Serializable {

	private int id;

	private String login;

	private String password;

	private String fname;

	private String lname;

	private String email;

	private Role role;

	public AppUser() {
	}

	public AppUser(int id, String login, String password, String fname, String lname, String email, Role role) {
		this.id = id;
		this.login = login;
		this.password = password;
		this.fname = fname;
		this.lname = lname;
		this.email = email;
		this.role = role;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
