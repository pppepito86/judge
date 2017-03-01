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
@Table(name = "submissions")
public class Submission implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "id")
	private int id;
	
	@ManyToOne
	@JoinColumn(name = "assignmentid")
	private Assignment assignment;
	
	@ManyToOne
	@JoinColumn(name = "problemid")
	private Problem problem;
	
	@ManyToOne
	@JoinColumn(name = "userid")
	private User user;
	
	@Column(name = "language")
	private String language;
	
	@Column(name = "sourcefile")
	private String sourceFile;
	
	@Column(name = "time", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Timestamp time;
	
	@Column(name = "verdict")
	private String verdict;
	
	@Column(name = "reason")
	private String reason;
	
	@Column(name = "correct")
	private int correct;
	
	@Column(name = "total")
	private int total;
	
	@Column(name = "points")
	private int points;
	
	public Submission() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Assignment getAssignment() {
		return assignment;
	}

	public void setAssignment(Assignment assignment) {
		this.assignment = assignment;
	}

	public Problem getProblem() {
		return problem;
	}

	public void setProblem(Problem problem) {
		this.problem = problem;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getSourceFile() {
		return sourceFile;
	}

	public void setSourceFile(String sourceFile) {
		this.sourceFile = sourceFile;
	}

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

	public String getVerdict() {
		return verdict;
	}

	public void setVerdict(String verdict) {
		this.verdict = verdict;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public int getCorrect() {
		return correct;
	}

	public void setCorrect(int correct) {
		this.correct = correct;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

}
