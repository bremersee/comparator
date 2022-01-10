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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * The value comparator tests.
 *
 * @author Christian Bremer
 */
class ValueComparatorTests {

  /**
   * Test with non comparable values and expect comparator exception.
   */
  @Test
  void testWithNonComparableValuesAndExpectComparatorException() {
    Assertions.assertThrows(ComparatorException.class, () -> {
      ValueExtractor valueExtractor = mock(ValueExtractor.class);
      when(valueExtractor.findValue(any(), anyString())).thenReturn(new Object());

      int result = new ValueComparator("someField", true, true, false, valueExtractor)
          .compare(new Object(), new Object());

      assertEquals(0, result);
      verify(valueExtractor, times(2)).findValue(any(), anyString());

      result = new ValueComparator("someField", true, true, false)
          .compare(new Object(), new Object());
      assertEquals(0, result);

      result = new ValueComparator("someField", true, true, true)
          .compare(new Object(), new Object());
      assertEquals(0, result);
    });
  }

  /**
   * Test with two null values.
   */
  @Test
  void testWithTwoNullValues() {
    ValueExtractor valueExtractor = mock(ValueExtractor.class);
    when(valueExtractor.findValue(any(), anyString())).thenReturn(null);

    int result = new ValueComparator("someField", true, true, false, valueExtractor)
        .compare(new Object(), new Object());

    assertEquals(0, result);
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

    int result = new ValueComparator("someField", true, true, true, valueExtractor)
        .compare("null", 1);

    assertTrue(result < 0);
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

    int result = new ValueComparator("someField", false, true, true, valueExtractor)
        .compare("null", 1);

    assertTrue(result > 0);
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

    int result = new ValueComparator("someField", true, true, false, valueExtractor)
        .compare("null", 1);

    assertTrue(result > 0);
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

    int result = new ValueComparator("someField", false, true, false, valueExtractor)
        .compare("null", 1);

    assertTrue(result < 0);
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

    int result = new ValueComparator("someField", true, true, true, valueExtractor)
        .compare(1, "null");

    assertTrue(result > 0);
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

    int result = new ValueComparator("someField", false, true, true, valueExtractor)
        .compare(1, "null");

    assertTrue(result < 0);
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

    int result = new ValueComparator("someField", true, true, false, valueExtractor)
        .compare(1, "null");

    assertTrue(result < 0);
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

    int result = new ValueComparator("someField", false, true, false, valueExtractor)
        .compare(1, "null");

    assertTrue(result > 0);
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

    int result = new ValueComparator("someField", true, false, false, valueExtractor)
        .compare("A", "A");

    assertEquals(0, result);
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

    int result = new ValueComparator("someField", true, false, false, valueExtractor)
        .compare("A", 8);

    assertTrue(result < 0);
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

    int result = new ValueComparator("someField", false, false, false, valueExtractor)
        .compare("A", 8);

    assertTrue(result > 0);
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

    int result = new ValueComparator("someField", true, false, false, valueExtractor)
        .compare(8, "A");

    assertTrue(result > 0);
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

    int result = new ValueComparator("someField", false, false, false, valueExtractor)
        .compare(8, "A");

    assertTrue(result < 0);
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

    int result = new ValueComparator("someField", true, true, false, valueExtractor)
        .compare("A", "A");

    assertEquals(0, result);
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

    int result = new ValueComparator("someField", true, true, false, valueExtractor)
        .compare("A", 8);

    assertTrue(result < 0);
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

    int result = new ValueComparator("someField", false, true, false, valueExtractor)
        .compare("A", 8);

    assertTrue(result > 0);
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

    int result = new ValueComparator("someField", true, true, false, valueExtractor)
        .compare(8, "A");

    assertTrue(result > 0);
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

    int result = new ValueComparator("someField", false, true, false, valueExtractor)
        .compare(8, "A");

    assertTrue(result < 0);
    verify(valueExtractor, times(1)).findValue(anyString(), anyString());
    verify(valueExtractor, times(1)).findValue(anyInt(), anyString());
  }

  /**
   * Test to string.
   */
  @Test
  void testToString() {
    ValueExtractor valueExtractor = new DefaultValueExtractor();
    ValueComparator valueComparator = new ValueComparator(
        "someField", true, true, false, valueExtractor);
    assertTrue(valueComparator.toString().contains("someField"));
  }

  /**
   * Test equals and hash code.
   */
  @SuppressWarnings({"EqualsWithItself", "SimplifiableJUnitAssertion",
      "EqualsBetweenInconvertibleTypes"})
  @Test
  void testEqualsAndHashCode() {
    ValueExtractor valueExtractor = new DefaultValueExtractor();
    ValueComparator valueComparator = new ValueComparator(
        "someField", true, true, false, valueExtractor);
    assertTrue(valueComparator.equals(valueComparator));
    assertTrue(valueComparator
        .equals(new ValueComparator("someField", true, true, false, valueExtractor)));
    assertFalse(valueComparator.equals(new ValueComparator("anotherField", true, true, true)));
    assertFalse(valueComparator.equals("test"));
    //noinspection ConstantConditions
    assertFalse(valueComparator.equals(null));

    assertEquals(
        valueComparator.hashCode(),
        new ValueComparator("someField", true, true, false, valueExtractor).hashCode());
  }

}