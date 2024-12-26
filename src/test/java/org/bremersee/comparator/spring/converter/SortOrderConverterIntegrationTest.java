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

import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * The sort order converter integration test.
 *
 * @author Christian Bremer
 */
@SpringBootTest(
    classes = {SortOrderConverterIntegrationTestConfiguration.class},
    webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(SoftAssertionsExtension.class)
public class SortOrderConverterIntegrationTest {

  /**
   * The test rest template.
   */
  @Autowired
  TestRestTemplate restTemplate;

  /**
   * Test convert sort parameter.
   *
   * @param softly the soft assertions
   */
  @Test
  void testConvertSortParameter(SoftAssertions softly) {
    ResponseEntity<String> response = restTemplate.getForEntity(
        "/?sort=field0;desc&sort=field1;desc,field3;desc",
        String.class);
    softly.assertThat(response.getStatusCode())
        .isEqualTo(HttpStatus.OK);
    softly.assertThat(response.getBody())
        .as("Convert sort parameter in Spring application")
        .isEqualTo("field0;desc,field1;desc,field3;desc");
  }

  /**
   * Test convert pageable parameters.
   *
   * @param softly the softly
   */
  @Test
  void testConvertPageableParameters(SoftAssertions softly) {
    String url = "/with-spring-pageable"
        + "?sort=field0,desc"
        + "&sort=field1,asc"
        + "&page=12"
        + "&size=50";
    ResponseEntity<String> response = restTemplate.getForEntity(
        url,
        String.class);
    softly.assertThat(response.getStatusCode())
        .isEqualTo(HttpStatus.OK);
    softly.assertThat(response.getBody())
        .as("Convert pageable parameters in Spring application")
        .isEqualTo("field0;desc,field1");
  }

}
