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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.bremersee.comparator.model.SortOrder;
import org.bremersee.comparator.model.SortOrders;
import org.bremersee.comparator.testmodel.ComplexObject;
import org.bremersee.comparator.testmodel.ComplexObjectExtension;
import org.bremersee.comparator.testmodel.ComplexObjectExtensionComparator;
import org.bremersee.comparator.testmodel.SimpleGetObject;
import org.bremersee.comparator.testmodel.SimpleIsObject;
import org.bremersee.comparator.testmodel.SimpleObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * The comparator builder tests.
 *
 * @author Christian Bremer
 */
@ExtendWith(SoftAssertionsExtension.class)
class ComparatorBuilderTest {

  /**
   * Test primitive type.
   */
  @Test
  void testPrimitiveType() {
    int result = ComparatorBuilder.builder()
        .build()
        .compare(1, 2);
    assertThat(result)
        .as("Compare 1 with 2")
        .isLessThan(0);
  }

  /**
   * Test simple object.
   */
  @Test
  void testSimpleObject() {
    SimpleObject one = new SimpleObject(1);
    SimpleObject two = new SimpleObject(2);
    int result = ComparatorBuilder.builder()
        .add("number", true, true, false)
        .build()
        .compare(one, two);
    assertThat(result)
        .as("Compare %s with %s", one, two)
        .isLessThan(0);
  }

  /**
   * Test simple get object.
   */
  @Test
  void testSimpleGetObject() {
    SimpleGetObject one = new SimpleGetObject(1);
    SimpleGetObject two = new SimpleGetObject(2);
    int result = ComparatorBuilder.builder()
        .add("number", true, true, false)
        .build()
        .compare(one, two);
    assertThat(result)
        .as("Compare %s with %s", one, two)
        .isLessThan(0);
  }

  /**
   * Test simple get object with sort orders.
   *
   * @param softly the soft assertions
   */
  @Test
  void testSimpleGetObjectWithSortOrders(SoftAssertions softly) {
    List<SortOrder> fields = List.of(
        new SortOrder("number", true, false, false),
        new SortOrder("anotherNumber", true, false, false));
    SimpleGetObject one = new SimpleGetObject(1, 1);
    SimpleGetObject two = new SimpleGetObject(1, 3);
    int result = ComparatorBuilder.builder()
        .addAll(fields)
        .build()
        .compare(one, two);
    softly.assertThat(result)
        .as("Compare %s with %s", one, two)
        .isLessThan(0);

    result = ComparatorBuilder.builder()
        .addAll(fields, new DefaultValueExtractor())
        .build()
        .compare(two, one);
    softly.assertThat(result)
        .as("Compare with given value extractor %s with %s", two, one)
        .isGreaterThan(0);

    SortOrders comparatorFields = new SortOrders(fields);
    result = ComparatorBuilder.builder()
        .addAll(comparatorFields)
        .build()
        .compare(one, two);
    softly.assertThat(result)
        .as("Compare %s with %s using %s", one, two, comparatorFields)
        .isLessThan(0);

    result = ComparatorBuilder.builder()
        .addAll(comparatorFields, new DefaultValueExtractor())
        .build()
        .compare(two, one);
    softly.assertThat(result)
        .as("Compare with given value extractor %s with %s using %s", two, one, comparatorFields)
        .isGreaterThan(0);
  }

  /**
   * Test simple is object.
   */
  @Test
  void testSimpleIsObject() {
    SimpleIsObject one = new SimpleIsObject(true);
    SimpleIsObject two = new SimpleIsObject(false);
    int result = ComparatorBuilder.builder()
        .add("nice", false, true, false)
        .build()
        .compare(one, two);
    assertThat(result)
        .as("Compare %s with %s", one, two)
        .isLessThan(0);
  }

  /**
   * Test complex object.
   */
  @Test
  void testComplexObject() {
    ComplexObject one = new ComplexObject(new SimpleObject(1));
    ComplexObject two = new ComplexObject(new SimpleObject(2));
    int result = ComparatorBuilder.builder()
        .add("simple.number", true, true, false)
        .build()
        .compare(one, two);
    assertThat(result)
        .as("Compare %s with %s", one, two)
        .isLessThan(0);
  }

  /**
   * Test comparing of complex objects.
   *
   * @param softly the soft assertions
   */
  @Test
  void testComparingOfComplexObjects(SoftAssertions softly) {
    ComplexObject a = new ComplexObjectExtension(new SimpleObject(1), "a");
    ComplexObject b = new ComplexObjectExtension(new SimpleObject(1), "b");
    List<ComplexObject> list = new ArrayList<>(List.of(b, a));
    // natural order
    list.sort(ComparatorBuilder.builder().build());
    softly.assertThat(list)
        .containsExactly(a, b);

    list = new ArrayList<>(List.of(b, a));
    // natural order
    list.sort(ComparatorBuilder.builder()
        .add(new ComplexObjectExtensionComparator())
        .build());
    softly.assertThat(list)
        .containsExactly(a, b);

    list = new ArrayList<>(List.of(b, a));
    // natural order
    list.sort(ComparatorBuilder.builder()
        .add((SortOrder) null)
        .build());
    softly.assertThat(list)
        .containsExactly(a, b);

    list = new ArrayList<>(List.of(a, b));
    // natural order
    list.sort(ComparatorBuilder.builder()
        .add(new SortOrder("value", false, true, false))
        .build());
    softly.assertThat(list)
        .containsExactly(b, a);

    a = new ComplexObjectExtension(new SimpleObject(4), "d");
    b = new ComplexObjectExtension(new SimpleObject(3), "d");
    ComplexObject c = new ComplexObjectExtension(new SimpleObject(2), "c");
    list = new ArrayList<>(List.of(b, c, a));

    List<SortOrder> comparatorFields = List.of(
        new SortOrder("not_exists", true, true, false),
        new SortOrder("simple.number", false, true, false)
    );
    list.sort(ComparatorBuilder.builder()
        .addAll(comparatorFields, comparatorField -> {
          if ("not_exists".equals(comparatorField.getField())) {
            return new ComplexObjectExtensionComparator();
          }
          return new ValueComparator(comparatorField);
        })
        .build());
    softly.assertThat(list)
        .containsExactly(c, a, b);

    Collections.shuffle(list);
    list.sort(ComparatorBuilder.builder()
        .addAll(new SortOrders(comparatorFields), comparatorField -> {
          if ("not_exists".equals(comparatorField.getField())) {
            return new ComplexObjectExtensionComparator();
          }
          return new ValueComparator(comparatorField);
        })
        .build());
    softly.assertThat(list)
        .containsExactly(c, a, b);
  }

  /**
   * Test objects with same values.
   */
  @Test
  void testObjectsWithSameValues() {
    SimpleObject a = new SimpleObject(1);
    SimpleObject b = new SimpleObject(1);
    List<SimpleObject> list = new ArrayList<>(List.of(b, a));
    list.sort(ComparatorBuilder.builder()
        .add("number")
        .build());
    assertThat(list)
        .containsExactly(b, a);
  }

  /**
   * Test strings.
   */
  @Test
  void testStrings() {
    List<String> list = new ArrayList<>(List.of("b", "a"));
    list.sort(ComparatorBuilder.builder().add(null, Comparator.naturalOrder()).build());
    assertThat(list)
        .containsExactly("a", "b");
  }

  /**
   * Test objects and expect comparator exception.
   */
  @Test
  void testObjectsAndExpectComparatorException() {
    assertThatExceptionOfType(ComparatorException.class).isThrownBy(() -> {
      SimpleObject a = new SimpleObject(1);
      SimpleObject b = new SimpleObject(1);
      List<SimpleObject> list = new ArrayList<>(List.of(b, a));
      list.sort(ComparatorBuilder.builder().add(null, new DefaultValueExtractor()).build());
    });
  }

}
