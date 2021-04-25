package com.codingexercise.repository;

import org.springframework.data.repository.CrudRepository;

import com.codingexercise.entity.Person;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends CrudRepository<Person, Long> {

  boolean existsByFirstNameAndLastName(String firstName, String lastName);
  
}
