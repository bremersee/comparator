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

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.XmlType;
import java.util.Objects;
import java.util.Optional;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * This class defines the sort order of a field.
 *
 * <pre>
 *  ---------------------------------------------------------------------------------------------
 * | Attribute    | Description                                                    | Default     |
 * |--------------|----------------------------------------------------------------|-------------|
 * | field        | The field name (or method name) of the object. It can be a     | null        |
 * |              | path. The segments are separated by a dot (.):                 |             |
 * |              | field0.field1.field2                                           |             |
 * |              | It can be null. Then the object itself must be comparable.     |             |
 * |--------------|----------------------------------------------------------------|-------------|
 * | direction    | Defines ascending or descending ordering.                      | asc         |
 * |--------------|----------------------------------------------------------------|-------------|
 * | caseHandling | Makes a case ignoring comparison (only for strings).           | insensitive |
 * |--------------|----------------------------------------------------------------|-------------|
 * | nullHandling | Defines the ordering if one of the values is null.             | last        |
 *  ---------------------------------------------------------------------------------------------
 * </pre>
 *
 * <p>These values have a 'sort oder text' representation. The values are concatenated with
 * semicolon (;):
 * <pre>
 * fieldNameOrPath;direction;caseHandling;nullHandling
 * </pre>
 *
 * <p>For example:
 * <pre>
 * properties.customSettings.priority;asc;insensitive;nulls-first
 * </pre>
 *
 * <p>Defaults can be omitted. This is the same:
 * <pre>
 * properties.customSettings.priority
 * </pre>
 *
 * <p>The building of a chain is done by concatenate the fields with a comma (,):
 * <pre>
 * field0;desc,field1;desc
 * </pre>
 *
 * @author Christian Bremer
 */
@XmlRootElement(name = "sortOrderItem")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sortOrderItemType", propOrder = {
    "field",
    "direction",
    "caseHandling",
    "nullHandling"
})
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder(value = {
    "field",
    "direction",
    "caseHandling",
    "nullHandling"
})
@Schema(description = "A sort order defines how a field of an object is sorted.")
@Getter
@EqualsAndHashCode
public class SortOrderItem {

  /**
   * The constant SEPARATOR.
   */
  public static final String SEPARATOR = ";";

  /**
   * The constant DEFAULT_DIRECTION.
   */
  protected static final Direction DEFAULT_DIRECTION = Direction.ASC;

  /**
   * The constant DEFAULT_CASE_HANDLING.
   */
  protected static final CaseHandling DEFAULT_CASE_HANDLING = CaseHandling.INSENSITIVE;

  /**
   * The constant DEFAULT_NULL_HANDLING.
   */
  protected static final NullHandling DEFAULT_NULL_HANDLING = NullHandling.NATIVE;

  @Schema(description = "The field name or path.")
  @XmlElement(name = "field")
  @JsonProperty("field")
  private final String field;

  /**
   * The direction. Default is {@link Direction#ASC}.
   */
  @Schema(description = "The direction.")
  @XmlElement(name = "direction", defaultValue = "ASC")
  @JsonProperty("direction")
  private final Direction direction;

  /**
   * The case-handling. Default is {@link CaseHandling#INSENSITIVE}.
   */
  @Schema(description = "The case-handling.")
  @XmlElement(name = "case-handling", defaultValue = "INSENSITIVE")
  @JsonProperty("case-handling")
  private final CaseHandling caseHandling;

  /**
   * The null-handling. Default is {@link NullHandling#NATIVE}.
   */
  @Schema(description = "The null-handling.")
  @XmlElement(name = "null-handling", defaultValue = "NATIVE")
  @JsonProperty("null-handling")
  private final NullHandling nullHandling;

  /**
   * Instantiates a new sort order item.
   */
  protected SortOrderItem() {
    this(null, DEFAULT_DIRECTION, DEFAULT_CASE_HANDLING, DEFAULT_NULL_HANDLING);
  }

  /**
   * Instantiates a new sort order item.
   *
   * @param field the field name or path (can be {@code null})
   * @param direction the direction
   * @param caseHandling the case-handling
   * @param nullHandling the null-handling
   */
  @JsonCreator
  public SortOrderItem(
      @JsonProperty("field") String field,
      @JsonProperty("direction") Direction direction,
      @JsonProperty("case-handling") CaseHandling caseHandling,
      @JsonProperty("null-handling") NullHandling nullHandling) {
    this.field = isNull(field) || field.isBlank() ? null : field;
    this.direction = Objects.requireNonNullElse(direction, DEFAULT_DIRECTION);
    this.caseHandling = Objects.requireNonNullElse(caseHandling, DEFAULT_CASE_HANDLING);
    this.nullHandling = Objects.requireNonNullElse(nullHandling, DEFAULT_NULL_HANDLING);
  }

  /**
   * Gets field (can be {@code null}).
   *
   * @return the field
   */
  public String getField() {
    return isNull(field) || field.isBlank() ? null : field;
  }

  /**
   * With given direction.
   *
   * @param direction the direction
   * @return the new sort order
   */
  public SortOrderItem with(Direction direction) {
    return Optional.ofNullable(direction)
        .map(dir -> new SortOrderItem(getField(), dir, getCaseHandling(), getNullHandling()))
        .orElse(this);
  }

  /**
   * With given case-handling.
   *
   * @param caseHandling the case-handling
   * @return the new sort order
   */
  public SortOrderItem with(CaseHandling caseHandling) {
    return Optional.ofNullable(caseHandling)
        .map(ch -> new SortOrderItem(getField(), getDirection(), ch, getNullHandling()))
        .orElse(this);
  }

  /**
   * With given null-handling.
   *
   * @param nullHandling the null-handling
   * @return the new sort order
   */
  public SortOrderItem with(NullHandling nullHandling) {
    return Optional.ofNullable(nullHandling)
        .map(nh -> new SortOrderItem(getField(), getDirection(), getCaseHandling(), nh))
        .orElse(this);
  }

  /**
   * Creates the sort order text of this ordering description.
   *
   * <p>The syntax of the ordering description is
   * <pre>
   * fieldNameOrPath;direction;caseHandling;nullHandling
   * </pre>
   *
   * <p>For example
   * <pre>
   * person.lastName;asc;sensitive;nulls-first
   * </pre>
   *
   * @return the sort order text
   */
  @JsonIgnore
  @XmlTransient
  public String getSortOrderText() {
    return getSortOrderText(SortOrderTextSeparators.defaults());
  }

  /**
   * Creates the sort order text of this ordering description.
   *
   * <p>The syntax of the ordering description is
   * <pre>
   * fieldNameOrPath;direction;caseHandling;nullHandling
   * </pre>
   *
   * <p>For example
   * <pre>
   * person.lastName;asc;sensitive;nulls-first
   * </pre>
   *
   * @param separators the separators
   * @return the sort order text
   */
  public String getSortOrderText(SortOrderTextSeparators separators) {
    String separator = Optional.ofNullable(separators)
        .orElseGet(SortOrderTextSeparators::defaults)
        .getArgumentSeparator();
    StringBuilder sb = new StringBuilder();
    if (nonNull(getField())) {
      sb.append(getField());
    }
    boolean isNullHandlingDefault = getNullHandling() == DEFAULT_NULL_HANDLING;
    boolean isCaseHandlingDefault = getCaseHandling() == DEFAULT_CASE_HANDLING
        && isNullHandlingDefault;
    boolean isDirectionDefault = getDirection() == DEFAULT_DIRECTION && isCaseHandlingDefault;
    if (!isDirectionDefault) {
      sb.append(separator).append(getDirection().toString());
    }
    if (!isCaseHandlingDefault) {
      sb.append(separator).append(getCaseHandling().toString());
    }
    if (!isNullHandlingDefault) {
      sb.append(separator).append(getNullHandling().toString());
    }
    return sb.toString();
  }

  @Override
  public String toString() {
    return getSortOrderText();
  }

  /**
   * Creates a new sort order for the given field.
   *
   * @param field the field
   * @return the sort order
   */
  public static SortOrderItem by(String field) {
    return new SortOrderItem(
        field, DEFAULT_DIRECTION, DEFAULT_CASE_HANDLING, DEFAULT_NULL_HANDLING);
  }

  /**
   * From sort order text.
   *
   * @param source the sort order text
   * @return the sort order
   */
  public static SortOrderItem fromSortOrderText(String source) {
    return fromSortOrderText(source, SortOrderTextSeparators.defaults());
  }

  /**
   * From sort order text.
   *
   * @param source the sort order text
   * @param separators the separators
   * @return the sort order
   */
  public static SortOrderItem fromSortOrderText(String source, SortOrderTextSeparators separators) {
    return Optional.ofNullable(source)
        .map(text -> {
          String separator = Optional.ofNullable(separators)
              .orElseGet(SortOrderTextSeparators::defaults)
              .getArgumentSeparator();
          String field;
          Direction direction = DEFAULT_DIRECTION;
          CaseHandling caseHandling = DEFAULT_CASE_HANDLING;
          NullHandling nullHandling = DEFAULT_NULL_HANDLING;
          int index = text.indexOf(separator);
          if (index < 0) {
            field = text.trim();
          } else {
            field = text.substring(0, index).trim();
            int from = index + separator.length();
            index = text.indexOf(separator, from);
            if (index < 0) {
              direction = Direction.fromString(text.substring(from).trim());
            } else {
              direction = Direction.fromString(text.substring(from, index).trim());
              from = index + separator.length();
              index = text.indexOf(separator, from);
              if (index < 0) {
                caseHandling = CaseHandling.fromString(text.substring(from).trim());
              } else {
                caseHandling = CaseHandling.fromString(text.substring(from, index).trim());
                from = index + separator.length();
                nullHandling = NullHandling.fromString(text.substring(from).trim());
              }
            }
          }
          field = field.isEmpty() ? null : field;
          return new SortOrderItem(field, direction, caseHandling, nullHandling);
        })
        .orElseGet(SortOrderItem::new);
  }

  /**
   * The direction.
   */
  public enum Direction {

    /**
     * Ascending direction.
     */
    ASC,

    /**
     * Descending direction.
     */
    DESC;

    /**
     * Returns whether the direction is ascending.
     *
     * @return true if ascending, false otherwise.
     */
    public boolean isAscending() {
      return ASC.equals(this);
    }

    /**
     * Returns whether the direction is descending.
     *
     * @return true if descending, false otherwise.
     */
    public boolean isDescending() {
      return DESC.equals(this);
    }

    @Override
    public String toString() {
      return name().toLowerCase();
    }

    /**
     * From string.
     *
     * @param direction the direction
     * @return the direction
     */
    public static Direction fromString(String direction) {
      if ("DESC".equalsIgnoreCase(direction)) {
        return DESC;
      }
      return ASC;
    }
  }

  /**
   * The case-handling.
   */
  public enum CaseHandling {

    /**
     * Insensitive case-handling.
     */
    INSENSITIVE,

    /**
     * Sensitive case-handling.
     */
    SENSITIVE;

    /**
     * Returns whether the case-handling is insensitive.
     *
     * @return true if insensitive, false otherwise.
     */
    public boolean isInsensitive() {
      return INSENSITIVE.equals(this);
    }

    /**
     * Returns whether the case-handling is sensitive.
     *
     * @return true if sensitive, false otherwise.
     */
    public boolean isSensitive() {
      return SENSITIVE.equals(this);
    }

    @Override
    public String toString() {
      return name().toLowerCase();
    }

    /**
     * From string.
     *
     * @param caseHandling the case-handling
     * @return the case-handling
     */
    public static CaseHandling fromString(String caseHandling) {
      if ("SENSITIVE".equalsIgnoreCase(caseHandling)) {
        return SENSITIVE;
      }
      return INSENSITIVE;
    }
  }

  /**
   * The null-handling.
   */
  public enum NullHandling {

    /**
     * Nulls first handling.
     */
    NULLS_FIRST,

    /**
     * Nulls last handling.
     */
    NULLS_LAST,

    /**
     * Native null-handling.
     */
    NATIVE;

    @Override
    public String toString() {
      return name().replaceAll("_", "-").toLowerCase();
    }

    /**
     * Is null first.
     *
     * @return the boolean
     */
    public boolean isNullFirst() {
      return NULLS_FIRST.equals(this);
    }

    /**
     * Is null last.
     *
     * @return the boolean
     */
    public boolean isNullLast() {
      return !isNullFirst();
    }

    /**
     * From string.
     *
     * @param nullHandling the null-handling
     * @return the null-handling
     */
    public static NullHandling fromString(String nullHandling) {
      if ("NULLS_FIRST".equalsIgnoreCase(nullHandling)
          || "NULLS-FIRST".equalsIgnoreCase(nullHandling)
          || "FIRST".equalsIgnoreCase(nullHandling)) {
        return NULLS_FIRST;
      }
      if ("NATIVE".equalsIgnoreCase(nullHandling)) {
        return NATIVE;
      }
      return NULLS_LAST;
    }

  }

}
