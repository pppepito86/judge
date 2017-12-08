package org.pesho.judge.problems;

import java.io.File;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SubmissionsStorage {

	@Value("${work.dir}")
	private String workDir;

	public File storeSubmission(String id, String name, InputStream is) {
		File submissionsDir = new File(workDir, "submissions");
		File submissionDir = new File(submissionsDir, id);
		submissionsDir.mkdirs();
		File submissionFile = new File(submissionDir, name);
		try {
			FileUtils.copyInputStreamToFile(is, submissionFile);
			return submissionFile;
		} catch (Exception e) {
			throw new IllegalStateException("problem copying archive", e);
		}
	}

}
