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

package org.bremersee.comparator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.bremersee.comparator.model.SortOrderItem;
import org.bremersee.comparator.model.SortOrderItem.CaseHandling;
import org.bremersee.comparator.model.SortOrderItem.Direction;
import org.bremersee.comparator.model.SortOrderItem.NullHandling;
import org.junit.jupiter.api.Test;

/**
 * The value comparator tests.
 *
 * @author Christian Bremer
 */
class ValueComparatorTest {

  /**
   * Test with non-comparable values and expect comparator exception.
   */
  @Test
  void testWithNonComparableValuesAndExpectComparatorException() {
    assertThatExceptionOfType(ComparatorException.class).isThrownBy(() -> {
      ValueExtractor valueExtractor = mock(ValueExtractor.class);
      when(valueExtractor.findValue(any(), anyString())).thenReturn(new Object());
      //noinspection EqualsWithItself,ResultOfMethodCallIgnored
      new ValueComparator(SortOrderItem.by("someField"), valueExtractor)
          .compare(new Object(), new Object());
    });
  }

  /**
   * Test with two null values.
   */
  @Test
  void testWithTwoNullValues() {
    ValueExtractor valueExtractor = mock(ValueExtractor.class);
    when(valueExtractor.findValue(any(), anyString())).thenReturn(null);

    //noinspection EqualsWithItself
    int result = new ValueComparator(SortOrderItem.by("someField"), valueExtractor)
        .compare(new Object(), new Object());

    assertThat(result)
        .isEqualTo(0);
    verify(valueExtractor, times(2)).findValue(any(), anyString());
  }

  /**
   * Test with first is null value and asc and null is first.
   */
  @Test
  void testWithFirstIsNullValueAndAscAndNullIsFirst() {
    ValueExtractor valueExtractor = mock(ValueExtractor.class);
    when(valueExtractor.findValue(anyString(), anyString())).thenReturn(null);
    when(valueExtractor.findValue(anyInt(), anyString())).thenReturn(1);

    SortOrderItem orderItem = SortOrderItem.by("someField")
        .with(Direction.ASC)
        .with(NullHandling.NULLS_FIRST);
    int result = new ValueComparator(orderItem, valueExtractor)
        .compare("null", 1);

    assertThat(result)
        .isLessThan(0);
    verify(valueExtractor, times(1)).findValue(anyString(), anyString());
    verify(valueExtractor, times(1)).findValue(anyInt(), anyString());
  }

  /**
   * Test with first is null value and desc and null is first.
   */
  @Test
  void testWithFirstIsNullValueAndDescAndNullIsFirst() {
    ValueExtractor valueExtractor = mock(ValueExtractor.class);
    when(valueExtractor.findValue(anyString(), anyString())).thenReturn(null);
    when(valueExtractor.findValue(anyInt(), anyString())).thenReturn(1);

    SortOrderItem orderItem = SortOrderItem.by("someField")
        .with(Direction.DESC)
        .with(NullHandling.NULLS_FIRST);
    int result = new ValueComparator(orderItem, valueExtractor)
        .compare("null", 1);

    assertThat(result)
        .isGreaterThan(0);
    verify(valueExtractor, times(1)).findValue(anyString(), anyString());
    verify(valueExtractor, times(1)).findValue(anyInt(), anyString());
  }

  /**
   * Test with first is null value and asc and null is last.
   */
  @Test
  void testWithFirstIsNullValueAndAscAndNullIsLast() {
    ValueExtractor valueExtractor = mock(ValueExtractor.class);
    when(valueExtractor.findValue(anyString(), anyString())).thenReturn(null);
    when(valueExtractor.findValue(anyInt(), anyString())).thenReturn(1);

    SortOrderItem orderItem = SortOrderItem.by("someField")
        .with(Direction.ASC)
        .with(NullHandling.NULLS_LAST);
    int result = new ValueComparator(orderItem, valueExtractor)
        .compare("null", 1);

    assertThat(result)
        .isGreaterThan(0);
    verify(valueExtractor, times(1)).findValue(anyString(), anyString());
    verify(valueExtractor, times(1)).findValue(anyInt(), anyString());
  }

  /**
   * Test with first is null value and desc and null is last.
   */
  @Test
  void testWithFirstIsNullValueAndDescAndNullIsLast() {
    ValueExtractor valueExtractor = mock(ValueExtractor.class);
    when(valueExtractor.findValue(anyString(), anyString())).thenReturn(null);
    when(valueExtractor.findValue(anyInt(), anyString())).thenReturn(1);

    SortOrderItem orderItem = SortOrderItem.by("someField")
        .with(Direction.DESC)
        .with(NullHandling.NULLS_LAST);
    int result = new ValueComparator(orderItem, valueExtractor)
        .compare("null", 1);

    assertThat(result)
        .isLessThan(0);
    verify(valueExtractor, times(1)).findValue(anyString(), anyString());
    verify(valueExtractor, times(1)).findValue(anyInt(), anyString());
  }

  /**
   * Test with second is null value and asc and null is first.
   */
  @Test
  void testWithSecondIsNullValueAndAscAndNullIsFirst() {
    ValueExtractor valueExtractor = mock(ValueExtractor.class);
    when(valueExtractor.findValue(anyString(), anyString())).thenReturn(null);
    when(valueExtractor.findValue(anyInt(), anyString())).thenReturn(1);

    SortOrderItem orderItem = SortOrderItem.by("someField")
        .with(Direction.ASC)
        .with(NullHandling.NULLS_FIRST);
    int result = new ValueComparator(orderItem, valueExtractor)
        .compare(1, "null");

    assertThat(result)
        .isGreaterThan(0);
    verify(valueExtractor, times(1)).findValue(anyString(), anyString());
    verify(valueExtractor, times(1)).findValue(anyInt(), anyString());
  }

  /**
   * Test with second is null value and desc and null is first.
   */
  @Test
  void testWithSecondIsNullValueAndDescAndNullIsFirst() {
    ValueExtractor valueExtractor = mock(ValueExtractor.class);
    when(valueExtractor.findValue(anyString(), anyString())).thenReturn(null);
    when(valueExtractor.findValue(anyInt(), anyString())).thenReturn(1);

    SortOrderItem orderItem = SortOrderItem.by("someField")
        .with(Direction.DESC)
        .with(NullHandling.NULLS_FIRST);
    int result = new ValueComparator(orderItem, valueExtractor)
        .compare(1, "null");

    assertThat(result)
        .isLessThan(0);
    verify(valueExtractor, times(1)).findValue(anyString(), anyString());
    verify(valueExtractor, times(1)).findValue(anyInt(), anyString());
  }

  /**
   * Test with second is null value and asc and null is last.
   */
  @Test
  void testWithSecondIsNullValueAndAscAndNullIsLast() {
    ValueExtractor valueExtractor = mock(ValueExtractor.class);
    when(valueExtractor.findValue(anyString(), anyString())).thenReturn(null);
    when(valueExtractor.findValue(anyInt(), anyString())).thenReturn(1);

    SortOrderItem orderItem = SortOrderItem.by("someField")
        .with(Direction.ASC)
        .with(NullHandling.NULLS_LAST);
    int result = new ValueComparator(orderItem, valueExtractor)
        .compare(1, "null");

    assertThat(result)
        .isLessThan(0);
    verify(valueExtractor, times(1)).findValue(anyString(), anyString());
    verify(valueExtractor, times(1)).findValue(anyInt(), anyString());
  }

  /**
   * Test with second is null value and desc and null is last.
   */
  @Test
  void testWithSecondIsNullValueAndDescAndNullIsLast() {
    ValueExtractor valueExtractor = mock(ValueExtractor.class);
    when(valueExtractor.findValue(anyString(), anyString())).thenReturn(null);
    when(valueExtractor.findValue(anyInt(), anyString())).thenReturn(1);

    SortOrderItem orderItem = SortOrderItem.by("someField")
        .with(Direction.DESC)
        .with(NullHandling.NULLS_LAST);
    int result = new ValueComparator(orderItem, valueExtractor)
        .compare(1, "null");

    assertThat(result)
        .isGreaterThan(0);
    verify(valueExtractor, times(1)).findValue(anyString(), anyString());
    verify(valueExtractor, times(1)).findValue(anyInt(), anyString());
  }

  /**
   * Test with 1 and 1.
   */
  @Test
  void testWith_1_And_1() {
    ValueExtractor valueExtractor = mock(ValueExtractor.class);
    when(valueExtractor.findValue(anyString(), anyString())).thenReturn(1);

    SortOrderItem orderItem = SortOrderItem.by("someField");
    //noinspection EqualsWithItself
    int result = new ValueComparator(orderItem, valueExtractor)
        .compare("A", "A");

    assertThat(result)
        .isEqualTo(0);
    verify(valueExtractor, times(2)).findValue(anyString(), anyString());
  }

  /**
   * Test with 1 and 2 and asc.
   */
  @Test
  void testWith_1_And_2_And_Asc() {
    ValueExtractor valueExtractor = mock(ValueExtractor.class);
    when(valueExtractor.findValue(anyString(), anyString())).thenReturn(1);
    when(valueExtractor.findValue(anyInt(), anyString())).thenReturn(2);

    SortOrderItem orderItem = SortOrderItem.by("someField");
    int result = new ValueComparator(orderItem, valueExtractor)
        .compare("A", 8);

    assertThat(result)
        .isLessThan(0);
    verify(valueExtractor, times(1)).findValue(anyString(), anyString());
    verify(valueExtractor, times(1)).findValue(anyInt(), anyString());
  }

  /**
   * Test with 1 and 2 and desc.
   */
  @Test
  void testWith_1_And_2_And_Desc() {
    ValueExtractor valueExtractor = mock(ValueExtractor.class);
    when(valueExtractor.findValue(anyString(), anyString())).thenReturn(1);
    when(valueExtractor.findValue(anyInt(), anyString())).thenReturn(2);

    SortOrderItem orderItem = SortOrderItem.by("someField")
        .with(Direction.DESC);
    int result = new ValueComparator(orderItem, valueExtractor)
        .compare("A", 8);

    assertThat(result)
        .isGreaterThan(0);
    verify(valueExtractor, times(1)).findValue(anyString(), anyString());
    verify(valueExtractor, times(1)).findValue(anyInt(), anyString());
  }

  /**
   * Test with 2 and 1 and asc.
   */
  @Test
  void testWith_2_And_1_And_Asc() {
    ValueExtractor valueExtractor = mock(ValueExtractor.class);
    when(valueExtractor.findValue(anyString(), anyString())).thenReturn(1);
    when(valueExtractor.findValue(anyInt(), anyString())).thenReturn(2);

    SortOrderItem orderItem = SortOrderItem.by("someField")
        .with(Direction.ASC);
    int result = new ValueComparator(orderItem, valueExtractor)
        .compare(8, "A");

    assertThat(result)
        .isGreaterThan(0);
    verify(valueExtractor, times(1)).findValue(anyString(), anyString());
    verify(valueExtractor, times(1)).findValue(anyInt(), anyString());
  }

  /**
   * Test with 2 and 1 and desc.
   */
  @Test
  void testWith_2_And_1_And_Desc() {
    ValueExtractor valueExtractor = mock(ValueExtractor.class);
    when(valueExtractor.findValue(anyString(), anyString())).thenReturn(1);
    when(valueExtractor.findValue(anyInt(), anyString())).thenReturn(2);

    SortOrderItem orderItem = SortOrderItem.by("someField")
        .with(Direction.DESC);
    int result = new ValueComparator(orderItem, valueExtractor)
        .compare(8, "A");

    assertThat(result)
        .isLessThan(0);
    verify(valueExtractor, times(1)).findValue(anyString(), anyString());
    verify(valueExtractor, times(1)).findValue(anyInt(), anyString());
  }

  /**
   * Test with a and a.
   */
  @Test
  void testWith_A_And_A() {
    ValueExtractor valueExtractor = mock(ValueExtractor.class);
    when(valueExtractor.findValue(anyString(), anyString())).thenReturn("a");

    SortOrderItem orderItem = SortOrderItem.by("someField");
    //noinspection EqualsWithItself
    int result = new ValueComparator(orderItem, valueExtractor)
        .compare("A", "A");

    assertThat(result)
        .isEqualTo(0);
    verify(valueExtractor, times(2)).findValue(anyString(), anyString());
  }

  /**
   * Test with a and b and asc and ignore case.
   */
  @Test
  void testWith_A_And_B_And_Asc_And_IgnoreCase() {
    ValueExtractor valueExtractor = mock(ValueExtractor.class);
    when(valueExtractor.findValue(anyString(), anyString())).thenReturn("a");
    when(valueExtractor.findValue(anyInt(), anyString())).thenReturn("b");

    SortOrderItem orderItem = SortOrderItem.by("someField")
        .with(Direction.ASC)
        .with(CaseHandling.INSENSITIVE);
    int result = new ValueComparator(orderItem, valueExtractor)
        .compare("A", 8);

    assertThat(result)
        .isLessThan(0);
    verify(valueExtractor, times(1)).findValue(anyString(), anyString());
    verify(valueExtractor, times(1)).findValue(anyInt(), anyString());
  }

  /**
   * Test with a and b and desc and ignore case.
   */
  @Test
  void testWith_A_And_B_And_Desc_And_IgnoreCase() {
    ValueExtractor valueExtractor = mock(ValueExtractor.class);
    when(valueExtractor.findValue(anyString(), anyString())).thenReturn("a");
    when(valueExtractor.findValue(anyInt(), anyString())).thenReturn("b");

    SortOrderItem orderItem = SortOrderItem.by("someField")
        .with(Direction.DESC)
        .with(CaseHandling.INSENSITIVE);
    int result = new ValueComparator(orderItem, valueExtractor)
        .compare("A", 8);

    assertThat(result)
        .isGreaterThan(0);
    verify(valueExtractor, times(1)).findValue(anyString(), anyString());
    verify(valueExtractor, times(1)).findValue(anyInt(), anyString());
  }

  /**
   * Test with b and a and asc and ignore case.
   */
  @Test
  void testWith_B_And_A_And_Asc_And_IgnoreCase() {
    ValueExtractor valueExtractor = mock(ValueExtractor.class);
    when(valueExtractor.findValue(anyString(), anyString())).thenReturn("a");
    when(valueExtractor.findValue(anyInt(), anyString())).thenReturn("b");

    SortOrderItem orderItem = SortOrderItem.by("someField")
        .with(Direction.ASC)
        .with(CaseHandling.INSENSITIVE);
    int result = new ValueComparator(orderItem, valueExtractor)
        .compare(8, "A");

    assertThat(result)
        .isGreaterThan(0);
    verify(valueExtractor, times(1)).findValue(anyString(), anyString());
    verify(valueExtractor, times(1)).findValue(anyInt(), anyString());
  }

  /**
   * Test with b and a and desc and ignore case.
   */
  @Test
  void testWith_B_And_A_And_Desc_And_IgnoreCase() {
    ValueExtractor valueExtractor = mock(ValueExtractor.class);
    when(valueExtractor.findValue(anyString(), anyString())).thenReturn("a");
    when(valueExtractor.findValue(anyInt(), anyString())).thenReturn("b");

    SortOrderItem orderItem = SortOrderItem.by("someField")
        .with(Direction.DESC)
        .with(CaseHandling.INSENSITIVE);
    int result = new ValueComparator(orderItem, valueExtractor)
        .compare(8, "A");

    assertThat(result)
        .isLessThan(0);
    verify(valueExtractor, times(1)).findValue(anyString(), anyString());
    verify(valueExtractor, times(1)).findValue(anyInt(), anyString());
  }

  /**
   * Test to string.
   */
  @Test
  void testToString() {
    SortOrderItem orderItem = SortOrderItem.by("qwertz");
    assertThat(new ValueComparator(orderItem).toString())
        .contains(orderItem.toString());
  }

}