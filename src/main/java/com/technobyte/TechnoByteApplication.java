package com.technobyte;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.technobyte"})
@Slf4j
public class TechnoByteApplication {

  public static void main(String[] args) {
    log.info("server about to start");
    SpringApplication.run(TechnoByteApplication.class, args);
    log.info("server started");
  }
}
