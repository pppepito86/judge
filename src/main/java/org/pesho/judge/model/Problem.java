package org.pesho.judge.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "problems")
public class Problem implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name = "id")
	private int id;
	
	@Column(name = "name")
	private String name;
	  
	@Column(name = "version")
	private String version;

	@Column(name = "description")
	private String description;
	
	// TODO create new table
	@Column(name = "languages")
	private String languages;

	@Column(name = "points")
	private int points;
	
	@Column(name = "visibility")
	private String visibility;
	
	@ManyToOne
	@JoinColumn(name = "author")
	private User author;

	@Column(name = "tests")
	private int tests;
	
	@Column(name = "source_checker")
	private String sourceChecker;
	
	@Column(name = "test_checker")
	private String testChecker;
	
	public Problem() {
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

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public int getTests() {
		return tests;
	}

	public void setTests(int tests) {
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
