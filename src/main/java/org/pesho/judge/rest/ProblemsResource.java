package org.pesho.judge.rest;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.pesho.judge.dto.ProblemDTO;
import org.pesho.judge.dto.mapper.Mapper;
import org.pesho.judge.ejb.ProblemsDAO;
import org.pesho.judge.model.Problem;
import org.pesho.judge.model.Tag;

@Path("problems")
public class ProblemsResource {
	
	@Inject 
	ProblemsDAO problemsDAO;
	
	@Inject 
	Mapper mapper;
	
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"admin", "teacher", "user"})
    public List<ProblemDTO> listProblems() {
    	List<Problem> listProblems = problemsDAO.listProblems();
    	List<ProblemDTO> listProblemsDTO = mapper.mapList(listProblems, ProblemDTO.class);
    	return listProblemsDTO;
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"admin", "teacher"})
    public ProblemDTO createProblem(ProblemDTO problemDto) {
    	Problem problem = mapper.map(problemDto, Problem.class);
    	Problem res = problemsDAO.createProblem(problem);
    	ProblemDTO resDto = mapper.map(res, ProblemDTO.class);
        return resDto;
    }
    
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"admin", "teacher", "user"})
    public ProblemDTO getProblem(@PathParam("id") int problemId) {
    	Problem res = problemsDAO.getProblem(problemId); 
    	ProblemDTO resDto = mapper.map(res, ProblemDTO.class);
        return resDto;
    }
    
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"admin", "teacher"})
    public ProblemDTO updateProblem(@PathParam("id") int problemId, Problem problem) {
    	Problem res = problemsDAO.updateProblem(problemId, problem);
    	ProblemDTO resDto = mapper.map(res, ProblemDTO.class);
        return resDto;
    }
    
    @GET
    @Path("{id}/tags")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"admin", "teacher", "user"})
    public List<String> getTags(@PathParam("id") int problemId) {
    	List<Tag> tags = problemsDAO.getTags(problemId);
    	
    	List<String> tagsStr = tags.stream()
    		.map((x)->x.getTag())
    		.collect(Collectors.toList());
    	
        return tagsStr;
    }
}
