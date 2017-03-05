package org.pesho.judge.dto.mapper;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import javax.inject.Inject;

import org.hamcrest.core.IsNull;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pesho.judge.TestDataCreator;
import org.pesho.judge.dto.SubmissionDTO;
import org.pesho.judge.ejb.UserDAO;
import org.pesho.judge.model.Role;
import org.pesho.judge.model.Submission;
import org.pesho.judge.model.User;
import org.pesho.judge.rest.JaxRsActivator;

@RunWith(Arquillian.class)
public class MapperTest  {
	
	@Deployment
	public static JavaArchive createDeployment() {
		JavaArchive war = ShrinkWrap.create(JavaArchive.class, "judge_test.jar")
				.addPackage(JaxRsActivator.class.getPackage()).addPackage("org.pesho.judge.dto")
				.addPackage("org.pesho.judge.dto.mapper").addPackage("org.pesho.judge.ejb")
				.addPackage("org.pesho.judge.model").addPackage("org.pesho.judge.rest")
				.addPackage("org.pesho.judge.exception")
				.addPackage("org.pesho.judge.security")
				.addClass(TestDataCreator.class)
				.addAsResource("test-persistence.xml", "META-INF/persistence.xml");
		return war;
	}
	
	@Inject
	Mapper mapper;
	
	@Inject 
	UserDAO userEJB;
	
	@Test
	public void testEntityToDTO() {
		User user = new User();
		user.setId(19);
		
		Submission submission = new Submission();
		submission.setId(5);
		submission.setCorrect(6);
		submission.setUser(user);
		
		SubmissionDTO copy = mapper.map(submission, SubmissionDTO.class);
		
		assertThat(copy.getId(), is(5));
		assertThat(copy.getCorrect(), is(6));
		assertThat(copy.getUserId(), is(19));
		assertThat(copy.getLanguage(), is(IsNull.nullValue()));
	}
	
	@Test
	public void testDTOToEntity() {
		System.out.println("hiii");
		User user = new User();

		user.setUsername("pesho");
		user.setFirstname("online");
		user.setLastname("judge");
		user.setEmail("pesho@pesho.org");
		user.setPasswordHash("admin");
		user.setRoles(Role.USER);
		
		user = userEJB.createUser(user);
		
		SubmissionDTO submissionDto = new SubmissionDTO();
		submissionDto.setId(5);
		submissionDto.setCorrect(6);
		submissionDto.setUserId(user.getId());
		System.out.println("user id " + user.getId() );
		
		Submission copy = mapper.map(submissionDto, Submission.class);
		
		System.out.println("copy " + copy);
		
		assertThat(copy.getId(), is(5));
		assertThat(copy.getCorrect(), is(6));
		assertThat(copy.getUser().getId(), is(user.getId()));
		assertThat(copy.getLanguage(), is(IsNull.nullValue()));
	}
}
