package com.Thomas.JwtAuthCrud.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Thomas.JwtAuthCrud.dao.UserDao;
import com.Thomas.JwtAuthCrud.model.User;

@Service
public class UserService {

	
	@Autowired
	UserDao userDao;
	
	public List<User> getUser() {
		return userDao.getAllUsers();
	}
	
	public User findById(int id) {
		return userDao.findById(id);
	}
	
	public void deleteById(int id) {
		userDao.deleteById(id);
	}
	
	public void saveUser(User user) {
		userDao.saveUser(user);
	}
	
	public int updateUser(User user) {
		return userDao.updateUser(user);
	}
	
}
