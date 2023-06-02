package com.visma.task.model;

public enum Type {

    LIVE("Live"), INPERSON("InPerson");

    private final String meetingType;


    Type(String meetingType) {
        this.meetingType = meetingType;
    }

    public String getName() {
        return meetingType;
    }
}
