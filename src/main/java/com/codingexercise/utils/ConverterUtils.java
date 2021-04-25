package com.codingexercise.utils;

import java.util.HashSet;
import java.util.stream.Collectors;
import com.codingexercise.dto.AddressDto;
import com.codingexercise.dto.PersonDto;
import com.codingexercise.entity.Address;
import com.codingexercise.entity.Person;

public class ConverterUtils {

  public static Person dtoToPerson(PersonDto personDto) {
    return new Person(personDto.getId(), personDto.getFirstName(), personDto.getLastName());
  }

  public static PersonDto personToDto(Person person) {
    return new PersonDto(person.getId(), person.getFirstName(), person.getLastName(),
        person.getAddress() != null
            ? person.getAddress().stream().map(a -> addressToDto(a)).collect(Collectors.toSet())
            : new HashSet<>());
  }

  public static Address dtoToAddress(AddressDto addressDto) {
    return new Address(addressDto.getId(), addressDto.getStreet(), addressDto.getCity(),
        addressDto.getState(), addressDto.getPostalCode(),
        addressDto.getPersonId() > 0 ? new Person(addressDto.getPersonId()) : null);
  }

  public static AddressDto addressToDto(Address address) {
    return new AddressDto(address.getId(), address.getStreet(), address.getCity(),
        address.getState(), address.getPostalCode(),
        address.getPerson() != null ? address.getPerson().getId() : 0);
  }
}
