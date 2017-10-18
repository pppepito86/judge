package org.pesho.judge.repositories;

import org.pesho.judge.UserService;
import org.pesho.judge.daos.AddProblemDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Configuration
public class ProblemRepository {

    @Autowired
    private JdbcTemplate template;

    @Autowired
    private UserService userService;

    public List<Map<String,Object>> listProblems() {
        List<Map<String, Object>> problems = template.queryForList("select * from problems");
        Iterator<Map<String, Object>> it = problems.iterator();
        while (it.hasNext()) {
            Map<String, Object> next = it.next();
            List<Map<String, Object>> tags = template.queryForList("select tag from tags where problemid=?", new Object[] {next.get("id")});
            if (tags == null) tags = new LinkedList<>();
            next.put("tags", tags);
        }
        return problems;
    }

    public List<Map<String,Object>> listAuthorProblems(long authorId) {
        List<Map<String, Object>> problems = template.queryForList("select * from problems where author=?",
                new Object[] {authorId});
        Iterator<Map<String, Object>> it = problems.iterator();
        while (it.hasNext()) {
            Map<String, Object> next = it.next();
            List<Map<String, Object>> tags = template.queryForList("select tag from tags where problemid=?", new Object[] {next.get("id")});
            System.out.println("tagovete sa: " + tags);
            if (tags == null) tags = new LinkedList<>();
            next.put("tags", tags);
        }
        return problems;
    }

    public Map<String, Object> getProblem(long id) {
         return template.queryForList("select * from problems where id=?",
                new Object[] {id}).stream().findFirst().orElseThrow(() -> new IllegalStateException("No such problem"));
    }

    @Transactional
    public void createProblem(AddProblemDao problem) {
		long authorId = userService.getCurrentUserId();
		template.update("INSERT INTO problems(name, version, description, languages, visibility, author, points) VALUES(?, ?, ?, ?, ?, ?, ?)",
				new Object[] {
		                problem.getProblemname(),
                        problem.getVersion(),
                        problem.getText(),
                        "java",
                        problem.getVisibility(),
                        authorId,
                        problem.getPoints()
		        });
		Optional<Object> first = template.queryForList("SELECT MAX(id) FROM problems").stream().map(x->x.get("MAX(id)")).findFirst();

		for (String tag: problem.getTags().split(",")) {
			tag = tag.trim();
			if (tag.isEmpty()) continue;
			template.update("INSERT INTO tags(problemid, tag) VALUES(?, ?)",
					new Object[] {first.get(), tag});
		}
    }

}
