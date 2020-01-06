/*
 * Copyright 2015-2019 the original author or authors.
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import org.bremersee.comparator.model.ComparatorField;
import org.bremersee.comparator.model.ComparatorFields;
import org.bremersee.comparator.testmodel.ComplexObject;
import org.bremersee.comparator.testmodel.ComplexObjectExtension;
import org.bremersee.comparator.testmodel.ComplexObjectExtensionComparator;
import org.bremersee.comparator.testmodel.SimpleGetObject;
import org.bremersee.comparator.testmodel.SimpleIsObject;
import org.bremersee.comparator.testmodel.SimpleObject;
import org.junit.Test;

/**
 * The comparator builder tests.
 *
 * @author Christian Bremer
 */
public class ComparatorBuilderTests {

  /**
   * Test primitive type.
   */
  @Test
  public void testPrimitiveType() {
    System.out.println("Testing primitive type ...");
    int result = ComparatorBuilder.builder()
        .build()
        .compare(1, 2);
    System.out.println(result);
    assertTrue(result < 0);
    System.out.println("OK\n");
  }

  /**
   * Test simple object.
   */
  @Test
  public void testSimpleObject() {
    System.out.println("Testing simple object ...");
    int result = ComparatorBuilder.builder()
        .add("number", true, true, false)
        .build()
        .compare(new SimpleObject(1), new SimpleObject(2));
    System.out.println(result);
    assertTrue(result < 0);
    System.out.println("OK\n");
  }

  /**
   * Test simple get object.
   */
  @Test
  public void testSimpleGetObject() {
    System.out.println("Testing simple 'get' object ...");
    int result = ComparatorBuilder.builder()
        .add("number", true, true, false)
        .build()
        .compare(new SimpleGetObject(1),
            new SimpleGetObject(2));
    System.out.println(result);
    assertTrue(result < 0);
    System.out.println("OK\n");
  }

  /**
   * Test simple get object with comparator fields.
   */
  @Test
  public void testSimpleGetObjectWithComparatorFields() {
    System.out.println("Testing simple 'get' object with list of comparator fields...");
    List<ComparatorField> fields = Arrays.asList(
        new ComparatorField("number", true, false, false),
        new ComparatorField("anotherNumber", true, false, false));
    int result = ComparatorBuilder.builder()
        .addAll(fields)
        .build()
        .compare(new SimpleGetObject(1, 1),
            new SimpleGetObject(1, 2));
    System.out.println(result);
    assertTrue(result < 0);

    result = ComparatorBuilder.builder()
        .addAll(fields, new DefaultValueExtractor())
        .build()
        .compare(new SimpleGetObject(1, 1),
            new SimpleGetObject(1, 2));
    System.out.println(result);
    assertTrue(result < 0);

    result = ComparatorBuilder.builder()
        .addAll(new ComparatorFields(fields))
        .build()
        .compare(new SimpleGetObject(1, 1),
            new SimpleGetObject(1, 2));
    System.out.println(result);
    assertTrue(result < 0);

    result = ComparatorBuilder.builder()
        .addAll(new ComparatorFields(fields), new DefaultValueExtractor())
        .build()
        .compare(new SimpleGetObject(1, 1),
            new SimpleGetObject(1, 2));
    System.out.println(result);
    assertTrue(result < 0);

    System.out.println("OK\n");
  }

  /**
   * Test simple is object.
   */
  @Test
  public void testSimpleIsObject() {
    System.out.println("Testing simple 'is' object ...");
    int result = ComparatorBuilder.builder()
        .add("nice", false, true, false)
        .build()
        .compare(new SimpleIsObject(true),
            new SimpleIsObject(false));
    System.out.println(result);
    assertTrue(result < 0);
    System.out.println("OK\n");
  }

  /**
   * Test complex object.
   */
  @Test
  public void testComplexObject() {
    System.out.println("Testing complex object ...");
    int result = ComparatorBuilder.builder()
        .add("simple.number", true, true, false)
        .build()
        .compare(
            new ComplexObject(new SimpleObject(1)),
            new ComplexObject(new SimpleObject(2)));
    System.out.println(result);
    assertTrue(result < 0);
    System.out.println("OK\n");
  }

  /**
   * Test comparing of complex objects.
   */
  @Test
  public void testComparingOfComplexObjects() {
    ComplexObject a = new ComplexObjectExtension(new SimpleObject(1), "same");
    ComplexObject b = new ComplexObjectExtension(new SimpleObject(2), "same");
    List<ComplexObject> list = Arrays.asList(b, a);
    assertEquals(b, list.get(0));
    assertEquals(a, list.get(1));
    list.sort(ComparatorBuilder.builder()
        .add(new ComparatorField("value", true, true, false))
        .fromWellKnownText(new ComparatorField("simple.number", true, true, false).toWkt())
        .build());
    assertEquals(a, list.get(0));
    assertEquals(b, list.get(1));

    ComplexObject c = new ComplexObjectExtension(new SimpleObject(2), "first");
    list = Arrays.asList(b, a, c);
    list.sort(ComparatorBuilder.builder()
        .fromWellKnownText("not_exists|simple.number", comparatorField -> {
          if ("not_exists".equals(comparatorField.getField())) {
            return new ComplexObjectExtensionComparator();
          }
          return new ValueComparator(comparatorField);
        })
        .build());
    assertEquals(c, list.get(0));
    assertEquals(a, list.get(1));
    assertEquals(b, list.get(2));
  }

  /**
   * Test objects with same values.
   */
  @Test
  public void testObjectsWithSameValues() {
    SimpleObject a = new SimpleObject(1);
    SimpleObject b = new SimpleObject(1);
    List<SimpleObject> list = Arrays.asList(b, a);
    list.sort(ComparatorBuilder.builder().fromWellKnownText("number").build());
    assertEquals(list.get(0), list.get(1));
  }

  /**
   * Test strings.
   */
  @Test
  public void testStrings() {
    List<String> list = Arrays.asList("b", "a");
    list.sort(ComparatorBuilder.builder().add(null, Comparator.naturalOrder()).build());
    assertEquals("a", list.get(0));
    assertEquals("b", list.get(1));
  }

  /**
   * Test objects and expect illegal argument exception.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testObjectsAndExpectIllegalArgumentException() {
    SimpleObject a = new SimpleObject(1);
    SimpleObject b = new SimpleObject(1);
    List<SimpleObject> list = Arrays.asList(b, a);
    list.sort(ComparatorBuilder.builder().add(null, new DefaultValueExtractor()).build());
    assertEquals(list.get(0), list.get(1));
  }

}
