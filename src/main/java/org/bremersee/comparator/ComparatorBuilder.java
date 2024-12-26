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

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import org.bremersee.comparator.model.SortOrder;
import org.bremersee.comparator.model.SortOrderItem;

/**
 * The comparator builder.
 *
 * @author Christian Bremer
 */
public interface ComparatorBuilder {

  /**
   * Creates a new comparator builder.
   *
   * @return the new comparator builder
   */
  static ComparatorBuilder newInstance() {
    return new DefaultComparatorBuilder();
  }

  /**
   * Adds the given comparator to this builder.
   *
   * @param comparator the comparator (can be {@code null} - then no comparator is added)
   * @return the comparator builder
   */
  ComparatorBuilder add(Comparator<?> comparator);

  /**
   * Adds a default comparator for the given field (the value of the field must be comparable).
   *
   * @param field the field
   * @return the comparator builder
   */
  default ComparatorBuilder add(String field) {
    return add(SortOrderItem.by(field));
  }

  /**
   * Adds the given comparator for the given field name or path to this builder.
   *
   * @param field the field name or path (can be {@code null})
   * @param comparator the comparator (can be {@code null} - then no comparator is added)
   * @return the comparator builder
   */
  default ComparatorBuilder add(String field, Comparator<?> comparator) {
    return add(field, null, comparator);
  }

  /**
   * Adds the given comparator for the given field name or path to this builder. A custom value
   * extractor can be specified.
   *
   * @param field the field name or path (can be {@code null})
   * @param valueExtractor the value extractor (can be {@code null})
   * @param comparator the comparator (can be {@code null} - then no comparator is added)
   * @return the comparator builder
   */
  default ComparatorBuilder add(
      String field,
      ValueExtractor valueExtractor,
      Comparator<?> comparator) {

    return Optional.ofNullable(comparator)
        .map(c -> add(new DelegatingComparator(field, valueExtractor, c)))
        .orElse(this);
  }

  /**
   * Creates and adds a value comparator for the given field ordering description.
   *
   * @param field the field ordering description (can be {@code null})
   * @return the comparator builder
   */
  default ComparatorBuilder add(SortOrderItem field) {
    return add(field, null);
  }

  /**
   * Creates and adds a value comparator for the given field ordering description. A custom value
   * extractor can be specified.
   *
   * @param field the field ordering description (can be {@code null})
   * @param valueExtractor the value extractor (can be {@code null})
   * @return the comparator builder
   */
  default ComparatorBuilder add(SortOrderItem field, ValueExtractor valueExtractor) {
    return add(new ValueComparator(field, valueExtractor));
  }

  /**
   * Creates and adds value comparators for the given field ordering descriptions.
   *
   * @param sortOrders the ordering descriptions (can be {@code null} - no comparator will be
   *     added)
   * @return the comparator builder
   */
  default ComparatorBuilder addAll(Collection<? extends SortOrderItem> sortOrders) {
    return addAll(sortOrders, (ValueExtractor) null);
  }

  /**
   * Creates and adds value comparators for the given field ordering descriptions. A custom value
   * extractor can be specified.
   *
   * @param sortOrders the ordering descriptions (can be {@code null} - no comparator will be
   *     added)
   * @param valueExtractor the value extractor (can be {@code null})
   * @return the comparator builder
   */
  default ComparatorBuilder addAll(
      Collection<? extends SortOrderItem> sortOrders,
      ValueExtractor valueExtractor) {
    Optional.ofNullable(sortOrders)
        .ifPresent(orders -> orders.stream()
            .filter(Objects::nonNull)
            .forEach(sortOrder -> add(sortOrder, valueExtractor)));
    return this;
  }

  /**
   * Adds comparators for the given sortOrders ordering descriptions.
   *
   * @param sortOrders the ordering descriptions (can be {@code null} - no comparator will be
   *     added)
   * @param comparatorFunction the comparator function
   * @return the comparator builder
   */
  default ComparatorBuilder addAll(
      Collection<? extends SortOrderItem> sortOrders,
      Function<SortOrderItem, Comparator<?>> comparatorFunction) {

    Optional.ofNullable(sortOrders)
        .ifPresent(orders -> orders.stream()
            .filter(Objects::nonNull)
            .forEach(sortOrder -> add(comparatorFunction.apply(sortOrder))));
    return this;
  }

  /**
   * Creates and adds value comparators for the given field ordering descriptions.
   *
   * @param sortOrder the ordering descriptions (can be {@code null} - no comparator will be
   *     added)
   * @return the comparator builder
   */
  default ComparatorBuilder addAll(SortOrder sortOrder) {
    return addAll(sortOrder, (ValueExtractor) null);
  }

  /**
   * Creates and adds value comparators for the given field ordering descriptions. A custom value
   * extractor can be specified.
   *
   * @param sortOrder the ordering descriptions (can be {@code null} - no comparator will be
   *     added)
   * @param valueExtractor the value extractor (can be {@code null})
   * @return the comparator builder
   */
  default ComparatorBuilder addAll(
      SortOrder sortOrder,
      ValueExtractor valueExtractor) {
    return Optional.ofNullable(sortOrder)
        .map(orders -> addAll(orders.getItems(), valueExtractor))
        .orElse(this);
  }

  /**
   * Add all comparator builder.
   *
   * @param sortOrder the sort orders
   * @param comparatorFunction the comparator function
   * @return the comparator builder
   */
  default ComparatorBuilder addAll(
      SortOrder sortOrder,
      Function<SortOrderItem, Comparator<?>> comparatorFunction) {
    return Optional.ofNullable(sortOrder)
        .map(orders -> addAll(orders.getItems(), comparatorFunction))
        .orElse(this);
  }

  /**
   * Creates and adds value comparators for the given field ordering descriptions.
   *
   * @param sortOrderText the ordering descriptions (can be {@code null} - no comparator will be
   *     added)
   * @return the comparator builder
   */
  default ComparatorBuilder addAll(String sortOrderText) {
    return addAll(SortOrder.fromSortOrderText(sortOrderText));
  }

  /**
   * Creates and adds value comparators for the given field ordering descriptions. A custom value
   * extractor can be specified.
   *
   * @param sortOrderText the ordering descriptions (can be {@code null} - no comparator will be
   *     added)
   * @param valueExtractor the value extractor (can be {@code null})
   * @return the comparator builder
   */
  default ComparatorBuilder addAll(
      String sortOrderText,
      ValueExtractor valueExtractor) {
    return addAll(SortOrder.fromSortOrderText(sortOrderText), valueExtractor);
  }

  /**
   * Add all comparator builder.
   *
   * @param sortOrderText the sort orders
   * @param comparatorFunction the comparator function
   * @return the comparator builder
   */
  default ComparatorBuilder addAll(
      String sortOrderText,
      Function<SortOrderItem, Comparator<?>> comparatorFunction) {
    return addAll(SortOrder.fromSortOrderText(sortOrderText), comparatorFunction);
  }

  /**
   * Build comparator.
   *
   * @param <T> the type parameter
   * @return the comparator
   */
  <T> Comparator<T> build();

  /**
   * The default comparator builder.
   */
  class DefaultComparatorBuilder implements ComparatorBuilder {

    @SuppressWarnings("rawtypes")
    private final List<Comparator> comparatorChain = new LinkedList<>();

    @Override
    public ComparatorBuilder add(Comparator<?> comparator) {
      Optional.ofNullable(comparator)
          .ifPresent(comparatorChain::add);
      return this;
    }

    @Override
    public <T> Comparator<T> build() {
      //noinspection unchecked
      return (Comparator<T>) new ComparatorChain(comparatorChain);
    }
  }

}
