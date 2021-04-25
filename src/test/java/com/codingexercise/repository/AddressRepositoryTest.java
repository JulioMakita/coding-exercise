package com.codingexercise.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.codingexercise.entity.Address;
import com.codingexercise.entity.Person;

@DataJpaTest
public class AddressRepositoryTest {

  @Autowired
  private AddressRepository underTest;

  @Autowired
  private PersonRepository personRepository;

  private Person personSaved;

  private Address addressSaved;

  @BeforeEach
  void setUp() {
    personSaved = personRepository.save(new Person("Mary", "Jane"));
    addressSaved = underTest
        .save(new Address("New Road", "New York City", "New York", "ABC1112", personSaved));
  }

  @AfterEach
  void tearDown() {
    underTest.deleteAll();
    personRepository.deleteAll();
  }

  @Test
  void given_addressVariables_when_checkWhetherAddressExists_then_returnTrue() {
    boolean result = underTest.existsByStreetAndCityAndStateAndPostalCodeAndPersonId("New Road",
        "New York City", "New York", "ABC1112", personSaved.getId());

    assertTrue(result);
  }

  @Test
  void given_addressVariablesWithNonExistingPersonId_when_checkWhetherAddressExists_then_returnFalse() {
    boolean result = underTest.existsByStreetAndCityAndStateAndPostalCodeAndPersonId("New Road",
        "New York City", "New York", "ABC1112", 3L);

    assertFalse(result);
  }

  @Test
  void given_listOfTwoAddress_when_saveNewListOfAddress_then_returnSavedAddress() {
    List<Address> result = (List) underTest.saveAll(Arrays
        .asList(new Address("New Road", "New York City", "New York", "ABC1112", personSaved)));

    assertEquals(result.size(), 1);
  }

  @Test
  void given_existingAddress_when_editExistingAddressAndSave_then_returnUpdatedAddressWithEditedValues() {
    Optional<Address> optionalAddress = underTest.findById(addressSaved.getId());
    Address addressQuery = optionalAddress.get();

    Address addressToEdit = new Address(addressQuery.getId(), "Old Road", "Old Towny", "New York",
        "ABC1112", addressQuery.getPerson());

    Address result = underTest.save(addressToEdit);
    assertEquals(addressToEdit, result);
  }

  @Test
  void given_existingAddress_when_deleteExistingAddress_then_returnEmptyWhenSearchDeletedAddress() {
    Optional<Address> address = underTest.findById(addressSaved.getId());

    assertTrue(address.isPresent());

    underTest.deleteById(address.get().getId());

    Optional<Address> afterDeletion = underTest.findById(addressSaved.getId());

    assertTrue(afterDeletion.isEmpty());
  }

  @Test
  void when_findAll_then_returnAllSavedAddresses() {
    Iterable<Address> result = underTest.findAll();
    assertEquals(1, ((List) result).size());
  }

  @Test
  void when_countNumberOfExistingAddress_then_returnCountValue() {
    long count = underTest.count();
    assertEquals(1, count);
  }
}
