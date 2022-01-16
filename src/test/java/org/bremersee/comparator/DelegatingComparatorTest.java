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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Comparator;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * The delegating comparator tests.
 *
 * @author Christian Bremer
 */
@ExtendWith(SoftAssertionsExtension.class)
class DelegatingComparatorTest {

  /**
   * Test delegating comparator.
   */
  @Test
  @SuppressWarnings({"rawtypes", "unchecked"})
  void testDelegatingComparator() {
    ValueExtractor valueExtractor = mock(ValueExtractor.class);
    when(valueExtractor.findValue(any(), anyString())).thenReturn(1);

    Comparator comparator = mock(Comparator.class);
    when(comparator.compare(any(), any())).thenReturn(-1);

    DelegatingComparator delegatingComparator = new DelegatingComparator(
        "someField",
        valueExtractor,
        comparator);

    int result = delegatingComparator.compare(1, 2);

    assertThat(result)
        .isLessThan(0);
    verify(comparator, times(1)).compare(any(), any());
    verify(valueExtractor, times(2)).findValue(any(), anyString());
  }

  /**
   * Test delegating comparator and expect illegal argument exception.
   */
  @Test
  void testDelegatingComparatorAndExpectIllegalArgumentException() {
    assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
      //noinspection ConstantConditions,ResultOfMethodCallIgnored
      new DelegatingComparator("someField", null).compare(1, 2);
    });
  }

  /**
   * Test to string.
   */
  @Test
  void testToString() {
    ValueExtractor valueExtractor = mock(ValueExtractor.class);
    when(valueExtractor.toString()).thenReturn("_ValueExtractor_");

    Comparator<?> comparator = mock(Comparator.class);
    when(comparator.toString()).thenReturn("_Comparator_");

    DelegatingComparator delegatingComparator = new DelegatingComparator(
        "_test_field_",
        valueExtractor,
        comparator);

    assertThat(delegatingComparator.toString())
        .contains("_ValueExtractor_", "_Comparator_", "_test_field_");
  }

}