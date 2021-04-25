package com.codingexercise.service;

import static com.codingexercise.utils.ConverterUtils.dtoToAddress;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.codingexercise.dto.AddressDto;
import com.codingexercise.entity.Address;
import com.codingexercise.entity.Person;
import com.codingexercise.exception.NotFoundException;
import com.codingexercise.repository.AddressRepository;
import com.codingexercise.utils.ConverterUtils;

@Service
public class AddressService {

  private PersonService personService;

  private AddressRepository addressRepository;

  public AddressService(PersonService personService, AddressRepository addressRepository) {
    this.personService = personService;
    this.addressRepository = addressRepository;
  }

  public Set<AddressDto> findAll() {
    List<Address> result = (List<Address>) addressRepository.findAll();
    return result.stream().map(address -> ConverterUtils.addressToDto(address))
        .collect(Collectors.toSet());
  }

  public List<AddressDto> save(long personId, List<AddressDto> addressDtoList) {
    Person person = personService.findById(personId);
    return addressDtoList.stream().map(a -> {
      a.setPersonId(person.getId());
      Address address = addressRepository.save(dtoToAddress(a));
      a.setId(address.getId());
      return a;
    }).collect(Collectors.toList());
  }

  public Address findById(long addressId) {
    Optional<Address> existingAddress = addressRepository.findById(addressId);
    if (existingAddress.isEmpty()) {
      throw new NotFoundException("Address does not exist with id: " + addressId);
    }
    return existingAddress.get();
  }

  public AddressDto edit(long addressId, AddressDto addressDto) {
    Address address = findById(addressId);
    addressDto.setId(address.getId());
    addressDto.setPersonId(address.getPerson().getId());
    addressRepository.save(dtoToAddress(addressDto));
    return addressDto;
  }

  public void deleteById(long id) {
    addressRepository.deleteById(id);
  }

  public long count() {
    return addressRepository.count();
  }

  public boolean isAddressExists(AddressDto address) {
    return addressRepository.existsByStreetAndCityAndStateAndPostalCodeAndPersonId(
        address.getStreet(), address.getCity(), address.getState(), address.getPostalCode(),
        address.getPersonId());
  }
}
