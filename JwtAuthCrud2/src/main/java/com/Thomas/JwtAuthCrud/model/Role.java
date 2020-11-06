package com.Thomas.JwtAuthCrud.model;

import java.io.Serializable;

public class Role implements Serializable{

	private int id;
	
	private int userId;
	
	private short role_admin;
	
	private short role_develop;
	
	private short role_cctid;
	
	private short role_gtid;
	
	private short role_billing;
	
	private short role_registry;
	
	private short role_purchase_read;
	
	private short role_purchase_write;
	
	private short role_sale_write;
	
	private short role_sql;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public short getRole_admin() {
		return role_admin;
	}

	public void setRole_admin(short role_admin) {
		this.role_admin = role_admin;
	}

	public short getRole_develop() {
		return role_develop;
	}

	public void setRole_develop(short role_develop) {
		this.role_develop = role_develop;
	}

	public short getRole_cctid() {
		return role_cctid;
	}

	public void setRole_cctid(short role_cctid) {
		this.role_cctid = role_cctid;
	}

	public short getRole_gtid() {
		return role_gtid;
	}

	public void setRole_gtid(short role_gtid) {
		this.role_gtid = role_gtid;
	}

	public short getRole_billing() {
		return role_billing;
	}

	public void setRole_billing(short role_billing) {
		this.role_billing = role_billing;
	}

	public short getRole_registry() {
		return role_registry;
	}

	public void setRole_registry(short role_registry) {
		this.role_registry = role_registry;
	}

	public short getRole_purchase_read() {
		return role_purchase_read;
	}

	public void setRole_purchase_read(short role_purchase_read) {
		this.role_purchase_read = role_purchase_read;
	}

	public short getRole_purchase_write() {
		return role_purchase_write;
	}

	public void setRole_purchase_write(short role_purchase_write) {
		this.role_purchase_write = role_purchase_write;
	}

	public short getRole_sale_write() {
		return role_sale_write;
	}

	public void setRole_sale_write(short role_sale_write) {
		this.role_sale_write = role_sale_write;
	}

	public short getRole_sql() {
		return role_sql;
	}

	public void setRole_sql(short role_sql) {
		this.role_sql = role_sql;
	}
	
	
	
}
