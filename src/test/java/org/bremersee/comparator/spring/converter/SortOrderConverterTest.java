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
import org.bremersee.comparator.model.SortOrder;
import org.bremersee.comparator.model.SortOrderItem;
import org.bremersee.comparator.model.SortOrderItem.CaseHandling;
import org.bremersee.comparator.model.SortOrderItem.Direction;
import org.bremersee.comparator.model.SortOrderItem.NullHandling;
import org.junit.jupiter.api.Test;

/**
 * The sort order converter test.
 *
 * @author Christian Bremer
 */
class SortOrderConverterTest {

  /**
   * Convert.
   */
  @Test
  void convert() {
    SortOrderConverter converter = new SortOrderConverter();

    SortOrder actual = converter.convert(
        "field0;asc;sensitive;nulls-first"
            + ",field1;desc;insensitive;nulls-last"
            + ",field2;desc;insensitive;native");

    SortOrderItem sortOrderItem0 = new SortOrderItem(
        "field0", Direction.ASC, CaseHandling.SENSITIVE, NullHandling.NULLS_FIRST);
    SortOrderItem sortOrderItem1 = new SortOrderItem(
        "field1", Direction.DESC, CaseHandling.INSENSITIVE, NullHandling.NULLS_LAST);
    SortOrderItem sortOrderItem2 = new SortOrderItem(
        "field2", Direction.DESC, CaseHandling.INSENSITIVE, NullHandling.NATIVE);
    List<SortOrderItem> expected = List.of(sortOrderItem0, sortOrderItem1, sortOrderItem2);

    assertThat(actual)
        .extracting(SortOrder::getItems, list(SortOrderItem.class))
        .containsExactlyElementsOf(expected);
  }

}