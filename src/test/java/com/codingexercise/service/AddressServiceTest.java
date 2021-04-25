package com.codingexercise.service;

import static com.codingexercise.utils.ConverterUtils.dtoToAddress;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import com.codingexercise.dto.AddressDto;
import com.codingexercise.entity.Address;
import com.codingexercise.entity.Person;
import com.codingexercise.exception.NotFoundException;
import com.codingexercise.repository.AddressRepository;
import com.codingexercise.utils.ConverterUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class AddressServiceTest {

  @Autowired
  private AddressService underTest;

  @MockBean
  private AddressRepository addressRepository;

  @MockBean
  private PersonService personService;

  @Test
  void given_listOfTwoAddress_when_saveNewListOfAddress_then_returnSavedAddress() {
    AddressDto addressDto1 =
        new AddressDto(1L, "New Road", "New York City", "New York", "ABC1112", 0);
    AddressDto addressDto2 = new AddressDto(2L, "Old Road", "Miami City", "Miami", "DEF9998", 0);

    List<AddressDto> addressListDto = Arrays.asList(addressDto1, addressDto2);
    List<Address> addressList = Arrays.asList(dtoToAddress(addressDto1), dtoToAddress(addressDto2));

    Person person = new Person(1L, "Norman", "Osborn");

    when(personService.findById(1L)).thenReturn(person);
    when(addressRepository.save(addressList.get(0))).thenReturn(addressList.get(0));
    when(addressRepository.save(addressList.get(1))).thenReturn(addressList.get(1));

    List<AddressDto> result = underTest.save(1L, addressListDto);
    assertEquals(addressListDto.size(), result.size());
    assertTrue(result.get(0).getPersonId() > 0);
    assertTrue(result.get(1).getPersonId() > 0);
    assertEquals(person.getId(), result.get(0).getId());
  }

  @Test
  void given_existingAddressId_when_callingFindById_then_returnExistingAddress() {
    Optional<Address> address = Optional.of(new Address(1L, "New Road", "New York City", "New York",
        "ABC1112", new Person(1L, "Harry", "Osborn")));

    when(addressRepository.findById(1L)).thenReturn(address);

    Address result = underTest.findById(1L);

    assertNotNull(result);
    assertEquals(address.get(), result);
  }

  @Test
  void given_notExistingAddressId_when_callingFindById_then_returnThrowNotFoundException() {
    long id = 3L;
    String expectingErrorMessage = "Address does not exist with id: " + id;

    NotFoundException notFoundException = Assertions.assertThrows(NotFoundException.class, () -> {
      underTest.findById(id);
    });

    assertEquals(expectingErrorMessage, notFoundException.getMessage());
  }

  @Test
  void given_existingAddress_when_editExistingAddressAndEdit_then_returnUpdatedAddressWithEditedValues() {
    Optional<Address> address = Optional.of(new Address("New Road", "New York City", "New York",
        "ABC1112", null));
    AddressDto addressDto = ConverterUtils.addressToDto(address.get());
    when(addressRepository.findById(1L)).thenReturn(address);
    when(addressRepository.save(address.get())).thenReturn(address.get());
    AddressDto result = underTest.edit(1L, addressDto);
    assertEquals(addressDto, result);
  }

  @Test
  void given_existingAddressId_when_deleteExistingAddressById_then_heckWhetherDeleteMethodHasCalled() {
    Long id = 1L;

    underTest.deleteById(id);

    verify(addressRepository, times(1)).deleteById(1L);
  }

  @Test
  void when_countNumberOfExistingAddress_then_returnCountValue() {
    long count = 3L;

    when(addressRepository.count()).thenReturn(count);
    long result = underTest.count();

    assertEquals(count, result);
  }
}
