package com.codingexercise.dto;

import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class PersonDto {

  private long id;

  @NotBlank(message = "First Name should not be empty")
  private String firstName;

  @NotBlank(message = "Last Name should not be empty")
  private String lastName;

  private Set<AddressDto> addresses;

  public PersonDto() {}

  public PersonDto(long id, String firstName, String lastName) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.addresses = new HashSet<>();
  }

  public PersonDto(long id, String firstName, String lastName, Set<AddressDto> addresses) {
    this(id, firstName, lastName);
    this.addresses = addresses;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public Set<AddressDto> getAddresses() {
    return addresses;
  }

  public void setAddresses(Set<AddressDto> addresses) {
    this.addresses = addresses;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    PersonDto personDto = (PersonDto) o;
    return firstName.equals(personDto.firstName) && lastName.equals(personDto.lastName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(firstName, lastName);
  }
}
