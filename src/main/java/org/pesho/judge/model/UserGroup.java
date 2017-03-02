package org.pesho.judge.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "usergroups")
@NamedQueries({
	@NamedQuery(name="UserGroup.findAll", query="SELECT u FROM UserGroup u"),
	@NamedQuery(name="UserGroup.findByGroup", 
		query="SELECT u FROM UserGroup u WHERE u.group = :group")
})

public class UserGroup implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "id")
	private int id;

	@ManyToOne
	@JoinColumn(name = "userid", unique=true)
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "groupid", unique=true)
	private Group group;
	
	@Column (name = "roleid")
	private int role;
	
	public UserGroup() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public Integer getRole() {
		return role;
	}

	public void setRole(Integer role) {
		this.role = role;
	}
}
