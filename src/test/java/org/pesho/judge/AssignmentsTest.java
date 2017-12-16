package org.pesho.judge;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pesho.judge.dtos.AddAssignmentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Sql("classpath:schema.sql")
@TestPropertySource(locations = "classpath:test.properties")
@TestConfiguration
@EnableWebSecurity
public class AssignmentsTest {

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
	public void testListAssignments() throws Exception {
		mvc.perform(get("/api/v1/assignments")).andExpect(status().isUnauthorized());
		mvc.perform(get("/api/v1/assignments").header("Authorization", STUDENT_AUTH))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(1))).andExpect(jsonPath("$[0].id", is(1)));
		mvc.perform(get("/api/v1/assignments").header("Authorization", TEACHER_AUTH)).andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1))).andExpect(jsonPath("$[0].id", is(2)));
		mvc.perform(get("/api/v1/assignments").header("Authorization", ADMIN_AUTH)).andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2))).andExpect(jsonPath("$[0].id", is(2)))
				.andExpect(jsonPath("$[1].id", is(1)));
	}

	@Test
	public void testGetAssignment() throws Exception {
		mvc.perform(get("/api/v1/assignments/1")).andExpect(status().isUnauthorized());
		mvc.perform(get("/api/v1/assignments/10")).andExpect(status().isUnauthorized());
		
		mvc.perform(get("/api/v1/assignments/1").header("Authorization", STUDENT_AUTH)).andExpect(status().isForbidden());
		mvc.perform(get("/api/v1/assignments/2").header("Authorization", STUDENT_AUTH)).andExpect(status().isForbidden());
		mvc.perform(get("/api/v1/assignments/3").header("Authorization", STUDENT_AUTH)).andExpect(status().isForbidden());
		
		mvc.perform(get("/api/v1/assignments/1").header("Authorization", TEACHER_AUTH)).andExpect(status().isOk())
			.andExpect(jsonPath("id", is(1)));
		mvc.perform(get("/api/v1/assignments/2").header("Authorization", TEACHER_AUTH)).andExpect(status().isOk())
			.andExpect(jsonPath("id", is(2)));
		mvc.perform(get("/api/v1/assignments/3").header("Authorization", TEACHER_AUTH)).andExpect(status().isNoContent());

		mvc.perform(get("/api/v1/assignments/1").header("Authorization", ADMIN_AUTH)).andExpect(status().isOk())
			.andExpect(jsonPath("id", is(1)));
		mvc.perform(get("/api/v1/assignments/2").header("Authorization", ADMIN_AUTH)).andExpect(status().isOk())
			.andExpect(jsonPath("id", is(2)));
		mvc.perform(get("/api/v1/assignments/3").header("Authorization", ADMIN_AUTH)).andExpect(status().isNoContent());
	}

	@Test
	public void testCreateAssignment() throws Exception {
		String locationHeader = mvc.perform(post("/api/v1/assignments")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(createAssignment()))
				.header("Authorization", TEACHER_AUTH))
				.andExpect(status().isCreated())
				.andReturn().getResponse().getHeader(HttpHeaders.LOCATION);
		assertThat(locationHeader, is("http://localhost/api/v1/assignments/7"));
		
		mvc.perform(get("/api/v1/assignments/7").header("Authorization", TEACHER_AUTH))
			.andExpect(status().isOk())
			.andExpect(jsonPath("name", is("Test 1")))
			.andExpect(jsonPath("problems", hasSize(2)))
			.andExpect(jsonPath("problems[0].problemid", is(1)))
			.andExpect(jsonPath("problems[1].problemid", is(2)));
	}
	
	private AddAssignmentDto createAssignment() {
		AddAssignmentDto assignment = new AddAssignmentDto();
		assignment.setName("Test 1");
		assignment.setGroupid("2");
		assignment.setStartTime(null);
		assignment.setEndTime(System.currentTimeMillis());
		assignment.setProblem1("1, 2");
		assignment.setTestinfo(Optional.empty());
		assignment.setStandings(Optional.empty());
		return assignment;
	}

}
