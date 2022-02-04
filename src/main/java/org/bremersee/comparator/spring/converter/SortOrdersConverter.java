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

import java.util.Objects;
import org.bremersee.comparator.model.SortOrders;
import org.bremersee.comparator.model.SortOrdersTextProperties;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;

/**
 * The sort orders converter.
 *
 * @author Christian Bremer
 */
public class SortOrdersConverter implements Converter<String, SortOrders> {

  private final SortOrdersTextProperties properties;

  /**
   * Instantiates a new sort orders converter.
   */
  public SortOrdersConverter() {
    this(SortOrdersTextProperties.defaults());
  }

  /**
   * Instantiates a new sort orders converter.
   *
   * @param properties the properties
   */
  public SortOrdersConverter(SortOrdersTextProperties properties) {
    this.properties = Objects
        .requireNonNullElse(properties, SortOrdersTextProperties.defaults());
  }

  @Override
  public SortOrders convert(@NonNull String source) {
    return SortOrders.fromSortOrdersText(source, properties);
  }
}
