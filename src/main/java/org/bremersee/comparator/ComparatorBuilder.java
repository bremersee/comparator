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
  default ComparatorBuilder add(Comparator<?> comparator) {
    return add(null, null, comparator);
  }

  /**
   * Adds the given comparator for the given field name or path to this builder.
   *
   * @param field      the field name or path (can be {@code null})
   * @param comparator the comparator (can be {@code null} - then no comparator is added)
   * @return the comparator builder
   */
  @SuppressWarnings("unused")
  default ComparatorBuilder add(String field, Comparator<?> comparator) {
    return add(field, null, comparator);
  }

  /**
   * Adds the given comparator for the given field name or path to this builder. A custom value
   * extractor can be specified.
   *
   * @param field          the field name or path (can be {@code null})
   * @param valueExtractor the value extractor (can be {@code null})
   * @param comparator     the comparator (can be {@code null} - then no comparator is added)
   * @return the comparator builder
   */
  ComparatorBuilder add(
      String field,
      ValueExtractor valueExtractor,
      Comparator<?> comparator);

  /**
   * Creates and adds a value comparator for the given field name or path to this builder.
   *
   * @param field       the field name or path (can be {@code null})
   * @param asc         {@code true} for an ascending order, {@code false} for a descending order
   * @param ignoreCase  {@code true} for a case insensitive order,  {@code false} for a case
   *                    sensitive order
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
   * @param field          the field name or path (can be {@code null})
   * @param asc            {@code true} for an ascending order, {@code false} for a descending
   *                       order
   * @param ignoreCase     {@code true} for a case insensitive order,  {@code false} for a case
   *                       sensitive order
   * @param nullIsFirst    specifies the order of {@code null} values
   * @param valueExtractor the value extractor
   * @return the comparator builder
   */
  ComparatorBuilder add(
      String field,
      boolean asc,
      boolean ignoreCase,
      boolean nullIsFirst,
      ValueExtractor valueExtractor);

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
   * @param field          the field ordering description (can be {@code null})
   * @param valueExtractor the value extractor
   * @return the comparator builder
   */
  default ComparatorBuilder add(ComparatorField field, ValueExtractor valueExtractor) {
    if (field == null) {
      throw new IllegalArgumentException("Field must not be null.");
    }
    return add(field.getField(), field.isAsc(), field.isIgnoreCase(), field.isNullIsFirst(),
        valueExtractor);
  }

  /**
   * Creates and adds value comparators for the given field ordering descriptions.
   *
   * @param fields the ordering descriptions (can be {@code null} - no comparator will be added)
   * @return the comparator builder
   */
  @SuppressWarnings("unused")
  default ComparatorBuilder addAll(Collection<? extends ComparatorField> fields) {
    return addAll(fields, null);
  }

  /**
   * Creates and adds value comparators for the given field ordering descriptions. A custom value
   * extractor can be specified.
   *
   * @param fields         the ordering descriptions (can be {@code null} - no comparator will be
   *                       added)
   * @param valueExtractor the value extractor
   * @return the comparator builder
   */
  default ComparatorBuilder addAll(
      Collection<? extends ComparatorField> fields,
      ValueExtractor valueExtractor) {
    if (fields != null) {
      for (ComparatorField field : fields) {
        add(field, valueExtractor);
      }
    }
    return this;
  }

  /**
   * Creates and adds value comparators for the given field ordering descriptions.
   *
   * @param comparatorFields the ordering descriptions (can be {@code null} - no comparator will be
   *                         added)
   * @return the comparator builder
   */
  @SuppressWarnings("unused")
  default ComparatorBuilder addAll(ComparatorFields comparatorFields) {
    return comparatorFields != null ? addAll(comparatorFields.getFields()) : this;
  }

  /**
   * Creates and adds value comparators for the given field ordering descriptions. A custom value
   * extractor can be specified.
   *
   * @param comparatorFields the ordering descriptions (can be {@code null} - no comparator will be
   *                         added)
   * @param valueExtractor   the value extractor
   * @return the comparator builder
   */
  @SuppressWarnings("unused")
  default ComparatorBuilder addAll(
      ComparatorFields comparatorFields,
      ValueExtractor valueExtractor) {
    return comparatorFields != null ? addAll(comparatorFields.getFields(), valueExtractor) : this;
  }

  /**
   * Creates and adds value comparators for the given well known text description (see {@link
   * ComparatorField#toWkt()}, {@link ComparatorFields#toWkt()} and {@link WellKnownTextParser}).
   *
   * <p>The syntax of the field ordering description is
   * <pre>
   * fieldNameOrPath0,asc,ignoreCase,nullIsFirst|fieldNameOrPath1,asc,ignoreCase,nullIsFirst
   * </pre>
   *
   * <p>For example
   * <pre>
   * person.lastName,asc,true,false|person.firstName,asc,true,false
   * </pre>
   *
   * @param wkt the well known text (field ordering description)
   * @return the comparator builder
   */
  default ComparatorBuilder fromWellKnownText(String wkt) {
    return fromWellKnownText(wkt, null);
  }

  /**
   * Creates and adds value comparators for the given well known text description (see {@link
   * ComparatorField#toWkt()}, {@link ComparatorFields#toWkt()} and {@link WellKnownTextParser}).
   *
   * <p>The syntax of the field ordering depends on the {@link WellKnownTextParser}. The default is
   * <pre>
   * fieldNameOrPath0,asc,ignoreCase,nullIsFirst|fieldNameOrPath1,asc,ignoreCase,nullIsFirst
   * </pre>
   *
   * <p>For example
   * <pre>
   * person.lastName,asc,true,false|person.firstName,asc,true,false
   * </pre>
   *
   * @param wkt       the well known text (field ordering description)
   * @param wktParser the well known text parser (can be {@code null})
   * @return the comparator builder
   */
  ComparatorBuilder fromWellKnownText(String wkt, WellKnownTextParser wktParser);

  /**
   * Build comparator.
   *
   * @return the comparator
   */
  Comparator<Object> build();

  /**
   * The default comparator builder.
   */
  class DefaultComparatorBuilder implements ComparatorBuilder {

    @SuppressWarnings("rawtypes")
    private final List<Comparator> comparatorChain = new LinkedList<>();

    @Override
    public ComparatorBuilder add(
        String field,
        ValueExtractor valueExtractor,
        Comparator<?> comparator) {

      if (comparator != null) {
        comparatorChain.add(new DelegatingComparator(field, valueExtractor, comparator));
      }
      return this;
    }

    @Override
    public ComparatorBuilder add(
        String field,
        boolean asc,
        boolean ignoreCase,
        boolean nullIsFirst,
        ValueExtractor valueExtractor) {

      comparatorChain.add(new ValueComparator(field, asc, ignoreCase, nullIsFirst, valueExtractor));
      return this;
    }

    @Override
    public ComparatorBuilder fromWellKnownText(
        String wkt,
        WellKnownTextParser wktParser) {

      if (wktParser != null) {
        comparatorChain.add(wktParser.parse(wkt));
      } else {
        WellKnownTextParser parser = ValueComparator::new;
        comparatorChain.add(parser.parse(wkt));
      }
      return this;
    }

    @Override
    public Comparator<Object> build() {
      return new ComparatorChain(comparatorChain);
    }
  }

}
