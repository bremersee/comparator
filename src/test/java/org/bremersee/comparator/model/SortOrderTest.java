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

package org.bremersee.comparator.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.list;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.bremersee.comparator.model.SortOrderItem.CaseHandling;
import org.bremersee.comparator.model.SortOrderItem.Direction;
import org.bremersee.comparator.model.SortOrderItem.NullHandling;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * The sort order tests.
 *
 * @author Christian Bremer
 */
@ExtendWith(SoftAssertionsExtension.class)
class SortOrderTest {

  private static JAXBContext jaxbContext;

  /**
   * Create jaxb context.
   *
   * @throws JAXBException the jaxb exception
   */
  @BeforeAll
  static void createJaxbContext() throws JAXBException {
    jaxbContext = JAXBContext.newInstance(ObjectFactory.class.getPackage().getName());
  }

  /**
   * Test read and write xml of sort order.
   *
   * @throws Exception the exception
   */
  @Test
  void testXml() throws Exception {
    SortOrderItem sortOrderItem0 = new SortOrderItem(
        "i0", Direction.ASC, CaseHandling.SENSITIVE, NullHandling.NULLS_FIRST);
    SortOrderItem sortOrderItem1 = new SortOrderItem(
        "i1", Direction.DESC, CaseHandling.INSENSITIVE, NullHandling.NULLS_LAST);
    SortOrderItem sortOrderItem2 = new SortOrderItem(
        "i2", Direction.DESC, CaseHandling.INSENSITIVE, NullHandling.NATIVE);

    SortOrder sortOrder = new SortOrder(List.of(sortOrderItem0, sortOrderItem1, sortOrderItem2));

    Marshaller marshaller = jaxbContext.createMarshaller();
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

    StringWriter sw = new StringWriter();

    marshaller.marshal(sortOrder, sw);

    String xmlStr = sw.toString();
    //System.out.println(xmlStr);

    SortOrder readFields = (SortOrder) jaxbContext.createUnmarshaller()
        .unmarshal(new StringReader(xmlStr));

    assertThat(readFields)
        .as("Write and read xml of %s", sortOrder)
        .isEqualTo(sortOrder);
  }

  /**
   * Test read and write json of sort order.
   *
   * @throws Exception the exception
   */
  @Test
  void testJsonSortOrders() throws Exception {
    SortOrderItem sortOrderItem0 = new SortOrderItem(
        "i0", Direction.ASC, CaseHandling.SENSITIVE, NullHandling.NULLS_FIRST);
    SortOrderItem sortOrderItem1 = new SortOrderItem(
        "i1", Direction.DESC, CaseHandling.INSENSITIVE, NullHandling.NULLS_LAST);
    SortOrderItem sortOrderItem2 = new SortOrderItem(
        "i2", Direction.DESC, CaseHandling.INSENSITIVE, NullHandling.NATIVE);

    SortOrder sortOrder = new SortOrder(List.of(sortOrderItem0, sortOrderItem1, sortOrderItem2));

    ObjectMapper om = new ObjectMapper();

    String jsonStr = om.writerWithDefaultPrettyPrinter().writeValueAsString(sortOrder);
    //System.out.println(jsonStr);

    SortOrder readFields = om.readValue(jsonStr, SortOrder.class);

    assertThat(readFields)
        .as("Write and read json of %s", sortOrder)
        .isEqualTo(sortOrder);
  }

  /**
   * Test equals and hash code.
   *
   * @param softly the soft assertions
   */
  @Test
  void testEqualsAndHashCode(SoftAssertions softly) {
    SortOrderItem sortOrderItem0 = new SortOrderItem(
        "i0", Direction.ASC, CaseHandling.SENSITIVE, NullHandling.NULLS_FIRST);
    SortOrderItem sortOrderItem1 = new SortOrderItem(
        "i1", Direction.DESC, CaseHandling.INSENSITIVE, NullHandling.NULLS_LAST);
    SortOrderItem sortOrderItem2 = new SortOrderItem(
        "i2", Direction.DESC, CaseHandling.INSENSITIVE, NullHandling.NATIVE);
    SortOrderItem sortOrderItem3 = new SortOrderItem(
        "i0", Direction.ASC, CaseHandling.SENSITIVE, NullHandling.NULLS_FIRST);
    SortOrderItem sortOrderItem4 = new SortOrderItem(
        "i1", Direction.DESC, CaseHandling.INSENSITIVE, NullHandling.NULLS_LAST);
    SortOrderItem sortOrderItem5 = new SortOrderItem(
        "i2", Direction.DESC, CaseHandling.INSENSITIVE, NullHandling.NATIVE);
    SortOrder sortOrder0 = new SortOrder(List.of(sortOrderItem0, sortOrderItem1, sortOrderItem2));
    SortOrder sortOrder2 = new SortOrder(List.of(sortOrderItem3, sortOrderItem4, sortOrderItem5));

    softly.assertThat(sortOrder0).isEqualTo(sortOrder2);
    softly.assertThat(sortOrder0.hashCode()).isEqualTo(sortOrder2.hashCode());

    //noinspection UnnecessaryLocalVariable
    SortOrder sortOrder1 = sortOrder0;
    //noinspection ConstantConditions
    softly.assertThat(sortOrder0.equals(sortOrder1)).isTrue();
    softly.assertThat(sortOrder0.equals(sortOrder2)).isTrue();

    SortOrder sortOrder3 = new SortOrder(List.of(sortOrderItem1, sortOrderItem3));
    softly.assertThat(sortOrder3.equals(sortOrder0)).isFalse();
    //noinspection EqualsBetweenInconvertibleTypes
    softly.assertThat(sortOrder0.equals(sortOrderItem0)).isFalse();

    softly.assertThat(new SortOrder(null).equals(new SortOrder())).isTrue();
  }

  /**
   * Test get sort order text.
   *
   * @param softly the soft assertions
   */
  @Test
  void testGetSortOrderText(SoftAssertions softly) {
    SortOrderItem sortOrderItem0 = new SortOrderItem(
        "i0", Direction.ASC, CaseHandling.SENSITIVE, NullHandling.NULLS_FIRST);
    SortOrderItem sortOrderItem1 = new SortOrderItem(
        "i1", Direction.DESC, CaseHandling.INSENSITIVE, NullHandling.NULLS_LAST);
    SortOrderItem sortOrderItem2 = new SortOrderItem(
        "i2", Direction.DESC, CaseHandling.INSENSITIVE, NullHandling.NATIVE);
    SortOrder sortOrder0 = new SortOrder(List.of(sortOrderItem0, sortOrderItem1, sortOrderItem2));
    String actual = sortOrder0.getSortOrderText();
    softly.assertThat(actual)
        .as("Create sort orders text of %s", sortOrder0)
        .isEqualTo("i0;asc;sensitive;nulls-first,i1;desc;insensitive;nulls-last,i2;desc");
    softly.assertThat(sortOrder0.toString())
        .as("toString is equal to sort orders text")
        .isEqualTo(actual);
  }

  /**
   * Test from sort orders text.
   *
   * @param softly the softly
   */
  @Test
  void testFromSortOrderText(SoftAssertions softly) {
    SortOrder actual = SortOrder.fromSortOrderText(null);
    softly.assertThat(actual)
        .extracting(SortOrder::getItems, list(SortOrderItem.class))
        .isEmpty();

    actual = SortOrder.fromSortOrderText(
        "i0;asc;sensitive;nulls-first,i1;desc;insensitive;nulls-last,i2;desc");
    SortOrderItem sortOrderItem0 = new SortOrderItem(
        "i0", Direction.ASC, CaseHandling.SENSITIVE, NullHandling.NULLS_FIRST);
    SortOrderItem sortOrderItem1 = new SortOrderItem(
        "i1", Direction.DESC, CaseHandling.INSENSITIVE, NullHandling.NULLS_LAST);
    SortOrderItem sortOrderItem2 = new SortOrderItem(
        "i2", Direction.DESC, CaseHandling.INSENSITIVE, NullHandling.NATIVE);
    SortOrder sortOrder = new SortOrder(List.of(sortOrderItem0, sortOrderItem1, sortOrderItem2));
    List<SortOrderItem> expected = sortOrder.getItems();
    softly.assertThat(actual)
        .extracting(SortOrder::getItems, list(SortOrderItem.class))
        .containsExactlyElementsOf(expected);

    actual = SortOrder.fromSortOrderText(
        "i0;asc;sensitive;nulls-first,i1;desc;insensitive;nulls-last,i2;desc");
    softly.assertThat(actual)
        .extracting(SortOrder::getItems, list(SortOrderItem.class))
        .containsExactlyElementsOf(expected);
  }

  /**
   * Test is empty.
   */
  @Test
  void testIsEmpty() {
    assertThat(SortOrder.by())
        .extracting(SortOrder::isEmpty, InstanceOfAssertFactories.BOOLEAN)
        .isTrue();
  }

  /**
   * Test is unsorted.
   */
  @Test
  void testIsUnsorted() {
    assertThat(SortOrder.by())
        .extracting(SortOrder::isUnsorted, InstanceOfAssertFactories.BOOLEAN)
        .isTrue();
  }

  /**
   * Test is sorted.
   */
  @Test
  void testIsSorted() {
    assertThat(SortOrder.by(SortOrderItem.by("home")))
        .extracting(SortOrder::isSorted, InstanceOfAssertFactories.BOOLEAN)
        .isTrue();
  }

  /**
   * By sort orders.
   */
  @Test
  void bySortOrders() {
    SortOrderItem sortOrderItem0 = new SortOrderItem(
        "i0", Direction.ASC, CaseHandling.SENSITIVE, NullHandling.NULLS_FIRST);
    SortOrderItem sortOrderItem1 = new SortOrderItem(
        "i1", Direction.DESC, CaseHandling.INSENSITIVE, NullHandling.NULLS_LAST);
    SortOrderItem sortOrderItem2 = new SortOrderItem(
        "i2", Direction.DESC, CaseHandling.INSENSITIVE, NullHandling.NATIVE);
    SortOrder sortOrder0 = new SortOrder(List.of(sortOrderItem0, sortOrderItem1));
    SortOrder sortOrder1 = new SortOrder(List.of(sortOrderItem2));
    SortOrder actual = SortOrder.by(List.of(sortOrder0, sortOrder1));
    List<SortOrderItem> expected = List.of(sortOrderItem0, sortOrderItem1, sortOrderItem2);
    assertThat(actual)
        .extracting(SortOrder::getItems, list(SortOrderItem.class))
        .containsExactlyElementsOf(expected);
  }

  /**
   * By sort orders with default.
   */
  @Test
  void bySortOrdersWithDefault() {
    SortOrderItem sortOrderItem0 = new SortOrderItem(
        "i0", Direction.ASC, CaseHandling.SENSITIVE, NullHandling.NULLS_FIRST);
    SortOrderItem sortOrderItem1 = new SortOrderItem(
        "i1", Direction.DESC, CaseHandling.INSENSITIVE, NullHandling.NULLS_LAST);
    SortOrderItem sortOrderItem2 = new SortOrderItem(
        "i2", Direction.DESC, CaseHandling.INSENSITIVE, NullHandling.NATIVE);
    SortOrder actual = SortOrder.by(List.of(),
        new SortOrder(List.of(sortOrderItem0, sortOrderItem1, sortOrderItem2)));
    List<SortOrderItem> expected = List.of(sortOrderItem0, sortOrderItem1, sortOrderItem2);
    assertThat(actual)
        .extracting(SortOrder::getItems, list(SortOrderItem.class))
        .containsExactlyElementsOf(expected);
  }

  /**
   * By sort orders with default as text.
   */
  @Test
  void bySortOrdersWithDefaultAsText() {
    SortOrderItem sortOrderItem0 = new SortOrderItem(
        "i0", Direction.ASC, CaseHandling.SENSITIVE, NullHandling.NULLS_FIRST);
    SortOrderItem sortOrderItem1 = new SortOrderItem(
        "i1", Direction.DESC, CaseHandling.INSENSITIVE, NullHandling.NULLS_LAST);
    SortOrderItem sortOrderItem2 = new SortOrderItem(
        "i2", Direction.DESC, CaseHandling.INSENSITIVE, NullHandling.NATIVE);
    SortOrder actual = SortOrder.by(null,
        "i0;asc;sensitive;nulls-first,i1;desc;insensitive;nulls-last,i2;desc;insensitive;native");
    List<SortOrderItem> expected = List.of(sortOrderItem0, sortOrderItem1, sortOrderItem2);
    assertThat(actual)
        .extracting(SortOrder::getItems, list(SortOrderItem.class))
        .containsExactlyElementsOf(expected);
  }

}
