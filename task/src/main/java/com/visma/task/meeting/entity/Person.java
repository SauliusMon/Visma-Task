package com.visma.task.meeting.entity;

import java.util.Objects;

public class Person {

    private String personalID;

    private String name;

    private String secondName;

    public Person() {
    }

    public Person(String personalID, String name, String secondName) {
        this.personalID = personalID;
        this.name = name;
        this.secondName = secondName;
    }

    public String getPersonalID() {
        return personalID;
    }

    public void setPersonalID(String personalID) {
        this.personalID = personalID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(personalID, person.personalID) && Objects.equals(name, person.name) && Objects.equals(secondName, person.secondName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(personalID, name, secondName);
    }

    @Override
    public String toString() {
        return "Person{" +
                "personalID='" + personalID + '\'' +
                ", name='" + name + '\'' +
                ", secondName='" + secondName + '\'' +
                '}';
    }
}
