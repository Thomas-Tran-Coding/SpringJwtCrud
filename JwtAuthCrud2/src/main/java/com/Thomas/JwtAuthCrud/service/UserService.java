package com.Thomas.JwtAuthCrud.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.Thomas.JwtAuthCrud.dao.UserDao;
import com.Thomas.JwtAuthCrud.model.Role;
import com.Thomas.JwtAuthCrud.model.AppUser;

@Service
public class UserService {

	
	@Autowired
	UserDao userDao;
	
	public List<AppUser> getAllUsers() {
		return userDao.getAllUsers();
	}
	
	public AppUser findById(int id) {
		return userDao.findById(id);
	}
	
	public void deleteById(int id) {
		userDao.deleteById(id);
	}
	
	public void saveUser(AppUser user) {
		userDao.saveUser(user);
	}
	
	public void updateUser(AppUser user) {
		userDao.updateUser(user);
	}
	
	public Role findUserRole(int id) {
		return userDao.findUserRole(id);
	}

	public AppUser findByLogin(String login) {
		return userDao.findByLogin(login);
	}
	
	public Collection<GrantedAuthority> dbToString(int id) {
		return userDao.dbToString(id);
	}
	
	public AppUser findByUsername(String login){
		return userDao.findByUsername(login);
	}
	
}
