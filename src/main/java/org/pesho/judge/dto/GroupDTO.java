package org.pesho.judge.dto;

import java.io.Serializable;

public class GroupDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int id;
	
	private String groupName;
	
	private String description;
	
	private Integer creatorId;
	
	public GroupDTO() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Integer creatorId) {
		this.creatorId = creatorId;
	}
}
