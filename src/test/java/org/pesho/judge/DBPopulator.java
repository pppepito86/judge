package org.pesho.judge;

import java.util.Random;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hamcrest.core.Is;
import org.junit.Assert;
import org.pesho.judge.dto.GroupDTO;
import org.pesho.judge.dto.TokenDTO;
import org.pesho.judge.model.Role;
import org.pesho.judge.model.User;

public class DBPopulator {
	 
	private String deploymentUrl;
	
	private TokenDTO token;
	private Random r;
	
	public DBPopulator(TokenDTO token, String deploymentUrl) {
		this.token = token;
		this.deploymentUrl = deploymentUrl;
		
		this.r = new Random();
	}
	
	
	public void addRandomUser() {
		User user = new User();
		user.setUsername(getRandomString(10));
		user.setPasswordHash(getRandomString(10));
		user.setEmail(getRandomString(10));
		user.setFirstname(getRandomString(10));
		user.setLastname(getRandomString(10));
		user.setRoles(Role.USER);
		
		createObject("users", user);
	}
	
	public void addRandomGroup() {
		GroupDTO group = new GroupDTO();
		group.setCreatorId(1);
		group.setDescription(getRandomString(10));
		group.setGroupName(getRandomString(10));
		
		createObject("groups", group);
	}
	
	private String getRandomString(int numChars) {
		String res = "";
		
		for(int i = 0; i < numChars; i++) {
			res += ('a' + r.nextInt(26));
		}
		
		return res;
	}
	
	private void createObject(String targetUrl, Object obj) {
		Response response = ClientBuilder.newClient()
				.target(getRequestUrl(targetUrl)).request()
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + token.getToken())
				.post(Entity.entity(obj, MediaType.APPLICATION_JSON));
		
		Assert.assertThat(response.getStatus(), Is.is(200));
	}
	
	protected String getRequestUrl(String path) {
		return new StringBuilder(deploymentUrl)
				.append("rest/1.0/" + path).toString();
	}
}
