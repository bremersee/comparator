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

package org.bremersee.comparator;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Optional;

/**
 * The default value extractor.
 *
 * @author Christian Bremer
 */
@SuppressWarnings("WeakerAccess")
public class DefaultValueExtractor implements ValueExtractor {

  private final boolean throwingException;

  /**
   * Instantiates a new default value extractor that will throw an exception, if the given field
   * cannot be found.
   */
  public DefaultValueExtractor() {
    this(true);
  }

  /**
   * Instantiates a new default value extractor.
   *
   * @param throwingException if {@code true} and the given field cannot be found, an exception will
   *                          be thrown; otherwise {@code null} will be returned
   */
  public DefaultValueExtractor(boolean throwingException) {
    this.throwingException = throwingException;
  }

  @Override
  public Object findValue(Object obj, String fieldPath) {
    final String fieldIdentifier = trimFieldPath(fieldPath);
    if (obj == null || fieldIdentifier == null || fieldIdentifier.length() == 0) {
      return obj;
    }

    final int index = fieldIdentifier.indexOf('.');
    final String fieldName = index == -1 ? fieldIdentifier : fieldIdentifier.substring(0, index);
    if (fieldName.length() == 0) {
      return findValue(obj, fieldIdentifier.substring(index + 1));
    }

    Object value;
    Optional<Field> field = findField(obj.getClass(), fieldName);
    if (field.isPresent()) {
      value = invoke(field.get(), obj);
    } else {
      Optional<Method> method = findMethod(obj.getClass(), fieldName);
      if (method.isPresent()) {
        value = invoke(method.get(), obj);
      } else if (throwingException) {
        throw new ComparatorException("Field [" + fieldName + "] was not found on object ["
            + obj + "].");
      } else {
        value = null;
      }
    }

    return index == -1 ? value : findValue(value, fieldIdentifier.substring(index + 1));
  }

  private static String trimFieldPath(String field) {
    if (field == null) {
      return null;
    }
    String tmp = field
        .trim()
        .replace("..", ".");
    while (tmp.startsWith(".")) {
      tmp = tmp.substring(1).trim();
    }
    while (tmp.endsWith(".")) {
      tmp = tmp.substring(0, tmp.length() - 1).trim();
    }
    return tmp;
  }

}
