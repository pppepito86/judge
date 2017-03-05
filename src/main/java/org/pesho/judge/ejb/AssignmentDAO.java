package org.pesho.judge.ejb;

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
import javax.ws.rs.PathParam;

import org.pesho.judge.model.Assignment;
import org.pesho.judge.model.AssignmentProblem;
import org.pesho.judge.model.Problem;
import org.pesho.judge.model.Submission;
import org.pesho.judge.model.User;

@Stateless
public class AssignmentDAO {

	@PersistenceContext(unitName = "judge")
	EntityManager em;
	
	@Inject
	UserDAO userDAO;

	public List<Assignment> listAssignments() {
    	TypedQuery<Assignment> query = em.createNamedQuery("Assignment.findAll", Assignment.class);
    	List<Assignment> results = query.getResultList();
    	return results;
    }

	public Assignment createAssingment(Assignment assignment) {
    	em.persist(assignment);
    	
        return assignment;
    }
    
	public Assignment getAssignment(int assignmentId) {
    	Assignment assignment = em.find(Assignment.class, (Integer)assignmentId);
    	
        return assignment;
    }
	
	public Assignment updateAssignment(int assignmentId, Assignment assignment) {
        // TODO:
    	return null;
    }
	
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
    	
		return problems;
    }
    
    public List<Submission> getSubmissions(@PathParam("id") int assignmentId) {
    	Assignment assignment = getAssignment(assignmentId);
    	
    	TypedQuery<Submission> query = em
    			.createNamedQuery("Submission.findByAssignment", Submission.class)
    			.setParameter("assignment", assignment);
    	
    	List<Submission> submissions = query.getResultList();
		return submissions;
    }
    
	public List<Submission> getUserSubmissions(int assignmentId, int userId) {
    	
    	Assignment assignment = getAssignment(assignmentId);
    	User user = userDAO.getUser(userId);
    	
    	TypedQuery<Submission> query = em
    			.createNamedQuery("Submission.findByAssignmentAndUser", Submission.class)
    			.setParameter("assignment", assignment)
    			.setParameter("user", user);
    	
    	List<Submission> submissions = query.getResultList();
		return submissions;
    }
	
	public int getUserPoints(int assignmentId, int userId) {
    	
    	List<Submission> submissions = getUserSubmissions(assignmentId, userId);
    	
    	Comparator<Submission> submissionComparator = 
    			(s1, s2) -> s1.getPoints() - s2.getPoints();
    	
    	Map<Problem, Optional<Submission>> problemBestToSubmission = submissions
    		.stream()
    		.collect(Collectors.groupingBy(Submission::getProblem, Collectors.maxBy(submissionComparator)));
    	
    	Integer points = problemBestToSubmission
    		.values()
    		.stream()
    		.filter(Optional<Submission>::isPresent)
    		.map((s) -> s.get().getPoints())
    		.collect(Collectors.summingInt(Integer::intValue));
    	
		return points;
    }
}