/*
 * Copyright 2019-2022 the original author or authors.
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
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import lombok.EqualsAndHashCode;

/**
 * This class defines the sort order of a field.
 *
 * <pre>
 *  ---------------------------------------------------------------------------------------------
 * | Attribute    | Description                                                       | Default  |
 * |--------------|-------------------------------------------------------------------|----------|
 * | field        | The field name (or method name) of the object. It can be a path.  | null     |
 * |              | The segments are separated by a dot (.): field0.field1.field2     |          |
 * |              | It can be null. Then the object itself must be comparable.        |          |
 * |--------------|-------------------------------------------------------------------|----------|
 * | asc or desc  | Defines ascending or descending ordering.                         | asc      |
 * |--------------|-------------------------------------------------------------------|----------|
 * | ignoreCase   | Makes a case ignoring comparison (only for strings).              | true     |
 * |--------------|-------------------------------------------------------------------|----------|
 * | nullIsFirst  | Defines the ordering if one of the values is null.                | false    |
 *  ---------------------------------------------------------------------------------------------
 * </pre>
 *
 * <p>These values have a 'well known text' representation. The values are concatenated with comma
 * (,):
 * <pre>
 * fieldNameOrPath,asc,ignoreCase,nullIsFirst
 * </pre>
 *
 * <p>For example:
 * <pre>
 * properties.customSettings.priority,asc,true,false
 * </pre>
 *
 * <p>Defaults can be omitted. This is the same:
 * <pre>
 * properties.customSettings.priority
 * </pre>
 *
 * <p>The building of a chain is done by concatenate the fields with a pipe (|):
 * <pre>
 * field0,asc,ignoreCase,nullIsFirst|field1,asc,ignoreCase,nullIsFirst
 * </pre>
 *
 * @author Christian Bremer
 */
@XmlRootElement(name = "comparatorField")
@XmlAccessorType(XmlAccessType.FIELD)
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
@Schema(description = "A comparator field defines how a field of an object is sorted.")
@SuppressWarnings({"FieldMayBeFinal"})
@EqualsAndHashCode
@Valid
public class ComparatorField {

  @Schema(description = "The field name or path.")
  @XmlElement(name = "field")
  private String field;

  @Schema(description = "Is ascending or descending order.", required = true)
  @XmlElement(name = "asc", defaultValue = "true")
  private boolean asc;

  @Schema(description = "Is case insensitive or sensitive order.", required = true)
  @XmlElement(name = "ignoreCase", defaultValue = "true")
  private boolean ignoreCase;

  @Schema(description = "Is null is first.", required = true)
  @XmlElement(name = "nullIsFirst", defaultValue = "false")
  private boolean nullIsFirst;

  /**
   * Instantiates a new comparator field.
   */
  public ComparatorField() {
    this(null, true, true, false);
  }

  /**
   * Instantiates a new comparator field.
   *
   * @param field the field name or path (can be {@code null})
   * @param asc {@code true} for an ascending order, {@code false} for a descending order
   * @param ignoreCase {@code true} for a case insensitive order,  {@code false} for a case
   *     sensitive order
   * @param nullIsFirst specifies the order of {@code null} values
   */
  @JsonCreator
  public ComparatorField(
      @JsonProperty("field") String field,
      @JsonProperty(value = "asc", required = true) boolean asc,
      @JsonProperty(value = "ignoreCase", required = true) boolean ignoreCase,
      @JsonProperty(value = "nullIsFirst", required = true) boolean nullIsFirst) {
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
   * <p>The syntax of the field ordering description is
   * <pre>
   * fieldNameOrPath,asc,ignoreCase,nullIsFirst
   * </pre>
   *
   * <p>For example
   * <pre>
   * person.lastName,asc,true,false
   * </pre>
   *
   * @return the well known text
   */
  @NotEmpty
  public String toWkt() {
    return toWkt(null);
  }

  /**
   * Creates the well known text of this field ordering description.
   *
   * <p>The syntax of the field ordering description is
   * <pre>
   * fieldNameOrPath,asc,ignoreCase,nullIsFirst
   * </pre>
   *
   * <p>For example
   * <pre>
   * person.lastName,asc,true,false
   * </pre>
   *
   * @param properties the properties (can be {@code null}
   * @return the well known text
   */
  @NotEmpty
  public String toWkt(WellKnownTextProperties properties) {
    WellKnownTextProperties props = Objects.requireNonNullElse(properties,
        WellKnownTextProperties.defaults());
    return (field != null ? field : "") + props.getFieldArgsSeparator()
        + props.getDirectionValue(asc) + props.getFieldArgsSeparator()
        + props.getIgnoreCaseValue(ignoreCase) + props.getFieldArgsSeparator()
        + props.getNullIsFirstValue(nullIsFirst);
  }

  @Override
  public String toString() {
    return toWkt();
  }

  /**
   * From well known text.
   *
   * @param source the well known text
   * @return the comparator field
   */
  public static ComparatorField fromWkt(String source) {
    return fromWkt(source, WellKnownTextProperties.defaults());
  }

  /**
   * From well known text.
   *
   * @param source the well known text
   * @param properties the properties
   * @return the comparator field
   */
  public static ComparatorField fromWkt(String source, WellKnownTextProperties properties) {
    return Optional.ofNullable(source)
        .map(wkt -> {
          WellKnownTextProperties props = Objects
              .requireNonNullElse(properties, WellKnownTextProperties.defaults());
          String field;
          boolean asc = props.isAsc(null);
          boolean ignoreCase = props.isCaseIgnored(null);
          boolean nullIsFirst = props.isNullFirst(null);
          String separator = props.getFieldArgsSeparator();
          int index = wkt.indexOf(separator);
          if (index < 0) {
            field = wkt.trim();
          } else {
            field = wkt.substring(0, index).trim();
            int from = index + separator.length();
            index = wkt.indexOf(separator, from);
            if (index < 0) {
              asc = props.isAsc(wkt.substring(from).trim());
            } else {
              asc = props.isAsc(wkt.substring(from, index).trim());
              from = index + separator.length();
              index = wkt.indexOf(separator, from);
              if (index < 0) {
                ignoreCase = props.isCaseIgnored(wkt.substring(from).trim());
              } else {
                ignoreCase = props.isCaseIgnored(wkt.substring(from, index).trim());
                from = index + separator.length();
                nullIsFirst = props.isNullFirst(wkt.substring(from).trim());
              }
            }
          }
          field = field.length() == 0 ? null : field;
          return new ComparatorField(field, asc, ignoreCase, nullIsFirst);
        })
        .orElseGet(() -> new ComparatorField(null, true, true, false));
  }

}
