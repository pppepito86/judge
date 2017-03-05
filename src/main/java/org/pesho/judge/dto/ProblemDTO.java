package org.pesho.judge.dto;

import java.io.Serializable;

public class ProblemDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int id;
	
	private String name;
	  
	private String version;

	private String description;
	
	private String languages;

	private Integer points;
	
	private String visibility;
	
	private Integer authorId;

	private Integer tests;
	
	private String sourceChecker;
	
	private String testChecker;
	
	public ProblemDTO() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLanguages() {
		return languages;
	}

	public void setLanguages(String languages) {
		this.languages = languages;
	}

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}

	public Integer getAuthorId() {
		return authorId;
	}

	public void setAuthorId(Integer authorId) {
		this.authorId = authorId;
	}

	public Integer getTests() {
		return tests;
	}

	public void setTests(Integer tests) {
		this.tests = tests;
	}

	public String getSourceChecker() {
		return sourceChecker;
	}

	public void setSourceChecker(String sourceChecker) {
		this.sourceChecker = sourceChecker;
	}

	public String getTestChecker() {
		return testChecker;
	}

	public void setTestChecker(String testChecker) {
		this.testChecker = testChecker;
	}
}
