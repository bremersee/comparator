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

import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.bremersee.comparator.testmodel.SimpleObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * The value extractor test.
 *
 * @author Christian Bremer
 */
@ExtendWith(SoftAssertionsExtension.class)
public class ValueExtractorTest {

  private static final ValueExtractor extractor = (obj, field) -> "TestValue";

  /**
   * Find field with class and name.
   */
  @Test
  void findFieldWithClassAndName() {
    assertThat(extractor.findField(SimpleObject.class, "number"))
        .isPresent();
  }

  /**
   * Gets possible method names.
   *
   * @param softly the soft assertions
   */
  @Test
  void getPossibleMethodNames(SoftAssertions softly) {
    softly.assertThat(extractor.getPossibleMethodNames(null))
        .isEmpty();
    softly.assertThat(extractor.getPossibleMethodNames(""))
        .isEmpty();

    softly.assertThat(extractor.getPossibleMethodNames("a"))
        .containsExactlyInAnyOrder("a", "getA", "isA");
    softly.assertThat(extractor.getPossibleMethodNames("abc"))
        .containsExactlyInAnyOrder("abc", "getAbc", "isAbc");
  }

}