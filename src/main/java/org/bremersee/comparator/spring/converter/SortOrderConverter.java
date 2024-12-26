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

package org.bremersee.comparator.spring.converter;

import java.util.Optional;
import org.bremersee.comparator.model.SortOrder;
import org.bremersee.comparator.model.SortOrderTextSeparators;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;

/**
 * The sort order converter.
 *
 * @author Christian Bremer
 */
public class SortOrderConverter implements Converter<String, SortOrder> {

  private final SortOrderTextSeparators separators;

  /**
   * Instantiates a new sort order converter.
   */
  public SortOrderConverter() {
    this(SortOrderTextSeparators.defaults());
  }

  /**
   * Instantiates a new sort order converter.
   *
   * @param separators the separators
   */
  public SortOrderConverter(SortOrderTextSeparators separators) {
    this.separators = Optional.ofNullable(separators)
        .orElseGet(SortOrderTextSeparators::defaults);
  }

  @Override
  public SortOrder convert(@NonNull String source) {
    return SortOrder.fromSortOrderText(source, separators);
  }
}
