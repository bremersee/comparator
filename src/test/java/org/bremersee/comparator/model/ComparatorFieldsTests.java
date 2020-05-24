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
import java.util.Arrays;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * The comparator fields tests.
 *
 * @author Christian Bremer
 */
class ComparatorFieldsTests {

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

    System.out.println("Testing XML write-read operations ...");

    ComparatorField field0 = new ComparatorField("i0", true, true, false);
    ComparatorField field1 = new ComparatorField("i1", false, true, false);
    ComparatorField field2 = new ComparatorField("i2", true, true, false);

    ComparatorFields fields = new ComparatorFields(Arrays.asList(field0, field1, field2));

    Marshaller marshaller = jaxbContext.createMarshaller();
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

    StringWriter sw = new StringWriter();

    marshaller.marshal(fields, sw);

    String xmlStr = sw.toString();

    System.out.println(xmlStr);

    ComparatorFields readFields = (ComparatorFields) jaxbContext.createUnmarshaller()
        .unmarshal(new StringReader(xmlStr));

    System.out.println(fields);

    assertEquals(fields, readFields);

    System.out.println("OK\n");
  }

  /**
   * Test json comparator item.
   *
   * @throws Exception the exception
   */
  @Test
  void testJsonComparatorFields() throws Exception {

    System.out.println("Testing JSON write-read operations ...");

    ComparatorField field0 = new ComparatorField("i0", true, false, true);
    ComparatorField field1 = new ComparatorField("i1", false, true, false);

    ComparatorFields fields = new ComparatorFields(Arrays.asList(field0, field1));

    ObjectMapper om = new ObjectMapper();

    String jsonStr = om.writerWithDefaultPrettyPrinter().writeValueAsString(fields);

    System.out.println(jsonStr);

    ComparatorFields readFields = om.readValue(jsonStr, ComparatorFields.class);

    System.out.println(fields);

    assertEquals(fields, readFields);

    System.out.println("OK\n");
  }

  /**
   * Test equals and hash code.
   */
  @SuppressWarnings({"UnnecessaryLocalVariable"})
  @Test
  void testEqualsAndHashCode() {
    ComparatorField field0 = new ComparatorField("i0", true, false, true);
    ComparatorField field1 = new ComparatorField("i1", true, false, true);
    ComparatorField field2 = new ComparatorField("i0", true, false, true);
    ComparatorField field3 = new ComparatorField("i1", true, false, true);
    ComparatorFields fields0 = new ComparatorFields(Arrays.asList(field0, field1));
    ComparatorFields fields2 = new ComparatorFields(Arrays.asList(field2, field3));

    assertEquals(fields0.hashCode(), fields2.hashCode());
    ComparatorFields fields1 = fields0;
    assertEquals(fields0, fields1);
    assertEquals(fields0, fields2);

    ComparatorFields fields3 = new ComparatorFields(Arrays.asList(field1, field3));
    assertNotEquals(fields0, fields3);
    //noinspection EqualsBetweenInconvertibleTypes,SimplifiableJUnitAssertion
    assertFalse(fields0.equals(field0));

    assertEquals(new ComparatorFields(null), new ComparatorFields());
  }

}
