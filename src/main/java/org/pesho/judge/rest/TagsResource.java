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

import org.pesho.judge.model.Tag;

@Stateless
@Path("tags")
public class TagsResource {
	
	@PersistenceContext(unitName = "judge")
	EntityManager em;
	
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Tag> listTags() {
    	TypedQuery<Tag> query = em.createNamedQuery("Tag.findAll", Tag.class);
    	List<Tag> results = query.getResultList();
        return results;
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Tag createProblem(Tag tag) {
    	em.persist(tag);
        return tag;
    }
    
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Tag getTag(@PathParam("id") int tagId) {
    	Tag tag = em.find(Tag.class, (Integer)tagId);
        return tag;
    }
    
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Tag updateTag(@PathParam("id") int tagId, Tag tag) {
    	// TODO
        return null;
    }
}
