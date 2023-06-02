package com.visma.task.rest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.visma.task.model.Category;
import com.visma.task.model.Person;
import com.visma.task.model.Type;

import java.time.LocalDateTime;
import java.util.Objects;


public class MeetingDto {

    private String name;

    private Person responsiblePerson;

    private String description;

    private Category category;

    private Type type;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endDate;

    public MeetingDto() {}

    public MeetingDto(String name, Person responsiblePerson, String description, Category category, Type type, LocalDateTime startDate, LocalDateTime endDate) {
        this.name = name;
        this.responsiblePerson = responsiblePerson;
        this.description = description;
        this.category = category;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MeetingDto that = (MeetingDto) o;
        return Objects.equals(name, that.name) && Objects.equals(responsiblePerson, that.responsiblePerson) && Objects.equals(description, that.description) && category == that.category && type == that.type && Objects.equals(startDate, that.startDate) && Objects.equals(endDate, that.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, responsiblePerson, description, category, type, startDate, endDate);
    }

    @Override
    public String toString() {
        return "MeetingDto{" +
                "name='" + name + '\'' +
                ", responsiblePerson=" + responsiblePerson +
                ", description='" + description + '\'' +
                ", category=" + category +
                ", type=" + type +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
