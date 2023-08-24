package com.technobyte.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventsDto {

  private String eventId;

  private boolean attended;

  private String certificateId;
}
