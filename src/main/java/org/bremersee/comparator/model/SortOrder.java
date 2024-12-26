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

import static java.util.Objects.isNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElementRef;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.StringTokenizer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.EqualsAndHashCode;

/**
 * The list of sort orders.
 *
 * @author Christian Bremer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "sortOrder")
@XmlType(name = "sortOrderType")
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "The sort order.")
@EqualsAndHashCode
public class SortOrder {

  /**
   * The constant SEPARATOR.
   */
  public static final String SEPARATOR = ",";

  @Schema(description = "The sort order items.")
  @XmlElementRef
  private final List<SortOrderItem> items = new ArrayList<>();

  /**
   * Instantiates an empty sort order.
   */
  protected SortOrder() {
  }

  /**
   * Instantiates a new unmodifiable sort order.
   *
   * @param sortOrderItems the sort order items
   */
  @JsonCreator
  public SortOrder(@JsonProperty("items") Collection<? extends SortOrderItem> sortOrderItems) {
    if (sortOrderItems != null) {
      this.items.addAll(sortOrderItems);
    }
  }

  /**
   * Gets the unmodifiable list of sort order items.
   *
   * @return the list of sort order items
   */
  public List<SortOrderItem> getItems() {
    return Collections.unmodifiableList(items);
  }

  /**
   * Checks whether the list of items is empty or not.
   *
   * @return {@code true} if the list of items is empty, otherwise {@code false}
   */
  @XmlTransient
  @JsonIgnore
  public boolean isEmpty() {
    return items.isEmpty();
  }

  /**
   * Checks whether this sort order contains any entries. If there are entries, this is sorted,
   * otherwise it is unsorted.
   *
   * @return {@code true} if the list of sort orders is not empty (aka sorted), otherwise
   *     {@code false}
   */
  @XmlTransient
  @JsonIgnore
  public boolean isSorted() {
    return !isEmpty();
  }

  /**
   * Checks whether this sort order contains any entries. If there are no entries, this is unsorted,
   * otherwise it is sorted.
   *
   * @return {@code true} if the list of sort orders is empty (aka unsorted), otherwise
   *     {@code false}
   */
  @XmlTransient
  @JsonIgnore
  public boolean isUnsorted() {
    return !isSorted();
  }

  /**
   * Creates the sort order text of this ordering descriptions.
   *
   * <p>The syntax of the ordering description is
   * <pre>
   * fieldNameOrPath0;direction;case-handling;null-handling,fieldNameOrPath1;direction;case-handling;null-handling
   * </pre>
   *
   * <p>For example
   * <pre>
   * created;desc,person.lastName;asc,person.firstName;asc
   * </pre>
   *
   * @return the sort order text
   */
  @JsonIgnore
  @XmlTransient
  public String getSortOrderText() {
    return getSortOrderText(SortOrderTextSeparators.defaults());
  }

  /**
   * Creates the sort order text of this ordering descriptions.
   *
   * <p>The syntax of the ordering description is
   * <pre>
   * fieldNameOrPath0;direction;case-handling;null-handling,fieldNameOrPath1;direction;case-handling;null-handling
   * </pre>
   *
   * <p>For example
   * <pre>
   * created;desc,person.lastName;asc,person.firstName;asc
   * </pre>
   *
   * @param separators the separators
   * @return the sort order text
   */
  public String getSortOrderText(SortOrderTextSeparators separators) {
    String separator = Optional.ofNullable(separators)
        .orElseGet(SortOrderTextSeparators::defaults)
        .getChainSeparator();
    return items.stream()
        .map(item -> item.getSortOrderText(separators))
        .collect(Collectors.joining(separator));
  }

  @Override
  public String toString() {
    return getSortOrderText();
  }

  /**
   * From sort order text.
   *
   * @param source the sort order text
   * @return the sort order
   */
  public static SortOrder fromSortOrderText(String source) {
    return fromSortOrderText(source, SortOrderTextSeparators.defaults());
  }

  /**
   * From sort order text.
   *
   * @param source the sort order text
   * @param separators the separators
   * @return the sort order
   */
  public static SortOrder fromSortOrderText(String source, SortOrderTextSeparators separators) {
    String separator = Optional.ofNullable(separators)
        .orElseGet(SortOrderTextSeparators::defaults)
        .getChainSeparator();
    return Optional.ofNullable(source)
        .map(text -> {
          List<SortOrderItem> sortOrderItems = new ArrayList<>();
          StringTokenizer tokenizer = new StringTokenizer(text, separator);
          while (tokenizer.hasMoreTokens()) {
            sortOrderItems.add(SortOrderItem
                .fromSortOrderText(tokenizer.nextToken(), separators));
          }
          return new SortOrder(sortOrderItems);
        })
        .orElseGet(SortOrder::new);
  }

  /**
   * Creates new sort order with the given items.
   *
   * @param sortOrderItems the sort orders
   * @return the sort order
   */
  public static SortOrder by(SortOrderItem... sortOrderItems) {
    return Optional.ofNullable(sortOrderItems)
        .map(so -> new SortOrder(Arrays.asList(so)))
        .orElseGet(SortOrder::new);
  }

  /**
   * Combines the given sort orders.
   *
   * @param sortOrders the sort orders
   * @return the combined sort order
   */
  public static SortOrder by(Collection<? extends SortOrder> sortOrders) {
    return by(sortOrders, new SortOrder());
  }

  /**
   * Combines the given sort orders. If they are empty, the default sort order will be used.
   *
   * @param sortOrders the sort orders
   * @param defaultSortOrder the default sort order
   * @return the combined sort order
   */
  public static SortOrder by(
      Collection<? extends SortOrder> sortOrders,
      String defaultSortOrder) {
    return by(sortOrders, SortOrder.fromSortOrderText(defaultSortOrder));
  }

  /**
   * Combines the given sort orders. If they are empty, the default sort order will be used.
   *
   * @param sortOrders the sort orders
   * @param defaultSortOrder the default sort order
   * @return the combined sort order
   */
  public static SortOrder by(
      Collection<? extends SortOrder> sortOrders,
      SortOrder defaultSortOrder) {

    if (isNull(sortOrders) || sortOrders.isEmpty()) {
      return Objects.requireNonNullElseGet(defaultSortOrder, SortOrder::new);
    }
    List<SortOrderItem> items = Stream.of(sortOrders)
        .flatMap(Collection::stream)
        .filter(Objects::nonNull)
        .map(SortOrder::getItems)
        .flatMap(Collection::stream)
        .filter(Objects::nonNull)
        .toList();
    return new SortOrder(items);
  }

}
