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

@Entity
@Table(name = "assignments")
public class Assignment implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name = "id")
	private int id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "starttime", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Timestamp startTime;
	
	@Column(name = "endtime", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Timestamp endTime;
	
	@ManyToOne
	@JoinColumn(name = "author")
	private User author;
	
	@ManyToOne
	@JoinColumn(name = "groupid")
	private Group group;
	
	@Column(name = "testinfo")
	private String testInfo;

	@Column(name = "standings")
	private String standings;
	
	public Assignment() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public String getTestInfo() {
		return testInfo;
	}

	public void setTestInfo(String testInfo) {
		this.testInfo = testInfo;
	}

	public String getStandings() {
		return standings;
	}

	public void setStandings(String standings) {
		this.standings = standings;
	}

}
