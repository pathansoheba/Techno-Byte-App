package com.technobyte.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationDetailsDto {
  private String userId;
  private String name;
  private String email;
}
