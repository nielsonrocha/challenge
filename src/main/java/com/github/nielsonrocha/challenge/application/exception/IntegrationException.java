package com.github.nielsonrocha.challenge.application.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@NoArgsConstructor
@ResponseStatus(HttpStatus.BAD_GATEWAY)
public class IntegrationException extends RuntimeException {

  public IntegrationException(String message) {
    super(message);
  }
}
