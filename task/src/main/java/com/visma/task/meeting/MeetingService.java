package com.visma.task.meeting;

import com.visma.task.meeting.dto.MeetingDto;
import com.visma.task.meeting.dto.MeetingFilterDto;
import com.visma.task.meeting.dto.PersonDto;
import com.visma.task.meeting.entity.Meeting;
import com.visma.task.meeting.entity.Person;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MeetingService {

    private final MeetingDatabase meetingDatabase;

    public MeetingService(MeetingDatabase meetingDatabase) {
        this.meetingDatabase = meetingDatabase;
    }

    private Long createIDForNewMeeting () {
        Set<Meeting> meetings = getMeetings();
        if (meetings != null) {
            Long[] takenIDs = meetings.stream().map(Meeting::getID).toArray(Long[]::new);
            for (int x = 1; x < 1000; x++) {
                long valueForStream = x;
                if (Arrays.stream(takenIDs).noneMatch(ID ->  ID.equals(valueForStream))) {
                    return valueForStream;
                }
            }
        }
        // In a case where meetings repository is empty
        else {
            return 1L;
        }
        // In a case meetings in repository exceed 1000 members
        return -1L;
    }

    public Set<Meeting> getMeetings () {
        return meetingDatabase.getMeetings();
    }

    public void createMeeting (MeetingDto meetingDto) {
        Meeting meetingToCreate = MeetingMapper.meetingDtoToMeeting(meetingDto);
        meetingToCreate.setID(createIDForNewMeeting());
        meetingDatabase.saveMeeting(meetingToCreate);
    }

    public void deleteMeeting (Long meetingID, PersonDto responsiblePerson) {
        Optional<Meeting> matchedMeeting = meetingDatabase.getMeeting(meetingID);

        if (matchedMeeting.isPresent()) {
            // Checks if person responsible for meeting is the same trying to delete the meeting
            if(matchedMeeting.get().getResponsiblePerson().equals(PersonMapper.personDtoToPerson(responsiblePerson))) {
                meetingDatabase.deleteMeeting(meetingID);
            }
        }
    }

    public String addPersonToMeeting (Long meetingID, PersonDto personDtoToAdd) {
        Optional<Meeting> matchedMeeting = meetingDatabase.getMeeting(meetingID);

        if (matchedMeeting.isPresent()) {
            Meeting meetingToModify = matchedMeeting.get();
            Person personToAdd = PersonMapper.personDtoToPerson(personDtoToAdd);

            // Checks if meeting already contains that person
            if (meetingToModify.getAttendees().contains(personToAdd)) {
                return "Meeting already contains this person.";
            }
            // Checks if that person has other meetings occurring at the same time
            if (isPersonInAnotherMeeting(personToAdd, meetingToModify.getStartDate(), meetingToModify.getEndDate())) {
                return "This person is already in another meeting, which has overlapping time intervals.";
            }
            meetingToModify.addAttendee(personToAdd);

            System.out.println("Attendee " + personToAdd.getName() + " " + personToAdd.getSecondName() +
                    " is being added to the meeting at " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + " time.");

            meetingDatabase.modifyMeeting(meetingToModify);
        }
        return "";
    }

    private boolean isPersonInAnotherMeeting (Person attendeeToAdd, LocalDateTime startTime, LocalDateTime endTime) {

        return meetingDatabase.getMeetings().stream().anyMatch(meeting ->
                        // Checks if the meeting contains an attendee (person) which is being searched for
                        meeting.getAttendees().contains(attendeeToAdd) &&
                                // Checks if meetings have overlapping time intervals
                                ((endTime.isAfter(meeting.getStartDate()) && startTime.isBefore(meeting.getEndDate())) ||
                                // Checks if meetings are happening at exactly the same time
                                (endTime.isEqual(meeting.getEndDate()) && endTime.isEqual(meeting.getStartDate())))
                );
    }

    public void removePersonFromMeeting (Long meetingID, PersonDto personDtoToRemove) {
        Optional<Meeting> matchedMeeting = meetingDatabase.getMeeting(meetingID);

        if (matchedMeeting.isPresent()) {
            Meeting meetingToModify = matchedMeeting.get();
            Person personToRemove = PersonMapper.personDtoToPerson(personDtoToRemove);

            // Checks if the person being removed is an admin (responsible person)
            if (!meetingToModify.getResponsiblePerson().equals(personToRemove)) {
                meetingToModify.removeAttendee(personToRemove);
                meetingDatabase.modifyMeeting(meetingToModify);
            }
        }
    }

    public List<MeetingDto> getMeetingsByFilter (MeetingFilterDto meetingFilterDto) {
        Set<Meeting> matchingMeetings = getMeetings();

        if (meetingFilterDto.getDescription() != null) {
            matchingMeetings = matchingMeetings.stream().filter(meeting -> meeting.getDescription().toLowerCase().contains(meetingFilterDto.getDescription().toLowerCase()))
                    .collect(Collectors.toSet());
        }
        if (meetingFilterDto.getResponsiblePerson() != null && meetingFilterDto.getResponsiblePerson().getPersonalID() != null) {
            matchingMeetings = matchingMeetings.stream().filter(meeting -> meeting.getResponsiblePerson().equals(meetingFilterDto.getResponsiblePerson()))
                    .collect(Collectors.toSet());
        }
        if (meetingFilterDto.getCategory() != null) {
            matchingMeetings = matchingMeetings.stream().filter(meeting -> meeting.getCategory().equals(meetingFilterDto.getCategory()))
                    .collect(Collectors.toSet());
        }
        if (meetingFilterDto.getType() != null) {
            matchingMeetings = matchingMeetings.stream().filter(meeting -> meeting.getType().equals(meetingFilterDto.getType()))
                    .collect(Collectors.toSet());
        }
        if (meetingFilterDto.getStartDate() != null && meetingFilterDto.getEndDate() != null) {
            matchingMeetings = matchingMeetings.stream().filter(meeting ->
                    (meetingFilterDto.getStartDate().isBefore(meeting.getStartDate()) || meetingFilterDto.getStartDate().equals(meeting.getStartDate())) &&
                    (meetingFilterDto.getEndDate().isAfter(meeting.getEndDate()) || meetingFilterDto.getEndDate().equals(meeting.getEndDate())))
                    .collect(Collectors.toSet());
        }
        if (meetingFilterDto.getNumberOfAttendees() != 0) {
            matchingMeetings = matchingMeetings.stream().filter(meeting -> meeting.getAttendees().size() > (meetingFilterDto.getNumberOfAttendees()))
                    .collect(Collectors.toSet());
        }
        return matchingMeetings.stream().map(MeetingMapper::meetingToMeetingDto).collect(Collectors.toList());
    }
}
