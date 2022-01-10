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

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import org.bremersee.comparator.model.ImmutableWellKnownTextProperties.Builder;
import org.immutables.value.Value;

/**
 * The well known text properties.
 *
 * @author Christian Bremer
 */
@Value.Immutable
@Valid
public interface WellKnownTextProperties {

  /**
   * Properties builder.
   *
   * @return the builder
   */
  static Builder builder() {
    return ImmutableWellKnownTextProperties.builder();
  }

  /**
   * Defaults well known text properties.
   *
   * @return the well known text properties
   */
  static WellKnownTextProperties defaults() {
    return builder().build();
  }

  /**
   * Gets field separator.
   *
   * @return the field separator
   */
  @Value.Default
  @NotEmpty
  default String getFieldSeparator() {
    return ";";
  }

  /**
   * Gets field args separator.
   *
   * @return the field args separator
   */
  @Value.Default
  @NotEmpty
  default String getFieldArgsSeparator() {
    return ",";
  }

  /**
   * Gets asc value.
   *
   * @return the asc value
   */
  @Value.Default
  @NotEmpty
  default String getAscValue() {
    return "asc";
  }

  /**
   * Gets desc value.
   *
   * @return the desc value
   */
  @Value.Default
  @NotEmpty
  default String getDescValue() {
    return "desc";
  }

  /**
   * Gets direction value.
   *
   * @param asc the asc
   * @return the direction value
   */
  @Value.Derived
  @NotEmpty
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
    return value == null || value.trim().length() == 0 || value.equalsIgnoreCase(getAscValue());
  }

  /**
   * Gets case insensitive value.
   *
   * @return the case insensitive value
   */
  @Value.Default
  @NotEmpty
  default String getCaseInsensitiveValue() {
    return "true";
  }

  /**
   * Gets case sensitive value.
   *
   * @return the case sensitive value
   */
  @Value.Default
  @NotEmpty
  default String getCaseSensitiveValue() {
    return "false";
  }

  /**
   * Gets ignore case value.
   *
   * @param ignoreCase the ignore case
   * @return the ignore case value
   */
  @Value.Derived
  @NotEmpty
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
    return value == null || value.trim().length() == 0 || value.equalsIgnoreCase(
        getCaseInsensitiveValue());
  }

  /**
   * Gets null is last value.
   *
   * @return the null is last value
   */
  @Value.Default
  @NotEmpty
  default String getNullIsLastValue() {
    return "false";
  }

  /**
   * Gets null is first value.
   *
   * @return the null is first value
   */
  @Value.Default
  @NotEmpty
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
  @NotEmpty
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