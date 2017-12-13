package org.pesho.judge.rest;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.input.BoundedInputStream;
import org.pesho.judge.UserService;
import org.pesho.judge.dtos.AddSubmissionDto;
import org.pesho.judge.queue.SubmissionsQueue;
import org.pesho.judge.repositories.SubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1")
public class SubmissionsRestService {
	
    @Value("${work.dir}")
    private String workDir;

	@Autowired
	private UserService userService;
	
	@Autowired
	private SubmissionRepository repository;
	
	@Autowired
	private SubmissionsQueue queue;

	@GetMapping("/submissions")
	@PreAuthorize("hasAnyAuthority({'admin','teacher','user'})")
	public List<Map<String, Object>> listSubmissions(
			@RequestParam(value = "page", defaultValue = "1") Integer page,
			@RequestParam(value = "size", defaultValue = "10") Integer size) {
		return repository.listUserAllSubmissions(userService.getCurrentUserId(), page, size);
	}
	
	@GetMapping("/submissions/users/{user_id}")
	@PreAuthorize("hasAnyAuthority({'admin','teacher','user'})")
	public List<Map<String, Object>> listUserSubmissions(
			@PathVariable("user_id") int userId,
			@RequestParam(value = "page", defaultValue = "1") Integer page,
			@RequestParam(value = "size", defaultValue = "10") Integer size) {
		return repository.listUserAllSubmissions(userId, page, size);
	}

	@GetMapping("/submissions/groups/{group_id}")
	@PreAuthorize("hasAnyAuthority({'admin','teacher','user'})")
	public List<Map<String, Object>> listGroupSubmissions(
			@PathVariable("group_id") int groupId,
			@RequestParam(value = "page", defaultValue = "1") Integer page,
			@RequestParam(value = "size", defaultValue = "10") Integer size) {
		return repository.listGroupAllSubmissions(groupId, page, size);
	}
	
	@GetMapping("/submissions/assignments/{id}")
	@PreAuthorize("hasAnyAuthority({'admin','teacher','user'})")
	public List<Map<String, Object>> listSubmissions(@PathVariable("id") int id) {
		if (userService.isAdmin()) {
			return repository.listAssignmentSubmissions(id);
		} else if (userService.isTeacher()) {
			return repository.listAssignmentSubmissions(id);
		} else {
			return repository.listUserAssignmentSubmissions(userService.getCurrentUserId(), id);
		}
	}
	
	@GetMapping("/submissions/{id}")
	@PreAuthorize("hasAnyAuthority({'admin','teacher','user'})")
	public ResponseEntity<?> getSubmission(@PathVariable("id") int id) {
		try {
			Optional<Map<String, Object>> submission = repository.getSubmission(id);
			if (submission.isPresent()) {
				return new ResponseEntity<>(submission.get(), HttpStatus.OK);
			} else {
		        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalStateException("bad");
		}
	}

	@PostMapping("/submissions")
	@PreAuthorize("hasAnyAuthority({'admin','teacher','user'})")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public ResponseEntity<?> createSubmissions(
			@RequestPart(name = "metadata") AddSubmissionDto submission, 
			@RequestPart("file") MultipartFile file
			) throws Exception {
		if (file.getSize() > 64*1024) {
			return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
		}
		int submissionId = repository.addSubmission(submission, file.getOriginalFilename());
		File submissionsDir = new File(workDir, "submissions");
		File currentDir = new File(submissionsDir, String.valueOf(submissionId));
		currentDir.mkdirs();
		File submissionFile = new File(currentDir, file.getOriginalFilename());
		BoundedInputStream boundedIS = new BoundedInputStream(file.getInputStream(), 64*1024);
		FileUtils.copyInputStreamToFile(boundedIS, submissionFile);
		queue.add(submissionId);
		return new ResponseEntity<>(submissionId, HttpStatus.CREATED);			
	}
}
