package com.Thomas.JwtAuthCrud.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Repository;

import com.Thomas.JwtAuthCrud.mapper.RoleRowMapper;
import com.Thomas.JwtAuthCrud.mapper.UserRowMapper;
import com.Thomas.JwtAuthCrud.model.Role;
import com.Thomas.JwtAuthCrud.model.AppUser;

@Repository
public class UserDao {

	@Autowired
	JdbcTemplate jdbcTemplate;

	public List<AppUser> getAllUsers() {
		return jdbcTemplate.query("SELECT id, fname, lname, email from user ", new UserRowMapper());
	}

	public AppUser findById(int id) {
		try {
			return (AppUser) this.jdbcTemplate.queryForObject("select * from user where id = ?", new Object[] { id },
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

	public void saveUser(AppUser user) {
		Object[] params = { user.getId(), user.getLogin(), user.getPassword(), user.getFname(), user.getLname(),
				user.getEmail() };
		jdbcTemplate.update("insert into user values(?,?,?,?,?,?)", params);
	}

	public void updateUser(AppUser user) {
		String query = "update user set login = ?, password = ?, fname = ?, lname = ?, email =? where Id=?";
		Object[] params = { user.getLogin(), user.getPassword(), user.getFname(), user.getLname(), user.getEmail(),
				user.getId() };
		int[] types = { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.INTEGER };

		jdbcTemplate.update(query, params, types);
	}

	public Role findUserRole(int id) {
		try {
			return (Role) this.jdbcTemplate.queryForObject("select * from role where id = ?", new Object[] { id },
					new RoleRowMapper());
		} catch (EmptyResultDataAccessException ex) {
			return null;
		}
	}

	public AppUser findByLogin(String login) {
		try {
			AppUser user = this.jdbcTemplate.queryForObject("select * from user where login = ?", new Object[] { login },
					new UserRowMapper());
			return user;
		} catch (EmptyResultDataAccessException ex) {
			return null;
		}
	}

	public Collection<GrantedAuthority> dbToString(int id) {
		List<Short> shortRoles = new ArrayList<Short>();
		List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();

		try {
			Role role = (Role) this.jdbcTemplate.queryForObject("select * from role where id = ?", new Object[] { id },
					new RoleRowMapper());

			shortRoles.add(role.getRole_admin());
			shortRoles.add(role.getRole_develop());
			shortRoles.add(role.getRole_cctid());
			shortRoles.add(role.getRole_gtid());
			shortRoles.add(role.getRole_billing());
			shortRoles.add(role.getRole_registry());
			shortRoles.add(role.getRole_purchase_read());
			shortRoles.add(role.getRole_purchase_write());
			shortRoles.add(role.getRole_sale_write());
			shortRoles.add(role.getRole_sql());

			for (Short shortValue : shortRoles) {
				int count = 0;
				if (count == 0 && shortValue == 1) {
					grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
				} else if (count == 1 && shortValue == 1) {
					grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_DEVELOP"));
				} else if (count == 2 && shortValue == 1) {
					grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_CCTID"));
				} else if (count == 3 && shortValue == 1) {
					grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_GTID"));
				} else if (count == 4 && shortValue == 1) {
					grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_BILLING"));
				} else if (count == 5 && shortValue == 1) {
					grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_REGISTRY"));
				} else if (count == 6 && shortValue == 1) {
					grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_PURCHASE_READ"));
				} else if (count == 7 && shortValue == 1) {
					grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_PURCHASE_WRITE"));
				} else if (count == 8 && shortValue == 1) {
					grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_SALE_WRITE"));
				} else if (count == 9 && shortValue == 1) {
					grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_SQL"));
				} else {
					continue;
				}

				count++;
			}
		} catch (EmptyResultDataAccessException ex) {
			return null;
		}
		return grantedAuthorities;
	}
	
	
	   public AppUser findByUsername(String login) {
		   AppUser user = jdbcTemplate.query(new PreparedStatementCreator() {
	 
	            @Override
	            // preparedStatement for executing a statement many times, prevents sql injection attacks, preparedstatement is executed without having to be compiled first
	            // get all users
	            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
	                PreparedStatement ps = con.prepareStatement("select * from user where login=?");
	                // int 1 => first argument placeholder
	                ps.setString(1, login);
	                return ps;
	            }
	        }, new ResultSetExtractor<AppUser>() {
	            @Override
	            public AppUser extractData(ResultSet rs) throws SQLException, DataAccessException {
	            	// iterate through every row/user 
	                if (rs.next()) {
	                    AppUser user = new AppUser(rs.getInt("id"), rs.getString("login"), rs.getString("password"), rs.getString("fname"), rs.getString("lname"), rs.getString("email"));
	                    return user;
	                } else {
	                    return null;
	                }
	            }
	        });
	        if (user != null) {
	            return user;
	        } else {
	            System.out.println("Kein User unter diesem Namen gefunden!");
	            return null;
	        }
	    }

}
