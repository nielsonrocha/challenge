package com.github.nielsonrocha.challenge.domain.dto;

import java.util.List;
import org.springframework.data.domain.Pageable;

public record PageDTO<T>(
    List<T> content, long totalElements, int totalPages, int pageNumber, int pageSize) {

  public static <T> PageDTO<T> from(List<T> content, Pageable pageable, long totalElements) {
    int totalPages = (int) Math.ceil((double) totalElements / pageable.getPageSize());
    return new PageDTO<>(
        content, totalElements, totalPages, pageable.getPageNumber(), pageable.getPageSize());
  }
}
