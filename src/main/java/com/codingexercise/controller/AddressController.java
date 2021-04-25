package com.codingexercise.controller;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;
import com.codingexercise.dto.AddressDto;
import com.codingexercise.service.AddressService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/address")
public class AddressController {

  private AddressService addressService;

  public AddressController(AddressService addressService) {
    this.addressService = addressService;
  }

  @GetMapping(path = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
  public Set<AddressDto> listAddress() {
    return addressService.findAll();
  }

  @PostMapping(path = "/add/{personId}", consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<AddressDto>> addAddress(@PathVariable long personId,
      @RequestBody List<@Valid AddressDto> address) {
    List<AddressDto> savedAddress = addressService.save(personId, address);
    return ResponseEntity.ok(savedAddress);
  }

  @PutMapping(path = "/edit/{addressId}", consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<AddressDto> editAddress(@PathVariable long addressId,
      @Valid @RequestBody AddressDto address) {
    AddressDto editedAddress = addressService.edit(addressId, address);
    return ResponseEntity.ok(editedAddress);
  }

  @DeleteMapping(path = "/delete/{id}")
  public ResponseEntity<String> deleteAddress(@PathVariable long id) {
    addressService.deleteById(id);
    return ResponseEntity.ok("Deleted Successfully");
  }
}
