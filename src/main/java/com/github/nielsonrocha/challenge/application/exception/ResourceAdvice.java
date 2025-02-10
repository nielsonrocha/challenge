package com.github.nielsonrocha.challenge.application.exception;

import com.github.nielsonrocha.challenge.domain.dto.ErrorResponse;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ResourceAdvice {

  @ExceptionHandler(BusinessException.class)
  @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
  public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
    return buildResponse(HttpStatus.UNPROCESSABLE_ENTITY, ex);
  }

  @ExceptionHandler(IntegrationException.class)
  @ResponseStatus(HttpStatus.GATEWAY_TIMEOUT)
  public ResponseEntity<ErrorResponse> handleIntegrationException(IntegrationException ex) {
    return buildResponse(HttpStatus.GATEWAY_TIMEOUT, ex);
  }

  @ExceptionHandler(NotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex) {
    return buildResponse(HttpStatus.NOT_FOUND, ex);
  }

  private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, Exception ex) {
    log.error("Erro: {}", ex.getMessage(), ex);

    ErrorResponse response = new ErrorResponse(
        LocalDateTime.now(),
        status.value(),
        status.getReasonPhrase(),
        ex.getMessage()
    );

    return ResponseEntity.status(status).body(response);
  }

}
