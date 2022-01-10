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

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.bremersee.comparator.model.ComparatorField;
import org.bremersee.comparator.model.WellKnownTextProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * The comparator field converter test.
 *
 * @author Christian Bremer
 */
@ExtendWith(SoftAssertionsExtension.class)
class ComparatorFieldConverterTest {

  /**
   * Convert.
   */
  @Test
  void convert() {
    ComparatorFieldConverter converter = new ComparatorFieldConverter();
    ComparatorField actual = converter.convert("field0,desc,false,true");
    ComparatorField expected = new ComparatorField("field0", false, false, true);

    assertThat(actual)
        .isEqualTo(expected);
  }

  /**
   * Convert with properties.
   *
   * @param softly the soft assertions
   */
  @Test
  void convertWithProperties(SoftAssertions softly) {
    ComparatorFieldConverter converter = new ComparatorFieldConverter(
        WellKnownTextProperties.builder()
            .fieldArgsSeparator("::")
            .caseSensitiveValue("cs")
            .caseInsensitiveValue("cis")
            .nullIsFirstValue("nif")
            .nullIsLastValue("nil")
            .build());

    ComparatorField actual = converter.convert("::asc::cis::nif");
    ComparatorField expected = new ComparatorField(null, true, true, true);
    softly.assertThat(actual).isEqualTo(expected);

    actual = converter.convert("field1");
    expected = new ComparatorField("field1", true, true, false);
    softly.assertThat(actual).isEqualTo(expected);

    actual = converter.convert("field2::desc");
    expected = new ComparatorField("field2", false, true, false);
    softly.assertThat(actual).isEqualTo(expected);

    actual = converter.convert("field3::desc::cs");
    expected = new ComparatorField("field3", false, false, false);
    softly.assertThat(actual).isEqualTo(expected);

    actual = converter.convert("field4::desc::cs::nif");
    expected = new ComparatorField("field4", false, false, true);
    softly.assertThat(actual).isEqualTo(expected);

    actual = converter.convert("::desc");
    expected = new ComparatorField(null, false, true, false);
    softly.assertThat(actual).isEqualTo(expected);
  }

  /**
   * Convert and expect empty.
   */
  @Test
  void convertAndExpectEmpty() {
    ComparatorFieldConverter converter = new ComparatorFieldConverter(ValueComparator::new);
    //noinspection ConstantConditions
    ComparatorField actual = converter.convert(null);
    assertThat(actual)
        .isEqualTo(new ComparatorField());
  }

}