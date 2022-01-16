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
import lombok.ToString;

/**
 * The delegating comparator extracts the field value of the specified field name or path and uses
 * the specified comparator for sorting.
 *
 * @author Christian Bremer
 */
@ToString
public class DelegatingComparator implements Comparator<Object> {

  private final ValueExtractor valueExtractor;

  private final String field;

  @SuppressWarnings("rawtypes")
  private final Comparator comparator;

  /**
   * Instantiates a new delegating comparator.
   *
   * @param field the field name or path (can be {@code null})
   * @param comparator the comparator to compare the value of the field
   */
  public DelegatingComparator(String field, Comparator<?> comparator) {
    this(field, null, comparator);
  }

  /**
   * Instantiates a new delegating comparator.
   *
   * @param field the field name or path (can be {@code null})
   * @param valueExtractor a custom value extractor (can be {@code null})
   * @param comparator the comparator to compare the value of the field
   */
  public DelegatingComparator(
      String field,
      ValueExtractor valueExtractor,
      Comparator<?> comparator) {
    if (comparator == null) {
      throw new IllegalArgumentException("Comparator must not be null.");
    }
    this.field = field;
    this.comparator = comparator;
    this.valueExtractor = valueExtractor != null ? valueExtractor : new DefaultValueExtractor();
  }

  @Override
  public int compare(Object o1, Object o2) {
    final Object v1 = valueExtractor.findValue(o1, field);
    final Object v2 = valueExtractor.findValue(o2, field);
    //noinspection unchecked
    return comparator.compare(v1, v2);
  }

}
