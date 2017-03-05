package org.pesho.judge.rest;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.pesho.judge.dto.UserDTO;
import org.pesho.judge.dto.mapper.Mapper;
import org.pesho.judge.ejb.UserDAO;
import org.pesho.judge.model.User;

@Path("users")
public class UsersResource {
	
	@Inject
	UserDAO userEJB;
	
	@Inject 
	Mapper mapper;
	
    @GET
    @RolesAllowed("admin")
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserDTO> listUsers() {
    	return mapper.mapList(userEJB.listUsers(), UserDTO.class);
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public UserDTO createUser(User user) {
    	User created = userEJB.createUser(user);
    	UserDTO userDto = mapper.map(created, UserDTO.class);
    	return userDto;
    }
    
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public UserDTO getUser(@PathParam("id") int userId) {
        return mapper.map(userEJB.getUser(userId), UserDTO.class);
    }
}
