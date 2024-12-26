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

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import java.io.StringReader;
import java.io.StringWriter;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.bremersee.comparator.model.SortOrderItem.CaseHandling;
import org.bremersee.comparator.model.SortOrderItem.Direction;
import org.bremersee.comparator.model.SortOrderItem.NullHandling;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * The sort order item tests.
 *
 * @author Christian Bremer
 */
@ExtendWith(SoftAssertionsExtension.class)
class SortOrderItemTest {

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
   * Test read and write xml of sort order item.
   *
   * @throws Exception the exception
   */
  @Test
  void testXml() throws Exception {
    SortOrderItem sortOrderItem = new SortOrderItem("i0", Direction.ASC, CaseHandling.SENSITIVE,
        NullHandling.NULLS_FIRST);

    Marshaller marshaller = jaxbContext.createMarshaller();
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

    StringWriter sw = new StringWriter();

    marshaller.marshal(sortOrderItem, sw);

    String xmlStr = sw.toString();

    SortOrderItem readField = (SortOrderItem) jaxbContext.createUnmarshaller()
        .unmarshal(new StringReader(xmlStr));

    assertThat(readField)
        .as("Write and read xml of %s", sortOrderItem)
        .isEqualTo(sortOrderItem);
  }

  /**
   * Test read and write json of sort order item.
   *
   * @throws Exception the exception
   */
  @Test
  void testJson() throws Exception {
    SortOrderItem sortOrderItem = new SortOrderItem("i0", Direction.DESC, CaseHandling.INSENSITIVE,
        NullHandling.NULLS_LAST);

    ObjectMapper om = new ObjectMapper();

    String jsonStr = om.writerWithDefaultPrettyPrinter().writeValueAsString(sortOrderItem);

    SortOrderItem readField = om.readValue(jsonStr, SortOrderItem.class);

    assertThat(readField)
        .as("Write and read json of %s", sortOrderItem)
        .isEqualTo(sortOrderItem);
  }

  /**
   * Test equals and hash code.
   *
   * @param softly the soft assertions
   */
  @SuppressWarnings({"UnnecessaryLocalVariable"})
  @Test
  void testEqualsAndHashCode(SoftAssertions softly) {
    SortOrderItem sortOrderItem0 = new SortOrderItem(
        "i0", Direction.DESC, CaseHandling.SENSITIVE, NullHandling.NATIVE);
    SortOrderItem sortOrderItem1 = sortOrderItem0;
    SortOrderItem sortOrderItem2 = new SortOrderItem(
        "i0", Direction.DESC, CaseHandling.SENSITIVE, NullHandling.NATIVE);

    softly.assertThat(sortOrderItem0.hashCode()).isEqualTo(sortOrderItem2.hashCode());
    //noinspection EqualsWithItself
    softly.assertThat(sortOrderItem0.equals(sortOrderItem0)).isTrue();
    //noinspection ConstantConditions
    softly.assertThat(sortOrderItem0.equals(sortOrderItem1)).isTrue();
    softly.assertThat(sortOrderItem0.equals(sortOrderItem2)).isTrue();

    SortOrderItem sortOrderItem3 = new SortOrderItem(
        "i1", Direction.DESC, CaseHandling.SENSITIVE, NullHandling.NATIVE);
    softly.assertThat(sortOrderItem0.equals(sortOrderItem3)).isFalse();

    //noinspection EqualsBetweenInconvertibleTypes
    softly.assertThat(sortOrderItem0.equals(new SortOrder())).isFalse();
  }

  /**
   * Test to sort order.
   *
   * @param softly the soft assertions
   */
  @Test
  void testGetSortOrderText(SoftAssertions softly) {
    SortOrderItem sortOrderItem0 = SortOrderItem.by("")
        .with(Direction.DESC);
    softly.assertThat(sortOrderItem0.getSortOrderText())
        .as("Create sort order text of %s", sortOrderItem0)
        .isEqualTo(";desc");
    softly.assertThat(sortOrderItem0.getField())
        .isNull();

    sortOrderItem0 = SortOrderItem.by("i0");
    softly.assertThat(sortOrderItem0.getSortOrderText())
        .as("Create sort order text of %s", sortOrderItem0)
        .isEqualTo("i0");

    sortOrderItem0 = sortOrderItem0.with(Direction.DESC);
    softly.assertThat(sortOrderItem0.getSortOrderText())
        .as("Create sort order text of %s", sortOrderItem0)
        .isEqualTo("i0;desc");

    sortOrderItem0 = sortOrderItem0
        .with(Direction.ASC)
        .with(CaseHandling.SENSITIVE);
    softly.assertThat(sortOrderItem0.getSortOrderText())
        .as("Create sort order text of %s", sortOrderItem0)
        .isEqualTo("i0;asc;sensitive");

    sortOrderItem0 = sortOrderItem0
        .with(Direction.ASC)
        .with(CaseHandling.INSENSITIVE)
        .with(NullHandling.NULLS_LAST);
    softly.assertThat(sortOrderItem0.getSortOrderText())
        .as("Create sort order text of %s", sortOrderItem0)
        .isEqualTo("i0;asc;insensitive;nulls-last");

    sortOrderItem0 = sortOrderItem0
        .with(Direction.ASC)
        .with(CaseHandling.INSENSITIVE)
        .with(NullHandling.NULLS_FIRST);
    softly.assertThat(sortOrderItem0.getSortOrderText())
        .as("Create sort order text of %s", sortOrderItem0)
        .isEqualTo("i0;asc;insensitive;nulls-first");

    softly.assertThat(sortOrderItem0.toString())
        .as("toString is equal to sort order text")
        .isEqualTo(sortOrderItem0.getSortOrderText());
  }

  /**
   * Test from sort order text.
   *
   * @param softly the softly
   */
  @Test
  void testFromSortOrderText(SoftAssertions softly) {
    SortOrderItem actual = SortOrderItem.fromSortOrderText(";desc");
    SortOrderItem expected = SortOrderItem.by(null)
        .with(Direction.DESC);
    softly.assertThat(actual)
        .isEqualTo(expected);
    softly.assertThat(actual.getDirection().isDescending())
        .isTrue();

    actual = SortOrderItem.fromSortOrderText("i0");
    expected = SortOrderItem.by("i0");
    softly.assertThat(actual)
        .isEqualTo(expected);
    softly.assertThat(actual.getDirection().isAscending())
        .isTrue();

    actual = SortOrderItem.fromSortOrderText("i0;desc");
    expected = SortOrderItem.by("i0")
        .with(Direction.DESC);
    softly.assertThat(actual)
        .isEqualTo(expected);

    actual = SortOrderItem.fromSortOrderText("i0;asc;sensitive");
    expected = SortOrderItem.by("i0")
        .with(Direction.ASC)
        .with(CaseHandling.SENSITIVE);
    softly.assertThat(actual)
        .isEqualTo(expected);
    softly.assertThat(actual.getCaseHandling().isSensitive())
        .isTrue();

    actual = SortOrderItem.fromSortOrderText("i0;asc;insensitive;nulls-last");
    expected = SortOrderItem.by("i0")
        .with(Direction.ASC)
        .with(CaseHandling.INSENSITIVE)
        .with(NullHandling.NULLS_LAST);
    softly.assertThat(actual)
        .isEqualTo(expected);
    softly.assertThat(actual.getCaseHandling().isInsensitive())
        .isTrue();
    softly.assertThat(actual.getNullHandling().isNullLast())
        .isTrue();

    actual = SortOrderItem.fromSortOrderText("i0;asc;insensitive;nulls-first");
    expected = SortOrderItem.by("i0")
        .with(Direction.ASC)
        .with(CaseHandling.INSENSITIVE)
        .with(NullHandling.NULLS_FIRST);
    softly.assertThat(actual)
        .isEqualTo(expected);
    softly.assertThat(actual.getNullHandling().isNullFirst())
        .isTrue();

    actual = SortOrderItem.fromSortOrderText("i0;asc;insensitive;native");
    expected = SortOrderItem.by("i0")
        .with(Direction.ASC)
        .with(CaseHandling.INSENSITIVE)
        .with(NullHandling.NATIVE);
    softly.assertThat(actual)
        .isEqualTo(expected);
    softly.assertThat(actual.getNullHandling().isNullLast())
        .isTrue();

    softly.assertThat(SortOrderItem.fromSortOrderText(null))
        .isEqualTo(new SortOrderItem());
  }

}
