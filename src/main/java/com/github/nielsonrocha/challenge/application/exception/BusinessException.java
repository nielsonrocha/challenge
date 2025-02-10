package com.github.nielsonrocha.challenge.application.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@NoArgsConstructor
@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class BusinessException extends RuntimeException {

  public BusinessException(String message) {
    super(message);
  }
}
