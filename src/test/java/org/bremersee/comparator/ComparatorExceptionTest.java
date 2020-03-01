/*
 * Copyright 2015-2020 the original author or authors.
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

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * The comparator exception test.
 *
 * @author Christian Bremer
 */
public class ComparatorExceptionTest {

  /**
   * Test with one argument.
   */
  @Test
  void testWithOneArgument() {
    assertThrows(ComparatorException.class, () -> {
      throw new ComparatorException("Test exception");
    });
  }

  /**
   * Test with two arguments.
   */
  @Test
  void testWithTwoArguments() {
    assertThrows(ComparatorException.class, () -> {
      throw new ComparatorException("Test exception", new Exception("Cause"));
    });
  }

}