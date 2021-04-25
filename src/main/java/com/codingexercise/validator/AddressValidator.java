package com.codingexercise.validator;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Map;
import com.codingexercise.dto.AddressDto;
import com.codingexercise.service.AddressService;
import org.springframework.web.servlet.HandlerMapping;

public class AddressValidator implements ConstraintValidator<ValidAddress, AddressDto> {

  private AddressService addressService;

  private HttpServletRequest request;

  public AddressValidator(AddressService addressService, HttpServletRequest request) {
    this.addressService = addressService;
    this.request = request;
  }

  @Override
  public void initialize(ValidAddress constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(AddressDto addressDto, ConstraintValidatorContext context) {

    Map<String, String> pathVariables =
        (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
    String personId = pathVariables.get("personId");
    addressDto.setPersonId(Long.valueOf(personId));


    boolean addressExists = addressService.isAddressExists(addressDto);
    if (addressExists) {
      context.disableDefaultConstraintViolation();
      context
          .buildConstraintViolationWithTemplate(
              "Address already exists for personId: " + addressDto.getPersonId())
          .addConstraintViolation();
      return false;
    }

    return true;
  }
}
