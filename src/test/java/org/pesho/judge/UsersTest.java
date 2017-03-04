package org.pesho.judge;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.net.URL;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pesho.judge.model.Role;
import org.pesho.judge.model.User;
import org.pesho.judge.rest.JaxRsActivator;

@RunWith(Arquillian.class)
@RunAsClient
public class UsersTest {

	@ArquillianResource
	private URL deploymentUrl;

	public String getURL() {
		return deploymentUrl.toString();
	}

	@Deployment(testable = false)
	public static WebArchive createDeployment() {
		WebArchive war = ShrinkWrap.create(WebArchive.class, "testweb.war")
				.addPackage(JaxRsActivator.class.getPackage()).addPackage("org.pesho.judge.dto")
				.addPackage("org.pesho.judge.dto.mapper").addPackage("org.pesho.judge.ejb")
				.addPackage("org.pesho.judge.model").addPackage("org.pesho.judge.rest")
				.addAsResource("test-persistence.xml", "META-INF/persistence.xml")
				.addAsResource("logging.properties", "classes/META-INF/logging.properties");
		return war;
	}

	private String getRequestUrl(String path) {
		return new StringBuilder(getURL()).append("rest/1.0/" + path).toString();
	}

	@Test
	public void usersTest() throws InterruptedException {
		createRoles();

		Response response = ClientBuilder.newClient().target(getRequestUrl("users")).request().get();
		assertThat(response.getStatus(), is(200));
		testCreateUser();
		// String output = response.readEntity(String.class);
		// System.out.println(output);
		// System.out.println(response.getStatus());
	}

	private void createRoles() {
		Response response = ClientBuilder.newClient().target(getRequestUrl("roles")).request()
				.post(Entity.entity(Role.USER, MediaType.APPLICATION_JSON));
		String output = response.readEntity(String.class);
		System.out.println(output);
		assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));
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