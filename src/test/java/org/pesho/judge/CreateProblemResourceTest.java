package org.pesho.judge;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.pesho.judge.dto.ProblemDTO;

public class CreateProblemResourceTest extends CreateResourceTest<ProblemDTO, ProblemDTO> {

	@Override
	protected String getRousurceURI() {
		return "problems";
	}

	@Override
	protected ProblemDTO createResource() {
		ProblemDTO problem = new ProblemDTO();
		problem.setAuthorId(3);
		problem.setDescription("example desc");
		problem.setLanguages("java, c++");
		problem.setName("problem x");
		problem.setPoints(100);
		problem.setSourceChecker("path/to/schecker");
		problem.setTestChecker("path/to/tchecker");
		problem.setTests(15);
		return problem;
	}
	
	@Override
	protected void beforeResouceCreated() {
		super.beforeResouceCreated();
		
		DBPopulator dbPopulator = new DBPopulator(tokenDTO, getURL());
		dbPopulator.addRandomUser();
		dbPopulator.addRandomUser();
	};
	
	@Override
	protected void assertCreatedResourceIsValid(ProblemDTO problem) {
		assertThat(problem.getAuthorId(), is(3));
		assertThat(problem.getDescription(), is("example desc"));
		assertThat(problem.getLanguages(), is("java, c++"));
		assertThat(problem.getName(), is("problem x"));
		assertThat(problem.getPoints(), is(100));
		assertThat(problem.getSourceChecker(), is("path/to/schecker"));
		assertThat(problem.getTestChecker(), is("path/to/tchecker"));
		assertThat(problem.getTests(), is(15));
	}

	@Override
	protected Class<ProblemDTO> getDtoClassForPost() {
		return ProblemDTO.class;
	}

	@Override
	protected Class<ProblemDTO> getDtoClassForGet() {
		return ProblemDTO.class;
	}
}
