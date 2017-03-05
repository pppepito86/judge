package org.pesho.judge;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.sql.Timestamp;

import org.pesho.judge.dto.AssignmentDTO;

public class CreateAssignmentResourceTest extends CreateResourceTest<AssignmentDTO, AssignmentDTO> {

	@Override
	protected String getRousurceURI() {
		return "assignments";
	}

	@Override
	protected AssignmentDTO createResource() {
		AssignmentDTO assignment = new AssignmentDTO();
		assignment.setAuthorId(1);
		assignment.setName("testass");
		assignment.setStandings("");
		assignment.setTestInfo("");
		assignment.setStartTime(new Timestamp(System.currentTimeMillis()));
		assignment.setEndTime(new Timestamp(System.currentTimeMillis()+1000));
		assignment.setGroupId(2);
		
		return assignment;
	}
	
	@Override
	protected void beforeResouceCreated() {
		super.beforeResouceCreated();
		
		DBPopulator dbPopulator = new DBPopulator(tokenDTO, getURL());
		dbPopulator.addRandomGroup();
		dbPopulator.addRandomGroup();
	};
	
	@Override
	protected void assertCreatedResourceIsValid(AssignmentDTO createdResource) {
		assertThat(createdResource.getAuthorId(), is(1));
		assertThat(createdResource.getName(), is("testass"));
		assertThat(createdResource.getStandings(), is(""));
		assertThat(createdResource.getTestInfo(), is(""));
		assertThat(createdResource.getGroupId(), is(2));
	}

	@Override
	protected Class<AssignmentDTO> getDtoClassForPost() {
		return AssignmentDTO.class;
	}

	@Override
	protected Class<AssignmentDTO> getDtoClassForGet() {
		return AssignmentDTO.class;
	}
}
