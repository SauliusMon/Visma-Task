package com.visma.task.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.visma.task.model.Meeting;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Repository
public class MeetingDatabase {

    private final ObjectMapper objectMapper;

    public MeetingDatabase (ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Optional<Meeting> getMeeting (Long meetingID) {
        Set<Meeting> existingMeetings;
        try {
            existingMeetings = objectMapper.readValue(new File("src/main/resources/data/meetings.json"), new TypeReference<>(){});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return existingMeetings.stream().filter(meeting -> meeting.getID().equals(meetingID)).findAny();
    }

    public Set<Meeting> getMeetings () {
        Set<Meeting> existingMeetings;
        try {
            existingMeetings = objectMapper.readValue(new File("src/main/resources/data/meetings.json"), new TypeReference<>(){});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return existingMeetings;
    }

    public void saveMeeting (Meeting meetingToSave) {
        try {
            Set<Meeting> existingMeetings = objectMapper.readValue(new File("src/main/resources/data/meetings.json"), new TypeReference<>(){});

            existingMeetings.add(meetingToSave);

            objectMapper.writeValue(new File("src/main/resources/data/meetings.json"), existingMeetings);
        } catch (IOException e) {
            System.err.println("Error occurred while saving meeting to JSON database: " + e.getMessage());
        }
    }

    public void deleteMeeting (Long meetingID) {
        try {
            Set<Meeting> existingMeetings = objectMapper.readValue(new File("src/main/resources/data/meetings.json"), new TypeReference<>(){});

            if (existingMeetings.removeIf(meeting -> meeting.getID().equals(meetingID))) {
                objectMapper.writeValue(new File("src/main/resources/data/meetings.json"), existingMeetings);
            } else {
                System.err.println("Meeting not found.");
            }
        } catch (IOException e) {
            System.err.println("Error occurred while deleting meeting from JSON database: " + e.getMessage());
        }
    }

    public void modifyMeeting (Meeting meetingToModify) {
        try {
            Set<Meeting> existingMeetings = objectMapper.readValue(new File("src/main/resources/data/meetings.json"), new TypeReference<>(){});

            if (existingMeetings.removeIf(meeting -> meeting.getID().equals(meetingToModify.getID()))) {
                existingMeetings.add(meetingToModify);
                objectMapper.writeValue(new File("src/main/resources/data/meetings.json"), existingMeetings);
            }
        } catch (IOException e) {
            System.err.println("Error occurred while modifying meeting data from JSON database: " + e.getMessage());
        }
    }
}
