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
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.Thomas.JwtAuthCrud.mapper.ResultRowMapper;
import com.Thomas.JwtAuthCrud.mapper.RoleRowMapper;
import com.Thomas.JwtAuthCrud.mapper.UserRowMapper;
import com.Thomas.JwtAuthCrud.model.Role;
import com.Thomas.JwtAuthCrud.model.AppUser;

@Repository
public class UserDao {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	// get all users
	public List<AppUser> getAllUsers() {
		try {
			return jdbcTemplate.query("SELECT * FROM user,role where user.id = role.user_id", new ResultRowMapper());
		} catch (EmptyResultDataAccessException ex) {
			return null;
		}
	}

	// find a user by ID
	public AppUser findById(int id) {
		try {
			return (AppUser) jdbcTemplate.queryForObject("SELECT * FROM user,role where user.id = role.user_id and role.user_id = ?", new Object[] { id }, new ResultRowMapper());
		} catch (EmptyResultDataAccessException ex) {
			return null;
		}
	}
	
	// update a user
	public void updateUser(AppUser user) {
		try {
			Object[] userParams = { user.getLogin(),  
									user.getPassword(), 
									user.getFname(), 
									user.getLname(),
									user.getEmail(),
									user.getId(),};
			jdbcTemplate.update("update user set login = ?, password = ?, fname = ?, lname = ?, email =? where Id=?", userParams);
			
			Object[] roleParams = { user.getRole().getId(), 
									user.getRole().getRole_admin(), 
									user.getRole().getRole_develop(), 
									user.getRole().getRole_cctid(), 
									user.getRole().getRole_gtid(), 
									user.getRole().getRole_billing(),
									user.getRole().getRole_registry(),
									user.getRole().getRole_purchase_read(), 
									user.getRole().getRole_purchase_write(), 
									user.getRole().getRole_sale_write(), 
									user.getRole().getRole_sql(),
									user.getRole().getUserId(), };
			
			jdbcTemplate.update("update role set id = ?, role_admin = ?, role_develop = ?, role_cctld = ?, role_gtld = ?, role_billing = ?, role_registry = ?, role_purchase_read = ?, role_purchase_write = ?, role_sale_write = ?, role_sql = ? where user_id = ?", roleParams);
		} catch(EmptyResultDataAccessException e) {
	        throw new RuntimeException("ERROR: User ID does not exist!");
		} catch(IncorrectResultSizeDataAccessException e) {
			  throw new RuntimeException("ERROR: Duplicate user ID!");
		}
		
	}

	// delete a user by ID
	public void deleteById(int id) {
		try {

			jdbcTemplate.update("delete from user where id = ?", id);
		} catch(EmptyResultDataAccessException e) {
	        throw new RuntimeException("ERROR: User ID does not exist");
		} catch(IncorrectResultSizeDataAccessException e) {
			  throw new RuntimeException("ERROR:Duplicate user ID!");
		}
	}

	// save user by ID
	public void saveUser(AppUser user) {
		try {
			Object[] userParams = { user.getId(), 
									user.getLogin(),  
									user.getPassword(), 
									user.getFname(), 
									user.getLname(),
									user.getEmail()};
			jdbcTemplate.update("insert into user values(?,?,?,?,?,?)", userParams);
			
			Object[] roleParams = { user.getRole().getId(), 
									user.getRole().getUserId(), 
									user.getRole().getRole_admin(), 
									user.getRole().getRole_develop(), 
									user.getRole().getRole_cctid(), 
									user.getRole().getRole_gtid(), 
									user.getRole().getRole_billing(),
									user.getRole().getRole_registry(),
									user.getRole().getRole_purchase_read(), 
									user.getRole().getRole_purchase_write(), 
									user.getRole().getRole_sale_write(), 
									user.getRole().getRole_sql() };
			
			jdbcTemplate.update("insert into role values(?,?,?,?,?,?,?,?,?,?,?,?)", roleParams);
		} catch(EmptyResultDataAccessException e) {
	        throw new RuntimeException("ERROR: User ID does not exist");
		} catch(IncorrectResultSizeDataAccessException e) {
			  throw new RuntimeException("ERROR:Duplicate user ID!");
		}
	      
	}

	// get roles of a user in a collection
	public Collection<GrantedAuthority> getAuthorityRolesById(int id) {
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
			
			for(int count = 0; count < shortRoles.size(); count++) {
				
				if (count == 0 && shortRoles.get(count) == 1) {
					grantedAuthorities.add(new SimpleGrantedAuthority("ADMIN"));
				} else if (count == 1 && shortRoles.get(count) == 1) {
					grantedAuthorities.add(new SimpleGrantedAuthority("DEVELOP"));
				} else if (count == 2 && shortRoles.get(count) == 1) {
					grantedAuthorities.add(new SimpleGrantedAuthority("CCTID"));
				} else if (count == 3 && shortRoles.get(count) == 1) {
					grantedAuthorities.add(new SimpleGrantedAuthority("GTID"));
				} else if (count == 4 && shortRoles.get(count) == 1) {
					grantedAuthorities.add(new SimpleGrantedAuthority("BILLING"));
				} else if (count == 5 && shortRoles.get(count) == 1) {
					grantedAuthorities.add(new SimpleGrantedAuthority("REGISTRY"));
				} else if (count == 6 && shortRoles.get(count) == 1) {
					grantedAuthorities.add(new SimpleGrantedAuthority("PURCHASE_READ"));
				} else if (count == 7 && shortRoles.get(count) == 1) {
					grantedAuthorities.add(new SimpleGrantedAuthority("PURCHASE_WRITE"));
				} else if (count == 8 && shortRoles.get(count) == 1) {
					grantedAuthorities.add(new SimpleGrantedAuthority("SALE_WRITE"));
				} else if (count == 9 && shortRoles.get(count) == 1) {
					grantedAuthorities.add(new SimpleGrantedAuthority("SQL"));
				} else {
				}
				
			}
		
		} catch (EmptyResultDataAccessException ex) {
			return null;
		}
		return grantedAuthorities;
	}
	// find user by login 
	public AppUser findByLogin(String login) {
		try {
			return (AppUser) jdbcTemplate.queryForObject("select * from user where user.login = ?", new Object[] { login }, new UserRowMapper());
		} catch (EmptyResultDataAccessException ex) {
			return null;
		}
	}
	
	// find a user by email
	public AppUser findByEmail(String email) {
		try {
			return (AppUser) jdbcTemplate.queryForObject("select * from user where user.email = ?", new Object[] { email }, new UserRowMapper());
		} catch (EmptyResultDataAccessException ex) {
			return null;
		}	
	}
	
	// find user role by ID
	public Role findUserRole(int id) {
		try {
			return (Role) this.jdbcTemplate.queryForObject("select * from role where id = ?", new Object[] { id },
					new RoleRowMapper());
		} catch (EmptyResultDataAccessException ex) {
			return null;
		}
	}

	
//	   public AppUser findByLogin(String login) {
//		   AppUser user = jdbcTemplate.query(new PreparedStatementCreator() {
//	 
//	            @Override
//	            // preparedStatement for executing a statement many times, prevents sql injection attacks, preparedstatement is executed without having to be compiled first
//	            // get all users
//	            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
//	                PreparedStatement ps = con.prepareStatement("select * from user where login=?");
//	                // int 1 => first argument placeholder
//	                ps.setString(1, login);
//	                return ps;
//	            }
//	        }, new ResultSetExtractor<AppUser>() {
//	            @Override
//	            public AppUser extractData(ResultSet rs) throws SQLException, DataAccessException {
//	            	// iterate through every row/user 
//	                if (rs.next()) {
//	                    AppUser user = new AppUser(rs.getInt("id"), rs.getString("login"), rs.getString("password"), rs.getString("fname"), rs.getString("lname"), rs.getString("email"), null);
//	                    return user;
//	                } else {
//	                    return null;
//	                }
//	            }
//	        });
//	        if (user != null) {
//	            return user;
//	        } else {
//	            return null;
//	        }
//	    }
//	   
//	   public AppUser findByEmail(String email) {
//		   AppUser user = jdbcTemplate.query(new PreparedStatementCreator() {
//	 
//	            @Override
//	            // preparedStatement for executing a statement many times, prevents sql injection attacks, preparedstatement is executed without having to be compiled first
//	            // get all users
//	            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
//	                PreparedStatement ps = con.prepareStatement("select * from user, role where user.email=?");
//	                // int 1 => first argument placeholder
//	                ps.setString(1, email);
//	                return ps;
//	            }
//	        }, new ResultSetExtractor<AppUser>() {
//	            @Override
//	            public AppUser extractData(ResultSet rs) throws SQLException, DataAccessException {
//	            	
//	            	// iterate through every row/user 
//	                if (rs.next()) {
//	                    AppUser user = new AppUser(rs.getInt("id"), rs.getString("login"), rs.getString("password"), rs.getString("fname"), rs.getString("lname"), rs.getString("email"));
//	                    return user;
//	                } else {
//	                    return null;
//	                }
//	            }
//	        });
//	        if (user != null) {
//	            return user;
//	        } else {
//	            return null;
//	        }
//	    }
//	



}
