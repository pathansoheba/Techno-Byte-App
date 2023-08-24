package com.technobyte.entity;

import com.technobyte.model.EventDetailsDTO;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "event_details")
public class EventDetails {

  @Id
  @Column(name = "event_id")
  private String eventId;

  @Column(name = "target_audience")
  private String targetAudience;

  @Column(name = "event_type")
  private String eventType;

  @Column(name = "event_name")
  private String eventName;

  @Column(name = "description")
  private String description;

  @Column(name = "duration")
  private Integer duration;

  @Column(name = "fee")
  private Double fee;

  @Column(name = "place")
  private String place;

  @Column(name = "address")
  private String address;

  @Column(name = "date")
  private LocalDateTime date;

  @Column(name = "google_map_link")
  private String googleMapLink;

  @Column(name = "latest_updates")
  private String latestUpdates;

  @Column(name = "registrations")
  private String registrations;

  @Column(name = "crt_dt")
  private LocalDateTime crtDt;

  @Column(name = "lst_updt_dt")
  private LocalDateTime lstUpDtDt;

  public EventDetailsDTO convertToDTO(EventDetails eventEntity) {
    EventDetailsDTO eventDTO = new EventDetailsDTO();
    eventDTO.setEventId(eventEntity.getEventId());
    eventDTO.setTargetAudience(eventEntity.getTargetAudience());
    eventDTO.setEventType(eventEntity.getEventType());
    eventDTO.setEventName(eventEntity.getEventName());
    eventDTO.setDescription(eventEntity.getDescription());
    eventDTO.setDuration(eventEntity.getDuration());
    eventDTO.setFee(eventEntity.getFee());
    eventDTO.setPlace(eventEntity.getPlace());
    eventDTO.setAddress(eventEntity.getAddress());
    eventDTO.setDate(eventEntity.getDate());
    eventDTO.setGoogleMapLink(eventEntity.getGoogleMapLink());
    eventDTO.setLatestUpdates(eventEntity.getLatestUpdates());
    eventDTO.setRegistrations(eventEntity.getRegistrations());
    eventDTO.setCrtDt(eventEntity.getCrtDt());
    eventDTO.setLstUpDtDt(eventEntity.getLstUpDtDt());
    return eventDTO;
  }
}
