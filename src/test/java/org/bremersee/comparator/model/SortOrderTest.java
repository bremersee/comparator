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
import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.bremersee.comparator.model.SortOrder.CaseHandling;
import org.bremersee.comparator.model.SortOrder.Direction;
import org.bremersee.comparator.model.SortOrder.NullHandling;
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
   * Test xml sort order.
   *
   * @throws Exception the exception
   */
  @Test
  void testXmlSortOrder() throws Exception {
    SortOrder sortOrder = new SortOrder("i0", true, false, true);

    Marshaller marshaller = jaxbContext.createMarshaller();
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

    StringWriter sw = new StringWriter();

    marshaller.marshal(sortOrder, sw);

    String xmlStr = sw.toString();

    SortOrder readField = (SortOrder) jaxbContext.createUnmarshaller()
        .unmarshal(new StringReader(xmlStr));

    assertThat(readField)
        .as("Write and read xml of %s", sortOrder)
        .isEqualTo(sortOrder);
  }

  /**
   * Test json sort order.
   *
   * @throws Exception the exception
   */
  @Test
  void testJsonSortOrder() throws Exception {
    SortOrder sortOrder = new SortOrder("i0", true, false, true);

    ObjectMapper om = new ObjectMapper();

    String jsonStr = om.writerWithDefaultPrettyPrinter().writeValueAsString(sortOrder);

    SortOrder readField = om.readValue(jsonStr, SortOrder.class);

    assertThat(readField)
        .as("Write and read json of %s", sortOrder)
        .isEqualTo(sortOrder);
  }

  /**
   * Test equals and hash code.
   *
   * @param softly the soft assertions
   */
  @SuppressWarnings({"UnnecessaryLocalVariable"})
  @Test
  void testEqualsAndHashCode(SoftAssertions softly) {
    SortOrder sortOrder0 = new SortOrder("i0", true, false, true);
    SortOrder sortOrder1 = sortOrder0;
    SortOrder sortOrder2 = new SortOrder("i0", true, false, true);

    softly.assertThat(sortOrder0.hashCode()).isEqualTo(sortOrder2.hashCode());
    //noinspection EqualsWithItself
    softly.assertThat(sortOrder0.equals(sortOrder0)).isTrue();
    //noinspection ConstantConditions
    softly.assertThat(sortOrder0.equals(sortOrder1)).isTrue();
    softly.assertThat(sortOrder0.equals(sortOrder2)).isTrue();

    SortOrder sortOrder3 = new SortOrder("i1", true, false, true);
    softly.assertThat(sortOrder0.equals(sortOrder3)).isFalse();

    //noinspection EqualsBetweenInconvertibleTypes
    softly.assertThat(sortOrder0.equals(new SortOrders())).isFalse();
  }

  /**
   * Test to sort order.
   *
   * @param softly the soft assertions
   */
  @Test
  void testToSortOrderText(SoftAssertions softly) {
    SortOrder sortOrder0 = new SortOrder("i0", true, false, true);
    SortOrder sortOrder1 = new SortOrder(null, false, true, false);
    softly.assertThat(sortOrder0.toSortOrderText())
        .as("Create sort order text of %s", sortOrder0)
        .isEqualTo("i0,asc,false,true");
    softly.assertThat(sortOrder0.toString())
        .as("toString is equal to sort order text")
        .isEqualTo(sortOrder0.toSortOrderText());

    softly.assertThat(sortOrder1.toSortOrderText(SortOrdersTextProperties.builder()
            .sortOrderArgsSeparator("-")
            .ascValue("true")
            .descValue("false")
            .build()))
        .as("Create sort order text with custom properties of %s", sortOrder0)
        .isEqualTo("-false-true-false");
  }

  /**
   * Test from sort order text.
   *
   * @param softly the softly
   */
  @Test
  void testFromSortOrderText(SoftAssertions softly) {
    SortOrder actual = SortOrder.fromSortOrderText("field0,desc,false,true");
    SortOrder expected = new SortOrder("field0", false, false, true);

    softly.assertThat(actual)
        .isEqualTo(expected);

    softly.assertThat(SortOrder.fromSortOrderText(null))
        .isEqualTo(new SortOrder(null, true, true, false));
  }

  /**
   * Test from sort order text with properties.
   *
   * @param softly the soft assertions
   */
  @Test
  void testFromSortOrderTextWithProperties(SoftAssertions softly) {
    SortOrdersTextProperties properties = SortOrdersTextProperties.builder()
        .sortOrderArgsSeparator("::")
        .caseSensitiveValue("cs")
        .caseInsensitiveValue("cis")
        .nullIsFirstValue("nif")
        .nullIsLastValue("nil")
        .build();

    SortOrder actual = SortOrder.fromSortOrderText("::asc::cis::nif", properties);
    SortOrder expected = new SortOrder(null, true, true, true);
    softly.assertThat(actual).isEqualTo(expected);

    actual = SortOrder.fromSortOrderText("field1", properties);
    expected = new SortOrder("field1", true, true, false);
    softly.assertThat(actual).isEqualTo(expected);

    actual = SortOrder.fromSortOrderText("field2::desc", properties);
    expected = new SortOrder("field2", false, true, false);
    softly.assertThat(actual).isEqualTo(expected);

    actual = SortOrder.fromSortOrderText("field3::desc::cs", properties);
    expected = new SortOrder("field3", false, false, false);
    softly.assertThat(actual).isEqualTo(expected);

    actual = SortOrder.fromSortOrderText("field4::desc::cs::nif", properties);
    expected = new SortOrder("field4", false, false, true);
    softly.assertThat(actual).isEqualTo(expected);

    actual = SortOrder.fromSortOrderText("::desc", properties);
    expected = new SortOrder(null, false, true, false);
    softly.assertThat(actual).isEqualTo(expected);
  }

  /**
   * Test builder.
   *
   * @param softly the soft assertions
   */
  @Test
  void testBuilder(SoftAssertions softly) {
    SortOrder actual = SortOrder.by("home");
    softly.assertThat(actual)
        .isEqualTo(new SortOrder("home", true, true, false));

    actual = actual.with((Direction) null);
    softly.assertThat(actual)
        .isEqualTo(new SortOrder("home", true, true, false));
    actual = actual.with(Direction.DESC);
    softly.assertThat(actual)
        .isEqualTo(new SortOrder("home", false, true, false));

    actual = actual.with((CaseHandling) null);
    softly.assertThat(actual)
        .isEqualTo(new SortOrder("home", false, true, false));
    actual = actual.with(CaseHandling.SENSITIVE);
    softly.assertThat(actual)
        .isEqualTo(new SortOrder("home", false, false, false));

    actual = actual.with((NullHandling) null);
    softly.assertThat(actual)
        .isEqualTo(new SortOrder("home", false, false, false));
    actual = actual.with(NullHandling.NULLS_FIRST);
    softly.assertThat(actual)
        .isEqualTo(new SortOrder("home", false, false, true));
  }

}
