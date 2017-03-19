package org.pesho.judge;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import org.junit.After;
import org.junit.Assert;
import org.pesho.judge.dto.SubmissionDTO;

public class CreateSubmissionResourceTest extends CreateResourceTest<SubmissionDTO, SubmissionDTO> {
	
	@Override
	protected void beforeResouceCreated() {
		tokenDTO = createAdminToken();
	}
	
	@Override
	protected String getRousurceURI() {
		return "assignments/1/problems/1/submissions?file=Sum.java";
	}
	
	protected Entity<?> getResourceEntity() {
		return Entity.entity(createResource2(), MediaType.APPLICATION_OCTET_STREAM);
	}
	
	protected InputStream createResource2() {
		try {
			InputStream is = new FileInputStream(new File("src/test/resources/workdir/submissions/1/Sum.java"));
			return is;
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	@After
	public void after() {
		new File("src/test/resources/docker/Sum.java").delete();
	}
	
	@Override
	protected void afterResourceCreated() {
		File submission = new File("src/test/resources/docker/Sum.java");
		Assert.assertTrue(submission.exists());
	}
	
	@Override
	protected SubmissionDTO createResource() {
		return null;
	}
	
	@Override
	protected void assertCreatedResourceIsValid(SubmissionDTO createdResource) {
	}

	@Override
	protected Class<SubmissionDTO> getDtoClassForPost() {
		return SubmissionDTO.class;
	}

	@Override
	protected Class<SubmissionDTO> getDtoClassForGet() {
		return SubmissionDTO.class;
	}

}
