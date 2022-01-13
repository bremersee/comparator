# Bremersee Comparator

This project contains a builder for comparing and sorting objects.

The comparator can compare any kind of objects which have the same attributes or the same 'getters'.
It uses reflection to get the values of these attributes or 'getters'.
The values may be a simple type like java.lang.String or a complex type which implements
java.lang.Comparable.

### Usage

Given is for example a tree that is represented by these classes:

The common Node class:

```java
import java.util.Date;

abstract class Node {
  private Date createdAt;
  private String name;
  // getter and setter
}
```

The Branch class:

```java
import java.util.List;
import java.util.ArrayList;

class Branch extends Node {
  private List<Node> children = new ArrayList<>();
  // getter
}
```

The Leaf class:

```java
class Leaf extends Node {
  private String value;
  // getter and setter
}
```

You want to sort a list of nodes by name. And if the names are equal by created date:

```java
import org.bremersee.comparator.*;
import java.util.List;
import java.util.ArrayList;

class Example {
  public static void main(String[] args) {
    List<Node> list = new ArrayList<>();
    // add nodes
    list.sort(ComparatorBuilder.builder()
        .add("name", true, true, false)        // fieldName, asc, ignoreCase, nullIsFirst
        .add("createdAt", false, true, false)  // fieldName, asc, ignoreCase, nullIsFirst
        .build());
  }
}
```

That's all. All nodes in the list are sorted by name and date. But what happens, if you want to sort
them by type (first the branches and then the leafs) and then by name and date? There is no field
that stores the type. Then you can do this:

```java
import org.bremersee.comparator.*;
import java.util.List;
import java.util.ArrayList;

class Example {
  public static void main(String[] args) {
    List<Node> list = new ArrayList<>();
    // add nodes
    list.sort(ComparatorBuilder.builder()
        .add((o1, o2) ->
            (o1 instanceof Branch && o2 instanceof Branch) ? 0 : o1 instanceof Branch ? -1 : 1)
        .add("name", true, true, false)        // fieldName, asc, ignoreCase, nullIsFirst
        .add("createdAt", false, true, false)  // fieldName, asc, ignoreCase, nullIsFirst
        .build());
  }
}
```

Now you have a list, that contains the branches first, sorted by name and date, and then the leafs.

#### Comparator arguments (class `ComparatorField`)

There are four attributes which define the comparison.

| Attribute    | Description                                                       | Default  |
|--------------|-------------------------------------------------------------------|----------|
| field        | The field name (or method name) of the object. It can be a path.  | null     |
|              | The segments are separated by a dot (.): field0.field1.field2     |          |
|              | It can be null. Then the object itself must be comparable.        |          |
| asc or desc  | Defines ascending or descending ordering.                         | asc      |
| ignoreCase   | Makes a case ignoring comparison (only for strings).              | true     |
| nullIsFirst  | Defines the ordering if one of the values is null.                | false    |

The field name can also be a path to a value, if you have complex objects, for example
`room.numer`, `person.lastName` or `person.firstName`, if your classes look like this:
```java
class Employee {
  private Person person;
  private Room room;
  // getter and setter
}
```

with Person
```java
class Person {
  private String lastName;
  private String firstName;
  // getter and setter
}
```

and Room
```java
class Room {
  private int number;
  // getter and setter
}
```

The sorting of employees:

```java
import org.bremersee.comparator.*;
import org.bremersee.comparator.model.*;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

class Example {

  public static void main(String[] args) {
    List<Employee> list = new ArrayList<>();
    // add employees
    list.sort(ComparatorBuilder.builder()
        .addAll(List.of(
            new ComparatorField("room.number", true, true, false),
            new ComparatorField("person.lastName", true, true, false),
            new ComparatorField("person.firstName", true, true, false)
        ))
        .build());
  }
}
```

### Spring Framework Support

#### REST support

The `CompratorField` has a string representation, that can be used to pass the sort order as a
query parameter into a `RestController`.

The syntax is:
```text
fieldName,asc,ignoreCase,nullIsFirst
```

Example:
```text
room.number,asc,true,false
```

Or:
```text
createdAt,desc,true,false
```

The rest controller may look like this (with OpenApi annotations):

```java
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.bremersee.comparator.model.ComparatorField;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestRestController {

  @Operation(
      summary = "Get something that can be sorted.",
      operationId = "getSomethingSorted",
      tags = {"test-controller"}
  )
  @GetMapping(path = "/")
  public ResponseEntity<List<Employee>> getSomethingSorted(
      @Parameter(array = @ArraySchema(schema = @Schema(type = "string")))
      @RequestParam(name = "sort", required = false) List<ComparatorField> sort) {

    List<Empployy> list = service.findEmployees();
    list.sort(ComparatorBuilder.builder()
        .addAll(sort)
        .build());
    
    return ResponseEntity.ok(list);
  }
}
```

The url may look like this:
```text
http://localhost:8080?sort=room.number,asc&sort=person.lastName,asc,true
```

The parsing of the string is done by providing the `CompratorFieldConverter` as a Spring bean:

```java
import org.bremersee.comparator.spring.converter.ComparatorFieldConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ComparatorFieldConverterConfiguration {

  @Bean
  public ComparatorFieldConverter comparatorFieldConverter() {
    return new ComparatorFieldConverter();
  }

}
```

#### Spring `Sort` Mapper

The Spring Common Data project contains a class for sorting, too.
The class `SortMapper` contains methods to transform the
comparator fields of this library into the objects of the spring framework.

To use the Spring Framework Support you have to add the following
dependency to your project:

```xml
<dependency>
    <groupId>org.springframework.data</groupId>
    <artifactId>spring-data-commons</artifactId>
    <version>{your-spring-data-commons-version}</version>
</dependency>
```

### XML Schema

The XML schema of the model is available
[here](http://bremersee.github.io/xmlschemas/comparator-v2.xsd).

