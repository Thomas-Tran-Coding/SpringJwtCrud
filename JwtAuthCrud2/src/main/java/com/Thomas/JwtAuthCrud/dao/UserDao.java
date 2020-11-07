package com.Thomas.JwtAuthCrud.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Repository;

import com.Thomas.JwtAuthCrud.mapper.UserRowMapper;
import com.Thomas.JwtAuthCrud.model.User;
import com.mysql.cj.xdevapi.PreparableStatement;

@Repository
public class UserDao {

	@Autowired
	JdbcTemplate jdbcTemplate;

	public List<User> getAllUsers() {
		return jdbcTemplate.query("SELECT id, fname, lname, email from user ", new UserRowMapper());
	}

	public User findById(int id) {
		try {
			return (User) this.jdbcTemplate.queryForObject("select * from user where id = ?", new Object[] { id },
					new UserRowMapper());
		} catch (EmptyResultDataAccessException ex) {
			return null;
		}
	}

	public void deleteById(int id) {
		jdbcTemplate.update("delete from user where id = ?", id);
	}

//	public Boolean saveUser(User user) {
//		return jdbcTemplate.execute("insert into user values(?,?,?,?,?,?)", new PreparedStatementCallback<Boolean>() {
//
//			@Override
//			public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
//
//				ps.setInt(1, user.getId());
//				ps.setString(2, user.getLogin());
//				ps.setString(3, user.getPassword());
//				ps.setString(4, user.getFname());
//				ps.setString(5, user.getLname());
//				ps.setString(6, user.getEmail());
//				
//				return ps.execute(); 
//			}
//		});
//
//	}

	public void saveUser(User user) {
		Object[] params = {user.getId(), user.getLogin(), user.getPassword(), user.getFname(), user.getLname(), user.getEmail()};
		jdbcTemplate.update("insert into user values(?,?,?,?,?,?)",params);
	}

	public void updateUser(User user) {
				String query = "update user set login = ?, password = ?, fname = ?, lname = ?, email =? where Id=?";
				Object[] params = {user.getLogin(), user.getPassword(), user.getFname(), user.getLname(), user.getEmail(),user.getId()};
				int[] types = { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.INTEGER};
				
				jdbcTemplate.update(query, params,types);
	}
}
