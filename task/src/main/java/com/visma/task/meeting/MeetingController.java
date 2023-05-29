package com.visma.task.meeting;

import com.visma.task.meeting.dto.MeetingDto;
import com.visma.task.meeting.dto.MeetingFilterDto;
import com.visma.task.meeting.dto.PersonDto;
import com.visma.task.meeting.entity.Category;
import com.visma.task.meeting.entity.Person;
import com.visma.task.meeting.entity.Type;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/meeting")
public class MeetingController {

    private final MeetingService meetingService;

    public MeetingController(MeetingService meetingService) {
        this.meetingService = meetingService;
    }

    @PostMapping("/create-meeting")
    public void createMeeting(@RequestBody MeetingDto meetingToCreate) {
        meetingService.createMeeting(meetingToCreate);
    }

    @DeleteMapping("/delete-meeting/{meetingID}")
    public void deleteMeeting (@PathVariable Long meetingID, @RequestBody PersonDto responsiblePerson) {
        meetingService.deleteMeeting(meetingID, responsiblePerson);
    }

    @PutMapping("add-person-to-meeting/{meetingID}")
    public String addPersonToMeeting (@PathVariable Long meetingID, @RequestBody PersonDto personToAdd) {
        return meetingService.addPersonToMeeting(meetingID, personToAdd);
    }

    @PutMapping("remove-person-from-meeting/{meetingID}")
    public void removePersonFromMeeting (@PathVariable Long meetingID, @RequestBody PersonDto personToRemove) {
        meetingService.removePersonFromMeeting(meetingID, personToRemove);
    }

    @GetMapping("get-meetings")
    public List<MeetingDto> getMeetingByFilter (@RequestBody MeetingFilterDto meetingFilterDto) {
        return meetingService.getMeetingsByFilter(meetingFilterDto);
    }
}
