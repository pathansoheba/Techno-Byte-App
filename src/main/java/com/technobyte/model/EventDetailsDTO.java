package com.technobyte.model;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EventDetailsDTO {
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

  private String latestUpdates;

  private String registrations;

  private LocalDateTime crtDt;

  private LocalDateTime lstUpDtDt;
}
