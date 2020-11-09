package com.Thomas.JwtAuthCrud.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.Thomas.JwtAuthCrud.model.AppUser;

public class UserRowMapper implements RowMapper<AppUser>{

	@Override
	public AppUser mapRow(ResultSet rs, int rowNum) throws SQLException {
		AppUser user = new AppUser();
		user.setId(rs.getInt("id"));
		user.setLogin(rs.getString("login"));
		user.setPassword(rs.getString("password"));
		user.setFname(rs.getString("fname"));
		user.setLname(rs.getString("lname"));
		user.setEmail(rs.getString("email"));
		return user;
	}

}
