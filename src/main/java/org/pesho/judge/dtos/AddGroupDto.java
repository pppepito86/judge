package org.pesho.judge.dtos;

import javax.validation.constraints.Size;

public class AddGroupDto {

    @Size(min=1, max=15)
    private String groupname;
    private String description;

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
