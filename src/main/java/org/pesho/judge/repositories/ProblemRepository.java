package org.pesho.judge.repositories;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.pesho.judge.UserService;
import org.pesho.judge.daos.AddProblemDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

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
		}
		return problems;
	}

	@Transactional
	public List<Map<String, Object>> listProblems(long teacherId) {
		List<Map<String, Object>> problems = template.queryForList(
				"select problems.id, problems.name, problems.version, problems.points, problems.description, problems.languages, problems.visibility, problems.author, users.username from problems"
						+ " inner join users on problems.author = users.id where problems.author=? OR problems.visibility='public'",
				new Object[] { teacherId });
		Iterator<Map<String, Object>> it = problems.iterator();
		while (it.hasNext()) {
			Map<String, Object> next = it.next();
			next.put("tags", getTags((int) next.get("id")));
		}
		return problems;
	}

	public List<Map<String, Object>> listAuthorProblems(long authorId) {
		List<Map<String, Object>> problems = template.queryForList("select * from problems where author=?",
				new Object[] { authorId });
		Iterator<Map<String, Object>> it = problems.iterator();
		while (it.hasNext()) {
			Map<String, Object> next = it.next();
			List<Map<String, Object>> tags = template.queryForList("select tag from tags where problemid=?",
					new Object[] { next.get("id") });
			System.out.println("tagovete sa: " + tags);
			if (tags == null)
				tags = new LinkedList<>();
			next.put("tags", tags);
		}
		return problems;
	}

	@Transactional
	public Optional<Map<String, Object>> getProblem(long id) {
		Optional<Map<String, Object>> problem = template
				.queryForList("select * from problems where id=?", new Object[] { id }).stream().findFirst();
		if (problem.isPresent()) {
			List<Map<String, Object>> tags = template.queryForList("select tag from tags where problemid=?",
					new Object[] { problem.get().get("id") });
			if (tags == null) {
				tags = new LinkedList<>();
			}
			problem.get().put("tags", tags);
		}
		return problem;
	}

	public List<Map<String, Object>> getTags(long problemId) {
		return template.queryForList("select tag from tags where problemid=?", new Object[] { problemId });
	}

	@Transactional
	public int createProblem(AddProblemDao problem) {
		long authorId = userService.getCurrentUserId();
		template.update(
				"INSERT INTO problems(name, version, description, languages, visibility, author, points) VALUES(?, ?, ?, ?, ?, ?, ?)",
				new Object[] { problem.getProblemname(), problem.getVersion(), problem.getText(), problem.getLanguage(),
						problem.getVisibility(), authorId, problem.getPoints() });
		Optional<Object> first = template.queryForList("SELECT MAX(id) FROM problems").stream()
				.map(x -> x.get("MAX(id)")).findFirst();

		for (String tag : problem.getTags().split(",")) {
			tag = tag.trim();
			if (tag.isEmpty())
				continue;
			template.update("INSERT INTO tags(problemid, tag) VALUES(?, ?)", new Object[] { first.get(), tag });
		}
		
		return (int) first.get();
	}

	public void createTag(long problemId, String tag) {
		template.update("INSERT INTO tags(problemid, tag) VALUES(?, ?)", new Object[] { problemId, tag });
	}

	public void deleteTags(long problemId) {
		template.update("DELETE FROM tags WHERE problemid=?");
	}

}