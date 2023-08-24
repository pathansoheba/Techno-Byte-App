package com.technobyte.service;

import com.technobyte.entity.EventDetails;
import com.technobyte.model.EventDetailsDto;

import java.util.List;

public interface EventDetailsService {
    EventDetailsDto saveEvent(EventDetails eventDetails);
    EventDetailsDto getEventDetails(String eventId);

    List<EventDetailsDto> getAllEventDetails();
}
