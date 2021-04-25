package com.codingexercise.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Address {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private String street;

  private String city;

  private String state;

  private String postalCode;

  @ManyToOne
  @JoinColumn(name = "personId", referencedColumnName = "id", nullable = false)
  private Person person;

  public Address() {}

  public Address(String street, String city, String state, String postalCode, Person person) {
    this.street = street;
    this.city = city;
    this.state = state;
    this.postalCode = postalCode;
    this.person = person;
  }

  public Address(long id, String street, String city, String state, String postalCode,
      Person person) {
    this(street, city, state, postalCode, person);
    this.id = id;
  }

  public long getId() {
    return id;
  }

  public String getStreet() {
    return street;
  }

  public String getCity() {
    return city;
  }

  public String getState() {
    return state;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public Person getPerson() {
    return person;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    Address address = (Address) o;
    return street.equals(address.street) && city.equals(address.city) && state.equals(address.state)
        && postalCode.equals(address.postalCode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(street, city, state, postalCode);
  }
}
