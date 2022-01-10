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

package org.bremersee.comparator;

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

/**
 * The comparator field converter integration test.
 *
 * @author Christian Bremer
 */
@SpringBootTest(
    classes = {ComparatorFieldConverterIntegrationTestConfiguration.class},
    webEnvironment = WebEnvironment.RANDOM_PORT
)
@ExtendWith(SoftAssertionsExtension.class)
public class ComparatorFieldConverterIntegrationTest {

  /**
   * The Rest template builder.
   */
  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired
  RestTemplateBuilder restTemplateBuilder;

  /**
   * The local server port.
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
    ResponseEntity<String> response = restTemplateBuilder.build().getForEntity(
        "http://localhost:" + port + "?sort=field0,desc&sort=field1,desc,false,true",
        String.class);
    softly.assertThat(response.getStatusCode())
        .isEqualTo(HttpStatus.OK);
    softly.assertThat(response.getBody())
        .as("Convert sort parameters in Spring application")
        .isEqualTo("field0,desc,true,false;field1,desc,false,true");
  }
}
