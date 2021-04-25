package com.codingexercise.entity;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
public class Person {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private String firstName;

  private String lastName;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "person", cascade = {CascadeType.ALL})
  private Set<Address> addresses;

  public Person() {}

  public Person(long id) {
    this.id = id;
  }

  public Person(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public Person(long id, String firstName, String lastName) {
    this(firstName, lastName);
    this.id = id;
  }

  public long getId() {
    return id;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public Set<Address> getAddress() {
    return addresses;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    Person person = (Person) o;
    return firstName.equals(person.firstName) && lastName.equals(person.lastName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(firstName, lastName);
  }

  @Override
  public String toString() {
    return "Person{" + "firstName='" + firstName + '\'' + ", lastName='" + lastName + '\'' + '}';
  }
}
