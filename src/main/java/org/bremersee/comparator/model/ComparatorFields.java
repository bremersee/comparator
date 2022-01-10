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
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import lombok.EqualsAndHashCode;

/**
 * The list of comparator fields.
 *
 * @author Christian Bremer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "comparatorFields")
@XmlType(name = "comparatorFieldsType")
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "A list of comparator fields.")
@EqualsAndHashCode
@Valid
public class ComparatorFields {

  @SuppressWarnings("FieldMayBeFinal")
  @Schema(description = "The list of comparator fields.")
  private List<ComparatorField> fields = new ArrayList<>();

  /**
   * Instantiates a new list of comparator fields.
   */
  public ComparatorFields() {
  }

  /**
   * Instantiates a new list of comparator fields.
   *
   * @param fields the comparator fields
   */
  @JsonCreator
  public ComparatorFields(@JsonProperty("fields") Collection<? extends ComparatorField> fields) {
    if (fields != null) {
      this.fields.addAll(fields);
    }
  }

  /**
   * Gets the list of comparator fields.
   *
   * @return the list of comparator fields
   */
  public List<ComparatorField> getFields() {
    return fields;
  }

  /**
   * Creates the well known text of this list of field ordering descriptions.
   *
   * <p>The syntax of the field ordering description is
   * <pre>
   * fieldNameOrPath0,asc,ignoreCase,nullIsFirst|fieldNameOrPath1,asc,ignoreCase,nullIsFirst
   * </pre>
   *
   * <p>For example
   * <pre>
   * room.number,asc,true,false|person.lastName,asc,true,false|person.firstName,asc,true,false
   * </pre>
   *
   * @return the well known text
   */
  @NotEmpty
  public String toWkt() {
    return toWkt(null);
  }

  /**
   * Creates the well known text of this list of field ordering descriptions.
   *
   * <p>The syntax of the field ordering description is
   * <pre>
   * fieldNameOrPath0,asc,ignoreCase,nullIsFirst|fieldNameOrPath1,asc,ignoreCase,nullIsFirst
   * </pre>
   *
   * <p>For example
   * <pre>
   * room.number,asc,true,false|person.lastName,asc,true,false|person.firstName,asc,true,false
   * </pre>
   *
   * @param properties the properties
   * @return the well known text
   */
  @NotEmpty
  public String toWkt(WellKnownTextProperties properties) {
    WellKnownTextProperties props = Objects.requireNonNullElse(properties, WellKnownTextProperties.defaults());
    return fields.stream()
        .map(comparatorField -> comparatorField.toWkt(props))
        .collect(Collectors.joining(props.getFieldSeparator()));
  }

  @Override
  public String toString() {
    return toWkt();
  }

}
