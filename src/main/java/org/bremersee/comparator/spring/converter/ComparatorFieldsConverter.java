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
import org.bremersee.comparator.model.ComparatorFields;
import org.bremersee.comparator.model.WellKnownTextProperties;
import org.springframework.core.convert.converter.Converter;

/**
 * The comparator fields converter.
 *
 * @author Christian Bremer
 */
public class ComparatorFieldsConverter implements Converter<String, ComparatorFields> {

  private final WellKnownTextProperties properties;

  /**
   * Instantiates a new Comparator fields converter.
   */
  public ComparatorFieldsConverter() {
    this(WellKnownTextProperties.defaults());
  }

  /**
   * Instantiates a new Comparator fields converter.
   *
   * @param properties the properties
   */
  public ComparatorFieldsConverter(WellKnownTextProperties properties) {
    this.properties = Objects
        .requireNonNullElse(properties, WellKnownTextProperties.defaults());
  }

  @Override
  public ComparatorFields convert(String source) {
    return ComparatorFields.fromWkt(source, properties);
  }
}
