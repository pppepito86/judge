package org.pesho.judge.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@NamedQuery(name="SubmissionDetail.findAll", query="SELECT u FROM SubmissionDetail u") 
@Table(name = "submissiondetails")
public class SubmissionDetail implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "id")
	private int id;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "submissionid")
	private Submission submission;
	
	@NotNull
	@Column(name = "step")
	private String step;
	
	@NotNull
	@Column(name = "status")
	private String status;
	
	@NotNull
	@Column(name = "reason")
	private String reason;
	
	@Column(name = "time")
	private Integer time = 0;

	public SubmissionDetail() {
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Submission getSubmission() {
		return submission;
	}

	public void setSubmission(Submission submission) {
		this.submission = submission;
	}

	public String getStep() {
		return step;
	}

	public void setStep(String step) {
		this.step = step;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Integer getTime() {
		return time;
	}

	public void setTime(Integer time) {
		this.time = time;
	}
}
