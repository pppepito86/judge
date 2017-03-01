package org.pesho.judge.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.pesho.judge.model.Problem;
import org.pesho.judge.model.Submission;

@Path("submissions")
public class SubmissionsResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Problem> listSubmisssions() {
        return null;
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Submission createSubmission(Submission submission) {
        return submission;
    }
    
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Submission getSubmission(@PathParam("id") int submissionId) {
        return null;
    }
    
}
