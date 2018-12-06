package com.smartgwt.sample.server;

import java.io.Serializable;

public class ChangeManagement implements Serializable {

    public ChangeManagement() {
    }

    public void setChangeId(Long id) {
        changeId = id;
    }


    public void setChangeName(String name) {
        changeName = name;
    }

    public void setDescription(String d) {
        description = d;
    }


    // SmartClient will call these getters when serializing a Java Bean to be transmitted to
    // client-side components.
    public Long getChangeId() {
        return changeId;
    }

    public String getChangeName() {
        return changeName;
    }

    public String getDescription() {
        return description;
    }

    protected Long changeId;
    protected String changeName;
    protected String description;
}
