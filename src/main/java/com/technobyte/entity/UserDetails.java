package com.technobyte.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.technobyte.model.EventsDto;
import com.technobyte.model.UserDetailsDto;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "user_details")
public class UserDetails {

  @Id
  @Column(name = "user_Id")
  private String userId;

  @Column(name = "user_Name")
  private String userName;

  @Column(name = "email_Id")
  private String emailId;

  @Column(name = "user_type")
  private String userType;

  @Column(name = "password")
  private String password;

  @Column(name = "mobile")
  private String mobile;

  @Column(name = "college")
  private String college;

  @Column(name = "office")
  private String office;

  @Column(name = "address")
  private String address;

  @Column(name = "study")
  private String study;

  @Column(name = "role")
  private String role;

  @Column(name = "events")
  private String events;

  @Column(name = "salt")
  private String salt;

  public UserDetailsDto convertToDTO(UserDetails user) {
    UserDetailsDto userDTO = new UserDetailsDto();
    userDTO.setUserId(user.getUserId());
    userDTO.setUserName(user.getUserName());
    userDTO.setEmailId(user.getEmailId());
    userDTO.setUserType(user.getUserType());
    userDTO.setMobile(user.getMobile());
    userDTO.setCollege(user.getCollege());
    userDTO.setOffice(user.getOffice());
    userDTO.setAddress(user.getAddress());
    userDTO.setStudy(user.getStudy());
    userDTO.setRole(user.getRole());
    userDTO.setPassword(user.getPassword());
    try {
      ObjectMapper objectMapper = new ObjectMapper();

      if (user.getEvents() != null && !user.getEvents().isEmpty()) {

        JsonNode jsonNode = objectMapper.readTree(user.getEvents());
        Iterator<JsonNode> iterator = jsonNode.elements();
        Stream<JsonNode> jsonNodeStream =
            StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, 0), false);

        List<EventsDto> eventsDtoList =
            jsonNodeStream
                .map(node -> objectMapper.convertValue(node, EventsDto.class))
                .collect(Collectors.toList());
        userDTO.setEvents(eventsDtoList.isEmpty() ? null : eventsDtoList);
      }
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
    return userDTO;
  }
}
