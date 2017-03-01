package org.pesho.judge.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "users")
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "id")
	private int id;

	@NotNull
	@ManyToOne
	@JoinColumn(name="roleid")
	private Role roles;
	
	@NotNull
	@Column(name = "username", unique=true, length = 50)
	private String username;

	@NotNull
	@Column(name = "firstname", length = 50)
	private String firstname;

	@NotNull
	@Column(name = "lastname", length = 50)
	private String lastname;

	@NotNull
	@Column(name = "email", unique=true, length = 50)
	private String email;

	@NotNull
	@Column (name = "passwordhash")
	private String passwordHash;
	
	@NotNull
	@Column (name = "passwordsalt")
	private String passwordSalt;
	
	@Column(name = "isdisabled")
	private boolean isDisabled;
	
	@NotNull
	@Column(name = "validationcode")
	private String validationCode = "";
	
	@Column(name = "changepasswordcode")
	private String changePasswordCode = null;
	
	@Column(name = "registrationdate", 
			columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Timestamp registrationDate;
	
	public User() {
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Role getRoles() {
		return roles;
	}

	public void setRoles(Role roles) {
		this.roles = roles;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public String getPasswordSalt() {
		return passwordSalt;
	}

	public void setPasswordSalt(String passwordSalt) {
		this.passwordSalt = passwordSalt;
	}

	public boolean isDisabled() {
		return isDisabled;
	}

	public void setDisabled(boolean isDisabled) {
		this.isDisabled = isDisabled;
	}

	public String getValidationCode() {
		return validationCode;
	}

	public void setValidationCode(String validationCode) {
		this.validationCode = validationCode;
	}

	public String getChangePasswordCode() {
		return changePasswordCode;
	}

	public void setChangePasswordCode(String changePasswordCode) {
		this.changePasswordCode = changePasswordCode;
	}

	public Timestamp getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Timestamp registrationDate) {
		this.registrationDate = registrationDate;
	}
	
	
}