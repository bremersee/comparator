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

package org.bremersee.comparator.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringTokenizer;
import org.bremersee.comparator.model.ComparatorField;
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

  private final Converter<String, ComparatorField> fieldConverter;

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
    this(properties, null);
  }

  public ComparatorFieldsConverter(WellKnownTextProperties properties,
      Converter<String, ComparatorField> fieldConverter) {
    this.properties = Objects
        .requireNonNullElse(properties, WellKnownTextProperties.defaults());
    this.fieldConverter = Objects
        .requireNonNullElse(fieldConverter, new ComparatorFieldConverter(this.properties));
  }

  @Override
  public ComparatorFields convert(String source) {
    List<ComparatorField> fields = new ArrayList<>();
    StringTokenizer tokenizer = new StringTokenizer(source, properties.getFieldSeparator());
    while (tokenizer.hasMoreTokens()) {
      fields.add(fieldConverter.convert(tokenizer.nextToken()));
    }
    return new ComparatorFields(fields);
  }
}
