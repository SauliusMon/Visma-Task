package com.visma.task.rest.dto;

import com.visma.task.model.Category;
import com.visma.task.model.Person;
import com.visma.task.model.Type;

import java.time.LocalDateTime;
import java.util.Objects;

public class MeetingFilterDto {

    private String description;

    private Person responsiblePerson;

    private Category category;

    private Type type;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private int numberOfAttendees;

    public MeetingFilterDto() {
    }

    public MeetingFilterDto(String description, Person responsiblePerson, Category category, Type type, LocalDateTime startDate, LocalDateTime endDate, int numberOfAttendees) {
        this.description = description;
        this.responsiblePerson = responsiblePerson;
        this.category = category;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numberOfAttendees = numberOfAttendees;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Person getResponsiblePerson() {
        return responsiblePerson;
    }

    public void setResponsiblePerson(Person responsiblePerson) {
        this.responsiblePerson = responsiblePerson;
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

    public void setStartDate(LocalDateTime startTime) {
        this.startDate = startTime;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public int getNumberOfAttendees() {
        return numberOfAttendees;
    }

    public void setNumberOfAttendees(int numberOfAttendees) {
        this.numberOfAttendees = numberOfAttendees;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MeetingFilterDto that = (MeetingFilterDto) o;
        return numberOfAttendees == that.numberOfAttendees && Objects.equals(description, that.description) && Objects.equals(responsiblePerson, that.responsiblePerson) && category == that.category && type == that.type && Objects.equals(startDate, that.startDate) && Objects.equals(endDate, that.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, responsiblePerson, category, type, startDate, endDate, numberOfAttendees);
    }

    @Override
    public String toString() {
        return "MeetingFilterDto{" +
                "description='" + description + '\'' +
                ", responsiblePerson=" + responsiblePerson +
                ", category=" + category +
                ", type=" + type +
                ", startTime=" + startDate +
                ", endTime=" + endDate +
                ", numberOfAttendees=" + numberOfAttendees +
                '}';
    }
}
