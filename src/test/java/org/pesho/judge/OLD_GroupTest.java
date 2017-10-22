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
import org.pesho.judge.daos.AddGroupDao;
import org.pesho.judge.daos.AddUserDao;
import org.pesho.judge.daos.EditRoleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
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
@Import(OLD_Config.class)
public class OLD_GroupTest {

	@Autowired
	private WebApplicationContext context;

	private MockMvc mvc;
	private ObjectMapper objectMapper = new ObjectMapper();

	@Before
	public void setUp() {
		this.mvc = MockMvcBuilders.webAppContextSetup(this.context).build();
	}

	@Test
	public void testCreateGroup() throws Exception {
		AddGroupDao group = new AddGroupDao();
		group.setGroupname("5a");
		group.setDescription("smg");
		mvc.perform(post("/api/v1/groups").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(group))).andExpect(status().isCreated());

		mvc.perform(get("/api/v1/groups")).andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[1].groupname", is("5a")))
				.andExpect(jsonPath("$[1].description", is("smg")))
				.andExpect(jsonPath("$[1].creatorid", is(2)))
				.andExpect(jsonPath("$[1].username", is("teacher")));
	}
	
	@Test
	public void testListGroup() throws Exception {
		AddGroupDao group = new AddGroupDao();
		group.setGroupname("5a");
		group.setDescription("smg");
		mvc.perform(post("/api/v1/groups").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(group))).andExpect(status().isCreated());
		
		group.setGroupname("9b");
		group.setDescription("java");
		mvc.perform(post("/api/v1/groups").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(group))).andExpect(status().isCreated());

		mvc.perform(get("/api/v1/groups")).andExpect(jsonPath("$", hasSize(3)))
				.andExpect(jsonPath("$[0].groupname", is("public")))
				.andExpect(jsonPath("$[1].groupname", is("5a")))
				.andExpect(jsonPath("$[2].groupname", is("9b")));
		
		mvc.perform(get("/api/v1/groups?teacher=2")).andExpect(jsonPath("$", hasSize(3)))
		.andExpect(jsonPath("$[0].groupname", is("public")))
		.andExpect(jsonPath("$[1].groupname", is("5a")))
		.andExpect(jsonPath("$[2].groupname", is("9b")));

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

		String users = mvc.perform(get("/api/v1/users/")).andExpect(jsonPath("$", hasSize(4)))
				.andExpect(jsonPath("$[3].roleid", is(3))).andReturn().getResponse().getContentAsString();

		long id = objectMapper.readTree(users).get(3).get("id").asLong();

		EditRoleDao role = new EditRoleDao();
		role.setRoleid(2);
		mvc.perform(put("/api/v1/users/" + id + "/roles").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(role))).andExpect(status().isOk());

		mvc.perform(get("/api/v1/users")).andExpect(status().isOk()).andExpect(jsonPath("$[3].roleid", is(2)));
	}

}
