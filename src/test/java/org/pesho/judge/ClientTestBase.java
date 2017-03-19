package org.pesho.judge;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

import java.net.URL;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.runner.RunWith;
import org.pesho.judge.dto.TokenDTO;
import org.pesho.judge.rest.JaxRsActivator;

@RunWith(Arquillian.class)
@RunAsClient
public abstract class ClientTestBase {

	@ArquillianResource
	private URL deploymentUrl;

	public String getURL() {
		return deploymentUrl.toString();
	}

	@Deployment(testable = false)
	public static WebArchive createDeployment() {
		WebArchive war = ShrinkWrap.create(WebArchive.class, "judge_test.war")
				.addPackage(JaxRsActivator.class.getPackage()).addPackage("org.pesho.judge.dto")
				.addPackage("org.pesho.judge.dto.mapper").addPackage("org.pesho.judge.ejb")
				.addPackage("org.pesho.judge.model").addPackage("org.pesho.judge.rest")
				.addPackage("org.pesho.judge.exception")
				.addPackage("org.pesho.judge.security")
				.addPackage("org.pesho.judge.run")
				.addPackage("org.pesho.judge.config")
				.addClass(TestDataCreator.class)
				.setWebXML("test-web.xml")
				.addAsResource("test-persistence.xml", "META-INF/persistence.xml")
				.addAsResource("config.properties", "META-INF/config.properties");
		return war;
	}

	protected String getRequestUrl(String path) {
		return new StringBuilder(getURL()).append("rest/1.0/" + path).toString();
	}
	
	protected TokenDTO createAdminToken() {
        Form form = new Form();
        form.param("username", "admin");
        form.param("password", "admin");
        Response response = ClientBuilder.newClient().target(getRequestUrl("authentication/login")).request().post(Entity.form(form));
        assertThat(response.getStatus(), is(200));
        TokenDTO tokenDTO = response.readEntity(TokenDTO.class);
        assertThat(tokenDTO.getToken(), not(nullValue()));
		return tokenDTO;
	}

}