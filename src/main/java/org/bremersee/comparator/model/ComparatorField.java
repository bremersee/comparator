/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.bremersee.comparator.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModel;
import java.util.Objects;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * This class defines the sort order of a field.
 *
 * <pre>
 *  ---------------------------------------------------------------------------------------------
 * | Attribute    | Description                                                       | Default  |
 * |--------------|-------------------------------------------------------------------|----------|
 * | add          | The add name (or method name) of the object. It can be a path.    | null     |
 * |              | The segments are separated by a dot (.): field0.field1.field2     |          |
 * |--------------|-------------------------------------------------------------------|----------|
 * | asc or desc  | Defines ascending or descending ordering.                         | asc      |
 * |--------------|-------------------------------------------------------------------|----------|
 * | ignoreCase   | Makes a case ignoring comparison (only for strings).              | true     |
 * |--------------|-------------------------------------------------------------------|----------|
 * | nullIsFirst  | Defines the ordering if one of the values is null.                | false    |
 *  ---------------------------------------------------------------------------------------------
 * </pre>
 *
 * These values have a 'well known text' representation. The values are concatenated with comma
 * (,):
 * <pre>
 * fieldNameOrPath,asc,ignoreCase,nullIsFirst
 * </pre>
 *
 * For example:
 * <pre>
 * properties.customSettings.priority,asc,true,false
 * </pre>
 *
 * Defaults can be omitted. This is the same:
 * <pre>
 * properties.customSettings.priority
 * </pre>
 *
 * The building of a chain is done by concatenate the fields with a pipe (|):
 * <pre>
 * field0,asc,ignoreCase,nullIsFirst|field1,asc,ignoreCase,nullIsFirst
 * </pre>
 *
 * @author Christian Bremer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "comparatorField")
@XmlType(name = "comparatorFieldType", propOrder = {
    "field",
    "asc",
    "ignoreCase",
    "nullIsFirst"
})
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder(value = {
    "field",
    "asc",
    "ignoreCase",
    "nullIsFirst"
})
@ApiModel(
    value = "ComparatorField",
    description = "A comparator field defines how a field of an object is sorted.")
@SuppressWarnings({"UnusedAssignment", "unused"})
public class ComparatorField {

  @XmlElement(name = "field")
  private String field;

  @XmlElement(name = "asc", defaultValue = "true")
  private boolean asc = true;

  @XmlElement(name = "ignoreCase", defaultValue = "true")
  private boolean ignoreCase = true;

  @XmlElement(name = "nullIsFirst", defaultValue = "false")
  private boolean nullIsFirst = false;

  /**
   * Instantiates a new comparator field.
   */
  public ComparatorField() {
    this(null, true, true, false);
  }

  /**
   * Instantiates a new comparator field.
   *
   * @param field       the field name or path (can be {@code null})
   * @param asc         {@code true} for an ascending order, {@code false} for a descending order
   * @param ignoreCase  {@code true} for a case insensitive order,  {@code false} for a case
   *                    sensitive order
   * @param nullIsFirst specifies the order of {@code null} values
   */
  @JsonCreator
  public ComparatorField(
      @JsonProperty("field") String field,
      @JsonProperty(value = "asc", defaultValue = "true") boolean asc,
      @JsonProperty(value = "ignoreCase", defaultValue = "true") boolean ignoreCase,
      @JsonProperty(value = "nullIsFirst", defaultValue = "false") boolean nullIsFirst) {
    this.field = field;
    this.asc = asc;
    this.ignoreCase = ignoreCase;
    this.nullIsFirst = nullIsFirst;
  }

  /**
   * Gets field name or path.
   *
   * @return the field name or path
   */
  public String getField() {
    return field;
  }

  /**
   * Is ascending or descending order.
   *
   * @return {@code true} if ascending order, {@code false} if descending order
   */
  public boolean isAsc() {
    return asc;
  }

  /**
   * Is case insensitive or sensitive order.
   *
   * @return {@code true} if case insensitive order, {@code false} if case sensitive order
   */
  public boolean isIgnoreCase() {
    return ignoreCase;
  }

  /**
   * Is null is first.
   *
   * @return {@code true} if null is first, otherwise {@code false}
   */
  public boolean isNullIsFirst() {
    return nullIsFirst;
  }

  /**
   * Creates the well known text of this field ordering description.
   *
   * <p>
   * The syntax of the field ordering description is
   * <pre>
   * fieldNameOrPath,asc,ignoreCase,nullIsFirst
   * </pre>
   *
   * For example
   * <pre>
   * person.lastName,asc,true,false
   * </pre>
   *
   * @return the well known text
   */
  public String toWkt() {
    return (field != null ? field : "") + ","
        + (asc ? "asc," : "desc,")
        + ignoreCase + ","
        + nullIsFirst;
  }

  @Override
  public String toString() {
    return toWkt();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ComparatorField)) {
      return false;
    }
    ComparatorField that = (ComparatorField) o;
    return asc == that.asc
        && ignoreCase == that.ignoreCase
        && nullIsFirst == that.nullIsFirst
        && Objects.equals(field, that.field);
  }

  @Override
  public int hashCode() {
    return Objects.hash(field, asc, ignoreCase, nullIsFirst);
  }

}
