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

@RunWith(Arquillian.class)
@RunAsClient
public abstract class CreateResourceTest<T, N> extends ClientTestBase {

	protected TokenDTO tokenDTO;

	private int numElementsBefore;
	
	@Test
	public void createResourceTest() throws InterruptedException {
		beforeResouceCreated();

		testCreateResource();
		
		afterResourceCreated();
	}
	
	protected void beforeResouceCreated() {
		Response response = ClientBuilder.newClient()
				.target(getRequestUrl(getRousurceURI())).request().get();
		
		assertThat(response.getStatus(), is(Response.Status.FORBIDDEN.getStatusCode()));
		
		tokenDTO = createAdminToken();
		
		response = ClientBuilder.newClient()
				.target(getRequestUrl(getRousurceURI())).request()
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenDTO.getToken()).get();
		assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));
		List<N> list = response.readEntity(new GenericType<List<N>>() {});
		numElementsBefore = list.size();
	}
	
	protected void afterResourceCreated() {
		Response response = ClientBuilder.newClient()
				.target(getRequestUrl(getRousurceURI())).request()
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenDTO.getToken()).get();
		
		assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));
		
		List<N> list = response.readEntity(new GenericType<List<N>>() {});
		assertThat(list.size(), is(numElementsBefore + 1));
	}
	
	private void testCreateResource() {
		T resource = createResource();
		
		Response response = ClientBuilder.newClient()
				.target(getRequestUrl(getRousurceURI())).request()
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenDTO.getToken())
				.post(Entity.entity(resource, MediaType.APPLICATION_JSON));
		
		assertThat(response.getStatus(), is(200));
		
		N created = response.readEntity(getDtoClassForGet());
		
		assertCreatedResourceIsValid(created);
	}

	protected abstract String getRousurceURI();
	
	protected abstract T createResource();
	
	protected abstract void assertCreatedResourceIsValid(N createdResource);
	
	protected abstract Class<T> getDtoClassForPost();
	
	protected abstract Class<N> getDtoClassForGet();
	
}