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

import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.bremersee.comparator.model.SortOrderItem;
import org.bremersee.comparator.model.SortOrderItem.CaseHandling;
import org.bremersee.comparator.model.SortOrderItem.Direction;
import org.bremersee.comparator.model.SortOrderItem.NullHandling;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * The sort order item converter test.
 *
 * @author Christian Bremer
 */
@ExtendWith(SoftAssertionsExtension.class)
class SortOrderItemConverterTest {

  /**
   * Convert.
   *
   * @param softly the softly
   */
  @Test
  void convert(SoftAssertions softly) {
    SortOrderItemConverter converter = new SortOrderItemConverter();
    SortOrderItem actual = converter.convert("field0,asc,sensitive,nulls-first");
    SortOrderItem expected = new SortOrderItem(
        "field0", Direction.ASC, CaseHandling.SENSITIVE, NullHandling.NULLS_FIRST);
    softly
        .assertThat(actual)
        .isEqualTo(expected);
  }

}