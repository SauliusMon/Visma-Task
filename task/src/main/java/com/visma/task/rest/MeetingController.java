package com.visma.task.rest;

import com.visma.task.rest.dto.MeetingDto;
import com.visma.task.rest.dto.MeetingFilterDto;
import com.visma.task.rest.dto.PersonDto;
import com.visma.task.service.MeetingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/meeting")
public class MeetingController {

    private final MeetingService meetingService;

    public MeetingController(MeetingService meetingService) {
        this.meetingService = meetingService;
    }


    @GetMapping("get-meetings")
    public List<MeetingDto> getMeetingByFilter (@RequestBody MeetingFilterDto meetingFilterDto) {
        return meetingService.getMeetingsByFilter(meetingFilterDto);
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
}
