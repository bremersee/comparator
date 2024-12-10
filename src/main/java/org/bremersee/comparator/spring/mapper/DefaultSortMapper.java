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
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import org.bremersee.comparator.model.SortOrder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.NullHandling;

/**
 * This mapper provides methods to transform a {@link SortOrder} into a {@code Sort} object from the
 * Spring framework (spring-data-common) and vice versa.
 *
 * @author Christian Bremer
 */
@Getter
public class DefaultSortMapper implements SortMapper {

  /**
   * Specifies whether null-handling is supported or not. If it is not supported, the mapper maps
   * null-handling to {@code Sort.NullHandling.NATIVE}.
   */
  private boolean nullHandlingSupported;

  /**
   * Specifies how {@code Sort.NullHandling.NATIVE} is mapped. If it is {@code true}, native becomes
   * null is first, otherwise null is last.
   */
  private boolean nativeNullHandlingIsNullIsFirst;

  /**
   * Instantiates a new default sort mapper. The default sort mapper doesn't support null-handling
   * and maps {@code Sort.NullHandling.NATIVE} to null is last.
   */
  public DefaultSortMapper() {
  }

  /**
   * Instantiates a new default sort mapper.
   *
   * @param nullHandlingSupported Specifies whether null-handling is supported or not. If it is
   *     not supported, the mapper maps null-handling to {@code Sort.NullHandling.NATIVE}.
   * @param nativeNullHandlingIsNullIsFirst Specifies how {@code Sort.NullHandling.NATIVE} is
   *     mapped. If it is {@code true}, native becomes null is first, otherwise null is last.
   */
  public DefaultSortMapper(
      boolean nullHandlingSupported,
      boolean nativeNullHandlingIsNullIsFirst) {
    this.nullHandlingSupported = nullHandlingSupported;
    this.nativeNullHandlingIsNullIsFirst = nativeNullHandlingIsNullIsFirst;
  }

  @Override
  public Sort.Order toSortOrder(SortOrder sortOrder) {
    if (sortOrder == null || sortOrder.getField() == null
        || sortOrder.getField().isBlank()) {
      return null;
    }
    Direction direction = sortOrder.isAsc() ? Direction.ASC : Direction.DESC;
    NullHandling nullHandlingHint;
    if (isNullHandlingSupported()) {
      nullHandlingHint = sortOrder.isNullIsFirst()
          ? NullHandling.NULLS_FIRST
          : NullHandling.NULLS_LAST;
    } else {
      nullHandlingHint = NullHandling.NATIVE;
    }

    Sort.Order order = new Sort.Order(direction, sortOrder.getField(), nullHandlingHint);
    return sortOrder.isIgnoreCase() ? order.ignoreCase() : order;
  }

  @Override
  public SortOrder fromSortOrder(Sort.Order sortOrder) {
    if (sortOrder == null) {
      return null;
    }
    boolean nullIsFirst;
    if (NullHandling.NATIVE.equals(sortOrder.getNullHandling())) {
      nullIsFirst = isNativeNullHandlingIsNullIsFirst();
    } else {
      nullIsFirst = NullHandling.NULLS_FIRST.equals(sortOrder.getNullHandling());
    }
    return new SortOrder(sortOrder.getProperty(), sortOrder.isAscending(),
        sortOrder.isIgnoreCase(),
        nullIsFirst);
  }

  @Override
  public Pageable applyDefaults(
      Pageable source,
      Boolean asc,
      Boolean ignoreCase,
      Boolean nullIsFirst,
      String... properties) {

    return isNull(source) ? null : PageRequest.of(
        source.getPageNumber(),
        source.getPageSize(),
        applyDefaults(source.getSort(), asc, ignoreCase, nullIsFirst, properties));
  }

  @Override
  public Sort applyDefaults(
      Sort source,
      Boolean asc,
      Boolean ignoreCase,
      Boolean nullIsFirst,
      String... properties) {

    if (isNull(source)) {
      return Sort.unsorted();
    }
    if (isNull(asc) && isNull(ignoreCase) && isNull(nullIsFirst)) {
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
                .with(newDirection(sortOrder.getDirection(), asc))
                .with(newNullHandling(sortOrder.getNullHandling(), nullIsFirst));
            return withNewCaseHandling(order, sortOrder.isIgnoreCase(), ignoreCase);
          }
          return sortOrder;
        })
        .toList());
  }

  private Direction newDirection(Direction oldDirection, Boolean asc) {
    return Optional.ofNullable(asc)
        .map(isAsc -> isAsc ? Direction.ASC : Direction.DESC)
        .orElse(oldDirection);
  }

  private NullHandling newNullHandling(NullHandling oldNullHandling, Boolean nullIsFirst) {
    return Optional.ofNullable(nullIsFirst)
        .map(isNullIsFirst -> {
          if (isNullHandlingSupported()) {
            return isNullIsFirst ? NullHandling.NULLS_FIRST : NullHandling.NULLS_LAST;
          }
          return NullHandling.NATIVE;
        })
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

}
