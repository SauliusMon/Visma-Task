package com.visma.task.mapper;

import com.visma.task.rest.dto.PersonDto;
import com.visma.task.model.Person;

public class PersonMapper {

    public static Person personDtoToPerson (PersonDto personDto) {
        return new Person(
                personDto.getPersonalID(),
                personDto.getName(),
                personDto.getSecondName()
        );
    }

    public static PersonDto personToPersonDto (Person person) {
        return new PersonDto(
                person.getPersonalID(),
                person.getName(),
                person.getSecondName()
        );
    }
}
