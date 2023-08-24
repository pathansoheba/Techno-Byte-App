package com.technobyte.controller;

import com.technobyte.entity.EventDetails;
import com.technobyte.model.EventDetailsDTO;
import com.technobyte.service.EventDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.id.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@Slf4j
public class EventController {
  @Autowired private final EventDetailsService eventDetailsService;

  public EventController(EventDetailsService eventDetailsService) {
    this.eventDetailsService = eventDetailsService;
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<EventDetailsDTO> createEvent(@RequestBody EventDetails eventDetails) {
    try {
      UUID uuid = UUID.randomUUID();
      eventDetails.setEventId(uuid.toString());
      EventDetailsDTO response = eventDetailsService.saveEvent(eventDetails);
      return new ResponseEntity<>(response, HttpStatus.CREATED);
    } catch (Exception e) {
      log.error("Exception occurred", e);
      return new ResponseEntity<>(new EventDetailsDTO(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/getEvents/{eventId}")
  public ResponseEntity<EventDetailsDTO> getEventDetails(@PathVariable String eventId) {
    try {
      EventDetailsDTO response = eventDetailsService.getEventDetails(eventId);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (Exception e) {
      log.error("Exception occurred", e);
      return new ResponseEntity<>(new EventDetailsDTO(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/getAllEvents")
  public ResponseEntity<List<EventDetailsDTO>> getAllEvents() {
    List<EventDetailsDTO> response = null;
    try {
      response = eventDetailsService.getAllEventDetails();
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (Exception e) {
      log.error("Exception occurred", e);
      return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }  }

}
