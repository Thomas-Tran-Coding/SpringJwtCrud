package com.Thomas.JwtAuthCrud.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.Thomas.JwtAuthCrud.model.User;

public class UserRowMapper implements RowMapper<User>{

	@Override
	public User mapRow(ResultSet rs, int rowNum) throws SQLException {
		User user = new User();
		user.setId(rs.getInt("id"));
		user.setFname(rs.getString("fname"));
		user.setLname(rs.getString("lname"));
		user.setEmail(rs.getString("email"));
		return user;
	}

}
