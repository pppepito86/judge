package org.pesho.judge.ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.pesho.judge.model.Submission;

@Stateless
public class SubmissionsDAO {
	
	@PersistenceContext(unitName = "judge")
	EntityManager em;
	
    public List<Submission> listSubmisssions() {
    	TypedQuery<Submission> query = em.createNamedQuery("Submission.findAll", Submission.class);
    	List<Submission> submissionsList = query.getResultList();
    	
    	return submissionsList;
    }
    
    public Submission createSubmission(Submission submission) {
    	// TODO
        return null;
    }
    
    public Submission getSubmission(int submissionId) {
    	Submission submission = em.find(Submission.class, (Integer)submissionId);
        return submission;
    }
    
}
