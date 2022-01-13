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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.list;

import java.util.List;
import org.bremersee.comparator.model.ComparatorField;
import org.bremersee.comparator.model.ComparatorFields;
import org.bremersee.comparator.model.WellKnownTextProperties;
import org.junit.jupiter.api.Test;

/**
 * The comparator fields converter test.
 *
 * @author Christian Bremer
 */
class ComparatorFieldsConverterTest {

  /**
   * Convert.
   */
  @Test
  void convert() {
    ComparatorFieldsConverter converter = new ComparatorFieldsConverter();

    ComparatorFields actual = converter.convert(
        "field0,asc,true,true"
            + ";field1,asc,false,true"
            + ";field2,asc,true,false"
            + ";field3,asc,false,false"
            + ";field4,desc,false,true"
            + ";field5,desc,false,false");

    ComparatorField field0 = new ComparatorField("field0", true, true, true);
    ComparatorField field1 = new ComparatorField("field1", true, false, true);
    ComparatorField field2 = new ComparatorField("field2", true, true, false);
    ComparatorField field3 = new ComparatorField("field3", true, false, false);
    ComparatorField field4 = new ComparatorField("field4", false, false, true);
    ComparatorField field5 = new ComparatorField("field5", false, false, false);
    List<ComparatorField> expected = List.of(field0, field1, field2, field3, field4, field5);

    assertThat(actual)
        .extracting(ComparatorFields::getFields, list(ComparatorField.class))
        .containsExactlyElementsOf(expected);
  }

  /**
   * Convert with properties.
   */
  @Test
  void convertWithProperties() {
    ComparatorFieldsConverter converter = new ComparatorFieldsConverter(
        WellKnownTextProperties.builder()
            .fieldArgsSeparator("-:-")
            .fieldSeparator("&&")
            .caseSensitiveValue("cs")
            .caseInsensitiveValue("cis")
            .nullIsFirstValue("nif")
            .nullIsLastValue("nil")
            .build());

    ComparatorFields actual = converter.convert(
        "-:-asc-:-cis-:-nif"
            + "&&field1"
            + "&&field2-:-desc"
            + "&&field3-:-desc-:-cs"
            + "&&field4-:-desc-:-cs-:-nif"
            + "&&-:-desc"
    );

    ComparatorField field0 = new ComparatorField(null, true, true, true);
    ComparatorField field1 = new ComparatorField("field1", true, true, false);
    ComparatorField field2 = new ComparatorField("field2", false, true, false);
    ComparatorField field3 = new ComparatorField("field3", false, false, false);
    ComparatorField field4 = new ComparatorField("field4", false, false, true);
    ComparatorField field5 = new ComparatorField(null, false, true, false);
    List<ComparatorField> expected = List.of(field0, field1, field2, field3, field4, field5);

    assertThat(actual)
        .extracting(ComparatorFields::getFields, list(ComparatorField.class))
        .containsExactlyElementsOf(expected);
  }

}