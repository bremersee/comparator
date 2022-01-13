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
 * The comparator field tests.
 *
 * @author Christian Bremer
 */
@ExtendWith(SoftAssertionsExtension.class)
class ComparatorFieldTest {

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
   * Test xml comparator field.
   *
   * @throws Exception the exception
   */
  @Test
  void testXmlComparatorField() throws Exception {
    ComparatorField field = new ComparatorField("i0", true, false, true);

    Marshaller marshaller = jaxbContext.createMarshaller();
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

    StringWriter sw = new StringWriter();

    marshaller.marshal(field, sw);

    String xmlStr = sw.toString();

    ComparatorField readField = (ComparatorField) jaxbContext.createUnmarshaller()
        .unmarshal(new StringReader(xmlStr));

    assertThat(readField)
        .as("Write and read xml of %s", field)
        .isEqualTo(field);
  }

  /**
   * Test json comparator field.
   *
   * @throws Exception the exception
   */
  @Test
  void testJsonComparatorItem() throws Exception {
    ComparatorField field = new ComparatorField("i0", true, false, true);

    ObjectMapper om = new ObjectMapper();

    String jsonStr = om.writerWithDefaultPrettyPrinter().writeValueAsString(field);

    ComparatorField readField = om.readValue(jsonStr, ComparatorField.class);

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
    ComparatorField field0 = new ComparatorField("i0", true, false, true);
    ComparatorField field1 = field0;
    ComparatorField field2 = new ComparatorField("i0", true, false, true);

    softly.assertThat(field0.hashCode()).isEqualTo(field2.hashCode());
    //noinspection EqualsWithItself
    softly.assertThat(field0.equals(field0)).isTrue();
    //noinspection ConstantConditions
    softly.assertThat(field0.equals(field1)).isTrue();
    softly.assertThat(field0.equals(field2)).isTrue();

    ComparatorField field3 = new ComparatorField("i1", true, false, true);
    softly.assertThat(field0.equals(field3)).isFalse();

    //noinspection EqualsBetweenInconvertibleTypes
    softly.assertThat(field0.equals(new ComparatorFields())).isFalse();
  }

  /**
   * Test to wkt.
   *
   * @param softly the soft assertions
   */
  @Test
  void testToWkt(SoftAssertions softly) {
    ComparatorField field0 = new ComparatorField("i0", true, false, true);
    ComparatorField field1 = new ComparatorField(null, false, true, false);
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
    ComparatorField actual = ComparatorField.fromWkt("field0,desc,false,true");
    ComparatorField expected = new ComparatorField("field0", false, false, true);

    softly.assertThat(actual)
        .isEqualTo(expected);

    softly.assertThat(ComparatorField.fromWkt(null))
        .isEqualTo(new ComparatorField(null, true, true, false));
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

    ComparatorField actual = ComparatorField.fromWkt("::asc::cis::nif", properties);
    ComparatorField expected = new ComparatorField(null, true, true, true);
    softly.assertThat(actual).isEqualTo(expected);

    actual = ComparatorField.fromWkt("field1", properties);
    expected = new ComparatorField("field1", true, true, false);
    softly.assertThat(actual).isEqualTo(expected);

    actual = ComparatorField.fromWkt("field2::desc", properties);
    expected = new ComparatorField("field2", false, true, false);
    softly.assertThat(actual).isEqualTo(expected);

    actual = ComparatorField.fromWkt("field3::desc::cs", properties);
    expected = new ComparatorField("field3", false, false, false);
    softly.assertThat(actual).isEqualTo(expected);

    actual = ComparatorField.fromWkt("field4::desc::cs::nif", properties);
    expected = new ComparatorField("field4", false, false, true);
    softly.assertThat(actual).isEqualTo(expected);

    actual = ComparatorField.fromWkt("::desc", properties);
    expected = new ComparatorField(null, false, true, false);
    softly.assertThat(actual).isEqualTo(expected);
  }

}
