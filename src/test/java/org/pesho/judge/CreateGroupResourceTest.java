package org.pesho.judge;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.pesho.judge.dto.GroupDTO;

public class CreateGroupResourceTest extends CreateResourceTest<GroupDTO, GroupDTO> {

	@Override
	protected String getRousurceURI() {
		return "groups";
	}

	@Override
	protected GroupDTO createResource() {
		GroupDTO group = new GroupDTO();
		group.setCreatorId(1);
		group.setDescription("test group");
		group.setGroupName("test");
		return group;
	}
	
	@Override
	protected void assertCreatedResourceIsValid(GroupDTO group) {
		assertThat(group.getDescription(), is("test group"));
		assertThat(group.getGroupName(), is("test"));
		assertThat(group.getCreatorId(), is(1));
	}

	@Override
	protected Class<GroupDTO> getDtoClassForPost() {
		return GroupDTO.class;
	}

	@Override
	protected Class<GroupDTO> getDtoClassForGet() {
		return GroupDTO.class;
	}
}
