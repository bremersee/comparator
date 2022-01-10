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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.StringTokenizer;
import java.util.function.Function;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.bremersee.comparator.model.ComparatorField;
import org.bremersee.comparator.model.WellKnownTextProperties;
import org.springframework.util.Assert;

/**
 * Parses the string representation of a sort order and creates a comparator.
 *
 * <p>The default implementation supports the following syntax:
 * <pre>
 * fieldNameOrPath0,asc,ignoreCase,nullIsFirst|fieldNameOrPath1,asc,ignoreCase,nullIsFirst
 * </pre>
 *
 * <p>For example
 * <pre>
 * person.lastName,asc,true,false|person.firstName,asc,true,false
 * </pre>
 *
 * @author Christian Bremer
 */
@Valid
public interface WellKnownTextParser {

  /**
   * Gets properties.
   *
   * @return the properties
   */
  @NotNull
  default WellKnownTextProperties getProperties() {
    return WellKnownTextProperties.defaults();
  }

  /**
   * Parses the string representation of a sort order and creates a comparator.
   *
   * <p>The default implementation supports the following syntax:
   * <pre>
   * fieldNameOrPath0,asc,ignoreCase,nullIsFirst|fieldNameOrPath1,asc,ignoreCase,nullIsFirst
   * </pre>
   *
   * <p>For example
   * <pre>
   * person.lastName,asc,true,false|person.firstName,asc,true,false
   * </pre>
   *
   * @param wkt the string representation of a sort order (as well known text)
   * @return the created comparator
   */
  @NotNull
  default Comparator<Object> parse(String wkt) {
    ComparatorBuilder builder = ComparatorBuilder.builder();
    for (ComparatorField comparatorField : buildComparatorFields(wkt)) {
      //noinspection rawtypes
      Comparator comparator = apply(comparatorField);
      if (comparator != null) {
        builder.add(comparator);
      }
    }
    return builder.build();
  }

  /**
   * Builds a list of comparator fields from the string representation of a sort order.
   *
   * <p>The default implementation supports the following syntax:
   * <pre>
   * fieldNameOrPath0,asc,ignoreCase,nullIsFirst|fieldNameOrPath1,asc,ignoreCase,nullIsFirst
   * </pre>
   *
   * <p>For example
   * <pre>
   * person.lastName,asc,true,false|person.firstName,asc,true,false
   * </pre>
   *
   * @param wkt the string representation of a sort order (as well known text)
   * @return the list of comparator fields
   */
  @NotNull
  default List<ComparatorField> buildComparatorFields(String wkt) {
    return Optional.ofNullable(wkt)
        .map(text -> {
          List<ComparatorField> fields = new ArrayList<>();
          StringTokenizer tokenizer = new StringTokenizer(text, getProperties().getFieldSeparator());
          while (tokenizer.hasMoreTokens()) {
            fields.add(buildComparatorField(tokenizer.nextToken()));
          }
          return fields;
        })
        .orElseGet(List::of);
  }

  /**
   * Builds a comparator field from the string representation of a sort order (must be a single field, not a path).
   *
   * <p>The default implementation supports the following syntax:
   * <pre>
   * fieldNameOrPath,asc,ignoreCase,nullIsFirst
   * </pre>
   *
   * <p>For example
   * <pre>
   * person.lastName,asc,true,false
   * </pre>
   *
   * @param fieldDescription the field description
   * @return the comparator field
   */
  @NotNull
  default ComparatorField buildComparatorField(String fieldDescription) {
    WellKnownTextProperties props = getProperties();
    String field = null;
    boolean asc = props.isAsc(null);
    boolean ignoreCase = props.isCaseIgnored(null);
    boolean nullIsFirst = props.isNullFirst(null);
    if (fieldDescription != null) {
      String separator = props.getFieldArgsSeparator();
      int index = fieldDescription.indexOf(separator);
      if (index < 0) {
        field = fieldDescription.trim();
      } else {
        field = fieldDescription.substring(0, index).trim();
        int from = index + separator.length();
        index = fieldDescription.indexOf(separator, from);
        if (index < 0) {
          asc = props.isAsc(fieldDescription.substring(from).trim());
        } else {
          asc = props.isAsc(fieldDescription.substring(from, index).trim());
          from = index + separator.length();
          index = fieldDescription.indexOf(separator, from);
          if (index < 0) {
            ignoreCase = props.isCaseIgnored(fieldDescription.substring(from).trim());
          } else {
            ignoreCase = props.isCaseIgnored(fieldDescription.substring(from, index).trim());
            from = index + separator.length();
            nullIsFirst = props.isNullFirst(fieldDescription.substring(from).trim());
          }
        }
      }
    }
    field = field == null || field.length() == 0 ? null : field;
    return new ComparatorField(field, asc, ignoreCase, nullIsFirst);
  }

  /**
   * Creates the comparator for the given field.
   *
   * @param comparatorField the comparator field
   * @return the comparator
   */
  @NotNull
  @SuppressWarnings("rawtypes")
  Comparator apply(ComparatorField comparatorField);

  /**
   * New instance well known text parser.
   *
   * @param comparatorFunction the comparator function
   * @return the well known text parser
   */
  @NotNull
  @SuppressWarnings("rawtypes")
  static WellKnownTextParser newInstance(@NotNull Function<ComparatorField, Comparator> comparatorFunction) {
    return newInstance(comparatorFunction, null);
  }

  /**
   * New instance well known text parser.
   *
   * @param comparatorFunction the comparator function
   * @param properties the properties
   * @return the well known text parser
   */
  @NotNull
  @SuppressWarnings("rawtypes")
  static WellKnownTextParser newInstance(
      @NotNull Function<ComparatorField, Comparator> comparatorFunction,
      WellKnownTextProperties properties) {
    return new Impl(comparatorFunction, properties);
  }

  /**
   * An implementation.
   */
  @SuppressWarnings("rawtypes")
  class Impl implements WellKnownTextParser {

    private final Function<ComparatorField, Comparator> comparatorFunction;

    private final WellKnownTextProperties properties;

    private Impl(Function<ComparatorField, Comparator> comparatorFunction, WellKnownTextProperties properties) {
      this.comparatorFunction = comparatorFunction;
      this.properties = Objects.requireNonNullElse(properties, WellKnownTextProperties.defaults());
      Assert.notNull(this.comparatorFunction, "Comparator function must be present.");
    }

    @Override
    public WellKnownTextProperties getProperties() {
      return properties;
    }

    @Override
    public Comparator apply(ComparatorField comparatorField) {
      return comparatorFunction.apply(comparatorField);
    }
  }

}
