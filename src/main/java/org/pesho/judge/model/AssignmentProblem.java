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
@NamedQueries({
	@NamedQuery(name="AssignmentProblem.findAll", query="SELECT u FROM AssignmentProblem u"),
	@NamedQuery(name="AssignmentProblem.findByAssignment", 
		query="SELECT u FROM AssignmentProblem u WHERE u.assignment = :assignment")
})
@Table(name = "assignmentproblems")
public class AssignmentProblem implements Serializable {
	
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
	
	@Column(name = "number")
	private int number;
	
	@Column(name = "points")
	private int points;
	
	public AssignmentProblem() {
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

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

}
