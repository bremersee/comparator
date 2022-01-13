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

package org.bremersee.comparator.converter;

import java.util.Objects;
import org.bremersee.comparator.model.ComparatorField;
import org.bremersee.comparator.model.WellKnownTextProperties;
import org.springframework.core.convert.converter.Converter;

/**
 * The comparator field converter.
 *
 * @author Christian Bremer
 */
public class ComparatorFieldConverter implements Converter<String, ComparatorField> {

  private final WellKnownTextProperties properties;

  /**
   * Instantiates a new Comparator field converter.
   */
  public ComparatorFieldConverter() {
    this(WellKnownTextProperties.defaults());
  }

  /**
   * Instantiates a new Comparator field converter.
   *
   * @param properties the properties
   */
  public ComparatorFieldConverter(WellKnownTextProperties properties) {
    this.properties = Objects.requireNonNullElse(properties, WellKnownTextProperties.defaults());
  }

  @Override
  public ComparatorField convert(String fieldDescription) {
    String field;
    boolean asc = properties.isAsc(null);
    boolean ignoreCase = properties.isCaseIgnored(null);
    boolean nullIsFirst = properties.isNullFirst(null);
    String separator = properties.getFieldArgsSeparator();
    int index = fieldDescription.indexOf(separator);
    if (index < 0) {
      field = fieldDescription.trim();
    } else {
      field = fieldDescription.substring(0, index).trim();
      int from = index + separator.length();
      index = fieldDescription.indexOf(separator, from);
      if (index < 0) {
        asc = properties.isAsc(fieldDescription.substring(from).trim());
      } else {
        asc = properties.isAsc(fieldDescription.substring(from, index).trim());
        from = index + separator.length();
        index = fieldDescription.indexOf(separator, from);
        if (index < 0) {
          ignoreCase = properties.isCaseIgnored(fieldDescription.substring(from).trim());
        } else {
          ignoreCase = properties.isCaseIgnored(fieldDescription.substring(from, index).trim());
          from = index + separator.length();
          nullIsFirst = properties.isNullFirst(fieldDescription.substring(from).trim());
        }
      }
    }
    field = field.length() == 0 ? null : field;
    return new ComparatorField(field, asc, ignoreCase, nullIsFirst);
  }
}
