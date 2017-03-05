package org.pesho.judge.rest;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.pesho.judge.dto.GroupDTO;
import org.pesho.judge.dto.UserDTO;
import org.pesho.judge.dto.mapper.Mapper;
import org.pesho.judge.ejb.GroupsDAO;
import org.pesho.judge.model.Group;
import org.pesho.judge.model.User;

@Path("groups")
public class GroupsResource {

	@Inject
	GroupsDAO groupsDAO;
	
	@Inject
	Mapper mapper;
	
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<GroupDTO> listGroups() {
    	
    	List<Group> groups = groupsDAO.listGroups();
    	List<GroupDTO> groupsDto = mapper.mapList(groups, GroupDTO.class);
        return groupsDto;
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public GroupDTO createGroup(Group group) {
    	Group created = groupsDAO.createGroup(group);
    	GroupDTO createdDTO = mapper.map(created, GroupDTO.class);
        return createdDTO;
    }
    
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public GroupDTO getGroup(@PathParam("id") int groupId) {
    	Group group = groupsDAO.getGroup(groupId);
    	GroupDTO groupDTO = mapper.map(group, GroupDTO.class);
        return groupDTO;
    }
    
    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public GroupDTO updateGroup(@PathParam("id") int groupId, Group group) {
    	Group updated = groupsDAO.updateGroup(groupId, group);
    	GroupDTO updatedDTO = mapper.map(updated, GroupDTO.class);
        return updatedDTO;
    }
    
    @GET
    @Path("{id}/users")
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserDTO> getUsersFromGroup(@PathParam("id") int groupId) {
    	List<User> users = groupsDAO.getUsersFromGroup(groupId);
    	List<UserDTO> usersDTO = mapper.mapList(users, UserDTO.class);
    	return usersDTO;
    }
}
