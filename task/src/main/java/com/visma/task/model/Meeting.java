package com.visma.task.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.*;

public class Meeting {

    private Long ID;

    private String name;

    private Person responsiblePerson;

    private String description;

    private Category category;

    private Type type;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endDate;

    private Set<Person> attendees;

    public Meeting() {
    }

    public Meeting(@JsonProperty("id") Long ID, String name, Person responsiblePerson, String description, Category category, Type type, LocalDateTime startDate, LocalDateTime endDate, Set<Person> attendees) {
        this.ID = ID;
        this.name = name;
        this.responsiblePerson = responsiblePerson;
        this.description = description;
        this.category = category;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.attendees = attendees;
    }

    public Meeting(String name, Person responsiblePerson, String description, Category category, Type type, LocalDateTime startDate, LocalDateTime endDate, Set<Person> attendees) {
        this.name = name;
        this.responsiblePerson = responsiblePerson;
        this.description = description;
        this.category = category;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.attendees = attendees;
    }

    public Meeting(Meeting originalMeeting) {

        setID(originalMeeting.getID());
        setName(originalMeeting.getName());
        setResponsiblePerson(originalMeeting.getResponsiblePerson());
        setDescription(originalMeeting.getDescription());
        setCategory(originalMeeting.getCategory());
        setType(originalMeeting.getType());
        setStartDate(originalMeeting.getStartDate());
        setEndDate(originalMeeting.getEndDate());

        Set<Person> originalAttendees = originalMeeting.getAttendees();
        Set<Person> clonedAttendees = new HashSet<>();
        for (Person attendee : originalAttendees) {
            clonedAttendees.add(new Person(attendee.getPersonalID(), attendee.getName(), attendee.getSecondName()));
        }
        setAttendees(clonedAttendees);

    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Person getResponsiblePerson() {
        return responsiblePerson;
    }

    public void setResponsiblePerson(Person responsiblePerson) {
        this.responsiblePerson = responsiblePerson;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public Set<Person> getAttendees() {
        return attendees;
    }

    public void setAttendees(Set<Person> attendees) {
        this.attendees = attendees;
    }

    public void addAttendee(Person attendeeToAdd) {
        this.attendees.add(attendeeToAdd);
    }

    public void removeAttendee(Person attendeeToRemove) {
        this.attendees.remove(attendeeToRemove);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Meeting meeting = (Meeting) o;
        return Objects.equals(ID, meeting.ID) && Objects.equals(name, meeting.name) && Objects.equals(responsiblePerson, meeting.responsiblePerson) && Objects.equals(description, meeting.description) && category == meeting.category && type == meeting.type && Objects.equals(startDate, meeting.startDate) && Objects.equals(endDate, meeting.endDate) && Objects.equals(attendees, meeting.attendees);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, name, responsiblePerson, description, category, type, startDate, endDate, attendees);
    }

    @Override
    public String toString() {
        return "Meeting{" +
                "name='" + name + '\'' +
                ", responsiblePerson=" + responsiblePerson +
                ", description='" + description + '\'' +
                ", category=" + category +
                ", type=" + type +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", attendees=" + attendees +
                '}';
    }
}
