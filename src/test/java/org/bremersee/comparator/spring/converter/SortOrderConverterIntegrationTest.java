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
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * The sort order converter integration test.
 *
 * @author Christian Bremer
 */
@SpringBootTest(
    classes = {SortOrderConverterIntegrationTestConfiguration.class},
    webEnvironment = WebEnvironment.RANDOM_PORT,
    properties = {
        "springdoc.packagesToScan=org.bremersee.comparator.spring.converter.components"
    }
)
@ExtendWith(SoftAssertionsExtension.class)
public class SortOrderConverterIntegrationTest {

  /**
   * The rest template builder.
   */
  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired
  RestTemplateBuilder restTemplateBuilder;

  /**
   * The local port.
   */
  @LocalServerPort
  int port;

  /**
   * Test convert sort parameter.
   *
   * @param softly the soft assertions
   */
  @Test
  void testConvertSortParameter(SoftAssertions softly) {
    RestTemplate restTemplate = restTemplateBuilder.build();
    ResponseEntity<String> response = restTemplate.getForEntity(
        "http://localhost:" + port + "?sort=field0,desc&sort=field1,desc,false,true",
        String.class);
    softly.assertThat(response.getStatusCode())
        .isEqualTo(HttpStatus.OK);
    softly.assertThat(response.getBody())
        .as("Convert sort parameter in Spring application")
        .isEqualTo("field0,desc,true,false;field1,desc,false,true");
  }

  /**
   * Test convert pageable parameters.
   *
   * @param softly the softly
   */
  @Test
  void testConvertPageableParameters(SoftAssertions softly) {
    RestTemplate restTemplate = restTemplateBuilder.build();
    String url = "http://localhost:" + port + "/with-spring-pageable"
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
        .isEqualTo("field0,desc,true,false;field1,asc,true,false");
  }

  /**
   * Test open api.
   *
   * @param softly the soft assertions
   */
  @Test
  void testOpenApi(SoftAssertions softly) {
    RestTemplate restTemplate = restTemplateBuilder.build();
    ResponseEntity<String> response = restTemplate.getForEntity(
        "http://localhost:" + port + "/v3/api-docs",
        String.class);
    String expected = "{"
        + "\"name\":\"sort\","
        + "\"in\":\"query\","
        + "\"required\":false,"
        + "\"schema\":{"
        + "\"type\":\"array\","
        + "\"items\":{"
        + "\"type\":\"string\""
        + "}}}";
    softly.assertThat(response.getStatusCode())
        .isEqualTo(HttpStatus.OK);
    String body = response.getBody();
    // System.out.println(body);
    softly.assertThat(body)
        .as("Rest parameter 'sort' should be of type 'array' "
            + "with items of type 'string'; generated api: %s", body)
        .contains(expected);
  }

}
