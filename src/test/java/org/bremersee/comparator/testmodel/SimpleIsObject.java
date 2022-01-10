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

package org.bremersee.comparator.testmodel;

import java.util.Objects;
import lombok.ToString;

/**
 * The simple test object of a boolean.
 *
 * @author Christian Bremer
 */
@ToString
public class SimpleIsObject {

  private final boolean value;

  /**
   * Instantiates a new simple test object of a boolean.
   *
   * @param isNice the is nice
   */
  public SimpleIsObject(boolean isNice) {
    this.value = isNice;
  }

  private boolean isNice() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof SimpleIsObject)) {
      return false;
    }
    SimpleIsObject that = (SimpleIsObject) o;
    return isNice() == that.isNice();
  }

  @Override
  public int hashCode() {
    return Objects.hash(isNice());
  }
}
