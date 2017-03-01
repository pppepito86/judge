package org.judge.pesho;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/test")
public class Rest {

	@Inject
	UserManager userManager;
	
    @GET
    @Produces("application/json")
    public User printMessage() {
        return new User("Petar Petrov");
    }
    
    @POST
    @Produces("application/json")
    public User printMessage3() {
    	System.out.println("rest/test");
    	System.out.println("Usermanager: " + userManager);
    	userManager.createCustomer("", "");
        return new User("Petar Petrov");
    }
    
    @GET
    @Path("/2")
    @Produces("application/json")
    public Response printMessage2() {
        return Response.ok("hello").build();
    }
	
}
