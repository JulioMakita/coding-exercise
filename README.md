# coding-exercise

How to run the project.

The quickest way is by maven command. 

`mvn spring-boot:run`

You can also run executing the main method of `CodingExerciseApplication` by your favorite IDE like Eclipse, IntelliJ etc.

```
@SpringBootApplication
public class CodingExerciseApplication {

  public static void main(String[] args) {
    SpringApplication.run(CodingExerciseApplication.class, args);
  }
}
```

### How it works.

This is a simple project that add/update/delete/list Person and Address.

The following technologies have been used:

* Spring Boot
* Spring Data JPA
* Hibernate
* H2 Memory Database
* Mockito
* Maven


By default the application is running on port 8080.

### The Rest endpoints.

***ADD Person:*** 
`/person/add`

POST request to add a new Person in Json format.

e.g.
```JSON
{
	"firstName" : "Clark",
	"lastName" :"Kent"
}
```

***EDIT Person***

`/person/edit/{personId}`

PUT Request with personId as parameter and Person json object

***DELETE Person***

`/person/delete/{personId}`

DELETE request with personId as parameter

***List Person***

`/person/list`

List all person

***Count Person***

`/person/count`

Count the number of saved Person

***ADD Address**

`/address/add/{personId}`

POST request with personId as parameter and multiple address json object.
It can add multiple address to a person.

e.g.

```JSON
[
	{
	"street": "Washington Square Nort",
	"city": "New York",
	"state": "New York",
	"postalCode": "abnc123"
	},
	{
	"street": "Miami Beach",
	"city": "Miami",
	"state": "Florida",
	"postalCode": "abnc333"
}
]
```

***EDIT Address***

`/address/edit/{addressId}`

PUT Request with addressId as parameter and a single Address json object

e.g.
```JSON
	{
	"street": "Washington Square Nort",
	"city": "New York",
	"state": "New York",
	"postalCode": "abnc123"
	}
```

***DELETE Address***

`/address/delete/{addressId}`

DELETE request with addressId as parameter
