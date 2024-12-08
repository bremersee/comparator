/*
 * Copyright 2022 the original author or authors.
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

import java.util.List;
import org.immutables.value.Value;

/**
 * The sort order(s) text properties.
 *
 * @author Christian Bremer
 */
@Value.Immutable
public interface SortOrdersTextProperties {

  /**
   * Properties builder.
   *
   * @return the builder
   */
  static ImmutableSortOrdersTextProperties.Builder builder() {
    return ImmutableSortOrdersTextProperties.builder();
  }

  /**
   * Defaults sort order(s) text properties.
   *
   * @return the sort order(s) text properties
   */
  static SortOrdersTextProperties defaults() {
    return builder().build();
  }

  /**
   * Gets sort order separator.
   *
   * @return the sort order separator
   */
  @Value.Default
  default String getSortOrderSeparator() {
    return ";";
  }

  /**
   * Gets sort order args separator.
   *
   * @return the sort order args separator
   */
  @Value.Default
  default String getSortOrderArgsSeparator() {
    return ",";
  }

  /**
   * Gets asc value.
   *
   * @return the asc value
   */
  @Value.Derived
  default String getAscValue() {
    return getAscValues().stream().findFirst().orElse("asc");
  }

  /**
   * Gets asc values.
   *
   * @return the asc values
   */
  @Value.Default
  default List<String> getAscValues() {
    return List.of("asc", "true", "on", "1");
  }

  /**
   * Gets desc value.
   *
   * @return the desc value
   */
  @Value.Default
  default String getDescValue() {
    return getDescValues().stream().findFirst().orElse("desc");
  }

  /**
   * Gets desc values.
   *
   * @return the desc values
   */
  @Value.Default
  default List<String> getDescValues() {
    return List.of("desc", "false", "off", "0");
  }

  /**
   * Gets direction value.
   *
   * @param asc the asc
   * @return the direction value
   */
  @Value.Derived
  default String getDirectionValue(boolean asc) {
    return asc ? getAscValue() : getDescValue();
  }

  /**
   * Is asc boolean.
   *
   * @param value the value
   * @return the boolean
   */
  @Value.Derived
  default boolean isAsc(String value) {
    return value == null || value.trim().isEmpty()
        || !getDescValues().contains(value.toLowerCase());
  }

  /**
   * Gets case-insensitive value.
   *
   * @return the case-insensitive value
   */
  @Value.Derived
  default String getCaseInsensitiveValue() {
    return getCaseInsensitiveValues().stream().findFirst().orElse("true");
  }

  /**
   * Gets case-insensitive values.
   *
   * @return the case-insensitive values
   */
  @Value.Default
  default List<String> getCaseInsensitiveValues() {
    return List.of("true", "1", "on", "i", "insensitive");
  }

  /**
   * Gets case-sensitive value.
   *
   * @return the case-sensitive value
   */
  @Value.Derived
  default String getCaseSensitiveValue() {
    return getCaseSensitiveValues().stream().findFirst().orElse("false");
  }

  /**
   * Gets case-sensitive values.
   *
   * @return the case-sensitive values
   */
  @Value.Default
  default List<String> getCaseSensitiveValues() {
    return List.of("false", "0", "off", "s", "sensitive");
  }

  /**
   * Gets ignore case value.
   *
   * @param ignoreCase the ignore case
   * @return the ignore case value
   */
  @Value.Derived
  default String getIgnoreCaseValue(boolean ignoreCase) {
    return ignoreCase ? getCaseInsensitiveValue() : getCaseSensitiveValue();
  }

  /**
   * Is case ignored boolean.
   *
   * @param value the value
   * @return the boolean
   */
  @Value.Derived
  default boolean isCaseIgnored(String value) {
    return value == null || value.trim().isEmpty()
        || !getCaseSensitiveValues().contains(value.toLowerCase());
  }

  /**
   * Gets null is last value.
   *
   * @return the null is last value
   */
  @Value.Default
  default String getNullIsLastValue() {
    return "false";
  }

  /**
   * Gets null is first value.
   *
   * @return the null is first value
   */
  @Value.Default
  default String getNullIsFirstValue() {
    return "true";
  }

  /**
   * Gets null is first value.
   *
   * @param nullIsFirst the null is first
   * @return the null is first value
   */
  @Value.Derived
  default String getNullIsFirstValue(boolean nullIsFirst) {
    return nullIsFirst ? getNullIsFirstValue() : getNullIsLastValue();
  }

  /**
   * Is null first boolean.
   *
   * @param value the value
   * @return the boolean
   */
  @Value.Derived
  default boolean isNullFirst(String value) {
    return getNullIsFirstValue().equalsIgnoreCase(value);
  }

}
