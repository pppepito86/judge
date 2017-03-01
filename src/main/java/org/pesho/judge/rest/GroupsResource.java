package org.pesho.judge.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.pesho.judge.model.Group;
import org.pesho.judge.model.User;

@Path("groups")
public class GroupsResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Group> listGroups() {
        return null;
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Group createGroup(Group group) {
        return group;
    }
    
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Group getGroup(@PathParam("id") int groupId) {
        return null;
    }
    
    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Group updateGroup(@PathParam("id") int groupId) {
        return null;
    }
    
    @GET
    @Path("{id}/users")
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getUsersFromGroup(@PathParam("id") int groupId) {
    	return null;
    }
}
