package org.pesho.judge.rest;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.pesho.judge.dto.AssignmentDTO;
import org.pesho.judge.dto.ProblemDTO;
import org.pesho.judge.dto.SubmissionDTO;
import org.pesho.judge.dto.mapper.Mapper;
import org.pesho.judge.ejb.AssignmentDAO;
import org.pesho.judge.ejb.UserDAO;
import org.pesho.judge.model.Assignment;
import org.pesho.judge.model.Problem;
import org.pesho.judge.model.Submission;

@Path("assignments")
public class AssignmentsResource {
	
	@Inject
	AssignmentDAO assignmentsDAO;
	
	@Inject 
	UserDAO usersDAO;
	
	@Inject
	Mapper mapper;
	
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"admin", "teacher", "user"})
    public List<AssignmentDTO> listAssignments() {
    	List<Assignment> assignments = assignmentsDAO.listAssignments();
    	List<AssignmentDTO> assignmentsDTO =  mapper.mapList(assignments, AssignmentDTO.class);
    	return assignmentsDTO;
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"admin", "teacher"})
    public AssignmentDTO createAssingment(AssignmentDTO assignment) {
    	Assignment toBeCreated = mapper.map(assignment, Assignment.class);
    	Assignment created = assignmentsDAO.createAssingment(toBeCreated);
    	AssignmentDTO createdDTO = mapper.map(created, AssignmentDTO.class);
        return createdDTO;
    }
    
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public AssignmentDTO getAssignment(@PathParam("id") int assignmentId) {
    	Assignment assignment = assignmentsDAO.getAssignment(assignmentId);
    	
    	AssignmentDTO assDTO = mapper.map(assignment, AssignmentDTO.class);
        return assDTO;
    }
    
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public AssignmentDTO updateAssignment(@PathParam("id") int assignmentId, Assignment assignment) {
        Assignment updated = assignmentsDAO.updateAssignment(assignmentId, assignment);
        AssignmentDTO assDTO = mapper.map(updated, AssignmentDTO.class);
    	return assDTO;
    }
    
    @GET
    @Path("{id}/problems")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProblemDTO> getProblemsPerAssignment(@PathParam("id") int assignmentId) {
    	
    	List<Problem> problems = assignmentsDAO.getProblemsPerAssignment(assignmentId);
    	List<ProblemDTO> problemsDTO = mapper.mapList(problems, ProblemDTO.class);
    			
		return problemsDTO;
    }
    
    @POST
    @Path("{id}/problems/{problemId}/submissions")
    @PermitAll
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces(MediaType.APPLICATION_JSON)
    public SubmissionDTO uploadSubmission(@PathParam("id") int assignmentId, 
    		@PathParam("problemId") int problemId,
    		@QueryParam("file") String fileName,
    		InputStream is) throws IOException {
    	System.out.println("******** -> " + fileName);
    	Files.copy(is, FileSystems.getDefault().getPath("src/test/resources/docker/", fileName));
    	SubmissionDTO submissionDTO = new SubmissionDTO();
    	submissionDTO.setAssignmentId(assignmentId);
    	submissionDTO.setProblemId(problemId);
		return submissionDTO;
    }
    
    @GET
    @Path("{id}/submissions")
    @Produces(MediaType.APPLICATION_JSON)
    public List<SubmissionDTO> getSubmissions(@PathParam("id") int assignmentId) {
    	List<Submission> submissions = assignmentsDAO.getSubmissions(assignmentId);
    	List<SubmissionDTO> submissionsDTO = mapper.mapList(submissions, SubmissionDTO.class);
		return submissionsDTO;
    }
    
    @GET
    @Path("{id}/users/{userId}/submissions")
    @Produces(MediaType.APPLICATION_JSON)
    public List<SubmissionDTO> getUserSubmissions(@PathParam("id") int assignmentId, 
    		@PathParam("userId") int userId) {
    	
    	List<Submission> submissions = assignmentsDAO.getUserSubmissions(assignmentId, userId);
    	List<SubmissionDTO> submissionsDTO = mapper.mapList(submissions, SubmissionDTO.class);
		return submissionsDTO;
    }
    
    @GET
    @Path("{id}/users/{userId}/points")
    @Produces(MediaType.APPLICATION_JSON)
    public int getUserPoints(@PathParam("id") int assignmentId, 
    		@PathParam("userId") int userId) {
    	
    	Integer points = assignmentsDAO.getUserPoints(assignmentId, userId);
		return points;
    }
}
