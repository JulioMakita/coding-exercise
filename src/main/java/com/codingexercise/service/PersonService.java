package com.codingexercise.service;

import static com.codingexercise.utils.ConverterUtils.dtoToPerson;
import static com.codingexercise.utils.ConverterUtils.personToDto;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.codingexercise.dto.PersonDto;
import com.codingexercise.entity.Person;
import com.codingexercise.exception.NotFoundException;
import com.codingexercise.repository.PersonRepository;
import com.codingexercise.utils.ConverterUtils;

@Service
public class PersonService {

  private PersonRepository personRepository;

  public PersonService(PersonRepository personRepository) {
    this.personRepository = personRepository;
  }

  public PersonDto save(PersonDto personDto) {
    Person savedPerson = personRepository.save(dtoToPerson(personDto));
    return personToDto(savedPerson);
  }

  public void deleteById(Long id) {
    personRepository.deleteById(id);
  }

  public boolean existsByFirstNameAndLastName(String firstName, String lastName) {
    return personRepository.existsByFirstNameAndLastName(firstName, lastName);
  }

  public Person findById(long personId) {
    Optional<Person> result = personRepository.findById(personId);
    if (result.isEmpty()) {
      throw new NotFoundException("Person does not exist with id: " + personId);
    }

    return result.get();
  }

  public PersonDto editPerson(long personId, PersonDto personDto) {
    Person personFound = findById(personId);
    personDto.setId(personFound.getId());
    return save(personDto);
  }

  public Set<PersonDto> findAll() {
    List<Person> result = (List<Person>) personRepository.findAll();
    return result.stream().map(person -> ConverterUtils.personToDto(person))
        .collect(Collectors.toSet());
  }

  public long count() {
    return personRepository.count();
  }
}
