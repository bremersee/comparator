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

package org.bremersee.comparator.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * The comparator field tests.
 *
 * @author Christian Bremer
 */
class ComparatorFieldTests {

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

    System.out.println("Testing XML write-read operations ...");

    ComparatorField field = new ComparatorField("i0", true, false, true);

    Marshaller marshaller = jaxbContext.createMarshaller();
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

    StringWriter sw = new StringWriter();

    marshaller.marshal(field, sw);

    String xmlStr = sw.toString();

    System.out.println(xmlStr);

    ComparatorField readField = (ComparatorField) jaxbContext.createUnmarshaller()
        .unmarshal(new StringReader(xmlStr));

    System.out.println(field);

    assertEquals(field, readField);

    System.out.println("OK\n");
  }

  /**
   * Test json comparator field.
   *
   * @throws Exception the exception
   */
  @Test
  void testJsonComparatorItem() throws Exception {

    System.out.println("Testing JSON write-read operations ...");

    ComparatorField field = new ComparatorField("i0", true, false, true);

    ObjectMapper om = new ObjectMapper();

    String jsonStr = om.writerWithDefaultPrettyPrinter().writeValueAsString(field);

    System.out.println(jsonStr);

    ComparatorField readField = om.readValue(jsonStr, ComparatorField.class);

    System.out.println(field);

    assertEquals(field, readField);

    System.out.println("OK\n");
  }

  /**
   * Test equals and hash code.
   */
  @SuppressWarnings({"UnnecessaryLocalVariable"})
  @Test
  void testEqualsAndHashCode() {
    ComparatorField field0 = new ComparatorField("i0", true, false, true);
    ComparatorField field1 = field0;
    ComparatorField field2 = new ComparatorField("i0", true, false, true);
    assertEquals(field0.hashCode(), field2.hashCode());
    assertEquals(field0, field0);
    assertEquals(field0, field1);
    assertEquals(field0, field2);

    ComparatorField field3 = new ComparatorField("i1", true, false, true);
    assertNotEquals(field0, field3);

    //noinspection EqualsBetweenInconvertibleTypes,SimplifiableJUnitAssertion
    assertFalse(field0.equals(new ComparatorFields()));
  }

  /**
   * Test to wkt.
   */
  @Test
  void testToWkt() {
    ComparatorField field0 = new ComparatorField("i0", true, false, true);
    ComparatorField field1 = new ComparatorField(null, false, false, true);
    assertEquals("i0,asc,false,true", field0.toWkt());
    assertEquals(",desc,false,true", field1.toWkt());
  }

}
