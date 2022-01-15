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
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * The sort orders tests.
 *
 * @author Christian Bremer
 */
@ExtendWith(SoftAssertionsExtension.class)
class SortOrdersTest {

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
   * Test xml sort orders.
   *
   * @throws Exception the exception
   */
  @Test
  void testXmlSortOrders() throws Exception {
    SortOrder sortOrder0 = new SortOrder("i0", true, true, false);
    SortOrder sortOrder1 = new SortOrder("i1", false, true, false);
    SortOrder sortOrder2 = new SortOrder("i2", true, true, false);

    SortOrders sortOrders = new SortOrders(List.of(sortOrder0, sortOrder1, sortOrder2));

    Marshaller marshaller = jaxbContext.createMarshaller();
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

    StringWriter sw = new StringWriter();

    marshaller.marshal(sortOrders, sw);

    String xmlStr = sw.toString();

    SortOrders readFields = (SortOrders) jaxbContext.createUnmarshaller()
        .unmarshal(new StringReader(xmlStr));

    assertThat(readFields)
        .as("Write and read xml of %s", sortOrders)
        .isEqualTo(sortOrders);
  }

  /**
   * Test json comparator item.
   *
   * @throws Exception the exception
   */
  @Test
  void testJsonSortOrders() throws Exception {
    SortOrder sortOrder0 = new SortOrder("i0", true, false, true);
    SortOrder sortOrder1 = new SortOrder("i1", false, true, false);

    SortOrders sortOrders = new SortOrders(List.of(sortOrder0, sortOrder1));

    ObjectMapper om = new ObjectMapper();

    String jsonStr = om.writerWithDefaultPrettyPrinter().writeValueAsString(sortOrders);

    SortOrders readFields = om.readValue(jsonStr, SortOrders.class);

    assertThat(readFields)
        .as("Write and read json of %s", sortOrders)
        .isEqualTo(sortOrders);
  }

  /**
   * Test equals and hash code.
   *
   * @param softly the soft assertions
   */
  @Test
  void testEqualsAndHashCode(SoftAssertions softly) {
    SortOrder sortOrder0 = new SortOrder("i0", true, false, true);
    SortOrder sortOrder1 = new SortOrder("i1", true, false, true);
    SortOrder sortOrder2 = new SortOrder("i0", true, false, true);
    SortOrder sortOrder3 = new SortOrder("i1", true, false, true);
    SortOrders sortOrders0 = new SortOrders(List.of(sortOrder0, sortOrder1));
    SortOrders sortOrders2 = new SortOrders(List.of(sortOrder2, sortOrder3));

    softly.assertThat(sortOrders0.hashCode()).isEqualTo(sortOrders2.hashCode());

    //noinspection UnnecessaryLocalVariable
    SortOrders sortOrders1 = sortOrders0;
    //noinspection ConstantConditions
    softly.assertThat(sortOrders0.equals(sortOrders1)).isTrue();
    softly.assertThat(sortOrders0.equals(sortOrders2)).isTrue();

    SortOrders sortOrders3 = new SortOrders(List.of(sortOrder1, sortOrder3));
    softly.assertThat(sortOrders3.equals(sortOrders0)).isFalse();
    //noinspection EqualsBetweenInconvertibleTypes
    softly.assertThat(sortOrders0.equals(sortOrder0)).isFalse();

    softly.assertThat(new SortOrders(null).equals(new SortOrders())).isTrue();
  }

  /**
   * Test to wkt.
   *
   * @param softly the soft assertions
   */
  @Test
  void testToWkt(SoftAssertions softly) {
    SortOrder sortOrder0 = new SortOrder("i0", true, false, true);
    SortOrder sortOrder1 = new SortOrder("i1", false, true, false);
    SortOrders sortOrders0 = new SortOrders(List.of(sortOrder0, sortOrder1));
    String actual = sortOrders0.toWkt();
    softly.assertThat(actual)
        .as("Create wkt of %s", sortOrders0)
        .isEqualTo("i0,asc,false,true;i1,desc,true,false");
    softly.assertThat(sortOrders0.toString())
        .as("toString is equal to WKT")
        .isEqualTo(actual);

    actual = sortOrders0.toWkt(WellKnownTextProperties.builder()
        .fieldSeparator("&")
        .fieldArgsSeparator(":")
        .build());
    softly.assertThat(actual)
        .as("Create wkt with custom properties of %s", sortOrders0)
        .isEqualTo("i0:asc:false:true&i1:desc:true:false");
  }

  /**
   * Test from wkt.
   */
  @Test
  void testFromWkt(SoftAssertions softly) {
    SortOrders actual = SortOrders.fromWkt(null);
    softly.assertThat(actual)
        .extracting(SortOrders::getSortOrders, list(SortOrder.class))
        .isEmpty();

    actual = SortOrders.fromWkt(
        "field0,asc,true,true");
    SortOrder sortOrder0 = new SortOrder("field0", true, true, true);
    List<SortOrder> expected = List.of(sortOrder0);
    softly.assertThat(actual)
        .extracting(SortOrders::getSortOrders, list(SortOrder.class))
        .containsExactlyElementsOf(expected);

    actual = SortOrders.fromWkt(
        "field0,asc,true,true"
            + ";field1,asc,false,true"
            + ";field2,asc,true,false"
            + ";field3,asc,false,false"
            + ";field4,desc,false,true"
            + ";field5,desc,false,false");
    SortOrder sortOrder1 = new SortOrder("field1", true, false, true);
    SortOrder sortOrder2 = new SortOrder("field2", true, true, false);
    SortOrder sortOrder3 = new SortOrder("field3", true, false, false);
    SortOrder sortOrder4 = new SortOrder("field4", false, false, true);
    SortOrder sortOrder5 = new SortOrder("field5", false, false, false);
    expected = List.of(sortOrder0, sortOrder1, sortOrder2, sortOrder3, sortOrder4, sortOrder5);
    softly.assertThat(actual)
        .extracting(SortOrders::getSortOrders, list(SortOrder.class))
        .containsExactlyElementsOf(expected);
  }

  /**
   * Test from wkt with properties.
   */
  @Test
  void testFromWktWithProperties() {
    WellKnownTextProperties properties = WellKnownTextProperties.builder()
        .fieldArgsSeparator("-:-")
        .fieldSeparator("&&")
        .caseSensitiveValue("cs")
        .caseInsensitiveValue("cis")
        .nullIsFirstValue("nif")
        .nullIsLastValue("nil")
        .build();

    SortOrders actual = SortOrders.fromWkt(
        "-:-asc-:-cis-:-nif"
            + "&&field1"
            + "&&field2-:-desc"
            + "&&field3-:-desc-:-cs"
            + "&&field4-:-desc-:-cs-:-nif"
            + "&&-:-desc",
        properties
    );

    SortOrder sortOrder0 = new SortOrder(null, true, true, true);
    SortOrder sortOrder1 = new SortOrder("field1", true, true, false);
    SortOrder sortOrder2 = new SortOrder("field2", false, true, false);
    SortOrder sortOrder3 = new SortOrder("field3", false, false, false);
    SortOrder sortOrder4 = new SortOrder("field4", false, false, true);
    SortOrder sortOrder5 = new SortOrder(null, false, true, false);
    List<SortOrder> expected = List.of(sortOrder0, sortOrder1, sortOrder2, sortOrder3, sortOrder4, sortOrder5);

    assertThat(actual)
        .extracting(SortOrders::getSortOrders, list(SortOrder.class))
        .containsExactlyElementsOf(expected);
  }

}
