package com.technobyte.serviceImpl;

import com.technobyte.entity.UserDetails;
import com.technobyte.model.UserDetailsDto;
import com.technobyte.repository.UserDetailsRepository;
import com.technobyte.service.UserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  @Autowired UserDetailsRepository userDetailsRepository;

  @Override
  public UserDetailsDto saveUser(UserDetails userDetails) {
    UserDetailsDto userDetailsDto = null;
    try {
      String salt = BCrypt.gensalt(12);
      String hashedPassword = BCrypt.hashpw(userDetails.getPassword(), salt);
      userDetails.setPassword(hashedPassword);
      userDetails.setSalt(salt);
      userDetails = userDetailsRepository.save(userDetails);
      userDetailsDto = userDetails.convertToDTO(userDetails);

    } catch (Exception exc) {
      log.error("Exception occurred", exc);
      throw exc;
    }
    return userDetailsDto;
  }

  @Override
  public UserDetailsDto getUserDetails(String userId) {
    UserDetailsDto userDetailsDto = null;
    try {
      userDetailsDto =
          userDetailsRepository
              .findById(userId)
              .map(userDetail -> userDetail.convertToDTO(userDetail))
              .orElse(null);
    } catch (Exception exc) {
      log.error("Exception occurred while fetching used details", exc);
      throw exc;
    }
    return userDetailsDto;
  }

}
