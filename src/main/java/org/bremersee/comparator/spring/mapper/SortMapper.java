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

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.bremersee.comparator.model.SortOrder;
import org.bremersee.comparator.model.SortOrders;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * This mapper provides methods to transform a {@link SortOrder} into a {@code Sort} object from the
 * Spring framework (spring-data-common) and vice versa.
 *
 * @author Christian Bremer
 */
public interface SortMapper {

  /**
   * Returns default sort mapper.
   *
   * @return the sort mapper
   */
  static SortMapper defaultSortMapper() {
    return new DefaultSortMapper();
  }

  /**
   * Transforms sort orders into a {@code Sort} object.
   *
   * @param sortOrders the sort orders
   * @return the sort
   */
  @NonNull
  default Sort toSort(@Nullable SortOrders sortOrders) {
    return toSort(sortOrders != null ? sortOrders.getSortOrders() : null);
  }

  /**
   * Transforms the sort order into a {@code Sort} object.
   *
   * @param sortOrders the sort orders
   * @return the sort object
   */
  @NonNull
  default Sort toSort(@Nullable List<? extends SortOrder> sortOrders) {
    List<Sort.Order> orderList = Optional.ofNullable(sortOrders)
        .stream()
        .flatMap(List::stream)
        .map(this::toSortOrder)
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
  @NonNull
  default List<SortOrder> fromSort(@Nullable Sort sort) {
    return Optional.ofNullable(sort)
        .stream()
        .flatMap(Sort::stream)
        .map(this::fromSortOrder)
        .filter(Objects::nonNull)
        .toList();
  }

  /**
   * Transforms the sort order into a {@code Sort.Order} object.
   *
   * @param sortOrder the sort order
   * @return the sort object
   */
  @Nullable
  Sort.Order toSortOrder(@Nullable SortOrder sortOrder);

  /**
   * Transforms a {@code Sort.Order} object into a sort order.
   *
   * @param sortOrder the {@code Sort.Order} object
   * @return the sort order
   */
  @Nullable
  SortOrder fromSortOrder(@Nullable Sort.Order sortOrder);

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
  @Nullable
  default Pageable applyDefaults(
      @Nullable Pageable source,
      @Nullable Boolean asc,
      @Nullable Boolean ignoreCase,
      @Nullable Boolean nullIsFirst,
      @Nullable String... properties) {

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
  Sort applyDefaults(
      @Nullable Sort source,
      @Nullable Boolean asc,
      @Nullable Boolean ignoreCase,
      @Nullable Boolean nullIsFirst,
      @Nullable String... properties);

}
