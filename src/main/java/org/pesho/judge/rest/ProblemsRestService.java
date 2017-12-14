package org.pesho.judge.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.ws.rs.Consumes;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.FileUtils;
import org.pesho.judge.UserService;
import org.pesho.judge.dtos.AddProblemDto;
import org.pesho.judge.repositories.ProblemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1")
public class ProblemsRestService {

    @Value("${work.dir}")
    private String workDir;

	@Autowired
	private ProblemRepository repository;
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/problems")
	@PreAuthorize("hasAnyAuthority({'admin','teacher'})")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Map<String, Object>> listProblems() {
		if (userService.isAdmin()) {
			return repository.listProblems();
		} else {
			return repository.listProblems(userService.getCurrentUserId());			
		}
	}
	
	@GetMapping("/myproblems")
	@PreAuthorize("hasAnyAuthority({'admin','teacher'})")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Map<String, Object>> listMyProblems() {
		return repository.listAuthorProblems(userService.getCurrentUserId());
	}
	
	@GetMapping("/problems/{problem_id}")
	@PreAuthorize("hasAnyAuthority({'admin','teacher'})")
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity<?> getProblem(@PathVariable("problem_id") int problem_id) {
		Optional<Map<String,Object>> problem = repository.getProblem(problem_id);
		if (problem.isPresent()) {
			if (userService.isAdmin() 
					|| "public".equals(problem.get().get("visibility"))
					|| userService.getCurrentUserId().equals(problem.get().get("id"))) {
				return new ResponseEntity<>(problem.get(), HttpStatus.OK);
			} else {
		        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}
		} else {
	        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
	}
	
	@GetMapping(value = "/problems/{problem_id}/tests")
	public ResponseEntity<InputStreamResource> getFile(@PathVariable("problem_id") String problemId) throws IOException {
	    HttpHeaders respHeaders = new HttpHeaders();
	    respHeaders.setContentDispositionFormData("attachment", "tests.zip");
	    
		File problemsDir = new File(workDir, "problems");
		File problemDir = new File(problemsDir, problemId);
	    InputStream is = new FileInputStream(new File(problemDir, "tests.zip"));
	    InputStreamResource isr = new InputStreamResource(is);
	    return new ResponseEntity<InputStreamResource>(isr, respHeaders, HttpStatus.OK);
	}
	
	@PostMapping("/problems")
	@PreAuthorize("hasAnyAuthority({'admin','teacher'})")
	@Consumes(MediaType.APPLICATION_JSON)
	@ResponseStatus(HttpStatus.CREATED)
	public int createProblem(@RequestBody AddProblemDto problem) {
		return repository.createProblem(problem);
	}
	
	@PostMapping("/problems/{problem_id}/tests")
	@PreAuthorize("hasAnyAuthority({'admin','teacher'})")
	@Consumes(MediaType.APPLICATION_OCTET_STREAM)
	public ResponseEntity<?> uploadTests(@PathVariable("problem_id") String problemId, MultipartFile file) throws Exception {
		File problemsDir = new File(workDir, "problems");
		File problemDir = new File(problemsDir, problemId);
		if (problemDir.exists()) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		} else {
			problemDir.mkdirs();
			File testsFile = new File(problemDir, "tests.zip");
			FileUtils.copyInputStreamToFile(file.getInputStream(), testsFile);
			try(ZipFile zipFile = new ZipFile(testsFile)) {
				Enumeration<? extends ZipEntry> entries = zipFile.entries();
				while (entries.hasMoreElements()) {
					ZipEntry entry = entries.nextElement();
					File test = new File(problemDir, entry.getName());
					FileUtils.copyInputStreamToFile( zipFile.getInputStream(entry), test);
				}
			}
			return new ResponseEntity<>(HttpStatus.CREATED);			
		}
	}
	
	@PutMapping("/problems/{problem_id}/tags")
	public void createTag(@PathParam("problem_id") int problemId, String tag) {
		repository.createTag(problemId, tag);
	}
	
	@DeleteMapping("/problems/{problem_id}/tags")
	public void deleteTags(@PathParam("problem_id") int problemId) {
		repository.deleteTags(problemId);
	}
	
}
