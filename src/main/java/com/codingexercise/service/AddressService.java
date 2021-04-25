package com.codingexercise.service;

import static com.codingexercise.utils.ConverterUtils.dtoToAddress;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import com.codingexercise.dto.AddressDto;
import com.codingexercise.entity.Address;
import com.codingexercise.entity.Person;
import com.codingexercise.exception.NotFoundException;
import com.codingexercise.repository.AddressRepository;
import com.codingexercise.utils.ConverterUtils;
import org.springframework.stereotype.Service;

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

    List<Address> addressList = addressDtoList.stream().map(a -> {
      a.setPersonId(person.getId());
      Address address = dtoToAddress(a);
      return address;
    }).collect(Collectors.toList());

    addressRepository.saveAll(addressList);

    return addressDtoList;
  }

  public Address findById(long addressId) {
    Optional<Address> existingAddress = addressRepository.findById(addressId);
    if (existingAddress.isEmpty()) {
      throw new NotFoundException("Address does not exist with id: " + addressId);
    }
    return existingAddress.get();
  }

  public AddressDto edit(AddressDto addressDto) {
    Address address = findById(addressDto.getId());
    addressDto.setId(address.getId());
    addressRepository.save(dtoToAddress(addressDto));
    return addressDto;
  }

  public void deleteById(long id) {
    addressRepository.deleteById(id);
  }

  public long count() {
    return addressRepository.count();
  }
}