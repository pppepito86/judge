package org.pesho.judge;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.ws.rs.core.MediaType;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pesho.judge.daos.AddProblemDao;
import org.pesho.judge.daos.AddUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:test.properties")
@TestConfiguration
@EnableWebSecurity
public class ProblemsTest {

	private static String ADMIN_AUTH = "Basic cGVzaG86cGFzc3dvcmQ=";
	private static String TEACHER_AUTH = "Basic dGVhY2hlcjpwYXNzd29yZA==";
	private static String STUDENT_AUTH = "Basic c3R1ZGVudDpwYXNzd29yZA==";

	@Autowired
	private WebApplicationContext context;

	private MockMvc mvc;
	private ObjectMapper objectMapper = new ObjectMapper();

	@Before
	public void setUp() {
		this.mvc = MockMvcBuilders.webAppContextSetup(this.context).apply(springSecurity()).build();
	}

	@Test
	public void testListProblems() throws Exception {
		mvc.perform(get("/api/v1/problems")).andExpect(status().isUnauthorized());
		mvc.perform(get("/api/v1/problems").header("Authorization", STUDENT_AUTH)).andExpect(status().isForbidden());
		mvc.perform(get("/api/v1/problems").header("Authorization", TEACHER_AUTH)).andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2))).andExpect(jsonPath("$[0].id", is(1)))
				.andExpect(jsonPath("$[1].id", is(2)));
		mvc.perform(get("/api/v1/problems").header("Authorization", ADMIN_AUTH)).andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(3))).andExpect(jsonPath("$[0].id", is(1)))
				.andExpect(jsonPath("$[1].id", is(2))).andExpect(jsonPath("$[2].id", is(3)));
	}

	@Test
	public void testListMyProblems() throws Exception {
		mvc.perform(get("/api/v1/myproblems")).andExpect(status().isUnauthorized());
		mvc.perform(get("/api/v1/myproblems").header("Authorization", STUDENT_AUTH)).andExpect(status().isForbidden());
		mvc.perform(get("/api/v1/myproblems").header("Authorization", TEACHER_AUTH)).andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1))).andExpect(jsonPath("$[0].id", is(2)));
		mvc.perform(get("/api/v1/myproblems").header("Authorization", ADMIN_AUTH)).andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2))).andExpect(jsonPath("$[0].id", is(1)))
				.andExpect(jsonPath("$[1].id", is(3)));
	}

	@Test
	public void testProblemsTags() throws Exception {
		mvc.perform(get("/api/v1/problems/1").header("Authorization", ADMIN_AUTH)).andExpect(status().isOk())
				.andExpect(jsonPath("tags", hasSize(2))).andExpect(jsonPath("tags[0].tag", is("dp")))
				.andExpect(jsonPath("tags[1].tag", is("binarysearch")));
	}

	@Test
	public void testGetProblem() throws Exception {
		mvc.perform(get("/api/v1/problems/1")).andExpect(status().isUnauthorized());
		mvc.perform(get("/api/v1/problems/10")).andExpect(status().isUnauthorized());
		mvc.perform(get("/api/v1/problems/1").header("Authorization", STUDENT_AUTH)).andExpect(status().isForbidden());
		mvc.perform(get("/api/v1/problems/10").header("Authorization", STUDENT_AUTH)).andExpect(status().isForbidden());
		mvc.perform(get("/api/v1/problems/1").header("Authorization", TEACHER_AUTH)).andExpect(status().isOk())
				.andExpect(jsonPath("id", is(1)));
		mvc.perform(get("/api/v1/problems/2").header("Authorization", TEACHER_AUTH)).andExpect(status().isOk())
				.andExpect(jsonPath("id", is(2)));
		mvc.perform(get("/api/v1/problems/3").header("Authorization", TEACHER_AUTH)).andExpect(status().isForbidden());
		mvc.perform(get("/api/v1/problems/10").header("Authorization", TEACHER_AUTH)).andExpect(status().isNoContent());
		mvc.perform(get("/api/v1/problems/1").header("Authorization", ADMIN_AUTH)).andExpect(status().isOk())
				.andExpect(jsonPath("id", is(1)));
		mvc.perform(get("/api/v1/problems/2").header("Authorization", ADMIN_AUTH)).andExpect(status().isOk())
				.andExpect(jsonPath("id", is(2)));
		mvc.perform(get("/api/v1/problems/3").header("Authorization", ADMIN_AUTH)).andExpect(status().isOk())
				.andExpect(jsonPath("id", is(3)));
		mvc.perform(get("/api/v1/problems/10").header("Authorization", ADMIN_AUTH)).andExpect(status().isNoContent());
	}

	@Test
	public void testCreateProblem() throws Exception {
		AddProblemDao problem = createProblem();
		mvc.perform(post("/api/v1/problems").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(problem))).andExpect(status().isUnauthorized());
		mvc.perform(post("/api/v1/problems").header("Authorization", STUDENT_AUTH)
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(problem)))
				.andExpect(status().isForbidden());

		mvc.perform(post("/api/v1/problems").header("Authorization", TEACHER_AUTH)
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(problem)))
				.andExpect(status().isCreated());

		mvc.perform(get("/api/v1/problems").header("Authorization", TEACHER_AUTH)).andExpect(jsonPath("$", hasSize(3)))
				.andExpect(jsonPath("$[2].name", is("a+b+c")));
	}

	private AddProblemDao createProblem() {
		AddProblemDao problem = new AddProblemDao();
		problem.setProblemname("a+b+c");
		problem.setVersion("v1");
		problem.setText("namerete sbora na chislata a, b i c");
		problem.setTest("1 2 3\n#\n6\n");
		problem.setLanguage("{\"c++\":{\"Language\":\"c++\",\"TimeLimit\":1000,\"MemoryLimit\":64}}");
		problem.setVisibility("public");
		problem.setPoints("100");
		problem.setTags("easy");
		return problem;
	}

}
