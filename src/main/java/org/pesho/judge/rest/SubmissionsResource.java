package org.pesho.judge.rest;

import java.util.Collections;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
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
import org.pesho.judge.ejb.SubmissionsDAO;
import org.pesho.judge.model.Submission;

@Path("submissions")
public class SubmissionsResource {
	
	private static final String SORT_LATEST = "latest";
	private static final String SORT_OLDEST = "oldest";
	
	@Inject
	SubmissionsDAO submissionsDAO;
	
	@Inject 
	Mapper mapper;
	
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public List<SubmissionDTO> listSubmisssions(@QueryParam("limit") Integer limit, 
    		@QueryParam("start") Integer start,
    		@QueryParam("sort") String sort) {
    	
    	List<Submission> submissionsList = submissionsDAO.listSubmisssions();
    	
    	List<SubmissionDTO> dtoList = mapper.mapList(submissionsList, SubmissionDTO.class);
    	
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
    public SubmissionDTO createSubmission(Submission submission) {
    	Submission res = submissionsDAO.createSubmission(submission);
    	SubmissionDTO resDto = mapper.map(res, SubmissionDTO.class);
        return resDto;
    }
    
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public SubmissionDTO getSubmission(@PathParam("id") int submissionId) {
    	Submission res = submissionsDAO.getSubmission(submissionId);
    	SubmissionDTO resDto = mapper.map(res, SubmissionDTO.class);
        return resDto;
    }
    
}
