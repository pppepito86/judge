package org.pesho.judge.controllers;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.pesho.judge.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ProblemsController {

	@Autowired
	private JdbcTemplate template;

	@Autowired
	private UserService userService;
	
	@RequestMapping(value="/myproblems", method=RequestMethod.GET)
	public String myproblems(Model model) {
		model.addAttribute("role", userService.getCurrentUserRole());
		List<Map<String, Object>> problems = template.queryForList("select * from problems where author=?",
				new Object[] {userService.getCurrentUserId()});
		Iterator<Map<String, Object>> it = problems.iterator();
		while (it.hasNext()) {
			Map<String, Object> next = it.next();
			List<Map<String, Object>> tags = template.queryForList("select tag from tags where problemid=?", new Object[] {next.get("id")});
			System.out.println("tagovete sa: " + tags);
			if (tags == null) tags = new LinkedList<>();
			next.put("tags", tags);
		}
		model.addAttribute("problems", problems);
		System.out.println(problems);
		return "problems";
	}


	@RequestMapping(value="/problems", method=RequestMethod.GET)
	public String problems(Model model) {
		model.addAttribute("role", userService.getCurrentUserRole());
		List<Map<String, Object>> problems = template.queryForList("select * from problems");
		Iterator<Map<String, Object>> it = problems.iterator();
		while (it.hasNext()) {
			Map<String, Object> next = it.next();
			List<Map<String, Object>> tags = template.queryForList("select tag from tags where problemid=?", new Object[] {next.get("id")});
			System.out.println("tagovete sa: " + tags);
			if (tags == null) tags = new LinkedList<>();
			next.put("tags", tags);
		}
		model.addAttribute("problems", problems);
		System.out.println(problems);
		return "problems";
	}
	
	@RequestMapping(value="/problem", method=RequestMethod.GET)
	public String problem(@RequestParam("id") long id, Model model) {
		model.addAttribute("role", userService.getCurrentUserRole());
		model.addAttribute("user", userService.getCurrentUserName());
		List<Map<String, Object>> problem = template.queryForList("select * from problems where id=?", 
				new Object[] {id});
		model.addAttribute("problem", problem.get(0));
		System.out.println(problem);
		System.out.println(model);
		return "problem";
	}
	

	@RequestMapping(value="/addproblem", method=RequestMethod.GET)
	public String addproblem(Model model) {
		model.addAttribute("role", userService.getCurrentUserRole());
		return "addproblem";
	}

	@Transactional
	@RequestMapping(value="/addproblem", method=RequestMethod.POST)
	public String createProblem(
			@RequestParam("file") MultipartFile file,
			@RequestParam("problemname") String name,
			@RequestParam("version") String version,
			@RequestParam("tags") String tags,
			@RequestParam("text") String description,
			@RequestParam("test") String test,
			@RequestParam("visibility") String visibility,
			@RequestParam("points") String points) {
		long authorId = userService.getCurrentUserId();
		template.update("INSERT INTO problems(name, version, description, languages, visibility, author, points) VALUES(?, ?, ?, ?, ?, ?, ?)",
				new Object[] {name, version, description, "java", visibility, authorId, points});
		Optional<Object> first = template.queryForList("SELECT MAX(id) FROM problems").stream().map(x->x.get("MAX(id)")).findFirst();

		for (String tag: tags.split(",")) {
			tag = tag.trim();
			if (tag.isEmpty()) continue;
			template.update("INSERT INTO tags(problemid, tag) VALUES(?, ?)",
					new Object[] {first.get(), tag});
		}
		
	    return "redirect:/problems.html";
	}
	
}
