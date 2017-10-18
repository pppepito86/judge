package org.pesho.judge.daos;

import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public class AddProblemDao {

    private String problemname;
    private String version;
    private String tags;
    private String text;
    private String test;
    private String visibility;
    private String points;
    private Optional<MultipartFile> file;

    public String getProblemname() {
        return problemname;
    }

    public void setProblemname(String problemname) {
        this.problemname = problemname;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public Optional<MultipartFile> getFile() {
        return file;
    }

    public void setFile(Optional<MultipartFile> file) {
        this.file = file;
    }
}
