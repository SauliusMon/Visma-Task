package com.visma.task.meeting;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.visma.task.rest.dto.MeetingDto;
import com.visma.task.rest.dto.MeetingFilterDto;
import com.visma.task.rest.dto.PersonDto;
import com.visma.task.model.Category;
import com.visma.task.model.Person;
import com.visma.task.model.Type;
import com.visma.task.rest.MeetingController;
import com.visma.task.service.MeetingService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class MeetingControllerTest {

    private final MeetingService meetingService = Mockito.mock(MeetingService.class);
    private final MeetingController meetingController = new MeetingController(meetingService);
    private final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(meetingController).build();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Test
    public void createMeeting() throws Exception {
        MeetingDto meetingToCreate = new MeetingDto(
                "New Meeting",
                new Person("123456789", "Jane", "Doe"),
                "Important description",
                Category.TEAMBUILDING,
                Type.INPERSON,
                LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES),
                LocalDateTime.now().plusHours(1L).truncatedTo(ChronoUnit.MINUTES)
        );

        String requestBody = objectMapper.writeValueAsString(meetingToCreate);

        mockMvc.perform(post("/api/v1/meeting/create-meeting")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());

        Mockito.verify(meetingService).createMeeting(Mockito.eq(meetingToCreate));
    }

    @Test
    public void deleteMeetingTest() throws Exception {
        Long meetingID = 1L;
        PersonDto responsiblePerson = new PersonDto("123456789", "John", "Doe");

        mockMvc.perform(delete("/api/v1/meeting/delete-meeting/{meetingID}", meetingID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(responsiblePerson)))
                .andExpect(status().isOk());

        Mockito.verify(meetingService).deleteMeeting(meetingID, responsiblePerson);
    }

    @Test
    public void testRemovePersonFromMeeting() throws Exception {
        Long meetingID = 1L;
        PersonDto personToRemove = new PersonDto("123456789", "John", "Doe");

        mockMvc.perform(put("/api/v1/meeting/remove-person-from-meeting/{meetingID}", meetingID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personToRemove)))
                .andExpect(status().isOk());

        Mockito.verify(meetingService).removePersonFromMeeting(Mockito.eq(meetingID), Mockito.eq(personToRemove));
    }

    @Test
    public void testAddPersonToMeeting() throws Exception {
        Long meetingID = 1L;
        PersonDto personToAdd = new PersonDto("123456789", "John", "Doe");

        mockMvc.perform(put("/api/v1/meeting/add-person-to-meeting/{meetingID}", meetingID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personToAdd)))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        Mockito.verify(meetingService).addPersonToMeeting(Mockito.eq(meetingID), Mockito.eq(personToAdd));
    }


    @Test
    public void testGetMeetingsByFilter() throws Exception {
        MeetingFilterDto meetingFilterDto = new MeetingFilterDto();
        meetingFilterDto.setCategory(Category.TEAMBUILDING);
        meetingFilterDto.setType(Type.INPERSON);

        MeetingDto meeting1 = new MeetingDto("Meeting 1", new Person("123456789", "Jane", "Doe"), "Not-important description", Category.TEAMBUILDING, Type.INPERSON, LocalDateTime.now(), LocalDateTime.now().plusHours(1L));
        MeetingDto meeting2 = new MeetingDto("Meeting 2", new Person("987654321", "John", "Smith"), "Important description", Category.HUB, Type.LIVE, LocalDateTime.now(), LocalDateTime.now().plusHours(2L));
        MeetingDto meeting3 = new MeetingDto("Meeting 3", new Person("94554321", "Jonathan", "Mark"), "Important description too", Category.SHORT, Type.LIVE, LocalDateTime.now(), LocalDateTime.now().plusHours(2L));

        List<MeetingDto> meetings = Arrays.asList(meeting1, meeting2, meeting3);
        Mockito.when(meetingService.getMeetingsByFilter(any(MeetingFilterDto.class))).thenReturn(meetings);

        mockMvc.perform(get("/api/v1/meeting/get-meetings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(meetingFilterDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].name").value("Meeting 1"))
                .andExpect(jsonPath("$[0].responsiblePerson.name").value("Jane"))
                .andExpect(jsonPath("$[0].description").value("Not-important description"))
                .andExpect(jsonPath("$[0].category").value("TEAMBUILDING"))
                .andExpect(jsonPath("$[0].type").value("INPERSON"))
                .andExpect(jsonPath("$[1].name").value("Meeting 2"))
                .andExpect(jsonPath("$[1].responsiblePerson.name").value("John"))
                .andExpect(jsonPath("$[1].description").value("Important description"))
                .andExpect(jsonPath("$[1].category").value("HUB"))
                .andExpect(jsonPath("$[1].type").value("LIVE"))
                .andExpect(jsonPath("$[2].name").value("Meeting 3"))
                .andExpect(jsonPath("$[2].responsiblePerson.name").value("Jonathan"))
                .andExpect(jsonPath("$[2].description").value("Important description too"))
                .andExpect(jsonPath("$[2].category").value("SHORT"))
                .andExpect(jsonPath("$[2].type").value("LIVE"));

        Mockito.verify(meetingService).getMeetingsByFilter(Mockito.eq(meetingFilterDto));
    }
}
