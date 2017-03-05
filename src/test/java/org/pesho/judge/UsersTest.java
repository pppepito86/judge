package org.pesho.judge;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pesho.judge.dto.TokenDTO;
import org.pesho.judge.model.Role;
import org.pesho.judge.model.User;

@RunWith(Arquillian.class)
@RunAsClient
public class UsersTest extends ClientTestBase {

	@Test
	public void usersTest() throws InterruptedException {
		Response response = ClientBuilder.newClient().target(getRequestUrl("users")).request().get();
		assertThat(response.getStatus(), is(Response.Status.FORBIDDEN.getStatusCode()));
		
		TokenDTO tokenDTO = createAdminToken();
		System.out.println("Token: " + tokenDTO.getToken());
		
		response = ClientBuilder.newClient().target(getRequestUrl("users")).request()
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenDTO.getToken()).get();
		assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));
		List<User> list = response.readEntity(new GenericType<List<User>>() {});
		assertThat(list.size(), is(1));
		
		testCreateUser();
		
		response = ClientBuilder.newClient().target(getRequestUrl("users")).request()
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenDTO.getToken()).get();
		assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));
		list = response.readEntity(new GenericType<List<User>>() {});
		assertThat(list.size(), is(2));
	}
	
	private void testCreateUser() {
		User user = createUser();
		Response response = ClientBuilder.newClient().target(getRequestUrl("users")).request()
				.post(Entity.entity(user, MediaType.APPLICATION_JSON));
		String output = response.readEntity(String.class);
		System.out.println(output);
		assertThat(response.getStatus(), is(200));
	}

	private User createUser() {
		User user = new User();
		user.setUsername("testuser");
		user.setPasswordHash("password");
		user.setEmail("user@email.com");
		user.setFirstname("firstname");
		user.setLastname("lastname");
		user.setRoles(Role.USER);
		return user;
	}
	
}