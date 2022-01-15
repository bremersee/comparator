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
import java.util.Collections;
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

  @Schema(description = "The list of sort orders.")
  private final List<SortOrder> sortOrders = new ArrayList<>();

  /**
   * Instantiates an empty list of sort orders.
   */
  protected SortOrders() {
  }

  /**
   * Instantiates a new unmodifiable list of sort orders.
   *
   * @param sortOrders the sort orders
   */
  @JsonCreator
  public SortOrders(@JsonProperty("sortOrders") Collection<? extends SortOrder> sortOrders) {
    if (sortOrders != null) {
      this.sortOrders.addAll(sortOrders);
    }
  }

  /**
   * Gets the unmodifiable list of sort orders.
   *
   * @return the list of sort orders
   */
  public List<SortOrder> getSortOrders() {
    return Collections.unmodifiableList(sortOrders);
  }

  /**
   * Creates the well known text of this list of field ordering descriptions.
   *
   * <p>The syntax of the field ordering description is
   * <pre>
   * fieldNameOrPath0,asc,ignoreCase,nullIsFirst;fieldNameOrPath1,asc,ignoreCase,nullIsFirst
   * </pre>
   *
   * <p>For example
   * <pre>
   * room.number,asc,true,false;person.lastName,asc,true,false;person.firstName,asc,true,false
   * </pre>
   *
   * @return the well known text
   */
  @NotEmpty
  public String toSortOrdersText() {
    return toSortOrdersText(null);
  }

  /**
   * Creates the well known text of this list of field ordering descriptions.
   *
   * <p>The syntax of the field ordering description is
   * <pre>
   * fieldNameOrPath0,asc,ignoreCase,nullIsFirst;fieldNameOrPath1,asc,ignoreCase,nullIsFirst
   * </pre>
   *
   * <p>For example
   * <pre>
   * room.number,asc,true,false;person.lastName,asc,true,false;person.firstName,asc,true,false
   * </pre>
   *
   * @param properties the properties
   * @return the well known text
   */
  @NotEmpty
  public String toSortOrdersText(SortOrdersTextProperties properties) {
    SortOrdersTextProperties props = Objects.requireNonNullElse(properties,
        SortOrdersTextProperties.defaults());
    return sortOrders.stream()
        .map(sortOrder -> sortOrder.toSortOrderText(props))
        .collect(Collectors.joining(props.getSortOrderSeparator()));
  }

  @Override
  public String toString() {
    return toSortOrdersText();
  }

  /**
   * From well known text.
   *
   * @param source the well known text
   * @return the sort orders
   */
  public static SortOrders fromSortOrdersText(String source) {
    return fromSortOrdersText(source, SortOrdersTextProperties.defaults());
  }

  /**
   * From well known text.
   *
   * @param source the well known text
   * @param properties the properties
   * @return the sort orders
   */
  public static SortOrders fromSortOrdersText(String source, SortOrdersTextProperties properties) {
    return Optional.ofNullable(source)
        .map(wkt -> {
          SortOrdersTextProperties props = Objects
              .requireNonNullElse(properties, SortOrdersTextProperties.defaults());
          List<SortOrder> sortOrders = new ArrayList<>();
          StringTokenizer tokenizer = new StringTokenizer(wkt, props.getSortOrderSeparator());
          while (tokenizer.hasMoreTokens()) {
            sortOrders.add(SortOrder.fromSortOrderText(tokenizer.nextToken(), props));
          }
          return new SortOrders(sortOrders);
        })
        .orElseGet(SortOrders::new);
  }

}
