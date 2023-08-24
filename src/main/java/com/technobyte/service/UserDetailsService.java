package com.technobyte.service;

import com.technobyte.entity.UserDetails;
import com.technobyte.model.UserDetailsDto;

public interface UserDetailsService {

  UserDetailsDto saveUser(UserDetails userDetails);

  UserDetailsDto getUserDetails(String userId);

}
