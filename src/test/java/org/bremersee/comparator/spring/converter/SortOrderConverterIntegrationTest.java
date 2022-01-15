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

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.bremersee.comparator.testmodel.Complex;
import org.bremersee.comparator.testmodel.ComplexObject;
import org.bremersee.comparator.testmodel.ComplexObjectExtension;
import org.bremersee.comparator.testmodel.SimpleObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
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

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired
  private Jackson2ObjectMapperBuilder objectMapperBuilder;

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired
  private RestTemplateBuilder restTemplateBuilder;

  @LocalServerPort
  private int port;

  @Test
  void printJson() throws Exception {
    ObjectMapper om = objectMapperBuilder
        .build();
    /*
    om.enableDefaultTypingAsProperty(
        ObjectMapper.DefaultTyping.JAVA_LANG_OBJECT,
        "_type");

     */
    //om.activateDefaultTyping(new DefaultBaseTypeLimitingValidator());
    om.activateDefaultTyping(BasicPolymorphicTypeValidator.builder()

        .build(), DefaultTyping.NON_CONCRETE_AND_ARRAYS, As.PROPERTY);
    ComplexObject o0 = new ComplexObject(new SimpleObject(0));
    System.out.println(om.writerWithDefaultPrettyPrinter().writeValueAsString(o0));

    ComplexObjectExtension o1 = new ComplexObjectExtension(new SimpleObject(1), "ext");
    System.out.println(om.writerWithDefaultPrettyPrinter().writeValueAsString(o1));

    List<Complex> list = List.of(o0, o1);
    System.out.println(om.writerWithDefaultPrettyPrinter().writeValueAsString(list));
  }

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
        .as("Convert sort parameters in Spring application")
        .isEqualTo("field0,desc,true,false;field1,desc,false,true");

    String url = "http://localhost:" + port + "/with-spring-sort"
        + "?sort=field0,desc"
        + "&sort=field1,desc"
        + "&page=12"
        + "&size=50";
    HttpEntity<?> httpEntity = new HttpEntity<>(null);
    ResponseEntity<String> page = restTemplate.exchange(url, HttpMethod.GET, httpEntity, new ParameterizedTypeReference<>() {});
    softly.assertThat(page.getStatusCode())
        .isEqualTo(HttpStatus.OK);
    System.out.println("" + page.getBody());
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
    System.out.println(body);
    softly.assertThat(body)
        .as("Rest parameter 'sort' should be of type 'array' "
            + "with items of type 'string'; generated api: %s", body)
        .contains(expected);
  }

}
