package org.pesho.judge;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.runner.RunWith;
import org.pesho.judge.rest.JaxRsActivator;

@RunWith(Arquillian.class)
@RunAsClient
public class ApplicationBase {

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
				.addClass(TestDataCreator.class)
				.setWebXML("test-web.xml")
				.addAsResource("test-persistence.xml", "META-INF/persistence.xml");
		return war;
	}

	protected String getRequestUrl(String path) {
		return new StringBuilder(getURL()).append("rest/1.0/" + path).toString();
	}

}