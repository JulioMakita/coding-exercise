package com.codingexercise.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import com.codingexercise.entity.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class PersonRepositoryTest {

  @Autowired
  private PersonRepository underTest;

  private Person personSaved;

  @BeforeEach
  void setUp() {
    personSaved = underTest.save(new Person("Peter", "Parker"));
  }

  @AfterEach
  void tearDown() {
    underTest.deleteAll();
  }

  @Test
  void given_newPerson_when_saveNewPerson_then_returnSavedPerson() {
    Person person = new Person("Eddie", "Block");
    Person result = underTest.save(person);
    assertTrue(result.getId() > 0L);
  }

  @Test
  void given_existingPerson_when_deleteExistingPerson_then_returnEmptyWhenSearchDeletedPerson() {
    Optional<Person> person = underTest.findById(personSaved.getId());

    assertTrue(person.isPresent());

    underTest.deleteById(person.get().getId());

    Optional<Person> afterDeletion = underTest.findById(personSaved.getId());

    assertTrue(afterDeletion.isEmpty());
  }

  @Test
  void when_findPersonByFirstAndLastName_then_shouldPersonExistAndReturnTrue() {
    boolean result = underTest.existsByFirstNameAndLastName("Peter", "Parker");
    assertTrue(result);
  }

  @Test
  void when_findAll_then_returnAllSavedPeople() {
    Iterable<Person> result = underTest.findAll();
    assertEquals(1, ((List) result).size());
  }

  @Test
  void when_countNumberOfExistingPerson_then_returnCountValue() {
    long count = underTest.count();
    assertEquals(1, count);
  }
}
