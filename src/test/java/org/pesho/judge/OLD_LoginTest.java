package org.pesho.judge;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.ws.rs.core.MediaType;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pesho.judge.daos.AddUserDao;
import org.pesho.judge.daos.EditRoleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:test.properties")
@TestConfiguration
@EnableWebSecurity
public class OLD_LoginTest {

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
	public void testLogin() throws Exception {
		mvc.perform(get("/index.html")).andExpect(status().isOk());

		mvc.perform(get("/api/v1/users")).andExpect(status().isUnauthorized());
		mvc.perform(get("/api/v1/users")
				.header("Authorization", "Basic cGVzaG86cGFzc3dvcmQ="))
		.andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(3)));
		
		mvc.perform(get("/api/v1/users")
				.header("Authorization", "Basic dGVhY2hlcjpwYXNzd29yZA=="))
		.andExpect(status().isForbidden());
		mvc.perform(get("/api/v1/users")
				.header("Authorization","Basic c3R1ZGVudDpwYXNzd29yZA=="))
		.andExpect(status().isForbidden());
	}

	@Test
	public void testUpdateRole() throws Exception {
		mvc.perform(get("/api/v1/users")).andExpect(status().isOk()).andExpect(jsonPath("$[2].roleid", is(3)));
		EditRoleDao role = new EditRoleDao();
		role.setRoleid(2);
		mvc.perform(put("/api/v1/users/3/roles").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(role))).andExpect(status().isOk());

		mvc.perform(get("/api/v1/users")).andExpect(status().isOk()).andExpect(jsonPath("$[2].roleid", is(2)));
	}

	@Test
	public void testUserApi() throws Exception {
		AddUserDao user = new AddUserDao();
		user.setUsername("testuser");
		user.setFirstname("first");
		user.setLastname("last");
		user.setEmail("user@email.com");
		user.setPassword("abcd1234");
		mvc.perform(post("/api/v1/users").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(user))).andExpect(status().isCreated());

		String users = mvc.perform(get("/api/v1/users")).andExpect(jsonPath("$", hasSize(4)))
				.andExpect(jsonPath("$[3].roleid", is(3))).andReturn().getResponse().getContentAsString();

		long id = objectMapper.readTree(users).get(3).get("id").asLong();

		EditRoleDao role = new EditRoleDao();
		role.setRoleid(2);
		mvc.perform(put("/api/v1/users/" + id + "/roles").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(role))).andExpect(status().isOk());

		mvc.perform(get("/api/v1/users")).andExpect(status().isOk()).andExpect(jsonPath("$[3].roleid", is(2)));
	}

}
