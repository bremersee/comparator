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

import org.bremersee.comparator.model.ComparatorFields;
import org.bremersee.comparator.model.WellKnownTextProperties;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

/**
 * The comparator fields converter.
 *
 * @author Christian Bremer
 */
public class ComparatorFieldsConverter implements Converter<String, ComparatorFields> {

  private final WellKnownTextParser wellKnownTextParser;

  /**
   * Instantiates a new Comparator fields converter.
   */
  public ComparatorFieldsConverter() {
    this(WellKnownTextParser.newInstance(ValueComparator::new));
  }

  /**
   * Instantiates a new Comparator fields converter.
   *
   * @param properties the properties
   */
  public ComparatorFieldsConverter(WellKnownTextProperties properties) {
    this(WellKnownTextParser.newInstance(ValueComparator::new, properties));
  }

  /**
   * Instantiates a new Comparator fields converter.
   *
   * @param wellKnownTextParser the well known text parser
   */
  public ComparatorFieldsConverter(WellKnownTextParser wellKnownTextParser) {
    this.wellKnownTextParser = wellKnownTextParser != null
        ? wellKnownTextParser
        : ValueComparator::new;
  }

  @Override
  public ComparatorFields convert(String source) {
    if (!StringUtils.hasText(source)) {
      return new ComparatorFields();
    }
    return new ComparatorFields(wellKnownTextParser.buildComparatorFields(source));
  }
}
