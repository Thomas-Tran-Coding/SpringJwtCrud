package com.Thomas.JwtAuthCrud.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.Thomas.JwtAuthCrud.model.Role;

public class RoleRowMapper implements RowMapper<Role> {

	@Override
	public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
		Role role = new Role();
		role.setId(rs.getInt("id"));
		role.setUserId(rs.getInt("user_id"));
		role.setRole_admin(rs.getShort("role_admin"));
		role.setRole_develop(rs.getShort("role_develop"));
		role.setRole_cctid(rs.getShort("role_cctld"));
		role.setRole_gtid(rs.getShort("role_gtld"));
		role.setRole_billing(rs.getShort("role_billing"));
		role.setRole_registry(rs.getShort("role_registry"));
		role.setRole_purchase_read(rs.getShort("role_purchase_read"));
		role.setRole_purchase_write(rs.getShort("role_purchase_write"));
		role.setRole_sale_write(rs.getShort("role_sale_write"));
		role.setRole_sql(rs.getShort("role_sql"));

		return role;
	}

}
