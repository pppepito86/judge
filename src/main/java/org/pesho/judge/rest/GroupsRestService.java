package org.pesho.judge.rest;

import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import org.pesho.judge.UserService;
import org.pesho.judge.dtos.AddGroupDto;
import org.pesho.judge.repositories.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class GroupsRestService {

	@Autowired
	private UserService userService;

	@Autowired
	private GroupRepository repository;

	@GetMapping("/groups")
	@PreAuthorize("hasAnyAuthority({'admin','teacher','user'})")
	public List<Map<String, Object>> listGroups() {
		if (userService.isAdmin()) {
			return repository.listGroups();
		} else if (userService.isTeacher()) {
			return repository.listGroupsForTeacher(userService.getCurrentUserId());
		} else {
			return repository.listGroupsForUser(userService.getCurrentUserId());
		}
	}

	@PostMapping("/groups")
	@Consumes(MediaType.APPLICATION_JSON)
	@PreAuthorize("hasAnyAuthority({'admin','teacher'})")
	@ResponseStatus(HttpStatus.CREATED)
	public void createGroup(@RequestBody AddGroupDto group) {
		repository.createGroup(group);
	}
	
	@GetMapping("/groups/{group_id}/users")
	@PreAuthorize("hasAnyAuthority({'admin','teacher'})")
	public List<Map<String, Object>> studentsInGroup(@PathParam("group_id") int groupId) {
		return repository.studentsInGroup(groupId);
	}

	@PutMapping("/groups/{group_name}/users")
	@PreAuthorize("hasAnyAuthority({'admin','teacher','user'})")
	public void joinGroup(@PathVariable("group_name") String groupName) {
		repository.addGroupUser(groupName);
	}

}
