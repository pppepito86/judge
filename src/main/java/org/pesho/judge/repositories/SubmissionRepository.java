package org.pesho.judge.repositories;

import static org.pesho.judge.repositories.SqlUtil.limit;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.pesho.judge.UserService;
import org.pesho.judge.dtos.AddSubmissionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class SubmissionRepository {

    @Autowired
    private JdbcTemplate template;

    @Autowired
    private UserService userService;
    
    @Transactional
    public synchronized int addSubmission(AddSubmissionDto submission, String sourceFile) {
        int userId = userService.getCurrentUserId();
        template.update("INSERT INTO submissions(assignmentid, problemid, userid, language, sourcefile, verdict, reason) VALUES(?, ?, ?, ?, ?, ?, ?)",
                submission.getAssignmentId(),
                submission.getProblemId(),
                userId,
                submission.getLanguage(),
                sourceFile,
                "pending",
                ""
                );
		Optional<Object> first = template.queryForList("SELECT MAX(id) FROM submissions").stream()
				.map(x -> x.get("MAX(id)")).findFirst();
		return (int) first.get();
    }

    
	public List<Map<String, Object>> listSubmissions() {
		return template.queryForList(
				"select id, assignmentid, problemid, userid, language, sourcefile, time, verdict from submissions order by id desc");
	}
	
	public List<Map<String, Object>> listUserAssignmentSubmissions(int userId, int assignmentId) {
		return template.queryForList(
				"select submissions.id, language, sourcefile, time, verdict, problems.name from submissions" +
				" inner join problems on problems.id=submissions.problemid and submissions.assignmentid=? and submissions.userid=?",
				assignmentId, userId);
	}
	
	public List<Map<String, Object>> listUserAssignmentSubmissions(int userId, int assignmentId, int page, int size) {
		return template.queryForList(
				"select submissions.id, language, sourcefile, time, verdict, problems.name from submissions"+
						" inner join problems on problems.id=submissions.problemid and submissions.assignmentid=? and submissions.userid=?"+
						" order by submissions.id desc" + limit(page, size),
						assignmentId, userId);
	}
	
	public List<Map<String, Object>> listUserProblemSubmissions(int userId, int assignmentId, int problemId) {
		return template.queryForList(
				"select submissions.id, language, sourcefile, verdict from submissions"+
				" where userid=? and assignmentid=? and problemid=?",
				userId, assignmentId, problemId);
	}

	
	public List<Map<String, Object>> listUserAllSubmissions(int userId) {
		return template.queryForList(
				"select id, time, assignmentid, problemid, points from submissions where userid=?",
				userId);
	}

	public List<Map<String, Object>> listUserAllSubmissions(int userId, int page, int size) {
		return template.queryForList(
				"select id, time, assignmentid, problemid, points from submissions where userid=?"+
				" order by id desc"+
				" limit "+ (page-1)*size + "," + size,
				userId);
	}
	
	public List<Map<String, Object>> listGroupAllSubmissions(int groupId, int page, int size) {
		return template.queryForList(
				"select submissions.id, time, assignmentid, problemid, points from submissions" +
				" inner join assignments on assignments.id=submissions.assignmentid where assignments.groupid=?" +
				" order by id desc " + limit(page, size),
				groupId);
	}
	
	public List<Map<String, Object>> listAssignmentSubmissions(int assignmentId) {
		return template.queryForList(
				"select submissions.id, submissions.problemid, language, sourcefile, time, verdict, submissions.points, problems.name, users.id, users.username, users.firstname, users.lastname from submissions"+
				" inner join problems on problems.id=submissions.problemid and submissions.assignmentid=?"+
				" inner join users on users.id=submissions.userid",
				assignmentId);
	}
	
	public List<Map<String, Object>> listAssignmentAcceptedSubmissions(int assignmentId) {
		return template.queryForList(
				"select submissions.id, submissions.problemid, language, sourcefile, time, verdict, submissions.points, problems.name, users.id, users.username, users.firstname, users.lastname from submissions"+
				" inner join problems on problems.id=submissions.problemid and submissions.verdict='Accepted' and submissions.assignmentid=?"+
				" inner join users on users.id=submissions.userid",
				assignmentId);
	}
	
	public List<Map<String, Object>> listProblemSubmissions(int problemId) {
		return template.queryForList(
				"select submissions.id, submissions.problemid, submissions.userid, submissions.language, submissions.sourcefile, submissions.time, submissions.verdict, submissions.reason, assignmentproblems.points from submissions"+
				" inner join assignmentproblems"+
				" where assignmentproblems.problemid=submissions.problemid and assignmentproblems.assignmentid=submissions.assignmentid"+
				" and submissions.problemid=?",
				problemId);
	}
	
	public Optional<Map<String, Object>> getSubmission(int id) {
		Optional<Map<String, Object>> submission = template.queryForList(
				"select submissions.id, submissions.problemid, submissions.userid, language, sourcefile, time, verdict, reason, submissions.points, problems.name, assignments.testinfo from submissions"+
				" inner join problems on problems.id=submissions.problemid and submissions.id=?"+
				" inner join assignments on assignments.id=submissions.assignmentid", id).stream().findFirst();
		return submission;
	}

	public void updateVerdict(int id, String verdict, String reason, int correct, int total, int points) {
		reason = reason.substring(0, Integer.min(reason.length(), 1000));
		template.update(
				"update submissions set verdict=?, reason=?, correct=?, total=?, points=? where id=?",
				verdict, reason, correct, total, points, id);
	}

	public void addSubmissionDetails(int submissionId, String step, String verdict, String reason, int time) {
		template.update(
				"INSERT INTO submissiondetails(submissionid, step, status, reason, time) VALUES(?, ?, ?, ?, ?)",
				submissionId, step, verdict, reason, time);
	}

	
	public List<Map<String, Object>> listSubmissionDetails(int submissionId) {
		return template.queryForList(
				"select id, submissionid, step, status, reason, time from submissiondetails where submissionid = ? order by id asc"+
				submissionId);
	}
	
	public void deleteSubmissionDetails(int id) {
		template.update(
				"delete from submissiondetails where submissionid=?",
				id);
	}
	
}
