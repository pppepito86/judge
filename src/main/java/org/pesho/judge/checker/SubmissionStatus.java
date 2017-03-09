package org.pesho.judge.checker;

public interface SubmissionStatus {
	
	void updateVerdict(String step, String status, String reason, long time);

}
