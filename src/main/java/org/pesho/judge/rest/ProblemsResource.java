package org.pesho.judge.rest;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.pesho.judge.model.Problem;
import org.pesho.judge.model.Tag;

@Stateless
@Path("problems")
public class ProblemsResource {
	
	@PersistenceContext(unitName = "judge")
	EntityManager em;
	
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Problem> listProblems() {
    	TypedQuery<Problem> query = em.createNamedQuery("Problem.findAll", Problem.class);
    	List<Problem> results = query.getResultList();
        return results;
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Problem createProblem(Problem problem) {
    	// TODO
        return null;
    }
    
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Problem getProblem(@PathParam("id") int problemId) {
        Problem problem = em.find(Problem.class, (Integer)problemId);
    	return problem;
    }
    
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Problem updateProblem(@PathParam("id") int problemId, Problem problem) {
    	// TODO:
        return null;
    }
    
    @GET
    @Path("{id}/tags")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Tag> getTags(@PathParam("id") int problemId) {
    	Problem problem = getProblem(problemId);
    	
    	TypedQuery<Tag> query = em
    			.createNamedQuery("Tag.findByProblem", Tag.class)
    			.setParameter("problem", problem);
    	
    	List<Tag> results = query.getResultList();
        return results;
    }
}
