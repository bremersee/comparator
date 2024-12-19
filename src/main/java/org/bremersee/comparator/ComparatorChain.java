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

package org.bremersee.comparator;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import lombok.ToString;

/**
 * The comparator chain has a list of comparators. Beginning with the first each comparator is
 * invoked as long the previous one returns zero on {@link Comparator#compare(Object, Object)}.
 *
 * @author Christian Bremer
 */
@SuppressWarnings({"rawtypes"})
@ToString
public class ComparatorChain implements Comparator<Object> {

  private final List<Comparator> comparators;

  /**
   * Instantiates a new comparator chain.
   *
   * @param comparators the comparators (can be {@code null} or empty)
   */
  public ComparatorChain(List<Comparator> comparators) {
    this.comparators = comparators != null ? comparators : new LinkedList<>();
  }

  @Override
  public int compare(Object o1, Object o2) {
    boolean wasCompared = false;
    for (Comparator comparator : comparators) {
      wasCompared = true;
      //noinspection unchecked
      int result = comparator.compare(o1, o2);
      if (result != 0) {
        return result;
      }
    }
    if (wasCompared) {
      return 0;
    }
    if (o1 instanceof Comparable && o2 instanceof Comparable) {
      //noinspection unchecked
      return ((Comparable) o1).compareTo(o2);
    }
    throw new ComparatorException("Comparison of objects is not possible.");
  }

}
