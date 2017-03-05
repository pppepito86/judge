package org.pesho.judge.ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.pesho.judge.model.Problem;
import org.pesho.judge.model.Tag;

@Stateless
public class ProblemsDAO {
	
	@PersistenceContext(unitName = "judge")
	EntityManager em;
	
    public List<Problem> listProblems() {
    	TypedQuery<Problem> query = em.createNamedQuery("Problem.findAll", Problem.class);
    	List<Problem> results = query.getResultList();
        return results;
    }
    
    public Problem createProblem(Problem problem) {
    	em.persist(problem);
        return problem;
    }
    
    public Problem getProblem(int problemId) {
        Problem problem = em.find(Problem.class, (Integer)problemId);
    	return problem;
    }
    
    public Problem updateProblem(int problemId, Problem problem) {
    	// TODO:
        return null;
    }
    
    public List<Tag> getTags(int problemId) {
    	Problem problem = getProblem(problemId);
    	
    	TypedQuery<Tag> query = em
    			.createNamedQuery("Tag.findByProblem", Tag.class)
    			.setParameter("problem", problem);
    	
    	List<Tag> results = query.getResultList();
        return results;
    }
}
