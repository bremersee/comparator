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
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.bremersee.comparator.testmodel.SimpleObject;
import org.junit.jupiter.api.Test;

/**
 * The value extractor test.
 *
 * @author Christian Bremer
 */
public class ValueExtractorTest {

  private static final ValueExtractor extractor = (obj, field) -> "TestValue";

  /**
   * Find field with class and name.
   */
  @Test
  void findFieldWithClassAndName() {
    assertTrue(extractor.findField(SimpleObject.class, "number").isPresent());
  }

  /**
   * Gets possible method names.
   */
  @Test
  void getPossibleMethodNames() {
    assertEquals(0, extractor.getPossibleMethodNames(null).length);
    assertEquals(0, extractor.getPossibleMethodNames("").length);

    assertEquals(3, extractor.getPossibleMethodNames("a").length);
    assertEquals(3, extractor.getPossibleMethodNames("abc").length);
  }

}