/*
 * Copyright 2024 the original author or authors.
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

import org.immutables.value.Value;

/**
 * The sort order text separators.
 *
 * @author Christian Bremer
 */
@Value.Immutable
public interface SortOrderTextSeparators {

  /**
   * Gets argument separator.
   *
   * @return the argument separator
   */
  @Value.Default
  default String getArgumentSeparator() {
    return SortOrderItem.SEPARATOR;
  }

  /**
   * Gets chain separator.
   *
   * @return the chain separator
   */
  @Value.Default
  default String getChainSeparator() {
    return SortOrder.SEPARATOR;
  }

  /**
   * Defaults sort order text separators.
   *
   * @return the sort order text separators
   */
  static SortOrderTextSeparators defaults() {
    return builder().build();
  }

  /**
   * Builder immutable sort order text separators . builder.
   *
   * @return the immutable sort order text separators . builder
   */
  static ImmutableSortOrderTextSeparators.Builder builder() {
    return ImmutableSortOrderTextSeparators.builder();
  }

}
