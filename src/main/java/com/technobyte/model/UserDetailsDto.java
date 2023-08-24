package com.technobyte.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserDetailsDto {

  private String userId;

  private String userName;

  private String emailId;

  private String userType;

  private String password;

  private String mobile;

  private String college;

  private String office;

  private String address;

  private String study;

  private String role;

  private List<EventsDto> events;

}
