package org.pesho.judge.repositories;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.pesho.judge.UserService;
import org.pesho.judge.dtos.AddProblemDto;
import org.pesho.judge.dtos.AddProblemDto.Languages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class ProblemRepository {

	@Autowired
	private JdbcTemplate template;

	@Autowired
	private UserService userService;

	@Transactional
	public List<Map<String, Object>> listProblems() {
		List<Map<String, Object>> problems = template.queryForList(
				"select problems.id, problems.name, problems.version, problems.points, problems.description, problems.languages, problems.visibility, problems.author, users.username from problems"
						+ " inner join users on problems.author = users.id");
		Iterator<Map<String, Object>> it = problems.iterator();
		while (it.hasNext()) {
			Map<String, Object> next = it.next();
			next.put("tags", getTags((int) next.get("id")));
			fixLanguage(next);
		}
		return problems;
	}

	@Transactional
	public List<Map<String, Object>> listProblems(int teacherId) {
		List<Map<String, Object>> problems = template.queryForList(
				"select problems.id, problems.name, problems.version, problems.points, problems.description, problems.languages, problems.visibility, problems.author, users.username from problems"
						+ " inner join users on problems.author = users.id where problems.author=? OR problems.visibility='public'",
				teacherId);
		Iterator<Map<String, Object>> it = problems.iterator();
		while (it.hasNext()) {
			Map<String, Object> next = it.next();
			next.put("tags", getTags((int) next.get("id")));
			fixLanguage(next);
		}
		return problems;
	}

	public List<Map<String, Object>> listAuthorProblems(int authorId) {
		List<Map<String, Object>> problems = template.queryForList("select * from problems where author=?",
				authorId);
		Iterator<Map<String, Object>> it = problems.iterator();
		while (it.hasNext()) {
			Map<String, Object> next = it.next();
			List<Map<String, Object>> tags = template.queryForList("select tag from tags where problemid=?",
					next.get("id"));
			if (tags == null)
				tags = new LinkedList<>();
			next.put("tags", tags);
			fixLanguage(next);
		}
		return problems;
	}

	@Transactional
	public Optional<Map<String, Object>> getProblem(int id) {
		Optional<Map<String, Object>> problem = template
				.queryForList("select * from problems where id=?", id).stream().findFirst();
		if (problem.isPresent()) {
			List<Map<String, Object>> tags = template.queryForList("select tag from tags where problemid=?",
					problem.get().get("id"));
			if (tags == null) {
				tags = new LinkedList<>();
			}
			problem.get().put("tags", tags);
			fixLanguage(problem.get());
		}
		return problem;
	}

	private void fixLanguage(Map<String, Object> problem) {
		if (problem.get("languages") == null) return;
		
		String languagesString = (String) problem.get("languages");
		ObjectMapper mapper = new ObjectMapper();
		try {
			Languages languages = mapper.readValue(languagesString, Languages.class);
			problem.put("languages", languages);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<Map<String, Object>> getTags(int problemId) {
		return template.queryForList("select tag from tags where problemid=?", problemId);
	}

	@Transactional
	public synchronized int createProblem(AddProblemDto problem) {
		int authorId = userService.getCurrentUserId();
		template.update(
				"INSERT INTO problems(name, version, description, languages, visibility, author, points) VALUES(?, ?, ?, ?, ?, ?, ?)",
				problem.getProblemname(), problem.getVersion(), problem.getText(), problem.getLanguagesJson(),
						problem.getVisibility(), authorId, problem.getPoints());
		Optional<Object> first = template.queryForList("SELECT MAX(id) FROM problems").stream()
				.map(x -> x.get("MAX(id)")).findFirst();

		for (String tag : problem.getTags().split(",")) {
			tag = tag.trim();
			if (tag.isEmpty())
				continue;
			template.update("INSERT INTO tags(problemid, tag) VALUES(?, ?)", first.get(), tag);
		}
		
		return (int) first.get();
	}

	public void createTag(int problemId, String tag) {
		template.update("INSERT INTO tags(problemid, tag) VALUES(?, ?)", problemId, tag);
	}

	public void deleteTags(int problemId) {
		template.update("DELETE FROM tags WHERE problemid=?");
	}

}
