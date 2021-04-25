package com.codingexercise.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.codingexercise.dto.AddressDto;
import com.codingexercise.service.AddressService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AddressControllerTest {

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  @MockBean
  private AddressService addressService;

  @Test
  void given_addressList_when_callingAddAddress_then_shouldSaveAndReturnNewSAddress()
      throws Exception {
    URI uri = new URI("http://localhost:" + port + "/address/add/1");
    List<AddressDto> addressList =
        Arrays.asList(new AddressDto(1L, "New Road", "New York City", "New York", "ABC1112", 0),
            new AddressDto(2L, "Old Road", "Miami City", "Miami", "DEF9998", 0));

    HttpEntity<List<AddressDto>> requestEntity = new HttpEntity(addressList);

    when(addressService.save(Mockito.anyLong(), Mockito.any(List.class))).thenReturn(addressList);

    ResponseEntity<List> response =
        restTemplate.exchange(uri, HttpMethod.POST, requestEntity, List.class);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(requestEntity.getBody().size(), response.getBody().size());
  }

  @Test
  void given_existingAddress_when_editAddress_then_shouldSaveAndReturnEditedAddress()
      throws Exception {
    AddressDto address = new AddressDto( "New Road", "New York City", "New York", "ABC1112");

    when(addressService.edit(1L, address)).thenReturn(address);

    URI uri = new URI("http://localhost:" + port + "/address/edit/1");
    HttpEntity<AddressDto> requestEntity = new HttpEntity(
        new AddressDto(1L, "New Road2", "New York City2", "New York2", "ABC5555", 1L));
    ResponseEntity<AddressDto> response =
        restTemplate.exchange(uri, HttpMethod.PUT, requestEntity, AddressDto.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotEquals(requestEntity.getBody(), response.getBody());
  }

  @Test
  void given_idOfExistingAddress_when_deleteAddressById_then_shouldRemoveAddress()
      throws Exception {
    URI uri = new URI("http://localhost:" + port + "/address/delete/" + 1L);

    ResponseEntity<String> response =
        restTemplate.exchange(uri, HttpMethod.DELETE, null, String.class);

    verify(addressService, Mockito.times(1)).deleteById(1L);
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  void given_twoAddressWithEmptyStreet_when_callingAddAddress_then_returnBadRequestAndTwoErrorMessages()
      throws Exception {
    URI uri = new URI("http://localhost:" + port + "/address/add/1");
    List<AddressDto> addressList =
        Arrays.asList(new AddressDto(1L, "", "New York City", "New York", "ABC1112", 0),
            new AddressDto(2L, "", "Miami City", "Miami", "DEF9998", 0));

    HttpEntity<List<AddressDto>> requestEntity = new HttpEntity(addressList);

    when(addressService.save(Mockito.anyLong(), Mockito.any(List.class))).thenReturn(addressList);

    ResponseEntity<Object> response =
        restTemplate.exchange(uri, HttpMethod.POST, requestEntity, Object.class);

    Map<String, Object> errorResult = (HashMap) response.getBody();
    List<String> errorMessages = (List<String>) errorResult.get("messages");

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals(2, errorMessages.size());
  }
}
