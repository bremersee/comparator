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

package org.bremersee.comparator;

import org.bremersee.comparator.model.ComparatorField;
import org.bremersee.comparator.model.WellKnownTextProperties;
import org.springframework.core.convert.converter.Converter;

/**
 * @author Christian Bremer
 */
public class ComparatorFieldConverter implements Converter<String, ComparatorField> {

  private final WellKnownTextParser wellKnownTextParser;

  public ComparatorFieldConverter() {
    this(WellKnownTextParser.newInstance(ValueComparator::new));
  }

  public ComparatorFieldConverter(WellKnownTextProperties properties) {
    this(WellKnownTextParser.newInstance(ValueComparator::new, properties));
  }

  public ComparatorFieldConverter(WellKnownTextParser wellKnownTextParser) {
    this.wellKnownTextParser = wellKnownTextParser != null
        ? wellKnownTextParser
        : ValueComparator::new;
  }

  @Override
  public ComparatorField convert(String source) {
    return wellKnownTextParser.buildComparatorField(source);
  }
}
