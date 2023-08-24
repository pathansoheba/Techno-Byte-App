package com.technobyte.serviceImpl;

import com.technobyte.entity.EventDetails;
import com.technobyte.model.EventDetailsDTO;
import com.technobyte.repository.EventDetailsRepository;
import com.technobyte.service.EventDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EventDetailsServiceImpl implements EventDetailsService {

  @Autowired EventDetailsRepository eventDetailsRepository;

  @Override
  public EventDetailsDTO saveEvent(EventDetails eventDetails) {
    EventDetailsDTO eventDetailsDTO = null;
    try {
      eventDetails = eventDetailsRepository.save(eventDetails);
      eventDetailsDTO = eventDetails.convertToDTO(eventDetails);

    } catch (Exception exc) {
      log.error("Exception occurred", exc);
      throw exc;
    }
    return eventDetailsDTO;
  }

  @Override
  public EventDetailsDTO getEventDetails(String eventId) {
    EventDetailsDTO eventDetailsDTO = null;
    try {
      eventDetailsDTO =
          eventDetailsRepository
              .findById(eventId)
              .map(eventDetails -> eventDetails.convertToDTO(eventDetails))
              .orElse(null);

    } catch (Exception exc) {
      log.error("Exception occurred in getEventDetails", exc);
      throw exc;
    }
    return eventDetailsDTO;
  }

  @Override
  public List<EventDetailsDTO> getAllEventDetails() {
    List<EventDetailsDTO> eventDetailsDTOList = null;
    try {
      eventDetailsDTOList =
          eventDetailsRepository.findAll().stream()
              .map(eventDetails -> eventDetails.convertToDTO(eventDetails))
              .collect(Collectors.toList());

    } catch (Exception exc) {
      log.error("Exception occurred in getEventDetails", exc);
      throw exc;
    }
    return eventDetailsDTOList;
  }
}
