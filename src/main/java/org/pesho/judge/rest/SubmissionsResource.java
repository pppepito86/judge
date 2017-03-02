package org.pesho.judge.rest;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.pesho.judge.model.Submission;

@Stateless
@Path("submissions")
public class SubmissionsResource {
	
	@PersistenceContext(unitName = "judge")
	EntityManager em;
	
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Submission> listSubmisssions() {
    	TypedQuery<Submission> query = em.createNamedQuery("Submission.findAll", Submission.class);
    	List<Submission> results = query.getResultList();
    	return results;
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Submission createSubmission(Submission submission) {
    	// TODO
        return null;
    }
    
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Submission getSubmission(@PathParam("id") int submissionId) {
    	Submission submission = em.find(Submission.class, (Integer)submissionId);
        return submission;
    }
    
}
