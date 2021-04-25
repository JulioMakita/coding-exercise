package com.codingexercise.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.codingexercise.entity.Address;

@Repository
public interface AddressRepository extends CrudRepository<Address, Long> {

  boolean existsByStreetAndCityAndStateAndPostalCodeAndPersonId(String street, String city,
      String state, String postalCode, long personId);
}
