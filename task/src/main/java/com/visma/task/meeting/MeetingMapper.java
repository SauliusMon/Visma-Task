package com.visma.task.meeting;

import com.visma.task.meeting.dto.MeetingDto;
import com.visma.task.meeting.entity.Meeting;

import java.util.HashSet;

public class MeetingMapper {

    public static Meeting meetingDtoToMeeting (MeetingDto meetingDto) {
        return new Meeting(
                meetingDto.getName(),
                meetingDto.getResponsiblePerson(),
                meetingDto.getDescription(),
                meetingDto.getCategory(),
                meetingDto.getType(),
                meetingDto.getStartDate(),
                meetingDto.getEndDate(),
                new HashSet<>()
                );
    }

    public static MeetingDto meetingToMeetingDto (Meeting meeting) {
        return new MeetingDto(
                meeting.getName(),
                meeting.getResponsiblePerson(),
                meeting.getDescription(),
                meeting.getCategory(),
                meeting.getType(),
                meeting.getStartDate(),
                meeting.getEndDate()
        );
    }
}
