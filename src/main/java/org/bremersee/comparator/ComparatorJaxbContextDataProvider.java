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

import java.util.Collection;
import java.util.List;
import org.bremersee.comparator.model.ObjectFactory;
import org.bremersee.xml.JaxbContextData;
import org.bremersee.xml.JaxbContextDataProvider;

/**
 * @author Christian Bremer
 */
public class ComparatorJaxbContextDataProvider implements JaxbContextDataProvider {

  /**
   * The GPX XML name space.
   */
  // TODO text with package-info
  public static final String NAMESPACE = "http://bremersee.org/xmlschemas/comparator/v3";

  @Override
  public Collection<JaxbContextData> getJaxbContextData() {
    return List.of(new JaxbContextData(
        ObjectFactory.class.getPackage(),
        "http://bremersee.github.io/xmlschemas/comparator-v3.xsd"));
  }
}
