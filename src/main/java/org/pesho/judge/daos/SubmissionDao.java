package org.pesho.judge.daos;

public class SubmissionDao {

	private String problemId;
	
	public SubmissionDao() {
	}
	
	public SubmissionDao(String problemId) {
		this.problemId = problemId;
	}
	
	public void setProblemId(String problemId) {
		this.problemId = problemId;
	}
	
	public String getProblemId() {
		return problemId;
	}
	
}
