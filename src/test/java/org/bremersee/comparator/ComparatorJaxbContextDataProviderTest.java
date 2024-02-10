/*
 * Copyright 2022 the original author or authors.
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.core.annotation.AnnotationUtils.findAnnotation;

import jakarta.xml.bind.annotation.XmlSchema;
import java.util.Collection;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.bremersee.comparator.model.ObjectFactory;
import org.bremersee.xml.JaxbContextMember;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * The comparator jaxb context data provider test.
 *
 * @author Christian Bremer
 */
@ExtendWith(SoftAssertionsExtension.class)
class ComparatorJaxbContextDataProviderTest {

  /**
   * Get namespace.
   */
  @Test
  void getNamespace() {
    XmlSchema schema = findAnnotation(ObjectFactory.class.getPackage(), XmlSchema.class);
    String expected = schema != null ? schema.namespace() : null;
    String actual = ComparatorJaxbContextDataProvider.getNamespace();
    assertThat(actual)
        .isEqualTo(expected);
  }

  /**
   * Gets jaxb context data.
   *
   * @param softly the soft assertions
   */
  @Test
  void getJaxbContextData(SoftAssertions softly) {
    ComparatorJaxbContextDataProvider target = new ComparatorJaxbContextDataProvider();
    Collection<JaxbContextMember> actual = target.getJaxbContextData();
    softly.assertThat(actual)
        .containsExactly(JaxbContextMember.byPackage(ObjectFactory.class.getPackage()).build());
  }
}