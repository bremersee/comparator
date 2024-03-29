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

package org.bremersee.comparator.spring.converter;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.bremersee.comparator.model.SortOrder;
import org.bremersee.comparator.model.SortOrdersTextProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * The sort order converter test.
 *
 * @author Christian Bremer
 */
@ExtendWith(SoftAssertionsExtension.class)
class SortOrderConverterTest {

  /**
   * Convert.
   */
  @Test
  void convert() {
    SortOrderConverter converter = new SortOrderConverter();
    SortOrder actual = converter.convert("field0,desc,false,true");
    SortOrder expected = new SortOrder("field0", false, false, true);

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
    SortOrderConverter converter = new SortOrderConverter(
        SortOrdersTextProperties.builder()
            .sortOrderArgsSeparator("::")
            .caseSensitiveValue("cs")
            .caseInsensitiveValue("cis")
            .nullIsFirstValue("nif")
            .nullIsLastValue("nil")
            .build());

    SortOrder actual = converter.convert("::asc::cis::nif");
    SortOrder expected = new SortOrder(null, true, true, true);
    softly.assertThat(actual).isEqualTo(expected);

    actual = converter.convert("field1");
    expected = new SortOrder("field1", true, true, false);
    softly.assertThat(actual).isEqualTo(expected);

    actual = converter.convert("field2::desc");
    expected = new SortOrder("field2", false, true, false);
    softly.assertThat(actual).isEqualTo(expected);

    actual = converter.convert("field3::desc::cs");
    expected = new SortOrder("field3", false, false, false);
    softly.assertThat(actual).isEqualTo(expected);

    actual = converter.convert("field4::desc::cs::nif");
    expected = new SortOrder("field4", false, false, true);
    softly.assertThat(actual).isEqualTo(expected);

    actual = converter.convert("::desc");
    expected = new SortOrder(null, false, true, false);
    softly.assertThat(actual).isEqualTo(expected);
  }

}