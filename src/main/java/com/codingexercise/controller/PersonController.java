package com.codingexercise.controller;

import javax.validation.Valid;
import java.util.Set;
import com.codingexercise.dto.PersonDto;
import com.codingexercise.exception.NotFoundException;
import com.codingexercise.service.PersonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/person")
public class PersonController {

  private PersonService personService;

  public PersonController(PersonService personService) {
    this.personService = personService;
  }

  @PostMapping(path = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<PersonDto> addPerson(@RequestBody @Valid PersonDto person) {
    boolean result =
        personService.existsByFirstNameAndLastName(person.getFirstName(), person.getLastName());
    if (result) {
      throw new NotFoundException("Person already exists");
    }
    PersonDto savedPerson = personService.save(person);
    return ResponseEntity.ok(savedPerson);
  }

  @PutMapping(path = "/edit/{personId}", consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<PersonDto> editPerson(@PathVariable long personId, @RequestBody @Valid PersonDto person) {
    PersonDto savedPerson = personService.editPerson(personId, person);
    return ResponseEntity.ok(savedPerson);
  }

  @DeleteMapping(path = "/delete/{personId}")
  public ResponseEntity<Void> deletePerson(@PathVariable long personId) {
    personService.deleteById(personId);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @GetMapping(path = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
  public Set<PersonDto> listPerson() {
    return personService.findAll();
  }

  @GetMapping(path = "/count")
  public long countPerson() {
    return personService.count();
  }
}
