package com.codingexercise.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.codingexercise.dto.PersonDto;
import com.codingexercise.service.PersonService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PersonControllerTest {

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  @MockBean
  private PersonService personService;

  @Test
  void given_newPerson_when_callingAddPersonRestEndpoint_then_shouldSaveAndReturnNewPerson()
      throws Exception {

    URI uri = new URI("http://localhost:" + port + "/person/add");
    PersonDto personDto = new PersonDto("Peter", "Parker");
    HttpEntity<PersonDto> requestEntity = new HttpEntity(personDto);

    when(personService.save(any(PersonDto.class))).thenReturn(personDto);

    ResponseEntity<PersonDto> response =
        restTemplate.exchange(uri, HttpMethod.POST, requestEntity, PersonDto.class);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(requestEntity.getBody(), response.getBody());
  }

  @Test
  void given_savedPerson_when_callingEditPerson_then_shouldUpdatedPersonMatchToGivenPerson()
      throws Exception {
    PersonDto person = new PersonDto( "Peter2", "Parker2");
    when(personService.editPerson(1L, person)).thenReturn(person);

    URI uri = new URI("http://localhost:" + port + "/person/edit/1");
    HttpEntity<PersonDto> requestEntity = new HttpEntity(person);
    ResponseEntity<PersonDto> response =
        restTemplate.exchange(uri, HttpMethod.PUT, requestEntity, PersonDto.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(requestEntity.getBody(), response.getBody());
  }

  @Test
  void given_idOfExistingPersons_when_deletePersonById_then_shouldRemoveAPerson() throws Exception {
    URI uri = new URI("http://localhost:" + port + "/person/delete/" + 1L);

    ResponseEntity<String> response =
        restTemplate.exchange(uri, HttpMethod.DELETE, null, String.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  void given_twoSavedPeople_when_callingListPersons_then_shouldReturnListOfTwoPerson()
      throws Exception {
    Set<PersonDto> personList =
        Stream.of(new PersonDto(1L, "Peter", "Parker"), new PersonDto(2L, "Eddies", "Block"))
            .collect(Collectors.toSet());
    when(personService.findAll()).thenReturn(personList);

    URI uri = new URI("http://localhost:" + port + "/person/list");
    ResponseEntity<List<PersonDto>> response =
        restTemplate.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(personList.size(), response.getBody().size());
  }

  @Test
  void given_savedPerson_when_callingCountPeople_then_shouldReturnCountOfSavedPerson()
      throws Exception {

    when(personService.count()).thenReturn(1L);
    URI uri = new URI("http://localhost:" + port + "/person/count");
    ResponseEntity<Long> response =
        restTemplate.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(1, response.getBody());
  }

  @Test
  void given_newPersonWithNullFirstNameAndEmptyLastName_when_callingAddPerson_then_returnBadRequestAndTwoErrorMessages()
      throws Exception {
    URI uri = new URI("http://localhost:" + port + "/person/add");
    PersonDto personDto = new PersonDto("", null);
    HttpEntity<PersonDto> requestEntity = new HttpEntity(personDto);

    when(personService.save(any(PersonDto.class))).thenReturn(personDto);

    ResponseEntity<Object> response =
        restTemplate.exchange(uri, HttpMethod.POST, requestEntity, Object.class);

    Map<String, Object> errorResult = (HashMap) response.getBody();
    List<String> errorMessages = (List<String>) errorResult.get("messages");

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals(2, errorMessages.size());
  }
}
