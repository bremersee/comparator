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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * The value extractor finds the value of a given field name or path by reflection.
 *
 * @author Christian Bremer
 */
public interface ValueExtractor {

  /**
   * Find the value of the given add name or path of the given object.
   *
   * @param obj the object
   * @param field the field name or path
   * @return the object
   * @throws ValueExtractorException if no add nor method is found
   */
  Object findValue(Object obj, String field);

  /**
   * Find field with the given name of the specified class.
   *
   * @param clazz the class
   * @param name the field name
   * @return the field
   */
  default Optional<Field> findField(final Class<?> clazz, final String name) {
    return findField(clazz, name, null);
  }

  /**
   * Find the field with the given name of the specified class.
   *
   * @param clazz the class
   * @param name the field name
   * @param type the type of the field
   * @return the field
   */
  default Optional<Field> findField(final Class<?> clazz, final String name,
      @SuppressWarnings("SameParameterValue") final Class<?> type) {
    Class<?> searchType = clazz;
    while (!Object.class.equals(searchType) && searchType != null) {
      Field[] fields = searchType.getDeclaredFields();
      for (Field field : fields) {
        if ((name == null || name.equals(field.getName())) && (type == null || type
            .equals(field.getType()))) {
          return Optional.of(field);
        }
      }
      searchType = searchType.getSuperclass();
    }
    return Optional.empty();
  }

  /**
   * Get possible method names of the given field name. The default implementation returns the field
   * name, it's getter for an object and a primitive boolean.
   *
   * <p>If '{@code firstName}' is given for example, '{@code firstName}', '{@code getFirstName}'
   * and  '{@code isFirstName}' will be returned.
   *
   * @param name the field name
   * @return the possible method names
   */
  default String[] getPossibleMethodNames(final String name) {
    if (name == null || name.isEmpty()) {
      return new String[0];
    }
    final String baseName;
    if (name.length() == 1) {
      baseName = name.toUpperCase();
    } else {
      baseName = name.substring(0, 1).toUpperCase() + name.substring(1);
    }
    String[] names = new String[3];
    names[0] = name;
    names[1] = "get" + baseName;
    names[2] = "is" + baseName;
    return names;
  }

  /**
   * Find the method with the given name and no parameters of the specified class.
   *
   * @param clazz the class
   * @param name the method name
   * @return the method
   */
  default Optional<Method> findMethod(final Class<?> clazz, final String name) {

    return Arrays.stream(getPossibleMethodNames(name))
        .map(methodName -> findMethod(clazz, methodName, new Class[0]).orElse(null))
        .filter(Objects::nonNull)
        .findFirst();
  }

  /**
   * Find the method with the given name and parameters of the specified class.
   *
   * @param clazz the class
   * @param name the method name
   * @param paramTypes the parameter types
   * @return the method
   */
  default Optional<Method> findMethod(
      final Class<?> clazz,
      final String name,
      final Class<?>... paramTypes) {

    Class<?> searchType = clazz;
    while (searchType != null) {
      Method[] methods =
          searchType.isInterface() ? searchType.getMethods() : searchType.getDeclaredMethods();
      for (Method method : methods) {
        if (name.equals(method.getName())
            && (paramTypes == null || Arrays.equals(paramTypes, method.getParameterTypes()))) {
          return Optional.of(method);
        }
      }
      searchType = searchType.getSuperclass();
    }
    return Optional.empty();
  }

  /**
   * Invoke the given method on the given object. If the method is not accessible, {@code
   * setAccessible(true)} will be called.
   *
   * @param method the method
   * @param obj the object
   * @return the return value of the method
   * @throws ValueExtractorException if a {@link IllegalAccessException} or a {@link
   *     InvocationTargetException} occurs
   */
  default Object invoke(final Method method, final Object obj) {
    try {
      if (!method.canAccess(obj)) {
        method.setAccessible(true);
      }
      return method.invoke(obj);
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new ValueExtractorException("Invoking method '" + method.getName() + "' failed.", e);
    }
  }

  /**
   * Invoke the given field on the given object. If the field is not accessible, {@code
   * setAccessible(true)} will be called.
   *
   * @param field the field
   * @param obj the object
   * @return the value of the field
   * @throws ValueExtractorException if a {@link IllegalAccessException} occurs
   */
  default Object invoke(final Field field, final Object obj) {
    if (!field.canAccess(obj)) {
      field.setAccessible(true);
    }
    try {
      return field.get(obj);
    } catch (IllegalAccessException e) {
      throw new ValueExtractorException("Getting value from add '" + field.getName()
          + "' failed", e);
    }
  }

}
