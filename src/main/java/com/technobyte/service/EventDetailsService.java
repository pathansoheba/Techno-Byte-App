package com.technobyte.service;

import com.technobyte.entity.EventDetails;
import com.technobyte.model.EventDetailsDTO;

import java.util.List;

public interface EventDetailsService {
    EventDetailsDTO saveEvent(EventDetails eventDetails);
    EventDetailsDTO getEventDetails(String eventId);

    List<EventDetailsDTO> getAllEventDetails();
}
