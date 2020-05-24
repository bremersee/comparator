/*
 * Copyright 2019 the original author or authors.
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

package org.bremersee.comparator.testmodel;

import java.util.Objects;

/**
 * The complex test object extension.
 *
 * @author Christian Bremer
 */
public class ComplexObjectExtension extends ComplexObject
    implements Comparable<ComplexObjectExtension> {

  private final String value;

  /**
   * Instantiates a new complex test object extension.
   *
   * @param simple the simple
   * @param value the value
   */
  public ComplexObjectExtension(SimpleObject simple, String value) {
    super(simple);
    this.value = value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ComplexObjectExtension)) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    ComplexObjectExtension that = (ComplexObjectExtension) o;
    return Objects.equals(value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), value);
  }

  @Override
  public int compareTo(ComplexObjectExtension o) {
    return value.compareToIgnoreCase(o.value);
  }
}
