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

package org.bremersee.comparator;

import java.util.Comparator;
import java.util.Objects;
import lombok.ToString;
import org.bremersee.comparator.model.SortOrderItem;

/**
 * The value comparator extracts field value of the specified field name or path and uses the
 * specified description (ascending or descending, case-sensitive or insensitive and
 * 'null-handling') for sorting.
 *
 * @author Christian Bremer
 */
@ToString
public class ValueComparator implements Comparator<Object> {

  private final ValueExtractor valueExtractor;

  private final SortOrderItem sortOrder;

  /**
   * Instantiates a new value comparator.
   *
   * @param sortOrder the sort order
   */
  public ValueComparator(SortOrderItem sortOrder) {
    this(sortOrder, null);
  }

  /**
   * Instantiates a new value comparator.
   *
   * @param sortOrder the sort order
   * @param valueExtractor a custom value extractor (if it is {@code null}, a default will be
   *     used)
   */
  public ValueComparator(
      SortOrderItem sortOrder,
      ValueExtractor valueExtractor) {
    this.sortOrder = Objects.requireNonNullElseGet(sortOrder, () -> SortOrderItem.by(null));
    this.valueExtractor = valueExtractor != null ? valueExtractor : new DefaultValueExtractor();
  }

  @Override
  public int compare(Object o1, Object o2) {
    final Object v1 = valueExtractor.findValue(o1, sortOrder.getField());
    final Object v2 = valueExtractor.findValue(o2, sortOrder.getField());

    if (v1 == null && v2 == null) {
      return 0;
    }
    if (v1 == null) {
      if (sortOrder.getDirection().isAscending()) {
        return sortOrder.getNullHandling().isNullFirst() ? -1 : 1;
      } else {
        return sortOrder.getNullHandling().isNullFirst() ? 1 : -1;
      }
    }
    if (v2 == null) {
      if (sortOrder.getDirection().isAscending()) {
        return sortOrder.getNullHandling().isNullFirst() ? 1 : -1;
      } else {
        return sortOrder.getNullHandling().isNullFirst() ? -1 : 1;
      }
    }

    if (sortOrder.getDirection().isAscending() && v1 instanceof Comparable) {
      if (sortOrder.getCaseHandling().isInsensitive()
          && v1 instanceof String && v2 instanceof String) {
        return ((String) v1).compareToIgnoreCase((String) v2);
      } else {
        //noinspection unchecked,rawtypes
        return ((Comparable) v1).compareTo(v2);
      }

    } else if (!sortOrder.getDirection().isAscending() && v2 instanceof Comparable) {

      if (sortOrder.getCaseHandling().isInsensitive()
          && v1 instanceof String && v2 instanceof String) {
        return ((String) v2).compareToIgnoreCase((String) v1);
      } else {
        //noinspection unchecked,rawtypes
        return ((Comparable) v2).compareTo(v1);
      }
    }

    throw new ComparatorException(
        "Comparison of field '" + sortOrder.getField() + "' is not possible.");
  }

}
