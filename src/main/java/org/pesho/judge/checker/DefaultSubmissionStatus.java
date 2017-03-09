package org.pesho.judge.checker;

import org.pesho.judge.model.Submission;

public class DefaultSubmissionStatus implements SubmissionStatus {
	
	private Submission submission;
	
	public DefaultSubmissionStatus(Submission submission) {
		this.submission = submission;
	}
	
	public void updateVerdict(String step, String status, String reason, long time) {
		System.out.println("verdict updated for + " + submission.getId());
	}

}
