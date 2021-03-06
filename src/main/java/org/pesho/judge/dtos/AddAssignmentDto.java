package org.pesho.judge.dtos;

import java.util.Optional;

public class AddAssignmentDto {

    private String name;
    private String problem1;
    private String groupid;
    private Optional<String> testinfo;
    private Optional<String> standings;
    private Long startTime;
    private Long endTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProblem1() {
        return problem1;
    }

    public void setProblem1(String problem1) {
        this.problem1 = problem1;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public Optional<String> getTestinfo() {
        return testinfo;
    }

    public void setTestinfo(Optional<String> testinfo) {
        this.testinfo = testinfo;
    }

    public Optional<String> getStandings() {
        return standings;
    }

    public void setStandings(Optional<String> standings) {
        this.standings = standings;
    }
    
    public Long getStartTime() {
		return startTime;
	}
    
    public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}
    
    public Long getEndTime() {
		return endTime;
	}
    
    public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}
    
}
