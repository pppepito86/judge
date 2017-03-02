package org.pesho.judge.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "roles")
@NamedQuery(name="Role.findAll", query="SELECT u FROM Role u")
public class Role implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static final Role ADMIN = new Role(1, "admin", "admin role");
	public static final Role TEACHER = new Role(2, "teacher", "teacher role");
	public static final Role USER = new Role(3, "user", "user role");

	@Id
	@Column(name = "id")
	private int id;
	
	@Column(name = "rolename")
	private String roleName; // TODO: Reference to Role object/table
	
	@Column(name = "description")
	private String description;

	
	public Role() {
	}
	
	public Role(int id, String roleName, String description) {
		super();
		this.id = id;
		this.roleName = roleName;
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}