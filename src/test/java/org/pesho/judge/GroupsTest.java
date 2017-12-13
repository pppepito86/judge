package org.pesho.judge;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.ws.rs.core.MediaType;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pesho.judge.dtos.AddGroupDto;
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
public class GroupsTest {
	
	private static String ADMIN_AUTH = "Basic cGVzaG86cGFzc3dvcmQ=";
	private static String TEACHER_AUTH = "Basic dGVhY2hlcjpwYXNzd29yZA==";
	private static String STUDENT_AUTH = "Basic c3R1ZGVudDpwYXNzd29yZA==";

	@Autowired
	private WebApplicationContext context;

	private MockMvc mvc;
	private ObjectMapper objectMapper = new ObjectMapper();

	@Before
	public void setUp() {
		this.mvc = MockMvcBuilders.webAppContextSetup(this.context)
				.apply(springSecurity()).build();
	}

	@Test
	public void testCreateGroup() throws Exception {
		AddGroupDto group = new AddGroupDto();
		group.setGroupname("5a");
		group.setDescription("smg");

		mvc.perform(post("/api/v1/groups").header("Authorization", TEACHER_AUTH)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(group))).andExpect(status().isCreated());

		mvc.perform(get("/api/v1/groups").header("Authorization", ADMIN_AUTH)).andExpect(jsonPath("$", hasSize(3)))
				.andExpect(jsonPath("$[2].groupname", is("5a")))
				.andExpect(jsonPath("$[2].description", is("smg")))
				.andExpect(jsonPath("$[2].creatorid", is(2)))
				.andExpect(jsonPath("$[2].username", is("teacher")));
	}
	
	@Test
	public void testCreateGroupUnauthorized() throws Exception {
		AddGroupDto group = new AddGroupDto();
		group.setGroupname("5a");
		group.setDescription("smg");
		mvc.perform(post("/api/v1/groups")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(group))).andExpect(status().isUnauthorized());
	}

	@Test
	public void testCreateGroupForbidden() throws Exception {
		AddGroupDto group = new AddGroupDto();
		group.setGroupname("5a");
		group.setDescription("smg");
		mvc.perform(post("/api/v1/groups").header("Authorization", STUDENT_AUTH)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(group))).andExpect(status().isForbidden());
	}
	
	@Test
	public void testListGroups() throws Exception {
		mvc.perform(get("/api/v1/groups").header("Authorization", ADMIN_AUTH))
			.andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2)));
		mvc.perform(get("/api/v1/groups").header("Authorization", TEACHER_AUTH))
			.andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1)))
			.andExpect(jsonPath("$[0]groupname", is("9b")));
		mvc.perform(get("/api/v1/groups").header("Authorization", STUDENT_AUTH))
			.andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1)))
			.andExpect(jsonPath("$[0]groupname", is("public")));

	}
	
	@Test
	public void testJoinGroup() throws Exception {
		mvc.perform(put("/api/v1/groups/9b/users").header("Authorization", STUDENT_AUTH))
			.andExpect(status().isOk());
		mvc.perform(get("/api/v1/groups").header("Authorization", STUDENT_AUTH))
			.andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2)))
			.andExpect(jsonPath("$[1]groupname", is("9b")));
	}
	
}
