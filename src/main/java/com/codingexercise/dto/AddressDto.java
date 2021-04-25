package com.codingexercise.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

public class AddressDto {

  private long id;

  @NotBlank(message = "Street should not be empty")
  private String street;

  @NotBlank(message = "City should not be empty")
  private String city;

  @NotBlank(message = "State should not be empty")
  private String state;

  @NotBlank(message = "Postal code should not be empty")
  private String postalCode;

  @Min(value = 1, message = "Address should have a person id associated")
  private long personId;

  public AddressDto() {}

  public AddressDto(long id, String street, String city, String state, String postalCode,
      long personId) {
    this.id = id;
    this.street = street;
    this.city = city;
    this.state = state;
    this.postalCode = postalCode;
    this.personId = personId;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getStreet() {
    return street;
  }

  public String getCity() {
    return city;
  }

  public String getState() {
    return state;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public long getPersonId() {
    return personId;
  }

  public void setPersonId(long personId) {
    this.personId = personId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    AddressDto that = (AddressDto) o;
    return street.equals(that.street) && city.equals(that.city) && state.equals(that.state)
        && postalCode.equals(that.postalCode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(street, city, state, postalCode);
  }
}
