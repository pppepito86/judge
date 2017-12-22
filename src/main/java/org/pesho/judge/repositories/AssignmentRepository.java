package org.pesho.judge.repositories;

import static org.pesho.judge.repositories.SqlUtil.limit;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.pesho.judge.UserService;
import org.pesho.judge.dtos.AddAssignmentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class AssignmentRepository {

    @Autowired
    private JdbcTemplate template;

    @Autowired
    private UserService userService;
    
    @Autowired
    private ProblemRepository problemRepository;
    
	public List<Map<String, Object>> listAllAssignments(int page, int size) {
		return template.queryForList(
				"select assignments.id, assignments.name, assignments.author, assignments.groupid, users.username, groups.groupname, assignments.starttime, assignments.endtime from assignments" +
				" inner join users on assignments.author = users.id" +
				" inner join groups on assignments.groupid = groups.id" +
				" order by id desc " + limit(page, size));
	}
	
	public List<Map<String, Object>> listAuthorAssignments(int authorId, int page, int size) {
		return template.queryForList(
				"select assignments.id, assignments.name, assignments.author, assignments.groupid, users.username, groups.groupname, assignments.starttime, assignments.endtime from assignments" +
				" inner join users on assignments.author = users.id and assignments.author = ?" +
				" inner join groups on assignments.groupid = groups.id" +
				" order by id desc " + limit(page, size), authorId);
	}
	
	public List<Map<String, Object>> listUserAssignments(int userId, int page, int size) {
		return template.queryForList(
				"select assignments.id, assignments.name, assignments.author, assignments.groupid, users.username, groups.groupname, assignments.starttime, assignments.endtime from assignments" +
				" inner join users on assignments.author = users.id" +
				" inner join groups on assignments.groupid = groups.id" +
				" inner join usergroups on assignments.groupid = usergroups.groupid and usergroups.userid = ?" +
				" order by id desc " + limit(page, size),
				userId);
	}
	
	public List<Map<String, Object>> listGroupAssignments(int groupId, int page, int size) {
		return template.queryForList(
				"select assignments.id, assignments.name, assignments.author, assignments.groupid, users.username, groups.groupname, assignments.starttime, assignments.endtime from assignments" +
				" inner join users on assignments.author = users.id" +
				" inner join groups on assignments.groupid = groups.id" +
				" where assignments.groupid = ?" +
				" order by id desc " + limit(page, size),
				groupId);
	}
	
	@Transactional
	public Optional<Map<String, Object>> getAssignment(int id) {
		Optional<Map<String, Object>> assignment = template.queryForList(
				"select assignments.id, assignments.name, assignments.author, assignments.groupid, users.username, groups.groupname, assignments.starttime, assignments.endtime, assignments.testinfo, assignments.standings from assignments"+
				" inner join users on assignments.id=? and assignments.author = users.id"+
				" inner join groups on assignments.groupid = groups.id", id).stream().findFirst();
		if (assignment.isPresent()) {
			assignment.get().put("problems", listAssignmentProblems((int) assignment.get().get("id")));
		}
		return assignment;
	}

	@Transactional
    public synchronized int createAssignment(AddAssignmentDto assignment) {
        int authorId = userService.getCurrentUserId();
        template.update("INSERT INTO assignments(name, author, groupid, starttime, endtime, testinfo, standings) VALUES(?, ?, ?, ?, ?, ?, ?)",
        		assignment.getName(),
        		authorId,
        		assignment.getGroupid(),
        		null,
        		null,
        		assignment.getTestinfo().orElse("show"),
        		assignment.getStandings().orElse("")
        		);
		Optional<Object> first = template.queryForList("SELECT MAX(id) FROM assignments").stream()
				.map(x -> x.get("MAX(id)")).findFirst();
		
		int number = 1;
		for (String s: assignment.getProblem1().split(",")) {
			s=s.trim();
			if (s.isEmpty()) continue;
			createAssignmentProblems((int) first.get(), Integer.valueOf(s), number++);
		}
		return (int) first.get();
    }
	
	public void createAssignmentProblems(int assignmentId, int problemId, int number) {
		Optional<Map<String,Object>> problem = problemRepository.getProblem(problemId);
		if (problem.isPresent()) {
			template.update(
				"INSERT INTO assignmentproblems(assignmentid, problemid, number, points) VALUES(?, ?, ?, ?)",
				assignmentId, problemId, number, problem.get().get("points"));
		}
	}
	
	public List<Map<String, Object>> listAssignmentProblems(int assignmentId) {
		return template.queryForList(
				"select assignmentproblems.id, assignmentid, problemid, assignmentproblems.number, assignmentproblems.points, problems.name from assignmentproblems" + 
				" inner join problems on assignmentproblems.assignmentid=? and assignmentproblems.problemid=problems.id order by assignmentproblems.number",
				assignmentId);
	}
	
	public Optional<Map<String, Object>> getAssignmentProblem(int assignmentId, int problemNumber) {
		return template.queryForList(
				"select * from problems" + 
				" inner join assignmentproblems on assignmentproblems.assignmentid=? and assignmentproblems.problemid=problems.id and assignmentproblems.number=?",
						assignmentId, problemNumber).stream().findFirst();
	}

}
