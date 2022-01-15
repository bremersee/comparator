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
import org.bremersee.comparator.model.SortOrders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
    return ResponseEntity.ok(new SortOrders(sort).toSortOrdersText());
  }

  @Operation(
      summary = "Get something else that can be sorted.",
      operationId = "getSomethingElseSorted",
      tags = {"test-controller"},
      parameters = {
          @Parameter(name = "page", in = ParameterIn.QUERY, schema = @Schema(type = "integer")),
          @Parameter(name = "size", in = ParameterIn.QUERY, schema = @Schema(type = "integer")),
          @Parameter(name = "sort", in = ParameterIn.QUERY, array = @ArraySchema(schema = @Schema(type = "string")))
      }
  )
  @GetMapping(path = "/with-spring-sort")
  //@PageableAsQueryParam
  @SuppressWarnings("unused")
  public ResponseEntity<Page<String>> getSomethingElseSorted(
      //@Parameter(array = @ArraySchema(schema = @Schema(type = "string")))
      // @ParameterObject
      @Parameter(hidden = true)

      @PageableDefault(page = 1, size = 20, sort = "somethingElse,desc") Pageable pageRequest) {
      log.info("Pageable = {}", pageRequest);
      List<String> content = List.of("entry0", "entry1");
      Page<String> page = new PageImpl<>(
          content,
          pageRequest,
          (long)pageRequest.getPageNumber() * pageRequest.getPageSize() + content.size());
    return ResponseEntity.ok(page);
  }
}
