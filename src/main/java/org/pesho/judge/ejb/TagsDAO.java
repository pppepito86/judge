package org.pesho.judge.ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.PathParam;

import org.pesho.judge.model.Tag;

@Stateless
public class TagsDAO {
	
	@PersistenceContext(unitName = "judge")
	EntityManager em;
	
    public List<Tag> listTags() {
    	TypedQuery<Tag> query = em.createNamedQuery("Tag.findAll", Tag.class);
    	List<Tag> results = query.getResultList();
        return results;
    }
    
    public Tag createProblem(Tag tag) {
    	em.persist(tag);
        return tag;
    }
    
    public Tag getTag(@PathParam("id") int tagId) {
    	Tag tag = em.find(Tag.class, (Integer)tagId);
        return tag;
    }
}
