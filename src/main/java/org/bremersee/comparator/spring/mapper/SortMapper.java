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

package org.bremersee.comparator.spring.mapper;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.bremersee.comparator.model.SortOrder;
import org.bremersee.comparator.model.SortOrders;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.NullHandling;
import org.springframework.util.ObjectUtils;

/**
 * This mapper provides methods to transform a {@link SortOrder} into a {@code Sort} object from the
 * Spring framework (spring-data-common) and vice versa.
 *
 * @author Christian Bremer
 */
public abstract class SortMapper {

  private SortMapper() {
  }

  /**
   * Transforms sort orders into a {@code Sort} object.
   *
   * @param sortOrders the sort orders
   * @return the sort
   */
  public static Sort toSort(SortOrders sortOrders) {
    return toSort(sortOrders != null ? sortOrders.getSortOrders() : null);
  }

  /**
   * Transforms the sort order into a {@code Sort} object.
   *
   * @param sortOrders the sort orders
   * @return the sort object
   */
  public static Sort toSort(List<? extends SortOrder> sortOrders) {
    List<Sort.Order> orderList = Optional.ofNullable(sortOrders)
        .stream()
        .flatMap(List::stream)
        .map(SortMapper::toSortOrder)
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
    return orderList.isEmpty() ? Sort.unsorted() : Sort.by(orderList);
  }

  /**
   * Transforms a {@code Sort} object into a sort order list.
   *
   * @param sort the {@code Sort} object
   * @return the sort order list
   */
  public static List<SortOrder> fromSort(Sort sort) {
    return Optional.ofNullable(sort)
        .stream()
        .flatMap(Sort::stream)
        .map(SortMapper::fromSortOrder)
        .filter(Objects::nonNull)
        .collect(Collectors.toUnmodifiableList());
  }

  /**
   * Transforms the sort order into a {@code Sort.Order} object.
   *
   * @param sortOrder the sort order
   * @return the sort object
   */
  public static Sort.Order toSortOrder(SortOrder sortOrder) {
    if (sortOrder == null || sortOrder.getField() == null
        || sortOrder.getField().trim().length() == 0) {
      return null;
    }
    Sort.Direction direction = sortOrder.isAsc() ? Sort.Direction.ASC : Sort.Direction.DESC;
    Sort.NullHandling nullHandlingHint =
        sortOrder.isNullIsFirst() ? Sort.NullHandling.NULLS_FIRST
            : Sort.NullHandling.NULLS_LAST;
    Sort.Order order = new Sort.Order(direction, sortOrder.getField(), nullHandlingHint);
    return sortOrder.isIgnoreCase() ? order.ignoreCase() : order;
  }

  /**
   * Transforms a {@code Sort.Order} object into a sort order.
   *
   * @param sortOrder the {@code Sort.Order} object
   * @return the sort order
   */
  public static SortOrder fromSortOrder(Sort.Order sortOrder) {
    //noinspection
    if (sortOrder == null) {
      return null;
    }
    boolean nullIsFirst = Sort.NullHandling.NULLS_FIRST.equals(sortOrder.getNullHandling());
    return new SortOrder(sortOrder.getProperty(), sortOrder.isAscending(),
        sortOrder.isIgnoreCase(),
        nullIsFirst);
  }

  /**
   * Apply defaults to page request.
   *
   * @param source the source
   * @param asc the asc
   * @param ignoreCase the ignore case
   * @param nullIsFirst the null is first
   * @param properties the properties
   * @return the pageable
   */
  public static Pageable applyDefaults(
      Pageable source,
      Boolean asc,
      Boolean ignoreCase,
      Boolean nullIsFirst,
      String... properties) {

    return Objects.isNull(source) ? null : PageRequest.of(
        source.getPageNumber(),
        source.getPageSize(),
        applyDefaults(source.getSort(), asc, ignoreCase, nullIsFirst, properties));
  }

  /**
   * Apply defaults to sort.
   *
   * @param source the source
   * @param asc the asc
   * @param ignoreCase the ignore-case flag
   * @param nullIsFirst the null is first flag
   * @param properties the properties
   * @return the sort
   */
  public static Sort applyDefaults(
      Sort source,
      Boolean asc,
      Boolean ignoreCase,
      Boolean nullIsFirst,
      String... properties) {

    if (Objects.isNull(source)) {
      return Sort.unsorted();
    }
    if (Objects.isNull(asc) && Objects.isNull(ignoreCase) && Objects.isNull(nullIsFirst)) {
      return source;
    }
    Set<String> names;
    if (ObjectUtils.isEmpty(properties)) {
      names = source.stream().map(Sort.Order::getProperty).collect(Collectors.toSet());
    } else {
      names = Arrays.stream(properties).collect(Collectors.toSet());
    }
    return Sort.by(source.stream()
        .map(sortOrder -> {
          if (names.contains(sortOrder.getProperty())) {
            Sort.Order order = Sort.Order.by(sortOrder.getProperty())
                .with(newDirection(sortOrder.getDirection(), asc))
                .with(newNullHandling(sortOrder.getNullHandling(), nullIsFirst));
            return withNewCaseHandling(order, sortOrder.isIgnoreCase(), ignoreCase);
          }
          return sortOrder;
        })
        .collect(Collectors.toList()));
  }

  private static Direction newDirection(Direction oldDirection, Boolean asc) {
    return Optional.ofNullable(asc)
        .map(isAsc -> isAsc ? Direction.ASC : Direction.DESC)
        .orElse(oldDirection);
  }

  private static NullHandling newNullHandling(NullHandling oldNullHandling, Boolean nullIsFirst) {
    return Optional.ofNullable(nullIsFirst)
        .map(isNullIsFirst -> isNullIsFirst ? NullHandling.NULLS_FIRST : NullHandling.NULLS_LAST)
        .orElse(oldNullHandling);
  }

  private static Sort.Order withNewCaseHandling(
      Sort.Order order,
      boolean oldIgnoresCase,
      Boolean newIgnoresCase) {
    //noinspection ConstantConditions
    return Optional.ofNullable(newIgnoresCase)
        .map(ignoreCase -> ignoreCase ? order.ignoreCase() : order)
        .orElseGet(() -> oldIgnoresCase ? order.ignoreCase() : order);
  }

}
