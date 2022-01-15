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
import org.bremersee.comparator.model.WellKnownTextProperties;
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

    SortOrder field0 = new SortOrder("field0", true, true, true);
    SortOrder field1 = new SortOrder("field1", true, false, true);
    SortOrder field2 = new SortOrder("field2", true, true, false);
    SortOrder field3 = new SortOrder("field3", true, false, false);
    SortOrder field4 = new SortOrder("field4", false, false, true);
    SortOrder field5 = new SortOrder("field5", false, false, false);
    List<SortOrder> expected = List.of(field0, field1, field2, field3, field4, field5);

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
        WellKnownTextProperties.builder()
            .fieldArgsSeparator("-:-")
            .fieldSeparator("&&")
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

    SortOrder field0 = new SortOrder(null, true, true, true);
    SortOrder field1 = new SortOrder("field1", true, true, false);
    SortOrder field2 = new SortOrder("field2", false, true, false);
    SortOrder field3 = new SortOrder("field3", false, false, false);
    SortOrder field4 = new SortOrder("field4", false, false, true);
    SortOrder field5 = new SortOrder(null, false, true, false);
    List<SortOrder> expected = List.of(field0, field1, field2, field3, field4, field5);

    assertThat(actual)
        .extracting(SortOrders::getSortOrders, list(SortOrder.class))
        .containsExactlyElementsOf(expected);
  }

}