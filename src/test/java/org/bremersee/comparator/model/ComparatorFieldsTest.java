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
 * The comparator fields tests.
 *
 * @author Christian Bremer
 */
@ExtendWith(SoftAssertionsExtension.class)
class ComparatorFieldsTest {

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
   * Test xml comparator fields.
   *
   * @throws Exception the exception
   */
  @Test
  void testXmlComparatorFields() throws Exception {
    ComparatorField field0 = new ComparatorField("i0", true, true, false);
    ComparatorField field1 = new ComparatorField("i1", false, true, false);
    ComparatorField field2 = new ComparatorField("i2", true, true, false);

    ComparatorFields fields = new ComparatorFields(List.of(field0, field1, field2));

    Marshaller marshaller = jaxbContext.createMarshaller();
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

    StringWriter sw = new StringWriter();

    marshaller.marshal(fields, sw);

    String xmlStr = sw.toString();

    ComparatorFields readFields = (ComparatorFields) jaxbContext.createUnmarshaller()
        .unmarshal(new StringReader(xmlStr));

    assertThat(readFields)
        .as("Write and read xml of %s", fields)
        .isEqualTo(fields);
  }

  /**
   * Test json comparator item.
   *
   * @throws Exception the exception
   */
  @Test
  void testJsonComparatorFields() throws Exception {
    ComparatorField field0 = new ComparatorField("i0", true, false, true);
    ComparatorField field1 = new ComparatorField("i1", false, true, false);

    ComparatorFields fields = new ComparatorFields(List.of(field0, field1));

    ObjectMapper om = new ObjectMapper();

    String jsonStr = om.writerWithDefaultPrettyPrinter().writeValueAsString(fields);

    ComparatorFields readFields = om.readValue(jsonStr, ComparatorFields.class);

    assertThat(readFields)
        .as("Write and read json of %s", fields)
        .isEqualTo(fields);
  }

  /**
   * Test equals and hash code.
   *
   * @param softly the soft assertions
   */
  @Test
  void testEqualsAndHashCode(SoftAssertions softly) {
    ComparatorField field0 = new ComparatorField("i0", true, false, true);
    ComparatorField field1 = new ComparatorField("i1", true, false, true);
    ComparatorField field2 = new ComparatorField("i0", true, false, true);
    ComparatorField field3 = new ComparatorField("i1", true, false, true);
    ComparatorFields fields0 = new ComparatorFields(List.of(field0, field1));
    ComparatorFields fields2 = new ComparatorFields(List.of(field2, field3));

    softly.assertThat(fields0.hashCode()).isEqualTo(fields2.hashCode());

    //noinspection UnnecessaryLocalVariable
    ComparatorFields fields1 = fields0;
    //noinspection ConstantConditions
    softly.assertThat(fields0.equals(fields1)).isTrue();
    softly.assertThat(fields0.equals(fields2)).isTrue();

    ComparatorFields fields3 = new ComparatorFields(List.of(field1, field3));
    softly.assertThat(fields3.equals(fields0)).isFalse();
    //noinspection EqualsBetweenInconvertibleTypes
    softly.assertThat(fields0.equals(field0)).isFalse();

    softly.assertThat(new ComparatorFields(null).equals(new ComparatorFields())).isTrue();
  }

  /**
   * Test to wkt.
   *
   * @param softly the soft assertions
   */
  @Test
  void testToWkt(SoftAssertions softly) {
    ComparatorField field0 = new ComparatorField("i0", true, false, true);
    ComparatorField field1 = new ComparatorField("i1", false, true, false);
    ComparatorFields fields0 = new ComparatorFields(List.of(field0, field1));
    String actual = fields0.toWkt();
    softly.assertThat(actual)
        .as("Create wkt of %s", fields0)
        .isEqualTo("i0,asc,false,true;i1,desc,true,false");
    softly.assertThat(fields0.toString())
        .as("toString is equal to WKT")
        .isEqualTo(actual);

    actual = fields0.toWkt(WellKnownTextProperties.builder()
        .fieldSeparator("&")
        .fieldArgsSeparator(":")
        .build());
    softly.assertThat(actual)
        .as("Create wkt with custom properties of %s", fields0)
        .isEqualTo("i0:asc:false:true&i1:desc:true:false");
  }

  /**
   * Test from wkt.
   */
  @Test
  void testFromWkt(SoftAssertions softly) {
    ComparatorFields actual = ComparatorFields.fromWkt(null);
    softly.assertThat(actual)
        .extracting(ComparatorFields::getFields, list(ComparatorField.class))
        .isEmpty();

    actual = ComparatorFields.fromWkt(
        "field0,asc,true,true");
    ComparatorField field0 = new ComparatorField("field0", true, true, true);
    List<ComparatorField> expected = List.of(field0);
    softly.assertThat(actual)
        .extracting(ComparatorFields::getFields, list(ComparatorField.class))
        .containsExactlyElementsOf(expected);

    actual = ComparatorFields.fromWkt(
        "field0,asc,true,true"
            + ";field1,asc,false,true"
            + ";field2,asc,true,false"
            + ";field3,asc,false,false"
            + ";field4,desc,false,true"
            + ";field5,desc,false,false");
    ComparatorField field1 = new ComparatorField("field1", true, false, true);
    ComparatorField field2 = new ComparatorField("field2", true, true, false);
    ComparatorField field3 = new ComparatorField("field3", true, false, false);
    ComparatorField field4 = new ComparatorField("field4", false, false, true);
    ComparatorField field5 = new ComparatorField("field5", false, false, false);
    expected = List.of(field0, field1, field2, field3, field4, field5);
    softly.assertThat(actual)
        .extracting(ComparatorFields::getFields, list(ComparatorField.class))
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

    ComparatorFields actual = ComparatorFields.fromWkt(
        "-:-asc-:-cis-:-nif"
            + "&&field1"
            + "&&field2-:-desc"
            + "&&field3-:-desc-:-cs"
            + "&&field4-:-desc-:-cs-:-nif"
            + "&&-:-desc",
        properties
    );

    ComparatorField field0 = new ComparatorField(null, true, true, true);
    ComparatorField field1 = new ComparatorField("field1", true, true, false);
    ComparatorField field2 = new ComparatorField("field2", false, true, false);
    ComparatorField field3 = new ComparatorField("field3", false, false, false);
    ComparatorField field4 = new ComparatorField("field4", false, false, true);
    ComparatorField field5 = new ComparatorField(null, false, true, false);
    List<ComparatorField> expected = List.of(field0, field1, field2, field3, field4, field5);

    assertThat(actual)
        .extracting(ComparatorFields::getFields, list(ComparatorField.class))
        .containsExactlyElementsOf(expected);
  }

}
