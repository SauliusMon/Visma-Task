package com.visma.task.model;

public enum Category {

    CODEMONKEY("CodeMonkey"), HUB("Hub"),
    SHORT("Short"), TEAMBUILDING("TeamBuilding");

    private final String meetingCategory;

    Category(String meetingCategory) {
        this.meetingCategory = meetingCategory;
    }

    public String getName() {
        return meetingCategory;
    }
}
