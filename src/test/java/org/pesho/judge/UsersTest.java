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
import org.pesho.judge.dtos.AddUserDto;
import org.pesho.judge.dtos.EditRoleDto;
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
public class UsersTest {
	
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
	public void testListUsersOK() throws Exception {
		mvc.perform(get("/api/v1/users").header("Authorization", ADMIN_AUTH))
			.andExpect(status().isOk());
	}

	@Test
	public void testListUsers() throws Exception {
		mvc.perform(get("/api/v1/users")).andExpect(status().isUnauthorized());
	}
	
	@Test
	public void testListUsersForbidden() throws Exception {
		mvc.perform(get("/api/v1/users").header("Authorization", STUDENT_AUTH))
			.andExpect(status().isForbidden());
		
		mvc.perform(get("/api/v1/users").header("Authorization", TEACHER_AUTH))
			.andExpect(status().isForbidden());
	}
	
	@Test
	public void testCreateUser() throws Exception {
		AddUserDto user = createUser();
		mvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(user))).andExpect(status().isCreated());

		mvc.perform(get("/api/v1/users").header("Authorization", ADMIN_AUTH))
				.andExpect(jsonPath("$", hasSize(4)))
				.andExpect(jsonPath("$[3].roleid", is(3)));
	}

	@Test
	public void testUpdateRole() throws Exception {
		mvc.perform(get("/api/v1/users").header("Authorization", ADMIN_AUTH)).andExpect(status().isOk()).andExpect(jsonPath("$[2].roleid", is(3)));
		EditRoleDto role = new EditRoleDto();
		role.setRoleid(2);
		mvc.perform(put("/api/v1/users/3/roles").header("Authorization", ADMIN_AUTH)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(role))).andExpect(status().isOk());

		mvc.perform(get("/api/v1/users").header("Authorization", ADMIN_AUTH)).andExpect(status().isOk()).andExpect(jsonPath("$[2].roleid", is(2)));
	}
	
	@Test
	public void testUpdateRoleUnauthorized() throws Exception {
		EditRoleDto role = new EditRoleDto();
		role.setRoleid(2);
		mvc.perform(put("/api/v1/users/3/roles")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(role))).andExpect(status().isUnauthorized());
	}
	
	@Test
	public void testUpdateRoleForbidden() throws Exception {
		EditRoleDto role = new EditRoleDto();
		role.setRoleid(2);
		mvc.perform(put("/api/v1/users/3/roles").header("Authorization", TEACHER_AUTH)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(role))).andExpect(status().isForbidden());
		mvc.perform(put("/api/v1/users/3/roles").header("Authorization", STUDENT_AUTH)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(role))).andExpect(status().isForbidden());
	}

	private AddUserDto createUser() {
		AddUserDto user = new AddUserDto();
		user.setUsername("testuser");
		user.setFirstname("first");
		user.setLastname("last");
		user.setEmail("user@email.com");
		user.setPassword("abcd1234");
		return user;
	}

}
