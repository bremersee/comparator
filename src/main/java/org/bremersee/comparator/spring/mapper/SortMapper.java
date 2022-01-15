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

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.bremersee.comparator.model.SortOrder;
import org.springframework.data.domain.Sort;

/**
 * This mapper provides methods to transform a {@link SortOrder} into a {@code Sort} object
 * from the Spring framework (spring-data-common) and vice versa.
 *
 * @author Christian Bremer
 */
public abstract class SortMapper {

  private SortMapper() {
  }

  /**
   * Transforms the sort order into a {@code Sort} object.
   *
   * @param comparatorFields the sort orders
   * @return the sort object
   */
  public static Sort toSort(Collection<? extends SortOrder> comparatorFields) {
    List<Sort.Order> orderList = comparatorFields.stream()
        .map(SortMapper::toSortOrder)
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
    if (orderList.isEmpty()) {
      return Sort.unsorted();
    }
    return Sort.by(orderList);
  }

  /**
   * Transforms a {@code Sort} object into a sort order list.
   *
   * @param sort the {@code Sort} object
   * @return the sort order list
   */
  public static List<SortOrder> fromSort(Sort sort) {
    if (sort == null) {
      return Collections.emptyList();
    }
    return sort.stream()
        .map(SortMapper::fromSortOrder)
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

  /**
   * Transforms the sort order into a {@code Sort.Order} object.
   *
   * @param comparatorField the sort order
   * @return the sort object
   */
  public static Sort.Order toSortOrder(SortOrder comparatorField) {
    if (comparatorField == null || comparatorField.getField() == null
        || comparatorField.getField().trim().length() == 0) {
      return null;
    }
    Sort.Direction direction = comparatorField.isAsc() ? Sort.Direction.ASC : Sort.Direction.DESC;
    Sort.NullHandling nullHandlingHint =
        comparatorField.isNullIsFirst() ? Sort.NullHandling.NULLS_FIRST
            : Sort.NullHandling.NULLS_LAST;
    Sort.Order order = new Sort.Order(direction, comparatorField.getField(), nullHandlingHint);
    if (comparatorField.isIgnoreCase()) {
      return order.ignoreCase();
    }
    return order;
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

}
