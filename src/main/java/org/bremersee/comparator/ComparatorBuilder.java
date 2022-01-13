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
import org.bremersee.comparator.model.ComparatorField;
import org.bremersee.comparator.model.ComparatorFields;

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
  static ComparatorBuilder builder() {
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
    return add(field, true, true, false);
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
   * Creates and adds a value comparator for the given field name or path to this builder.
   *
   * @param field the field name or path (can be {@code null})
   * @param asc {@code true} for an ascending order, {@code false} for a descending order
   * @param ignoreCase {@code true} for a case-insensitive order, {@code false} for a
   *     case-sensitive order
   * @param nullIsFirst specifies the order of {@code null} values
   * @return the comparator builder
   */
  default ComparatorBuilder add(
      String field,
      boolean asc,
      boolean ignoreCase,
      boolean nullIsFirst) {
    return add(field, asc, ignoreCase, nullIsFirst, null);
  }

  /**
   * Creates and adds a value comparator for the given field name or path to this builder. A custom
   * value extractor can be specified.
   *
   * @param field the field name or path (can be {@code null})
   * @param asc {@code true} for an ascending order, {@code false} for a descending order
   * @param ignoreCase {@code true} for a case insensitive order,  {@code false} for a case
   *     sensitive order
   * @param nullIsFirst specifies the order of {@code null} values
   * @param valueExtractor the value extractor (can be {@code null})
   * @return the comparator builder
   */
  default ComparatorBuilder add(
      String field,
      boolean asc,
      boolean ignoreCase,
      boolean nullIsFirst,
      ValueExtractor valueExtractor) {
    return add(new ValueComparator(field, asc, ignoreCase, nullIsFirst, valueExtractor));
  }

  /**
   * Creates and adds a value comparator for the given field ordering description.
   *
   * @param field the field ordering description (can be {@code null})
   * @return the comparator builder
   */
  default ComparatorBuilder add(ComparatorField field) {
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
  default ComparatorBuilder add(ComparatorField field, ValueExtractor valueExtractor) {
    return Optional.ofNullable(field)
        .map(comparatorField -> add(
            comparatorField.getField(),
            comparatorField.isAsc(),
            comparatorField.isIgnoreCase(),
            comparatorField.isNullIsFirst(),
            valueExtractor))
        .orElse(this);
  }

  /**
   * Creates and adds value comparators for the given field ordering descriptions.
   *
   * @param fields the ordering descriptions (can be {@code null} - no comparator will be
   *     added)
   * @return the comparator builder
   */
  default ComparatorBuilder addAll(Collection<? extends ComparatorField> fields) {
    return addAll(fields, (ValueExtractor) null);
  }

  /**
   * Creates and adds value comparators for the given field ordering descriptions. A custom value
   * extractor can be specified.
   *
   * @param fields the ordering descriptions (can be {@code null} - no comparator will be
   *     added)
   * @param valueExtractor the value extractor (can be {@code null})
   * @return the comparator builder
   */
  default ComparatorBuilder addAll(
      Collection<? extends ComparatorField> fields,
      ValueExtractor valueExtractor) {
    Optional.ofNullable(fields)
        .ifPresent(comparatorFields -> comparatorFields.stream()
            .filter(Objects::nonNull)
            .forEach(comparatorField -> add(comparatorField, valueExtractor)));
    return this;
  }

  /**
   * Adds comparators for the given fields ordering descriptions.
   *
   * @param fields the ordering descriptions (can be {@code null} - no comparator will be
   *     added)
   * @param comparatorFunction the comparator function
   * @return the comparator builder
   */
  default ComparatorBuilder addAll(
      Collection<? extends ComparatorField> fields,
      Function<ComparatorField, Comparator<?>> comparatorFunction) {

    Optional.ofNullable(fields)
        .ifPresent(comparatorFields -> comparatorFields.stream()
            .filter(Objects::nonNull)
            .forEach(comparatorField -> add(comparatorFunction.apply(comparatorField))));
    return this;
  }

  /**
   * Creates and adds value comparators for the given field ordering descriptions.
   *
   * @param comparatorFields the ordering descriptions (can be {@code null} - no comparator will
   *     be                         added)
   * @return the comparator builder
   */
  default ComparatorBuilder addAll(ComparatorFields comparatorFields) {
    return Optional.ofNullable(comparatorFields)
        .map(fields -> addAll(fields.getFields()))
        .orElse(this);
  }

  /**
   * Creates and adds value comparators for the given field ordering descriptions. A custom value
   * extractor can be specified.
   *
   * @param comparatorFields the ordering descriptions (can be {@code null} - no comparator will
   *     be added)
   * @param valueExtractor the value extractor (can be {@code null})
   * @return the comparator builder
   */
  default ComparatorBuilder addAll(
      ComparatorFields comparatorFields,
      ValueExtractor valueExtractor) {
    return Optional.ofNullable(comparatorFields)
        .map(fields -> addAll(fields.getFields(), valueExtractor))
        .orElse(this);
  }

  /**
   * Add all comparator builder.
   *
   * @param comparatorFields the comparator fields
   * @param comparatorFunction the comparator function
   * @return the comparator builder
   */
  default ComparatorBuilder addAll(
      ComparatorFields comparatorFields,
      Function<ComparatorField, Comparator<?>> comparatorFunction) {
    return Optional.ofNullable(comparatorFields)
        .map(fields -> addAll(fields.getFields(), comparatorFunction))
        .orElse(this);
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
