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

import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.bremersee.comparator.testmodel.ComplexObject;
import org.bremersee.comparator.testmodel.ComplexObjectExtension;
import org.bremersee.comparator.testmodel.SimpleIsObject;
import org.bremersee.comparator.testmodel.SimpleObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * The default value extractor tests.
 *
 * @author Christian Bremer
 */
@ExtendWith(SoftAssertionsExtension.class)
class DefaultValueExtractorTest {

  private final DefaultValueExtractor extractor = new DefaultValueExtractor(false);

  private final DefaultValueExtractor throwingExtractor = new DefaultValueExtractor();

  /**
   * Test returning of given object.
   *
   * @param softly the soft assertions
   */
  @Test
  void testReturningOfGivenObject(SoftAssertions softly) {
    softly.assertThat(extractor.findValue(null, "foo")).isNull();
    softly.assertThat(extractor.findValue("Object", "foo")).isNull();
    softly.assertThat(extractor.findValue("Object", null))
        .isEqualTo("Object");
    softly.assertThat(extractor.findValue("Object", ""))
        .isEqualTo("Object");
  }

  /**
   * Test illegal field and expect exception.
   */
  @Test
  void testIllegalFieldAndExpectException() {
    assertThatExceptionOfType(ComparatorException.class).isThrownBy(() -> throwingExtractor
        .findValue("Object", "foo"));
  }

  /**
   * Test objects.
   *
   * @param softly the soft assertions
   */
  @Test
  void testObjects(SoftAssertions softly) {
    softly.assertThat(extractor.findValue(new SimpleObject(1), "number"))
        .isEqualTo(1);
    softly.assertThat(extractor.findValue(new SimpleObject(2), "number"))
        .isEqualTo(2);
    softly.assertThat(extractor.findValue(new SimpleIsObject(true), "nice"))
        .isEqualTo(true);
    softly.assertThat(extractor.findValue(new ComplexObject(new SimpleObject(3)), "simple"))
        .isEqualTo(new SimpleObject(3));
    softly.assertThat(extractor.findValue(new ComplexObject(new SimpleObject(4)), "simple.number"))
        .isEqualTo(4);
    softly.assertThat(
            extractor.findValue(new ComplexObject(new SimpleObject(4)), ". simple..number."))
        .isEqualTo(4);
    softly.assertThat(extractor.findValue(
            new ComplexObjectExtension(new SimpleObject(5), ""),
            "simple.number"))
        .isEqualTo(5);
  }

  /**
   * Test to string.
   */
  @Test
  void testToString() {
    assertThat(new DefaultValueExtractor(false).toString())
        .contains("false");
  }

  /**
   * Test equals and hash code.
   *
   * @param softly the softly
   */
  @Test
  void testEqualsAndHashCode(SoftAssertions softly) {
    softly.assertThat(new DefaultValueExtractor(false))
        .isEqualTo(new DefaultValueExtractor(false));
    softly.assertThat(new DefaultValueExtractor(false))
        .isNotEqualTo(new DefaultValueExtractor(true));
    softly.assertThat(new DefaultValueExtractor().hashCode())
        .isEqualTo(new DefaultValueExtractor().hashCode());
  }

}