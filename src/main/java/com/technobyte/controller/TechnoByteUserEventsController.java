package com.technobyte.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.technobyte.entity.EventDetails;
import com.technobyte.entity.UserDetails;
import com.technobyte.model.EventDetailsDto;
import com.technobyte.model.RegistrationDetailsDto;
import com.technobyte.model.UserDetailsDto;
import com.technobyte.service.EventDetailsService;
import com.technobyte.service.UserDetailsService;
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
public class TechnoByteUserEventsController {
  @Autowired private final EventDetailsService eventDetailsService;
  @Autowired private final UserDetailsService userDetailsService;

  private ObjectMapper objectMapper = new ObjectMapper();

  public TechnoByteUserEventsController(
      EventDetailsService eventDetailsService, UserDetailsService userDetailsService) {
    this.eventDetailsService = eventDetailsService;
    this.userDetailsService = userDetailsService;
  }

  @PutMapping("/event")
  public ResponseEntity<EventDetailsDto> createEvent(@RequestBody EventDetails eventDetails) {
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

        List<RegistrationDetailsDto> registrationDetailsDtoList =
            Stream.concat(
                    eventDetailsService
                        .getEventDetails(eventDetails.getEventId())
                        .getRegistrations()
                        .stream(),
                    getRegistrations(eventDetails.getRegistrations()).stream())
                .distinct() // Remove duplicates
                .collect(Collectors.toList());
        eventDetails.setRegistrations(convertObjectlistToString(registrationDetailsDtoList));

      } else {
        UUID uuid = UUID.randomUUID();
        eventDetails.setEventId(uuid.toString());
      }
      EventDetailsDto response = eventDetailsService.saveEvent(eventDetails);
      return new ResponseEntity<>(response, HttpStatus.CREATED);
    } catch (Exception e) {
      log.error("Exception occurred in createEvent ", e);
      return new ResponseEntity<>(new EventDetailsDto(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/getEvents/{eventId}")
  public ResponseEntity<EventDetailsDto> getEventDetails(@PathVariable String eventId) {
    try {
      EventDetailsDto response = eventDetailsService.getEventDetails(eventId);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (Exception e) {
      log.error("Exception occurred in getEventDetails", e);
      return new ResponseEntity<>(new EventDetailsDto(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/getAllEvents")
  public ResponseEntity<List<EventDetailsDto>> getAllEvents() {
    List<EventDetailsDto> response = null;
    try {
      response = eventDetailsService.getAllEventDetails();
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (Exception e) {
      log.error("Exception occurred in getAllEvents", e);
      return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  private String converListToString(List<String> list) {
    return list.stream().map(str -> "\"" + str + "\"").collect(Collectors.joining(",", "[", "]"));
  }

  private String convertObjectlistToString(
      List<RegistrationDetailsDto> registrationDetailsDtoList) {
    return registrationDetailsDtoList.stream()
        .map(
            registration ->
                "{\"name\":\""
                    + registration.getName()
                    + "\",\"email\":\""
                    + registration.getEmail()
                    + "\",\"userId\":\""
                    + registration.getUserId()
                    + " \"}")
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

  private List<RegistrationDetailsDto> getRegistrations(String registrations) {
    List<RegistrationDetailsDto> registrationDetailsDtoList = null;
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

      registrationDetailsDtoList =
          jsonNodeStream
              .map(node -> objectMapper.convertValue(node, RegistrationDetailsDto.class))
              .collect(Collectors.toList());
    }
    return registrationDetailsDtoList;
  }

  @PostMapping("/user")
  public ResponseEntity<UserDetailsDto> createUser(@RequestBody UserDetails userDetails) {
    try {

      UUID uuid = UUID.randomUUID();
      userDetails.setUserId(uuid.toString());
      UserDetailsDto response = userDetailsService.saveUser(userDetails);
      return new ResponseEntity<>(response, HttpStatus.CREATED);
    } catch (Exception e) {
      log.error("Exception occurred in createUser", e);
      return new ResponseEntity<>(new UserDetailsDto(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/getUser/{userId}")
  public ResponseEntity<UserDetailsDto> getUserDetails(@PathVariable String userId) {
    try {
      UserDetailsDto response = userDetailsService.getUserDetails(userId);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (Exception e) {
      log.error("Exception occurred in getUserDetails", e);
      return new ResponseEntity<>(new UserDetailsDto(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/getUserByEvent/{eventId}")
  public ResponseEntity<List<UserDetailsDto>> getUserByEventDetails(@PathVariable String eventId) {
    try {
      List<RegistrationDetailsDto> registrationDetailsDtoList =
          eventDetailsService.getEventDetails(eventId).getRegistrations().stream()
              .distinct() // Remove duplicates
              .collect(Collectors.toList());

      List<UserDetailsDto> userDetailsDtoList =
          registrationDetailsDtoList.stream()
              .map(registration -> userDetailsService.getUserDetails(registration.getUserId()))
              .collect(Collectors.toList());
      return new ResponseEntity<>(userDetailsDtoList, HttpStatus.OK);
    } catch (Exception e) {
      log.error("Exception occurred in getUserByEventDetails", e);
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
