package org.pesho.judge;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.BeforeClass;
import org.junit.Test;
import org.pesho.judge.model.User;

public class RestTest {

	@BeforeClass
	public static void init() throws Exception {
	}
	
	@Test
	public void test() {
		Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:8080/judge.web/rest/");
    	//Response response = target.path("test").request(MediaType.APPLICATION_JSON).post(Entity.json(role));
        Response response = target.path("test").request(MediaType.APPLICATION_JSON).get();
    	User responseRole = (User) response.readEntity(User.class);

        System.out.println(response.getStatus());
        System.out.println(responseRole);
	}
}
