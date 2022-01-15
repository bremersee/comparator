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

package org.bremersee.comparator.spring.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.BOOLEAN;

import java.util.Collections;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.bremersee.comparator.model.SortOrder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.NullHandling;

/**
 * The sort mapper tests.
 *
 * @author Christian Bremer
 */
@ExtendWith(SoftAssertionsExtension.class)
class SortMapperTest {

  /**
   * Test to sort.
   *
   * @param softly the soft assertions
   */
  @Test
  void toSort(SoftAssertions softly) {
    SortOrder sortOrder0 = new SortOrder("f0", true, true, true);
    SortOrder sortOrder1 = new SortOrder("f1", false, false, false);
    List<SortOrder> sortOrders = List.of(sortOrder0, sortOrder1);

    Sort sort = SortMapper.toSort(sortOrders);

    softly.assertThat(sort)
        .isNotNull();

    Sort.Order sortOrder = sort.getOrderFor("f0");
    softly.assertThat(sortOrder)
        .isNotNull()
        .extracting(Sort.Order::isAscending, BOOLEAN)
        .isTrue();
    softly.assertThat(sortOrder)
        .isNotNull()
        .extracting(Sort.Order::isIgnoreCase, BOOLEAN)
        .isTrue();
    softly.assertThat(sortOrder)
        .isNotNull()
        .extracting(Sort.Order::getNullHandling)
        .isEqualTo(NullHandling.NULLS_FIRST);

    sortOrder = sort.getOrderFor("f1");
    softly.assertThat(sortOrder)
        .isNotNull()
        .extracting(Sort.Order::isAscending, BOOLEAN)
        .isFalse();
    softly.assertThat(sortOrder)
        .isNotNull()
        .extracting(Sort.Order::isIgnoreCase, BOOLEAN)
        .isFalse();
    softly.assertThat(sortOrder)
        .isNotNull()
        .extracting(Sort.Order::getNullHandling)
        .isEqualTo(NullHandling.NULLS_LAST);
  }

  /**
   * To sort with empty list.
   */
  @Test
  void toSortWithEmptyList() {
    Sort sort = SortMapper.toSort(Collections.emptyList());
    assertThat(sort.isUnsorted()).isTrue();
  }

  /**
   * Test from sort.
   */
  @Test
  void fromSort() {
    SortOrder sortOrder0 = new SortOrder("f0", true, true, true);
    SortOrder sortOrder1 = new SortOrder("f1", false, false, false);
    List<SortOrder> sortOrders = List.of(sortOrder0, sortOrder1);
    List<SortOrder> actualFields = SortMapper
        .fromSort(SortMapper.toSort(sortOrders));
    assertThat(actualFields)
        .isEqualTo(sortOrders);
  }

  /**
   * From sort with null.
   */
  @Test
  void fromSortWithNull() {
    List<SortOrder> sortOrders = SortMapper.fromSort(null);
    assertThat(sortOrders).isEmpty();
  }

  /**
   * To sort order.
   */
  @Test
  void toSortOrder() {
    assertThat(SortMapper.toSortOrder(null))
        .isNull();
    assertThat(SortMapper.toSortOrder(new SortOrder(null, true, true, true)))
        .isNull();
    assertThat(SortMapper.toSortOrder(new SortOrder("", true, true, true)))
        .isNull();
  }

  /**
   * From sort order.
   */
  @Test
  void fromSortOrder() {
    assertThat(SortMapper.fromSortOrder(null))
        .isNull();
  }

}
