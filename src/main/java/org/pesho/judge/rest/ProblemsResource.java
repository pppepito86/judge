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

import org.pesho.judge.model.Problem;

@Path("problems")
public class ProblemsResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Problem> listProblems() {
        return null;
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Problem createProblem(Problem problem) {
        return problem;
    }
    
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Problem getProblem(@PathParam("id") int problemId) {
        return null;
    }
    
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Problem updateProblem(@PathParam("id") int problemId, Problem problem) {
        return null;
    }
}
