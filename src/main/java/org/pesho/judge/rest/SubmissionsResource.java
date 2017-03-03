package org.pesho.judge.rest;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.pesho.judge.dto.SubmissionDTO;
import org.pesho.judge.dto.mapper.Mapper;
import org.pesho.judge.model.Submission;

@Stateless
@Path("submissions")
public class SubmissionsResource {
	
	private static final String SORT_LATEST = "latest";
	private static final String SORT_OLDEST = "oldest";
	
	@PersistenceContext(unitName = "judge")
	EntityManager em;
	
	public List<SubmissionDTO> listSubmisssions() {
		return listSubmisssions(null, null, null);
	}
	
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<SubmissionDTO> listSubmisssions(@QueryParam("limit") Integer limit, 
    		@QueryParam("start") Integer start,
    		@QueryParam("sort") String sort) {
    	
    	TypedQuery<Submission> query = em.createNamedQuery("Submission.findAll", Submission.class);
    	List<Submission> submissionsList = query.getResultList();
    	
    	List<SubmissionDTO> dtoList = submissionsList.stream()
    		.map((s)->Mapper.copySimilarNames(s, SubmissionDTO.class))
    		.collect(Collectors.toList());
    	
    	if (limit == null) limit = Integer.MAX_VALUE;
    	if (start == null) start = 0;
    	if (sort == null) sort = SORT_LATEST;
    	
    	if (sort.equals(SORT_LATEST)) {
    		Collections.reverse(dtoList);
    	}
    	int lastIndex = start + Math.min(limit, dtoList.size()-start);
    	
    	System.out.println("start lats " + start + " " + lastIndex);
		List<SubmissionDTO> limited = dtoList.subList(start, lastIndex);
    	
    	return limited;
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
