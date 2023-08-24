package com.technobyte.repository;

import com.technobyte.entity.EventDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventDetailsRepository extends JpaRepository<EventDetails, String> {
}