package com.visma.task.meeting;

import com.visma.task.mapper.MeetingMapper;
import com.visma.task.mapper.PersonMapper;
import com.visma.task.rest.dto.MeetingDto;
import com.visma.task.rest.dto.MeetingFilterDto;
import com.visma.task.rest.dto.PersonDto;
import com.visma.task.model.Category;
import com.visma.task.model.Meeting;
import com.visma.task.model.Person;
import com.visma.task.model.Type;
import com.visma.task.repository.MeetingDatabase;
import com.visma.task.service.MeetingService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;

@ExtendWith(MockitoExtension.class)
public class MeetingServiceTest {

    @Mock
    private MeetingDatabase meetingDatabase;

    @InjectMocks
    private MeetingService meetingService;

    private MeetingDto mockMeetingDto;
    private Meeting mockMeeting;
    private PersonDto personDto;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        meetingService = new MeetingService(meetingDatabase);
        mockMeetingDto =  new MeetingDto("Meeting", new Person("245668790", "John", "Smith"),
                "Important meeting", Category.CODEMONKEY, Type.LIVE, LocalDateTime.now(),
                LocalDateTime.now().plusHours(1L));
        mockMeeting = new Meeting(1L, "Meeting", new Person("245668790", "John", "Smith"),
                "Important meeting", Category.CODEMONKEY, Type.LIVE, LocalDateTime.now(),
                LocalDateTime.now().plusHours(1L), new HashSet<>());

        personDto = new PersonDto("245668790", "John", "Smith");
    }

    @Test
    public void getMeetingsTest () {
        Mockito.when(meetingDatabase.getMeetings()).thenReturn(Set.of(mockMeeting));

        Set<Meeting> meetings = meetingService.getMeetings();
        assertEquals(meetings, Set.of(mockMeeting));
    }


    @Test
    public void createMeetingTest () {
        Mockito.doAnswer(invocation -> invocation.<Meeting>getArgument(0)).when(meetingDatabase).saveMeeting(MeetingMapper.meetingDtoToMeeting(mockMeetingDto));

        meetingService.createMeeting(mockMeetingDto);

        Mockito.verify(meetingDatabase).getMeetings();
        Mockito.verify(meetingDatabase, Mockito.atLeastOnce()).saveMeeting(mockMeeting);
    }

    @Test
    public void deleteMeetingTest() {
        Mockito.when(meetingDatabase.getMeeting(1L)).thenReturn(Optional.of(mockMeeting));

        Mockito.doAnswer(invocation -> invocation.<Meeting>getArgument(0)).when(meetingDatabase).deleteMeeting(1L);

        meetingService.deleteMeeting(1L, personDto);

        Mockito.verify(meetingDatabase).deleteMeeting(1L);
    }

    @Test
    public void addPersonMeetingTest() {
        Mockito.when(meetingDatabase.getMeeting(1L)).thenReturn(Optional.of(mockMeeting));

        assertEquals("", meetingService.addPersonToMeeting(1L, personDto));
    }

    @Test
    public void addExistingPersonToMeetingTest() {
        Meeting meetingWithPerson = new Meeting(mockMeeting);
        meetingWithPerson.addAttendee(PersonMapper.personDtoToPerson(personDto));

        Mockito.when(meetingDatabase.getMeeting(1L)).thenReturn(Optional.of(meetingWithPerson));

        assertEquals("Meeting already contains this person.", meetingService.addPersonToMeeting(1L, personDto));
    }

    @Test
    public void addAlreadyBusyPersonToMeetingTest() {
        Meeting meetingWithPerson = new Meeting(mockMeeting);
        meetingWithPerson.addAttendee(PersonMapper.personDtoToPerson(personDto));
        meetingWithPerson.setID(2L);

        Mockito.when(meetingDatabase.getMeetings()).thenReturn(Set.of(meetingWithPerson, mockMeeting));

        Mockito.when(meetingDatabase.getMeeting(1L)).thenReturn(Optional.of(mockMeeting));

        assertEquals("This person is already in another meeting, which has overlapping time intervals.", meetingService.addPersonToMeeting(1L, personDto));
    }

    @Test
    public void removePersonFromMeetingTest() {
        Meeting meetingWithPerson = new Meeting(mockMeeting);
        Person personToRemove = new Person("2578255", "Arvin", "Maroon");
        meetingWithPerson.addAttendee(personToRemove);

        Mockito.when(meetingDatabase.getMeeting(1L)).thenReturn(Optional.of(meetingWithPerson));

        Mockito.doAnswer(invocation -> invocation.<Meeting>getArgument(0)).when(meetingDatabase).modifyMeeting(meetingWithPerson);

        meetingService.removePersonFromMeeting(1L, PersonMapper.personToPersonDto(personToRemove));
        Mockito.verify(meetingDatabase).modifyMeeting(meetingWithPerson);
    }

    @Test
    public void removeResponsiblePersonFromMeetingTest() {
        Meeting meetingWithPerson = new Meeting(mockMeeting);
        meetingWithPerson.addAttendee(PersonMapper.personDtoToPerson(personDto));

        Mockito.when(meetingDatabase.getMeeting(1L)).thenReturn(Optional.of(meetingWithPerson));

        Mockito.doAnswer(invocation -> invocation.<Meeting>getArgument(0)).when(meetingDatabase).modifyMeeting(meetingWithPerson);

        meetingService.removePersonFromMeeting(1L, personDto);

        Mockito.verify(meetingDatabase).getMeeting(1L);
        Mockito.verify(meetingDatabase, Mockito.never()).modifyMeeting(meetingWithPerson);
    }

    @Test
    public void getMeetingsByFilter() {
        MeetingFilterDto filter = new MeetingFilterDto("important", PersonMapper.personDtoToPerson(personDto), mockMeeting.getCategory(), mockMeeting.getType(),
                mockMeeting.getStartDate(), mockMeeting.getEndDate(), 1);

        Meeting meetingWithPerson = new Meeting(mockMeeting);
        meetingWithPerson.addAttendee(PersonMapper.personDtoToPerson(personDto));
        meetingWithPerson.addAttendee(new Person("3578255", "Ginnie", "Douglas"));
        meetingWithPerson.setID(2L);

        Mockito.when(meetingDatabase.getMeetings()).thenReturn(Set.of(meetingWithPerson, mockMeeting));

        List<MeetingDto> expectedMeetings = meetingService.getMeetingsByFilter(filter);

        assertEquals(List.of(MeetingMapper.meetingToMeetingDto(meetingWithPerson)), expectedMeetings);
    }

    @Test
    public void getMeetingsByEmptyFilter() {
        MeetingFilterDto filter = new MeetingFilterDto(null, null, null, null,
                mockMeeting.getStartDate().minusHours(1L), mockMeeting.getEndDate(), 0);

        Meeting meetingWithPerson = new Meeting(mockMeeting);
        meetingWithPerson.addAttendee(new Person("3578255", "Ginnie", "Douglas"));
        meetingWithPerson.setID(2L);

        Mockito.when(meetingDatabase.getMeetings()).thenReturn(Set.of(meetingWithPerson, mockMeeting));

        List<MeetingDto> expectedMeetings = meetingService.getMeetingsByFilter(filter);

        assertEquals(List.of(MeetingMapper.meetingToMeetingDto(meetingWithPerson), MeetingMapper.meetingToMeetingDto(mockMeeting)), expectedMeetings);
    }
}
