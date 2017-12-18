package org.pesho.judge;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.InputStream;

import javax.ws.rs.core.HttpHeaders;

import org.apache.commons.io.FileUtils;
import org.apache.http.entity.ContentType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pesho.judge.dtos.AddProblemDto;
import org.pesho.judge.dtos.AddProblemDto.Language;
import org.pesho.judge.dtos.AddProblemDto.Languages;
import org.pesho.judge.dtos.AddSubmissionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql("classpath:schema.sql")
@TestPropertySource(locations = "classpath:test.properties")
@TestConfiguration
@EnableWebSecurity
public class SubmissionsParallelTest {

	private static String ADMIN_AUTH = "Basic cGVzaG86cGFzc3dvcmQ=";
	private static String TEACHER_AUTH = "Basic dGVhY2hlcjpwYXNzd29yZA==";
	private static String STUDENT_AUTH = "Basic c3R1ZGVudDpwYXNzd29yZA==";

	@Autowired
	private WebApplicationContext context;
	
    @Value("${work.dir}")
    private String workDir;
	
	private MockMvc mvc;
	private ObjectMapper objectMapper = new ObjectMapper();
	private int problemId;

	@Before
	public void setUp() throws Exception {
		this.mvc = MockMvcBuilders.webAppContextSetup(this.context).apply(springSecurity()).build();
		this.problemId = submitProblem();
	}
	
	@After
	public void clean() throws Exception {
		try {
			FileUtils.forceDelete(new File(workDir));
		} catch (Exception e) {
		}
	}

	@Test
	public void testCreateSubmissions() throws Exception {
		createSubmission("sum_three.cpp", 214);
		createSubmission("SumThree.java", 215);
		createSubmission("sum_three_tl.cpp", 216);
		createSubmission("sum_three_ce.cpp", 217);
		
		mvc.perform(get("/api/v1/submissions/queue").header("Authorization", ADMIN_AUTH))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(4)))
				.andExpect(jsonPath("$[0].id", is(214)))
				.andExpect(jsonPath("$[1].id", is(215)))
				.andExpect(jsonPath("$[2].id", is(216)))
				.andExpect(jsonPath("$[3].id", is(217)));

		Thread.sleep(15000);
		checkSubmission(214, "Accepted", 100);
		checkSubmission(215, "Accepted", 100);
		checkSubmission(216, "0%", 0);
		checkSubmission(217, "Compilation Failed", 0);
	}

	private void createSubmission(String sourceFile, int id) throws Exception {
		MockMultipartFile multipartMetadata = new MockMultipartFile("metadata", null,
				ContentType.APPLICATION_JSON.getMimeType(), objectMapper.writeValueAsBytes(createSubmission(problemId)));
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(sourceFile);
		MockMultipartFile multipartFile = new MockMultipartFile("file", sourceFile, "text/plain", is);
		String locationHeader = this.mvc.perform(fileUpload("/api/v1/submissions")
				.file(multipartMetadata)
				.file(multipartFile)
				.header("Authorization", STUDENT_AUTH))
				.andExpect(status().isCreated())
				.andReturn().getResponse().getHeader(HttpHeaders.LOCATION);
		assertThat(locationHeader, is("http://localhost/api/v1/submissions/" + id));
	}
	
	private void checkSubmission(int id, String verdict, int points) throws Exception {
		mvc.perform(get("/api/v1/submissions/" + id).header("Authorization", STUDENT_AUTH))
		.andExpect(jsonPath("verdict", is(verdict)))
		.andExpect(jsonPath("points", is(points)));		
	}
	
	private int submitProblem() throws Exception {
		MockMultipartFile multipartMetadata = new MockMultipartFile("metadata", null, ContentType.APPLICATION_JSON.getMimeType(), objectMapper.writeValueAsBytes(createProblem()));
		InputStream is = this.getClass().getClassLoader().getResourceAsStream("tests.zip");
		MockMultipartFile multipartFile = new MockMultipartFile("file", "tests.zip", "text/plain", is);
		
		String locationHeader = this.mvc.perform(fileUpload("/api/v1/problems")
				.file(multipartMetadata)
				.file(multipartFile)
				.header("Authorization", TEACHER_AUTH))
				.andExpect(status().isCreated()).andReturn().getResponse().getHeader("Location");
		
		String id = locationHeader.substring(locationHeader.lastIndexOf("/") + 1);
		return Integer.valueOf(id);
	}

	private AddProblemDto createProblem() {
		AddProblemDto problem = new AddProblemDto();
		problem.setProblemname("a+b+c");
		problem.setVersion("v1");
		problem.setText("namerete sbora na chislata a, b i c");
		problem.setTest("1 2 3\n#\n6\n");
		problem.setLanguages(new Languages(new Language("c++", 1000, 64), new Language("java", 1000, 64)));
		problem.setVisibility("public");
		problem.setPoints("100");
		problem.setTags("easy");
		return problem;
	}

	
	private AddSubmissionDto createSubmission(int problemId) {
		AddSubmissionDto submission = new AddSubmissionDto();
		submission.setAssignmentId(1);
		submission.setProblemId(problemId);
		submission.setLanguage("c++");
		return submission;
	}
	
}
