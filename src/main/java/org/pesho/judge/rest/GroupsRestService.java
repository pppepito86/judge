package org.pesho.judge.rest;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.pesho.judge.UserService;
import org.pesho.judge.dtos.AddGroupDto;
import org.pesho.judge.repositories.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1")
public class GroupsRestService {

	@Autowired
	private UserService userService;

	@Autowired
	private GroupRepository repository;

	@GetMapping("/groups")
	@PreAuthorize("hasAnyAuthority({'admin','teacher','user'})")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Map<String, Object>> listGroups(
			@RequestParam(value = "page", defaultValue = "1") Integer page,
			@RequestParam(value = "size", defaultValue = "10") Integer size) {
		if (userService.isAdmin()) {
			return repository.listGroups(page, size);
		} else if (userService.isTeacher()) {
			return repository.listGroupsForTeacher(userService.getCurrentUserId(), page, size);
		} else {
			return repository.listGroupsForUser(userService.getCurrentUserId(), page, size);
		}
	}

	@GetMapping("/groups/{id}")
	@PreAuthorize("hasAnyAuthority({'admin','teacher','user'})")
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity<?> getGroup(@PathVariable("id") int id) {
		Optional<Map<String,Object>> group = repository.getGroup(id);
		if (group.isPresent()) {
			return new ResponseEntity<>(group.get(), HttpStatus.OK);
		} else {
	        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
	}
	
	@PostMapping("/groups")
	@PreAuthorize("hasAnyAuthority({'admin','teacher'})")
	@Consumes(MediaType.APPLICATION_JSON)
	public ResponseEntity<?> createGroup(@RequestBody AddGroupDto group) {
		int groupId = repository.createGroup(group);
		
	    URI location = ServletUriComponentsBuilder.fromCurrentRequest()
	    		.path("/{id}").buildAndExpand(groupId).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping("/groups/{group_id}")
	@PreAuthorize("hasAnyAuthority({'admin','teacher'})")
	@Consumes(MediaType.APPLICATION_JSON)
	public void updateGroup(@PathVariable("group_id") int groupId,
			@RequestBody AddGroupDto group) {
		repository.updateGroup(groupId, group);
	}
	
	@GetMapping("/groups/{group_id}/users")
	@PreAuthorize("hasAnyAuthority({'admin','teacher'})")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Map<String, Object>> studentsInGroup(@PathVariable("group_id") int groupId) {
		return repository.studentsInGroup(groupId);
	}

	@PutMapping("/groups/{group_name}/users")
	@PreAuthorize("hasAnyAuthority({'admin','teacher','user'})")
	public void joinGroup(@PathVariable("group_name") String groupName) {
		repository.addGroupUser(groupName);
	}

}
