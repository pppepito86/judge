package org.pesho.judge.dto;

import java.io.Serializable;
import java.sql.Timestamp;

public class SubmissionDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int id;
	
	private Integer assignmentId;
	
	private Integer problemId;
	
	private Integer userId;
	
	private String language;
	
	private String sourceFile;
	
	private Timestamp time;
	
	private String verdict;
	
	private String reason;
	
	private Integer correct = 0;
	
	private Integer total;
	
	private Integer points;
	
	public SubmissionDTO() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getAssignmentId() {
		return assignmentId;
	}

	public void setAssignmentId(Integer assignmentId) {
		this.assignmentId = assignmentId;
	}

	public Integer getProblemId() {
		return problemId;
	}

	public void setProblemId(Integer problemId) {
		this.problemId = problemId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
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

	public Integer getCorrect() {
		return correct;
	}

	public void setCorrect(Integer correct) {
		this.correct = correct;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	@Override
	public String toString() {
		return "SubmissionDTO [id=" + id + ", assignmentId=" + assignmentId + ", problemId=" + problemId + ", userId="
				+ userId + ", language=" + language + ", sourceFile=" + sourceFile + ", time=" + time + ", verdict="
				+ verdict + ", reason=" + reason + ", correct=" + correct + ", total=" + total + ", points=" + points
				+ "]";
	}
}
