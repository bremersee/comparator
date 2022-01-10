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
 * The simple test object with getter.
 *
 * @author Christian Bremer
 */
@ToString
public class SimpleGetObject {

  private final int no;

  private int anotherNumber;

  /**
   * Instantiates a new simple object with getter.
   *
   * @param number the number
   */
  public SimpleGetObject(int number) {
    this.no = number;
  }

  /**
   * Instantiates a new Simple get object.
   *
   * @param no the no
   * @param anotherNumber the another number
   */
  public SimpleGetObject(int no, int anotherNumber) {
    this.no = no;
    this.anotherNumber = anotherNumber;
  }

  private int getNumber() {
    return no;
  }

  /**
   * Gets another number.
   *
   * @return the another number
   */
  @SuppressWarnings("unused")
  public int getAnotherNumber() {
    return anotherNumber;
  }

  /**
   * Sets another number.
   *
   * @param anotherNumber the another number
   */
  @SuppressWarnings("unused")
  public void setAnotherNumber(int anotherNumber) {
    this.anotherNumber = anotherNumber;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof SimpleGetObject)) {
      return false;
    }
    SimpleGetObject that = (SimpleGetObject) o;
    return getNumber() == that.getNumber() && anotherNumber == that.anotherNumber;
  }

  @Override
  public int hashCode() {
    return Objects.hash(getNumber(), anotherNumber);
  }
}
