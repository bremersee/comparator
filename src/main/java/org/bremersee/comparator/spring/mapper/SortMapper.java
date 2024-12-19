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

import static java.util.Objects.isNull;
import static org.springframework.util.ObjectUtils.isEmpty;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.bremersee.comparator.model.SortOrder;
import org.bremersee.comparator.model.SortOrderItem;
import org.bremersee.comparator.model.SortOrderItem.CaseHandling;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.NullHandling;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * This mapper provides methods to transform a {@link SortOrderItem} into a {@code Sort} object from
 * the Spring framework (spring-data-common) and vice versa.
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
    return DefaultSortMapper.getInstance();
  }

  /**
   * Transforms sort orders into a {@code Sort} object.
   *
   * @param sortOrders the sort orders
   * @return the sort
   */
  @NonNull
  default Sort toSort(@Nullable Collection<? extends SortOrder> sortOrders) {
    return toSort(SortOrder.by(sortOrders));
  }

  /**
   * Transforms sort order into a {@code Sort} object.
   *
   * @param sortOrder the sort order
   * @return the sort
   */
  @NonNull
  default Sort toSort(@Nullable SortOrder sortOrder) {
    List<Sort.Order> orderList = Stream.ofNullable(sortOrder)
        .map(SortOrder::getItems)
        .flatMap(Collection::stream)
        .filter(Objects::nonNull)
        .map(this::toSortOrder)
        .toList();
    return orderList.isEmpty() ? Sort.unsorted() : Sort.by(orderList);
  }

  /**
   * Transforms the sort order into a {@code Sort.Order} object.
   *
   * @param sortOrderItem the sort order
   * @return the sort object
   */
  @Nullable
  default Sort.Order toSortOrder(@Nullable SortOrderItem sortOrderItem) {
    if (sortOrderItem == null || sortOrderItem.getField() == null) {
      return null;
    }
    Direction direction = sortOrderItem.getDirection().isAscending()
        ? Direction.ASC
        : Direction.DESC;
    NullHandling nullHandling = switch (sortOrderItem.getNullHandling()) {
      case NULLS_FIRST -> NullHandling.NULLS_FIRST;
      case NULLS_LAST -> NullHandling.NULLS_LAST;
      case NATIVE -> NullHandling.NATIVE;
    };
    Sort.Order order = new Sort.Order(direction, sortOrderItem.getField(), nullHandling);
    return sortOrderItem.getCaseHandling().isInsensitive()
        ? order.ignoreCase()
        : order;
  }


  /**
   * Transforms a {@code Sort} object into a sort order.
   *
   * @param sort the {@code Sort} object
   * @return the sort order
   */
  @NonNull
  default SortOrder fromSort(@Nullable Sort sort) {
    List<SortOrderItem> items = Stream.ofNullable(sort)
        .flatMap(Sort::stream)
        .map(this::fromSortOrder)
        .filter(Objects::nonNull)
        .toList();
    return new SortOrder(items);
  }

  /**
   * Transforms a {@code Sort.Order} object into a sort order.
   *
   * @param sortOrder the {@code Sort.Order} object
   * @return the sort order
   */
  @Nullable
  default SortOrderItem fromSortOrder(@Nullable Sort.Order sortOrder) {
    if (sortOrder == null) {
      return null;
    }
    SortOrderItem.Direction direction = sortOrder.getDirection().isAscending()
        ? SortOrderItem.Direction.ASC
        : SortOrderItem.Direction.DESC;
    CaseHandling caseHandling = sortOrder.isIgnoreCase()
        ? CaseHandling.INSENSITIVE
        : CaseHandling.SENSITIVE;
    SortOrderItem.NullHandling nullHandling = switch (sortOrder.getNullHandling()) {
      case NULLS_FIRST -> SortOrderItem.NullHandling.NULLS_FIRST;
      case NULLS_LAST -> SortOrderItem.NullHandling.NULLS_LAST;
      case NATIVE -> SortOrderItem.NullHandling.NATIVE;
    };
    return new SortOrderItem(
        sortOrder.getProperty(),
        direction,
        caseHandling,
        nullHandling);
  }

  /**
   * Apply defaults to page request.
   *
   * @param source the source
   * @param direction the direction
   * @param ignoreCase the ignore case
   * @param nullHandling the null handling
   * @param properties the properties
   * @return the pageable
   */
  @Nullable
  default Pageable applyDefaults(
      @Nullable Pageable source,
      @Nullable Direction direction,
      @Nullable Boolean ignoreCase,
      @Nullable NullHandling nullHandling,
      @Nullable String... properties) {

    return Objects.isNull(source) ? null : PageRequest.of(
        source.getPageNumber(),
        source.getPageSize(),
        applyDefaults(source.getSort(), direction, ignoreCase, nullHandling, properties));
  }

  /**
   * Apply defaults to sort.
   *
   * @param source the source
   * @param direction the direction
   * @param ignoreCase the ignore case
   * @param nullHandling the null handling
   * @param properties the properties
   * @return the sort
   */
  @NonNull
  default Sort applyDefaults(
      @Nullable Sort source,
      @Nullable Direction direction,
      @Nullable Boolean ignoreCase,
      @Nullable NullHandling nullHandling,
      @Nullable String... properties) {

    if (isNull(source)) {
      return Sort.unsorted();
    }
    if (isNull(direction) && isNull(ignoreCase) && isNull(nullHandling)) {
      return source;
    }
    Set<String> names;
    if (isEmpty(properties)) {
      names = source.stream().map(Sort.Order::getProperty).collect(Collectors.toSet());
    } else {
      names = Arrays.stream(properties).collect(Collectors.toSet());
    }
    return Sort.by(source.stream()
        .map(sortOrder -> {
          if (names.contains(sortOrder.getProperty())) {
            Sort.Order order = Sort.Order.by(sortOrder.getProperty())
                .with(newDirection(sortOrder.getDirection(), direction))
                .with(newNullHandling(sortOrder.getNullHandling(), nullHandling));
            return withNewCaseHandling(order, sortOrder.isIgnoreCase(), ignoreCase);
          }
          return sortOrder;
        })
        .toList());
  }

  private Direction newDirection(Direction oldDirection, Direction newDirection) {
    return Optional.ofNullable(newDirection)
        .orElse(oldDirection);
  }

  private NullHandling newNullHandling(NullHandling oldNullHandling, NullHandling newNullHandling) {
    return Optional.ofNullable(newNullHandling)
        .orElse(oldNullHandling);
  }

  private Sort.Order withNewCaseHandling(
      Sort.Order order,
      boolean oldIgnoresCase,
      Boolean newIgnoresCase) {
    //noinspection ConstantConditions
    return Optional.ofNullable(newIgnoresCase)
        .map(ignoreCase -> ignoreCase ? order.ignoreCase() : order)
        .orElseGet(() -> oldIgnoresCase ? order.ignoreCase() : order);
  }

  /**
   * The type Default sort mapper.
   */
  class DefaultSortMapper implements SortMapper {

    private static final SortMapper INSTANCE = new DefaultSortMapper();

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static SortMapper getInstance() {
      return INSTANCE;
    }

    private DefaultSortMapper() {
    }
  }

}
