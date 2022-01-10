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
 * @author Christian Bremer
 */
@Value.Immutable
@Valid
public interface WellKnownTextProperties {

  static Builder builder() {
    return ImmutableWellKnownTextProperties.builder();
  }

  static WellKnownTextProperties defaults() {
    return builder().build();
  }

  @Value.Default
  @NotEmpty
  default String getFieldSeparator() {
    return ";";
  }

  @Value.Default
  @NotEmpty
  default String getFieldArgsSeparator() {
    return ",";
  }

  @Value.Default
  @NotEmpty
  default String getAscValue() {
    return "asc";
  }

  @Value.Default
  @NotEmpty
  default String getDescValue() {
    return "desc";
  }

  @Value.Derived
  @NotEmpty
  default String getDirectionValue(boolean asc) {
    return asc ? getAscValue() : getDescValue();
  }

  @Value.Derived
  default boolean isAsc(String value) {
    return value == null || value.trim().length() == 0 || value.equalsIgnoreCase(getAscValue());
  }

  @Value.Default
  @NotEmpty
  default String getCaseInsensitiveValue() {
    return "true";
  }

  @Value.Default
  @NotEmpty
  default String getCaseSensitiveValue() {
    return "false";
  }

  @Value.Derived
  @NotEmpty
  default String getIgnoreCaseValue(boolean ignoreCase) {
    return ignoreCase ? getCaseInsensitiveValue() : getCaseSensitiveValue();
  }

  @Value.Derived
  default boolean isCaseIgnored(String value) {
    return value == null || value.trim().length() == 0 || value.equalsIgnoreCase(getCaseInsensitiveValue());
  }

  @Value.Default
  @NotEmpty
  default String getNullIsLastValue() {
    return "false";
  }

  @Value.Default
  @NotEmpty
  default String getNullIsFirstValue() {
    return "true";
  }

  @Value.Derived
  @NotEmpty
  default String getNullIsFirstValue(boolean nullIsFirst) {
    return nullIsFirst ? getNullIsFirstValue() : getNullIsLastValue();
  }

  @Value.Derived
  default boolean isNullFirst(String value) {
    return getNullIsFirstValue().equalsIgnoreCase(value);
  }

}
