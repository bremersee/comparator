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
import java.util.Optional;
import java.util.StringTokenizer;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import lombok.EqualsAndHashCode;

/**
 * The list of sort orders.
 *
 * @author Christian Bremer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "sortOrders")
@XmlType(name = "sortOrdersType")
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "A list of sort orders.")
@EqualsAndHashCode
@Valid
public class SortOrders {

  @SuppressWarnings("FieldMayBeFinal")
  @Schema(description = "The list of sort orders.")
  private List<SortOrder> sortOrders = new ArrayList<>();

  /**
   * Instantiates a new list of sort orders.
   */
  public SortOrders() {
  }

  /**
   * Instantiates a new list of sort orders.
   *
   * @param sortOrders the sort orders
   */
  @JsonCreator
  public SortOrders(@JsonProperty("fields") Collection<? extends SortOrder> sortOrders) {
    if (sortOrders != null) {
      this.sortOrders.addAll(sortOrders);
    }
  }

  /**
   * Gets the list of sort orders.
   *
   * @return the list of sort orders
   */
  public List<SortOrder> getSortOrders() {
    return sortOrders;
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
    WellKnownTextProperties props = Objects.requireNonNullElse(properties,
        WellKnownTextProperties.defaults());
    return sortOrders.stream()
        .map(sortOrder -> sortOrder.toWkt(props))
        .collect(Collectors.joining(props.getFieldSeparator()));
  }

  @Override
  public String toString() {
    return toWkt();
  }

  /**
   * From well known text.
   *
   * @param source the well known text
   * @return the sort orders
   */
  public static SortOrders fromWkt(String source) {
    return fromWkt(source, WellKnownTextProperties.defaults());
  }

  /**
   * From well known text.
   *
   * @param source the well known text
   * @param properties the properties
   * @return the sort orders
   */
  public static SortOrders fromWkt(String source, WellKnownTextProperties properties) {
    return Optional.ofNullable(source)
        .map(wkt -> {
          WellKnownTextProperties props = Objects
              .requireNonNullElse(properties, WellKnownTextProperties.defaults());
          List<SortOrder> fields = new ArrayList<>();
          StringTokenizer tokenizer = new StringTokenizer(wkt, props.getFieldSeparator());
          while (tokenizer.hasMoreTokens()) {
            fields.add(SortOrder.fromWkt(tokenizer.nextToken(), props));
          }
          return new SortOrders(fields);
        })
        .orElseGet(SortOrders::new);
  }

}