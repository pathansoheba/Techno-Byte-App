package com.technobyte.model;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class EventDetailsDto {
  public String eventId;

  private String targetAudience;

  private String eventType;

  private String eventName;

  private String description;

  private Integer duration;

  private Double fee;

  private String place;

  private String address;

  private LocalDateTime date;

  private String googleMapLink;

  private List<String> latestUpdates;

  private List<RegistrationDetailsDto> registrations;

  private LocalDateTime crtDt;

  private LocalDateTime lstUpDtDt;
}
