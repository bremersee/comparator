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
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.StringTokenizer;
import java.util.stream.Collectors;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElementRef;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.XmlType;
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
  @XmlElementRef
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
   * Checks whether the list of sort orders is empty or not.
   *
   * @return {@code true} if the list of sort orders is empty, otherwise {@code false}
   */
  @XmlTransient
  @JsonIgnore
  public boolean isEmpty() {
    return sortOrders.isEmpty();
  }

  /**
   * Checks whether this sort orders contains any entries. If there are entries, this is sorted,
   * otherwise it is unsorted.
   *
   * @return {@code true} if the list of sort orders is not empty (aka sorted), otherwise {@code
   *     false}
   */
  @XmlTransient
  @JsonIgnore
  public boolean isSorted() {
    return !isEmpty();
  }

  /**
   * Checks whether this sort orders contains any entries. If there are no entries, this is
   * unsorted, otherwise it is sorted.
   *
   * @return {@code true} if the list of sort orders is empty (aka unsorted), otherwise {@code
   *     false}
   */
  @XmlTransient
  @JsonIgnore
  public boolean isUnsorted() {
    return !isSorted();
  }

  /**
   * Creates the sort orders text of this ordering descriptions.
   *
   * <p>The syntax of the ordering description is
   * <pre>
   * fieldNameOrPath0,asc,ignoreCase,nullIsFirst;fieldNameOrPath1,asc,ignoreCase,nullIsFirst
   * </pre>
   *
   * <p>For example
   * <pre>
   * room.number,asc,true,false;person.lastName,asc,true,false;person.firstName,asc,true,false
   * </pre>
   *
   * @return the sort orders text
   */
  @NotEmpty
  public String toSortOrdersText() {
    return toSortOrdersText(null);
  }

  /**
   * Creates the sort orders text of this ordering descriptions.
   *
   * <p>The syntax of the ordering description is
   * <pre>
   * fieldNameOrPath0,asc,ignoreCase,nullIsFirst;fieldNameOrPath1,asc,ignoreCase,nullIsFirst
   * </pre>
   *
   * <p>The separators (',') and (';') and the values of {@code direction}, {@code case-handling}
   * and {@code null-handling} depend on the given {@link SortOrdersTextProperties}.
   *
   * <p>For example with default properties:
   * <pre>
   * room.number,asc,true,false;person.lastName,asc,true,false;person.firstName,asc,true,false
   * </pre>
   *
   * @param properties the properties
   * @return the sort orders text
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
   * From sort orders text.
   *
   * @param source the sort orders text
   * @return the sort orders
   */
  public static SortOrders fromSortOrdersText(String source) {
    return fromSortOrdersText(source, SortOrdersTextProperties.defaults());
  }

  /**
   * From sort orders text.
   *
   * @param source the sort orders text
   * @param properties the properties
   * @return the sort orders
   */
  public static SortOrders fromSortOrdersText(String source, SortOrdersTextProperties properties) {
    return Optional.ofNullable(source)
        .map(text -> {
          SortOrdersTextProperties props = Objects
              .requireNonNullElse(properties, SortOrdersTextProperties.defaults());
          List<SortOrder> sortOrders = new ArrayList<>();
          StringTokenizer tokenizer = new StringTokenizer(text, props.getSortOrderSeparator());
          while (tokenizer.hasMoreTokens()) {
            sortOrders.add(SortOrder.fromSortOrderText(tokenizer.nextToken(), props));
          }
          return new SortOrders(sortOrders);
        })
        .orElseGet(SortOrders::new);
  }

  /**
   * Creates new sort orders with the given orders.
   *
   * @param sortOrders the sort orders
   * @return the sort orders
   */
  public static SortOrders by(SortOrder... sortOrders) {
    return Optional.ofNullable(sortOrders)
        .map(so -> new SortOrders(Arrays.asList(so)))
        .orElseGet(SortOrders::new);
  }

}
