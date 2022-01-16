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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import lombok.EqualsAndHashCode;

/**
 * This class defines the sort order of a field.
 *
 * <pre>
 *  ---------------------------------------------------------------------------------------------
 * | Attribute    | Description                                                       | Default  |
 * |--------------|-------------------------------------------------------------------|----------|
 * | field        | The field name (or method name) of the object. It can be a path.  | null     |
 * |              | The segments are separated by a dot (.): field0.field1.field2     |          |
 * |              | It can be null. Then the object itself must be comparable.        |          |
 * |--------------|-------------------------------------------------------------------|----------|
 * | asc or desc  | Defines ascending or descending ordering.                         | asc      |
 * |--------------|-------------------------------------------------------------------|----------|
 * | ignoreCase   | Makes a case ignoring comparison (only for strings).              | true     |
 * |--------------|-------------------------------------------------------------------|----------|
 * | nullIsFirst  | Defines the ordering if one of the values is null.                | false    |
 *  ---------------------------------------------------------------------------------------------
 * </pre>
 *
 * <p>These values have a 'well known text' representation. The values are concatenated with comma
 * (,):
 * <pre>
 * fieldNameOrPath,asc,ignoreCase,nullIsFirst
 * </pre>
 *
 * <p>For example:
 * <pre>
 * properties.customSettings.priority,asc,true,false
 * </pre>
 *
 * <p>Defaults can be omitted. This is the same:
 * <pre>
 * properties.customSettings.priority
 * </pre>
 *
 * <p>The building of a chain is done by concatenate the fields with a semicolon (;):
 * <pre>
 * field0,asc,ignoreCase,nullIsFirst;field1,asc,ignoreCase,nullIsFirst
 * </pre>
 *
 * @author Christian Bremer
 */
@XmlRootElement(name = "sortOrder")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sortOrderType", propOrder = {
    "field",
    "asc",
    "ignoreCase",
    "nullIsFirst"
})
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder(value = {
    "field",
    "asc",
    "ignoreCase",
    "nullIsFirst"
})
@Schema(description = "A sort order defines how a field of an object is sorted.")
@EqualsAndHashCode
@Valid
public class SortOrder {

  @Schema(description = "The field name or path.")
  @XmlElement(name = "field")
  private final String field;

  @Schema(description = "Is ascending or descending order.", required = true)
  @XmlElement(name = "asc", defaultValue = "true")
  private final boolean asc;

  @Schema(description = "Is case insensitive or sensitive order.", required = true)
  @XmlElement(name = "ignoreCase", defaultValue = "true")
  private final boolean ignoreCase;

  @Schema(description = "Is null is first.", required = true)
  @XmlElement(name = "nullIsFirst", defaultValue = "false")
  private final boolean nullIsFirst;

  /**
   * Instantiates a new sort order.
   */
  protected SortOrder() {
    this(null, true, true, false);
  }

  /**
   * Instantiates a new sort order.
   *
   * @param field the field name or path (can be {@code null})
   * @param asc {@code true} for an ascending order, {@code false} for a descending order
   * @param ignoreCase {@code true} for a case insensitive order,  {@code false} for a case
   *     sensitive order
   * @param nullIsFirst specifies the order of {@code null} values
   */
  @JsonCreator
  public SortOrder(
      @JsonProperty("field") String field,
      @JsonProperty(value = "asc", required = true) boolean asc,
      @JsonProperty(value = "ignoreCase", required = true) boolean ignoreCase,
      @JsonProperty(value = "nullIsFirst", required = true) boolean nullIsFirst) {
    this.field = field;
    this.asc = asc;
    this.ignoreCase = ignoreCase;
    this.nullIsFirst = nullIsFirst;
  }

  /**
   * Gets field name or path.
   *
   * @return the field name or path
   */
  public String getField() {
    return field;
  }

  /**
   * Is ascending or descending order.
   *
   * @return {@code true} if ascending order, {@code false} if descending order
   */
  public boolean isAsc() {
    return asc;
  }

  /**
   * Is case-insensitive or case-sensitive order.
   *
   * @return {@code true} if case insensitive order, {@code false} if case sensitive order
   */
  public boolean isIgnoreCase() {
    return ignoreCase;
  }

  /**
   * Is null is first.
   *
   * @return {@code true} if null is first, otherwise {@code false}
   */
  public boolean isNullIsFirst() {
    return nullIsFirst;
  }

  /**
   * With given direction.
   *
   * @param direction the direction
   * @return the sort order
   */
  public SortOrder with(Direction direction) {
    return Optional.ofNullable(direction)
        .map(dir -> new SortOrder(getField(), dir.isAsc(), isIgnoreCase(), isNullIsFirst()))
        .orElse(this);
  }

  /**
   * With given case handling.
   *
   * @param caseHandling the case handling
   * @return the sort order
   */
  public SortOrder with(CaseHandling caseHandling) {
    return Optional.ofNullable(caseHandling)
        .map(ch -> new SortOrder(getField(), isAsc(), ch.isIgnoreCase(), isNullIsFirst()))
        .orElse(this);
  }

  /**
   * With given null handling.
   *
   * @param nullHandling the null handling
   * @return the sort order
   */
  public SortOrder with(NullHandling nullHandling) {
    return Optional.ofNullable(nullHandling)
        .map(nh -> new SortOrder(getField(), isAsc(), isIgnoreCase(), nh.isNullIsFirst()))
        .orElse(this);
  }

  /**
   * Creates the sort order text of this ordering description.
   *
   * <p>The syntax of the ordering description is
   * <pre>
   * fieldNameOrPath,asc,ignoreCase,nullIsFirst
   * </pre>
   *
   * <p>For example
   * <pre>
   * person.lastName,asc,true,false
   * </pre>
   *
   * @return the well known text
   */
  @NotEmpty
  public String toSortOrderText() {
    return toSortOrderText(null);
  }

  /**
   * Creates the sort order text of this ordering description.
   *
   * <p>The syntax of the ordering description is
   * <pre>
   * fieldNameOrPath,asc,ignoreCase,nullIsFirst
   * </pre>
   *
   * <p>The separator (',') and the values of {@code direction}, {@code case-handling} and {@code
   * null-handling} depend on the given {@link SortOrdersTextProperties}.
   *
   * <p>For example with default properties:
   * <pre>
   * person.lastName,asc,true,false
   * </pre>
   *
   * @param properties the properties (can be {@code null}
   * @return the well known text
   */
  @NotEmpty
  public String toSortOrderText(SortOrdersTextProperties properties) {
    SortOrdersTextProperties props = Objects.requireNonNullElse(properties,
        SortOrdersTextProperties.defaults());
    return (field != null ? field : "") + props.getSortOrderArgsSeparator()
        + props.getDirectionValue(asc) + props.getSortOrderArgsSeparator()
        + props.getIgnoreCaseValue(ignoreCase) + props.getSortOrderArgsSeparator()
        + props.getNullIsFirstValue(nullIsFirst);
  }

  @Override
  public String toString() {
    return toSortOrderText();
  }

  /**
   * Creates a new sort order for the given field.
   *
   * @param field the field
   * @return the sort order
   */
  public static SortOrder by(String field) {
    return new SortOrder(field, true, true, false);
  }

  /**
   * From sort order text.
   *
   * @param source the sort order text
   * @return the sort order
   */
  public static SortOrder fromSortOrderText(String source) {
    return fromSortOrderText(source, SortOrdersTextProperties.defaults());
  }

  /**
   * From sort order text.
   *
   * @param source the sort order text
   * @param properties the properties
   * @return the sort order
   */
  public static SortOrder fromSortOrderText(String source, SortOrdersTextProperties properties) {
    return Optional.ofNullable(source)
        .map(wkt -> {
          SortOrdersTextProperties props = Objects
              .requireNonNullElse(properties, SortOrdersTextProperties.defaults());
          String field;
          boolean asc = props.isAsc(null);
          boolean ignoreCase = props.isCaseIgnored(null);
          boolean nullIsFirst = props.isNullFirst(null);
          String separator = props.getSortOrderArgsSeparator();
          int index = wkt.indexOf(separator);
          if (index < 0) {
            field = wkt.trim();
          } else {
            field = wkt.substring(0, index).trim();
            int from = index + separator.length();
            index = wkt.indexOf(separator, from);
            if (index < 0) {
              asc = props.isAsc(wkt.substring(from).trim());
            } else {
              asc = props.isAsc(wkt.substring(from, index).trim());
              from = index + separator.length();
              index = wkt.indexOf(separator, from);
              if (index < 0) {
                ignoreCase = props.isCaseIgnored(wkt.substring(from).trim());
              } else {
                ignoreCase = props.isCaseIgnored(wkt.substring(from, index).trim());
                from = index + separator.length();
                nullIsFirst = props.isNullFirst(wkt.substring(from).trim());
              }
            }
          }
          field = field.length() == 0 ? null : field;
          return new SortOrder(field, asc, ignoreCase, nullIsFirst);
        })
        .orElseGet(() -> new SortOrder(null, true, true, false));
  }

  /**
   * The direction.
   */
  public enum Direction {
    /**
     * Asc direction.
     */
    ASC,
    /**
     * Desc direction.
     */
    DESC;

    /**
     * Is asc.
     *
     * @return the boolean
     */
    public boolean isAsc() {
      return ASC.equals(this);
    }
  }

  /**
   * The case handling.
   */
  public enum CaseHandling {
    /**
     * Insensitive case handling.
     */
    INSENSITIVE,
    /**
     * Sensitive case handling.
     */
    SENSITIVE;

    /**
     * Is ignore case.
     *
     * @return the boolean
     */
    public boolean isIgnoreCase() {
      return INSENSITIVE.equals(this);
    }
  }

  /**
   * The null handling.
   */
  public enum NullHandling {
    /**
     * Nulls first handling.
     */
    NULLS_FIRST,
    /**
     * Nulls last handling.
     */
    NULLS_LAST;

    /**
     * Is null is first.
     *
     * @return the boolean
     */
    public boolean isNullIsFirst() {
      return NULLS_FIRST.equals(this);
    }
  }

}
