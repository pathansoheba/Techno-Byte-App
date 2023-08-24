package com.technobyte.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.technobyte.entity.EventDetails;
import com.technobyte.model.EventDetailsDTO;
import com.technobyte.model.RegistrationDetails;
import com.technobyte.service.EventDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/api")
@Slf4j
public class EventController {
  @Autowired private final EventDetailsService eventDetailsService;
  private ObjectMapper objectMapper = new ObjectMapper();

  public EventController(EventDetailsService eventDetailsService) {
    this.eventDetailsService = eventDetailsService;
  }

  @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<EventDetailsDTO> createEvent(@RequestBody EventDetails eventDetails) {
    try {
      if (eventDetails.getEventId() != null) {
        List<String> latestUpdates =
            Stream.concat(
                    eventDetailsService
                        .getEventDetails(eventDetails.getEventId())
                        .getLatestUpdates()
                        .stream(),
                    getLatestUpdates(eventDetails.getLatestUpdates()).stream())
                .distinct() // Remove duplicates
                .collect(Collectors.toList());
        eventDetails.setLatestUpdates(converListToString(latestUpdates));

        List<RegistrationDetails> registrationDetailsList =
            Stream.concat(
                    eventDetailsService
                        .getEventDetails(eventDetails.getEventId())
                        .getRegistrations()
                        .stream(),
                    getRegistrations(eventDetails.getRegistrations()).stream())
                .distinct() // Remove duplicates
                .collect(Collectors.toList());
        eventDetails.setRegistrations(convertObjectlistToString(registrationDetailsList));

      } else {
        UUID uuid = UUID.randomUUID();
        eventDetails.setEventId(uuid.toString());
      }
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
    }
  }

  private String converListToString(List<String> list) {
    return list.stream().map(str -> "\"" + str + "\"").collect(Collectors.joining(",", "[", "]"));
  }

  private String convertObjectlistToString(List<RegistrationDetails> registrationDetailsList) {
    return registrationDetailsList.stream()
        .map(
            registration ->
                "{\"name\":\""
                    + registration.getName()
                    + "\",\"email\":\""
                    + registration.getEmail()
                    + "\"}")
        .collect(Collectors.joining(",", "[", "]"));
  }

  private List<String> getLatestUpdates(String latestUpdates) {
    List<String> getLatestUpdates = null;
    if (latestUpdates != null) {

      try {
        getLatestUpdates =
            objectMapper.readValue(latestUpdates, new TypeReference<ArrayList<String>>() {});
      } catch (JsonProcessingException e) {
        throw new RuntimeException(e);
      }
    }
    return getLatestUpdates;
  }

  private List<RegistrationDetails> getRegistrations(String registrations) {
    List<RegistrationDetails> registrationDetailsList = null;
    if (registrations != null) {

      JsonNode jsonNode = null;
      try {
        jsonNode = objectMapper.readTree(registrations);
      } catch (JsonProcessingException e) {
        throw new RuntimeException(e);
      }
      Iterator<JsonNode> iterator = jsonNode.elements();
      Stream<JsonNode> jsonNodeStream =
          StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, 0), false);

      registrationDetailsList =
          jsonNodeStream
              .map(node -> objectMapper.convertValue(node, RegistrationDetails.class))
              .collect(Collectors.toList());
    }
    return registrationDetailsList;
  }
}
