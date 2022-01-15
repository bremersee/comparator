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
    SortOrder field = new SortOrder("i0", true, false, true);

    Marshaller marshaller = jaxbContext.createMarshaller();
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

    StringWriter sw = new StringWriter();

    marshaller.marshal(field, sw);

    String xmlStr = sw.toString();

    SortOrder readField = (SortOrder) jaxbContext.createUnmarshaller()
        .unmarshal(new StringReader(xmlStr));

    assertThat(readField)
        .as("Write and read xml of %s", field)
        .isEqualTo(field);
  }

  /**
   * Test json sort order.
   *
   * @throws Exception the exception
   */
  @Test
  void testJsonComparatorItem() throws Exception {
    SortOrder field = new SortOrder("i0", true, false, true);

    ObjectMapper om = new ObjectMapper();

    String jsonStr = om.writerWithDefaultPrettyPrinter().writeValueAsString(field);

    SortOrder readField = om.readValue(jsonStr, SortOrder.class);

    assertThat(readField)
        .as("Write and read json of %s", field)
        .isEqualTo(field);
  }

  /**
   * Test equals and hash code.
   *
   * @param softly the soft assertions
   */
  @SuppressWarnings({"UnnecessaryLocalVariable"})
  @Test
  void testEqualsAndHashCode(SoftAssertions softly) {
    SortOrder field0 = new SortOrder("i0", true, false, true);
    SortOrder field1 = field0;
    SortOrder field2 = new SortOrder("i0", true, false, true);

    softly.assertThat(field0.hashCode()).isEqualTo(field2.hashCode());
    //noinspection EqualsWithItself
    softly.assertThat(field0.equals(field0)).isTrue();
    //noinspection ConstantConditions
    softly.assertThat(field0.equals(field1)).isTrue();
    softly.assertThat(field0.equals(field2)).isTrue();

    SortOrder field3 = new SortOrder("i1", true, false, true);
    softly.assertThat(field0.equals(field3)).isFalse();

    //noinspection EqualsBetweenInconvertibleTypes
    softly.assertThat(field0.equals(new SortOrders())).isFalse();
  }

  /**
   * Test to wkt.
   *
   * @param softly the soft assertions
   */
  @Test
  void testToWkt(SoftAssertions softly) {
    SortOrder field0 = new SortOrder("i0", true, false, true);
    SortOrder field1 = new SortOrder(null, false, true, false);
    softly.assertThat(field0.toWkt())
        .as("Create wkt of %s", field0)
        .isEqualTo("i0,asc,false,true");
    softly.assertThat(field0.toString())
        .as("toString is equal to WKT")
        .isEqualTo(field0.toWkt());

    softly.assertThat(field1.toWkt(WellKnownTextProperties.builder()
            .fieldArgsSeparator("-")
            .ascValue("true")
            .descValue("false")
            .build()))
        .as("Create wkt with custom properties of %s", field0)
        .isEqualTo("-false-true-false");
  }

  /**
   * Test from wkt.
   *
   * @param softly the softly
   */
  @Test
  void testFromWkt(SoftAssertions softly) {
    SortOrder actual = SortOrder.fromWkt("field0,desc,false,true");
    SortOrder expected = new SortOrder("field0", false, false, true);

    softly.assertThat(actual)
        .isEqualTo(expected);

    softly.assertThat(SortOrder.fromWkt(null))
        .isEqualTo(new SortOrder(null, true, true, false));
  }

  /**
   * Test from wkt with properties.
   *
   * @param softly the softly
   */
  @Test
  void testFromWktWithProperties(SoftAssertions softly) {
    WellKnownTextProperties properties = WellKnownTextProperties.builder()
        .fieldArgsSeparator("::")
        .caseSensitiveValue("cs")
        .caseInsensitiveValue("cis")
        .nullIsFirstValue("nif")
        .nullIsLastValue("nil")
        .build();

    SortOrder actual = SortOrder.fromWkt("::asc::cis::nif", properties);
    SortOrder expected = new SortOrder(null, true, true, true);
    softly.assertThat(actual).isEqualTo(expected);

    actual = SortOrder.fromWkt("field1", properties);
    expected = new SortOrder("field1", true, true, false);
    softly.assertThat(actual).isEqualTo(expected);

    actual = SortOrder.fromWkt("field2::desc", properties);
    expected = new SortOrder("field2", false, true, false);
    softly.assertThat(actual).isEqualTo(expected);

    actual = SortOrder.fromWkt("field3::desc::cs", properties);
    expected = new SortOrder("field3", false, false, false);
    softly.assertThat(actual).isEqualTo(expected);

    actual = SortOrder.fromWkt("field4::desc::cs::nif", properties);
    expected = new SortOrder("field4", false, false, true);
    softly.assertThat(actual).isEqualTo(expected);

    actual = SortOrder.fromWkt("::desc", properties);
    expected = new SortOrder(null, false, true, false);
    softly.assertThat(actual).isEqualTo(expected);
  }

}
