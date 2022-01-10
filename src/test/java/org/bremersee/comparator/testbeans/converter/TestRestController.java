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

package org.bremersee.comparator.testbeans.converter;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.bremersee.comparator.model.ComparatorField;
import org.bremersee.comparator.model.ComparatorFields;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The test rest controller.
 *
 * @author Christian Bremer
 */
@Slf4j
@RestController
public class TestRestController {

  /**
   * Gets something sorted.
   *
   * @param sort the sort
   * @return the something sorted
   */
  @GetMapping(path = "/")
  @SuppressWarnings("unused")
  public ResponseEntity<String> getSomethingSorted(
      @RequestParam(name = "sort", required = false) List<ComparatorField> sort) {

    log.info("Received sort orders {}", sort);
    return ResponseEntity.ok(new ComparatorFields(sort).toWkt());
  }
}
