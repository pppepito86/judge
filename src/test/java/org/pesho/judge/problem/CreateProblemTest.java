package org.pesho.judge.problem;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.After;
import org.junit.Test;
import org.pesho.judge.ClientTestBase;
import org.pesho.judge.dto.ProblemDTO;
import org.pesho.judge.dto.TokenDTO;

public class CreateProblemTest extends ClientTestBase {
	
	private TokenDTO tokenDTO;
	private int problemId;

	@Test
	public void test() {
		tokenDTO = createAdminToken();
		Response response = ClientBuilder.newClient()
				.target(getRequestUrl("problems")).request()
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenDTO.getToken())
				.post(Entity.entity(createProblem(), MediaType.APPLICATION_JSON));
		
		assertThat(response.getStatus(), is(200));
		
		ProblemDTO created = response.readEntity(ProblemDTO.class);
		problemId = created.getId();
		assertThat(created.getTests(), is(0));

		System.out.println("******" + created.getId());
		
		response = ClientBuilder.newClient()
				.target(getRequestUrl("problems/" + created.getId() + "/tests")).request()
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenDTO.getToken())
				.put(Entity.entity(testsIS(), MediaType.APPLICATION_OCTET_STREAM));
		
		assertThat(response.getStatus(), is(200));
		String[] prefixes = new String[]{"input", "output"};
		for (int i = 1; i <= 6; i++) {
			for (String prefix: prefixes) {
				File file = new File("src/test/resources/docker/" + problemId, prefix + i);
				assertTrue(file.exists());
			}
		}
		
		response = ClientBuilder.newClient()
				.target(getRequestUrl("problems/" + created.getId())).request()
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenDTO.getToken())
				.get();
		ProblemDTO createdWithTests = response.readEntity(ProblemDTO.class);
		assertThat(createdWithTests.getTests(), is(6));
	}
	
	@After
	public void after() {
		String[] prefixes = new String[]{"input", "output"};
		for (int i = 1; i <= 6; i++) {
			for (String prefix: prefixes) {
				File file = new File("src/test/resources/docker/" + problemId + "/", prefix + i);
				assertTrue(file.delete());
			}
		}
	}
	

	protected ProblemDTO createProblem() {
		ProblemDTO problem = new ProblemDTO();
		problem.setAuthorId(3);
		problem.setDescription("example desc");
		problem.setLanguages("java, c++");
		problem.setName("problem x");
		problem.setPoints(100);
		problem.setSourceChecker("path/to/schecker");
		problem.setTestChecker("path/to/tchecker");
		problem.setTests(15);
		return problem;
	}
	
	protected InputStream testsIS() {
		try {
			InputStream is = new FileInputStream(new File("src/test/resources/problems/2/tests.zip"));
			return is;
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}
	

}
