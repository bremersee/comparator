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
import org.bremersee.comparator.model.SortOrders;
import org.bremersee.comparator.model.SortOrdersTextProperties;
import org.junit.jupiter.api.Test;

/**
 * The sort orders converter test.
 *
 * @author Christian Bremer
 */
class SortOrdersConverterTest {

  /**
   * Convert.
   */
  @Test
  void convert() {
    SortOrdersConverter converter = new SortOrdersConverter();

    SortOrders actual = converter.convert(
        "field0,asc,true,true"
            + ";field1,asc,false,true"
            + ";field2,asc,true,false"
            + ";field3,asc,false,false"
            + ";field4,desc,false,true"
            + ";field5,desc,false,false");

    SortOrder sortOrder0 = new SortOrder("field0", true, true, true);
    SortOrder sortOrder1 = new SortOrder("field1", true, false, true);
    SortOrder sortOrder2 = new SortOrder("field2", true, true, false);
    SortOrder sortOrder3 = new SortOrder("field3", true, false, false);
    SortOrder sortOrder4 = new SortOrder("field4", false, false, true);
    SortOrder sortOrder5 = new SortOrder("field5", false, false, false);
    List<SortOrder> expected = List.of(sortOrder0, sortOrder1, sortOrder2, sortOrder3, sortOrder4, sortOrder5);

    assertThat(actual)
        .extracting(SortOrders::getSortOrders, list(SortOrder.class))
        .containsExactlyElementsOf(expected);
  }

  /**
   * Convert with properties.
   */
  @Test
  void convertWithProperties() {
    SortOrdersConverter converter = new SortOrdersConverter(
        SortOrdersTextProperties.builder()
            .sortOrderArgsSeparator("-:-")
            .sortOrderSeparator("&&")
            .caseSensitiveValue("cs")
            .caseInsensitiveValue("cis")
            .nullIsFirstValue("nif")
            .nullIsLastValue("nil")
            .build());

    SortOrders actual = converter.convert(
        "-:-asc-:-cis-:-nif"
            + "&&field1"
            + "&&field2-:-desc"
            + "&&field3-:-desc-:-cs"
            + "&&field4-:-desc-:-cs-:-nif"
            + "&&-:-desc"
    );

    SortOrder sortOrder0 = new SortOrder(null, true, true, true);
    SortOrder sortOrder1 = new SortOrder("field1", true, true, false);
    SortOrder sortOrder2 = new SortOrder("field2", false, true, false);
    SortOrder sortOrder3 = new SortOrder("field3", false, false, false);
    SortOrder sortOrder4 = new SortOrder("field4", false, false, true);
    SortOrder sortOrder5 = new SortOrder(null, false, true, false);
    List<SortOrder> expected = List.of(sortOrder0, sortOrder1, sortOrder2, sortOrder3, sortOrder4, sortOrder5);

    assertThat(actual)
        .extracting(SortOrders::getSortOrders, list(SortOrder.class))
        .containsExactlyElementsOf(expected);
  }

}