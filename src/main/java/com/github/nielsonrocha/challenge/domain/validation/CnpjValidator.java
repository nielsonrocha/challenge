package com.github.nielsonrocha.challenge.domain.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CnpjValidator implements ConstraintValidator<Cnpj, String> {

  @Override
  public void initialize(Cnpj constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
    if (value == null || value.isEmpty()) {
      return false;
    }

    value = value.replaceAll("[^0-9]", "");

    if (value.length() != 14) {
      return false;
    }

    int[] multiplicadores1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
    int[] multiplicadores2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

    int soma1 = 0;
    for (int i = 0; i < 12; i++) {
      soma1 += Character.getNumericValue(value.charAt(i)) * multiplicadores1[i];
    }
    int digito1 = 11 - (soma1 % 11);
    if (digito1 >= 10) {
      digito1 = 0;
    }

    int soma2 = 0;
    for (int i = 0; i < 13; i++) {
      soma2 += Character.getNumericValue(value.charAt(i)) * multiplicadores2[i];
    }
    int digito2 = 11 - (soma2 % 11);
    if (digito2 >= 10) {
      digito2 = 0;
    }

    return value.endsWith(String.valueOf(digito1) + digito2);
  }
}
