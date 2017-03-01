package org.pesho.judge.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.pesho.judge.model.Assignment;
import org.pesho.judge.model.Problem;

@Path("assignments")
public class AssignmentsResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Problem> listAssignments() {
        return null;
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Assignment createAssingment(Assignment assignment) {
        return assignment;
    }
    
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Assignment getAssignment(@PathParam("id") int assignmentId) {
        return null;
    }
    
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Assignment updateAssignment(@PathParam("id") int assignmentId, Assignment assignment) {
        return null;
    }
    
    @PUT
    @Path("{id}/problems")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Problem> getProblemsPerAssignment(@PathParam("id") int assignmentId) {
    	
		return null;
    }
    
    @GET
    @Path("{id}/users/{userId}/points")
    @Produces(MediaType.APPLICATION_JSON)
    public int getProblemsPerAssignment(@PathParam("id") int assignmentId, 
    		@PathParam("userId") int userId) {
    	// TODO
		return 0;
    }
}
