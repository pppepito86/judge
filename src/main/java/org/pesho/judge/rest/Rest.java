package org.pesho.judge.rest;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.pesho.judge.ejb.UserManager;
import org.pesho.judge.model.User;

@Path("/test")
public class Rest {

	@Inject
	UserManager userManager;
	
    @GET
    @Produces("application/json")
    public User printMessage() {
        return new User();
    }
    
    @POST
    @Produces("application/json")
    public User printMessage3() {
    	System.out.println("rest/test");
    	System.out.println("Usermanager: " + userManager);
    	userManager.createCustomer("", "");
        return new User();
    }
    
    @GET
    @Path("/2")
    @Produces("application/json")
    public Response printMessage2() {
        return Response.ok("hello").build();
    }
	
}
