package org.pesho.judge.rest;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;
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

import org.pesho.judge.model.Assignment;
import org.pesho.judge.model.AssignmentProblem;
import org.pesho.judge.model.Problem;
import org.pesho.judge.model.Submission;
import org.pesho.judge.model.User;

@Stateless
@Path("assignments")
public class AssignmentsResource {
	
	@PersistenceContext(unitName = "judge")
	EntityManager em;
	
	@Inject
	UsersResource usersResource;
	
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Assignment> listAssignments() {
    	TypedQuery<Assignment> query = em.createNamedQuery("Assignment.findAll", Assignment.class);
    	List<Assignment> results = query.getResultList();
    	return results;
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Assignment createAssingment(Assignment assignment) {
    	em.persist(assignment);
    	
        return assignment;
    }
    
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Assignment getAssignment(@PathParam("id") int assignmentId) {
    	Assignment assignment = em.find(Assignment.class, (Integer)assignmentId);
    	
        return assignment;
    }
    
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Assignment updateAssignment(@PathParam("id") int assignmentId, Assignment assignment) {
        // TODO:
    	return null;
    }
    
    @GET
    @Path("{id}/problems")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Problem> getProblemsPerAssignment(@PathParam("id") int assignmentId) {
    	Assignment assignment = getAssignment(assignmentId);
    	
    	TypedQuery<AssignmentProblem> query = em
    			.createNamedQuery("AssignmentProblem.findByAssignment", AssignmentProblem.class)
    			.setParameter("assignment", assignment);
    	
    	List<AssignmentProblem> assignmentProblems = query.getResultList();
    	
    	List<Problem> problems = assignmentProblems
    		.stream()
    		.map((AssignmentProblem ap) -> ap.getProblem())
    		.collect(Collectors.toList());
    	
    	System.out.println(problems.toString());
		return problems;
    }
    
    @GET
    @Path("{id}/submissions")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Submission> getSubmissions(@PathParam("id") int assignmentId) {
    	
    	Assignment assignment = getAssignment(assignmentId);
    	
    	TypedQuery<Submission> query = em
    			.createNamedQuery("Submission.findByAssignment", Submission.class)
    			.setParameter("assignment", assignment);
    	
    	List<Submission> submissions = query.getResultList();
    	
		return submissions;
    }
    
    @GET
    @Path("{id}/users/{userId}/submissions")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Submission> getUserSubmissions(@PathParam("id") int assignmentId, 
    		@PathParam("userId") int userId) {
    	
    	Assignment assignment = getAssignment(assignmentId);
    	User user = usersResource.getUser(userId);
    	
    	TypedQuery<Submission> query = em
    			.createNamedQuery("Submission.findByAssignmentAndUser", Submission.class)
    			.setParameter("assignment", assignment)
    			.setParameter("user", user);
    	
    	List<Submission> submissions = query.getResultList();
    	for(Submission s : submissions) {
    		System.out.println(s);
    	}
		return submissions;
    }
    
    @GET
    @Path("{id}/users/{userId}/points")
    @Produces(MediaType.APPLICATION_JSON)
    public int getUserPoints(@PathParam("id") int assignmentId, 
    		@PathParam("userId") int userId) {
    	
    	List<Submission> submissions = getUserSubmissions(assignmentId, userId);
    	
    	Comparator<Submission> submissionComparator = (Submission s1, Submission s2) -> s1.getPoints() - s2.getPoints();
    	
    	Map<Problem, Optional<Submission>> problemBestToSubmission = submissions
    		.stream()
    		.collect(Collectors.groupingBy(Submission::getProblem, Collectors.maxBy(submissionComparator)));
    	
    	Integer points = problemBestToSubmission
    		.values()
    		.stream()
    		.filter(Optional<Submission>::isPresent)
    		.map((Optional<Submission> s) -> s.get().getPoints())
    		.collect(Collectors.summingInt(Integer::intValue));
    	
		return points;
    }
}
