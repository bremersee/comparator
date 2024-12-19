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

import jakarta.xml.bind.annotation.XmlRegistry;

/**
 * The xml object factory.
 *
 * @author Christian Bremer
 */
@XmlRegistry
public class ObjectFactory {

  /**
   * Create sort order.
   *
   * @return the sort order
   */
  public SortOrderItem createSortOrder() {
    return new SortOrderItem();
  }

  /**
   * Create sort orders.
   *
   * @return the sort orders
   */
  public SortOrder createSortOrders() {
    return new SortOrder();
  }

}
