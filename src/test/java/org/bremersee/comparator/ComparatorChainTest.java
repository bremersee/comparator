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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * The comparator chain tests.
 *
 * @author Christian Bremer
 */
@ExtendWith(SoftAssertionsExtension.class)
class ComparatorChainTest {

  /**
   * Test empty comparator chain.
   *
   * @param softly the soft assertions
   */
  @Test
  void testEmptyComparatorChain(SoftAssertions softly) {
    softly.assertThat(new ComparatorChain(List.of()).compare(1, 3))
        .isLessThan(0);

    softly.assertThat(new ComparatorChain(List.of()).compare(4, 1))
        .isGreaterThan(0);

    //noinspection EqualsWithItself
    softly.assertThat(new ComparatorChain(List.of()).compare(2, 2))
        .isEqualTo(0);
  }

  /**
   * Test not comparable objects and expect comparator exception.
   */
  @Test
  void testNotComparableObjectsAndExpectComparatorException() {
    assertThatExceptionOfType(ComparatorException.class).isThrownBy(() -> {
      //noinspection EqualsWithItself,ResultOfMethodCallIgnored
      new ComparatorChain(Collections.emptyList())
          .compare(new Object(), new Object());
    });
  }

  /**
   * Test null objects and expect comparator exception.
   */
  @Test
  void testNullObjectsAndExpectComparatorException() {
    assertThatExceptionOfType(ComparatorException.class).isThrownBy(() -> {
      //noinspection ResultOfMethodCallIgnored,EqualsWithItself
      new ComparatorChain(Collections.emptyList())
          .compare(null, null);
    });
  }

  /**
   * Test two comparators and use both.
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  @Test
  void testTwoComparatorsAndUseBoth() {
    Comparator comparatorA = mock(Comparator.class);
    when(comparatorA.compare(any(), any())).thenReturn(0);

    Comparator comparatorB = mock(Comparator.class);
    when(comparatorB.compare(any(), any())).thenReturn(-1);

    int result = new ComparatorChain(List.of(comparatorA, comparatorB))
        .compare(1, 2);

    assertThat(result)
        .isLessThan(0);

    verify(comparatorA, times(1)).compare(any(), any());
    verify(comparatorB, times(1)).compare(any(), any());
  }

  /**
   * Test two comparators and use only first.
   */
  @Test
  @SuppressWarnings({"rawtypes", "unchecked"})
  void testTwoComparatorsAndUseOnlyFirst() {
    Comparator comparatorA = mock(Comparator.class);
    when(comparatorA.compare(any(), any())).thenReturn(-1);

    Comparator comparatorB = mock(Comparator.class);
    when(comparatorB.compare(any(), any())).thenReturn(1);

    int result = new ComparatorChain(List.of(comparatorA, comparatorB))
        .compare(1, 2);

    assertThat(result)
        .isLessThan(0);

    verify(comparatorA, times(1)).compare(any(), any());
    verify(comparatorB, times(0)).compare(any(), any());
  }

  /**
   * Test to string.
   */
  @Test
  void testToString() {
    Comparator<?> c0 = mock(Comparator.class);
    when(c0.toString()).thenReturn("c0");
    Comparator<?> c1 = mock(Comparator.class);
    when(c1.toString()).thenReturn("c1");
    String actual = new ComparatorChain(List.of(c0, c1)).toString();
    assertThat(actual)
        .contains("c0", "c1");
  }

}