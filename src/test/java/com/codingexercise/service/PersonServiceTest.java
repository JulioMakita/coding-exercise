package com.codingexercise.service;

import com.codingexercise.dto.PersonDto;
import com.codingexercise.entity.Person;
import com.codingexercise.exception.NotFoundException;
import com.codingexercise.repository.PersonRepository;
import com.codingexercise.utils.ConverterUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootTest
class PersonServiceTest {

  @Autowired
  private PersonService underTest;

  @MockBean
  private PersonRepository personRepository;

  @Test
  void given_newPerson_when_saveNewPerson_then_returnSavedPerso() {
    PersonDto personToSave = new PersonDto(0L, "Peter", "Parker");
    when(personRepository.save(Mockito.any(Person.class)))
        .thenReturn(ConverterUtils.dtoToPerson(personToSave));
    PersonDto savedPerson = underTest.save(personToSave);
    assertEquals(personToSave, savedPerson);
  }

  @Test
  void given_existingPersonId_when_deleteExistingPerson_then_checkWhetherDeleteMethodHasCalled() {
    Long idPerson = 1L;
    underTest.deleteById(idPerson);
    Mockito.verify(personRepository, times(1)).deleteById(idPerson);
  }

  @Test
  void given_firstAndLastName_when_searchByFirstAndLastName_then_shouldPersonExistAndReturnTrue() {
    String firstName = "Peter";
    String lastName = "Parker";
    when(personRepository.existsByFirstNameAndLastName(firstName, lastName)).thenReturn(true);
    boolean result = underTest.existsByFirstNameAndLastName(firstName, lastName);
    assertEquals(true, result);
  }

  @Test
  void given_savedPersonId_when_callingFindById_then_returnExistingPerson() {
    Optional<Person> person = Optional.of(new Person(1L, "Peter", "Parker"));
    when(personRepository.findById(1L)).thenReturn(person);
    Person result = underTest.findById(1L);
    assertEquals(person.get(), result);
  }

  @Test
  void given_notExistingId_when_callingFindById_then_shouldThrowsNotFoundException() {
    long id = 3L;
    String expectingErrorMessage = "Person does not exist with id: " + id;

    NotFoundException notFoundException = Assertions.assertThrows(NotFoundException.class, () -> {
      underTest.findById(id);
    });

    assertEquals(expectingErrorMessage, notFoundException.getMessage());
  }

  @Test
  void given_existingPerson_when_editExistingPersonAndSave_then_returnUpdatedPersonWithEditedValues() {
    Optional<Person> person = Optional.of(new Person(1L, "Peter", "Parker"));
    PersonDto personDto = ConverterUtils.personToDto(person.get());
    when(personRepository.findById(1L)).thenReturn(person);
    when(personRepository.save(person.get())).thenReturn(person.get());
    PersonDto result = underTest.editPerson(personDto);
    assertEquals(personDto, result);
  }

  @Test
  void when_findAll_then_returnAllSavedPeople() {
    List<Person> people =
        Stream.of(new Person(1L, "Peter", "Parker"), new Person(2L, "Eddie", "Block"))
            .collect(Collectors.toList());
    when(personRepository.findAll()).thenReturn(people);

    Set<PersonDto> result = underTest.findAll();
    assertEquals(people.size(), result.size());
  }

  @Test
  void when_countNumberOfExistingPerson_then_returnCountValue() {
    when(personRepository.count()).thenReturn(3L);
    long count = underTest.count();
    assertEquals(3, count);
  }
}
