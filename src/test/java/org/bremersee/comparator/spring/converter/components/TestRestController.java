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

package org.bremersee.comparator.spring.converter.components;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.bremersee.comparator.model.SortOrder;
import org.bremersee.comparator.spring.mapper.SortMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

  private final SortMapper sortMapper = SortMapper.defaultSortMapper();

  /**
   * Gets something sorted.
   *
   * @param sort the sort
   * @return the something sorted
   */
  @Operation(
      summary = "Get something that can be sorted.",
      operationId = "getSomethingSorted",
      tags = {"test-controller"}
  )
  @GetMapping(path = "/")
  @SuppressWarnings("unused")
  public ResponseEntity<String> getSomethingSorted(
      @Parameter(array = @ArraySchema(schema = @Schema(type = "string")))
      @RequestParam(name = "sort", required = false) List<SortOrder> sort) {

    log.info("Received sort orders {}", sort);
    return ResponseEntity.ok(SortOrder.by(sort).getSortOrderText());
  }

  /**
   * Gets something else sorted.
   *
   * @param pageRequest the page request
   * @return the something else sorted
   */
  @Operation(
      summary = "Get something else that can be sorted.",
      operationId = "getSomethingElseSorted",
      tags = {"test-controller"},
      parameters = {
          @Parameter(name = "page", in = ParameterIn.QUERY, schema = @Schema(type = "integer")),
          @Parameter(name = "size", in = ParameterIn.QUERY, schema = @Schema(type = "integer")),
          @Parameter(name = "sort", in = ParameterIn.QUERY,
              array = @ArraySchema(schema = @Schema(type = "string")))
      }
  )
  @GetMapping(path = "/with-spring-pageable")
  @SuppressWarnings("unused")
  public ResponseEntity<String> getSomethingElseSorted(
      @Parameter(hidden = true)
      @PageableDefault(page = 1, size = 20, sort = "somethingElse,desc") Pageable pageRequest) {

    log.info("Pageable = {}", pageRequest);
    // We can't pass ignore case as url parameter value, so we set it here
    Sort sort = sortMapper
        .applyDefaults(pageRequest.getSort(), null, true, null);
    Pageable pageable = PageRequest
        .of(pageRequest.getPageNumber(), pageRequest.getPageSize(), sort);
    SortOrder sortOrder = sortMapper.fromSort(pageable.getSort());
    return ResponseEntity.ok(sortOrder.getSortOrderText());
  }
}
