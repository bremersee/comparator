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

import static org.springframework.core.annotation.AnnotationUtils.findAnnotation;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import jakarta.xml.bind.annotation.XmlSchema;
import org.bremersee.comparator.model.ObjectFactory;
import org.bremersee.xml.JaxbContextDataProvider;
import org.bremersee.xml.JaxbContextMember;

/**
 * The comparator jaxb context data provider.
 *
 * @author Christian Bremer
 */
public class ComparatorJaxbContextDataProvider implements JaxbContextDataProvider {

  /**
   * Gets namespace.
   *
   * @return the namespace
   */
  public static String getNamespace() {
    return Optional.ofNullable(findAnnotation(ObjectFactory.class.getPackage(), XmlSchema.class))
        .filter(xmlSchema -> !xmlSchema.namespace().trim().isEmpty())
        .map(xmlSchema -> xmlSchema.namespace().trim())
        .orElseThrow(() -> new IllegalStateException(
            "Comparator model is missing xml namespace."));
  }

  @Override
  public Collection<JaxbContextMember> getJaxbContextData() {
    return List.of(JaxbContextMember.byPackage(ObjectFactory.class.getPackage()).build());
  }
}
