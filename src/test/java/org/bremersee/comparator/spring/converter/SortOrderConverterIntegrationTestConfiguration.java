/*
 * Copyright 2022 the original author or authors.
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

package org.bremersee.comparator.spring.converter;

import org.bremersee.comparator.spring.converter.components.TestRestController;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * The sort order converter integration test configuration.
 *
 * @author Christian Bremer
 */
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(basePackageClasses = {TestRestController.class})
public class SortOrderConverterIntegrationTestConfiguration {

  /**
   * Registers the sort order converter.
   *
   * @return the sort order converter
   */
  @Bean
  public SortOrderConverter sortOrderConverter() {
    return new SortOrderConverter();
  }

}
