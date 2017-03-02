package org.pesho.judge;

import java.net.URL;
import java.util.logging.Logger;

import javax.print.attribute.standard.Media;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pesho.judge.rest.JaxRsActivator;
import org.pesho.judge.rest.UsersResource;

@RunWith(Arquillian.class)
@RunAsClient
public class UsersIT {

    private Logger log = Logger.getLogger(UsersIT.class.getName());

    @ArquillianResource
    private URL deploymentUrl;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        WebArchive war = ShrinkWrap.create(WebArchive.class, "testweb.war")
                .addPackage(JaxRsActivator.class.getPackage())
                .addPackage(UsersResource.class.getPackage())
                .addPackage("org.pesho.judge.model");
        		
                //.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
        return war;
    }

    private String getRequestUrl() {
        return new StringBuilder(deploymentUrl.toString())
                .append("rest/1.0/users/10")
                .toString();
    }

    @Test
    public void usersTest() throws InterruptedException{
        Response response = ClientBuilder.newClient().target(getRequestUrl()).request().get();
        System.out.println(response.getStatus());
    }
}