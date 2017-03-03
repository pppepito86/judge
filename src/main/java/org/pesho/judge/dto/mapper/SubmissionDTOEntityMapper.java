package org.pesho.judge.dto.mapper;

import org.pesho.judge.dto.SubmissionDTO;
import org.pesho.judge.model.Submission;
import org.pesho.judge.model.User;

public class SubmissionDTOEntityMapper extends Mapper<Submission, SubmissionDTO> {

	@Override
	public SubmissionDTO entityToDTO(Submission entity) {
		return null;
	}

	@Override
	public Submission dtoToEntity(SubmissionDTO entity) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static void main(String[] args) {
//		SubmissionDTOEntityMapper mapper = new SubmissionDTOEntityMapper();
//		mapper.entityToDTO(null);
		
		User user = new User();
		user.setId(5);
		
		Submission submission = new Submission();
		submission.setId(5);
		submission.setCorrect(5);
		submission.setUser(user);
		
		SubmissionDTO copy = Mapper.copySimilarNames(submission, SubmissionDTO.class);
		System.out.println(copy);
	}
	
}
