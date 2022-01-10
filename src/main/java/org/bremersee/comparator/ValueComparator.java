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
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.bremersee.comparator.model.ComparatorField;

/**
 * The value comparator extracts field value of the specified field name or path and uses the
 * specified description (ascending or descending, case sensitive or insensitive and 'null is
 * first') for sorting.
 *
 * @author Christian Bremer
 */
@ToString
@EqualsAndHashCode
public class ValueComparator implements Comparator<Object> {

  private final ValueExtractor valueExtractor;

  private final String field;

  private final boolean asc;

  private final boolean ignoreCase;

  private final boolean nullIsFirst;

  /**
   * Instantiates a new value comparator.
   *
   * @param comparatorField the comparator field (cannot be {@code null})
   */
  public ValueComparator(ComparatorField comparatorField) {
    this(comparatorField.getField(),
        comparatorField.isAsc(),
        comparatorField.isIgnoreCase(),
        comparatorField.isNullIsFirst(),
        null);
  }

  /**
   * Instantiates a new value comparator.
   *
   * @param comparatorField the comparator field (cannot be {@code null})
   * @param valueExtractor  the value extractor (if it is {@code null}, a default will be used)
   */
  public ValueComparator(ComparatorField comparatorField, ValueExtractor valueExtractor) {
    this(comparatorField.getField(),
        comparatorField.isAsc(),
        comparatorField.isIgnoreCase(),
        comparatorField.isNullIsFirst(),
        valueExtractor);
  }

  /**
   * Instantiates a new value comparator.
   *
   * @param field       the field name or path
   * @param asc         ascending or descending
   * @param ignoreCase  case insensitive or sensitive
   * @param nullIsFirst null is first
   */
  public ValueComparator(
      String field,
      boolean asc,
      boolean ignoreCase,
      boolean nullIsFirst) {
    this(field, asc, ignoreCase, nullIsFirst, null);
  }

  /**
   * Instantiates a new value comparator.
   *
   * @param field          the field name or path
   * @param asc            ascending or descending
   * @param ignoreCase     case insensitive or sensitive
   * @param nullIsFirst    null is first
   * @param valueExtractor a custom value extractor (if it is {@code null}, a default will be used)
   */
  public ValueComparator(
      String field,
      boolean asc,
      boolean ignoreCase,
      boolean nullIsFirst,
      ValueExtractor valueExtractor) {
    this.field = field;
    this.asc = asc;
    this.ignoreCase = ignoreCase;
    this.nullIsFirst = nullIsFirst;
    this.valueExtractor = valueExtractor != null ? valueExtractor : new DefaultValueExtractor();
  }

  @Override
  public int compare(Object o1, Object o2) {
    final Object v1 = valueExtractor.findValue(o1, field);
    final Object v2 = valueExtractor.findValue(o2, field);

    if (v1 == null && v2 == null) {
      return 0;
    }
    if (v1 == null) {
      if (asc) {
        return nullIsFirst ? -1 : 1;
      } else {
        return nullIsFirst ? 1 : -1;
      }
    }
    if (v2 == null) {
      if (asc) {
        return nullIsFirst ? 1 : -1;
      } else {
        return nullIsFirst ? -1 : 1;
      }
    }

    if (asc && v1 instanceof Comparable) {
      if (ignoreCase && v1 instanceof String && v2 instanceof String) {
        return ((String) v1).compareToIgnoreCase((String) v2);
      } else {
        //noinspection unchecked,rawtypes
        return ((Comparable) v1).compareTo(v2);
      }

    } else if (!asc && v2 instanceof Comparable) {

      if (ignoreCase && v1 instanceof String && v2 instanceof String) {
        return ((String) v2).compareToIgnoreCase((String) v1);
      } else {
        //noinspection unchecked,rawtypes
        return ((Comparable) v2).compareTo(v1);
      }
    }

    throw new ComparatorException("Comparison of field '" + field + "' is not possible.");
  }
}
