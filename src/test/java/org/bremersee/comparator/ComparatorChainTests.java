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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * The comparator chain tests.
 *
 * @author Christian Bremer
 */
class ComparatorChainTests {

  /**
   * Test empty comparator chain.
   */
  @Test
  void testEmptyComparatorChain() {
    int result = new ComparatorChain(Collections.emptyList())
        .compare(1, 2);
    assertTrue(result < 0);

    result = new ComparatorChain(Collections.emptyList())
        .compare(2, 1);
    assertTrue(result > 0);

    result = new ComparatorChain(Collections.emptyList())
        .compare(1, 1);
    assertEquals(0, result);
  }

  /**
   * Test not comparable objects and expect comparator exception.
   */
  @Test
  void testNotComparableObjectsAndExpectComparatorException() {
    assertThrows(ComparatorException.class, () -> {
      //noinspection ResultOfMethodCallIgnored
      new ComparatorChain(Collections.emptyList())
          .compare(new Object(), new Object());
    });
  }

  /**
   * Test null objects and expect comparator exception.
   */
  @Test
  void testNullObjectsAndExpectComparatorException() {
    assertThrows(ComparatorException.class, () -> {
      //noinspection ResultOfMethodCallIgnored
      new ComparatorChain(Collections.emptyList())
          .compare(null, null);
    });
  }

  /**
   * Test two comparators and use both.
   */
  @Test
  @SuppressWarnings({"unchecked", "ResultOfMethodCallIgnored", "rawtypes"})
  void testTwoComparatorsAndUseBoth() {
    Comparator comparatorA = mock(Comparator.class);
    when(comparatorA.compare(any(), any())).thenReturn(0);

    Comparator comparatorB = mock(Comparator.class);
    when(comparatorB.compare(any(), any())).thenReturn(-1);

    int result = new ComparatorChain(List.of(comparatorA, comparatorB))
        .compare(1, 2);

    assertTrue(result < 0);

    verify(comparatorA, times(1)).compare(any(), any());
    verify(comparatorB, times(1)).compare(any(), any());
  }

  /**
   * Test two comparators and use only first.
   */
  @Test
  @SuppressWarnings({"unchecked", "ResultOfMethodCallIgnored", "rawtypes"})
  void testTwoComparatorsAndUseOnlyFirst() {
    Comparator comparatorA = mock(Comparator.class);
    when(comparatorA.compare(any(), any())).thenReturn(-1);

    Comparator comparatorB = mock(Comparator.class);
    when(comparatorB.compare(any(), any())).thenReturn(1);

    int result = new ComparatorChain(List.of(comparatorA, comparatorB))
        .compare(1, 2);

    assertTrue(result < 0);

    verify(comparatorA, times(1)).compare(any(), any());
    verify(comparatorB, times(0)).compare(any(), any());
  }

}