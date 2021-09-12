# Bremersee Comparator

[![codecov](https://codecov.io/gh/bremersee/comparator/branch/master/graph/badge.svg)](https://codecov.io/gh/bremersee/comparator)

This project contains a builder for comparing and sorting objects.

The comparator can compare any kind of objects which have the same attributes or the same 'getters'.
It uses reflection to get the values of these attributes or 'getters'. 
The values may be a simple type like java.lang.String or a complex type which implements 
java.lang.Comparable.

#### Maven Site

- [Release](https://bremersee.github.io/comparator/index.html)

- [Snapshot](https://nexus.bremersee.org/repository/maven-sites/comparator/2.4.1-SNAPSHOT/index.html)

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

The definition of the sorting can also be described by a string. The string of the example above can
look like this:

```text
type|name|createdAt,desc
```

The syntax is:

```text
fieldName0,asc,ignoreCase,nullIsFirst|fieldName1,desc,ignoreCase,nullIsFirst
```

The pipe (|) character separtes the fields. The field values are separated by comma (,).
The defaults are asc = true, ignoreCase = true and nullIsFirst = false and can be omitted. That's
why
```text
type|name|createdAt,desc
```
is a short form for
```text
type,asc,true,false|name,asc,true,false|createdAt,asc,true,false
```

The field name can also be a path to a value, if you have complex objects:
```text
room.number,asc,true,false|person.lastName,asc,true,false|person.firstName,asc,true,false
```

if your class looks like this for example:
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

Let's sort our list of nodes with this 'well known text':

```java
import org.bremersee.comparator.*;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

class Example {
  public static void main(String[] args) {
    List<Node> list = new ArrayList<>();
    // add nodes
    list.sort(ComparatorBuilder.builder()
        .fromWellKnownText("type|name|createdAt,desc", comparatorField -> {
          if ("type".equals(comparatorField.getField())) {
            return  (Comparator) (o1, o2) -> (o1 instanceof Branch && o2 instanceof Branch) 
                ? 0 
                : o1 instanceof Branch ? -1 : 1;
          }
          return new ValueComparator(comparatorField);
        })
        .build());
  }
}
```

### XML Schema

The XML schema of the model is available 
[here](http://bremersee.github.io/xmlschemas/comparator-v2.xsd).


### Spring Framework Support

The Spring Common Data project contains a class for sorting, too.
The class ComparatorSpringUtils contains methods to transform the 
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
