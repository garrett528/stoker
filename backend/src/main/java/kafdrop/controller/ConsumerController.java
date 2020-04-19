/*
 * Copyright 2017 Kafdrop contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 */

package kafdrop.controller;

import io.swagger.annotations.*;
import kafdrop.exception.ConsumerNotFoundException;
import kafdrop.model.*;
import kafdrop.service.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/consumer")
public final class ConsumerController {
  private final KafkaMonitor kafkaMonitor;

  public ConsumerController(KafkaMonitor kafkaMonitor) {
    this.kafkaMonitor = kafkaMonitor;
  }

  @ApiOperation(value = "getConsumer", notes = "Get topic and partition details for a consumer group")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Success", response = ConsumerVO.class),
      @ApiResponse(code = 404, message = "Invalid consumer group")
  })
  @GetMapping(path = "/{groupId:.+}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ConsumerVO getConsumer(@PathVariable("groupId") String groupId) throws ConsumerNotFoundException {
    final var topicVos = kafkaMonitor.getTopics();
    final var consumer = kafkaMonitor.getConsumers(topicVos)
        .stream()
        .filter(c -> c.getGroupId().equals(groupId))
        .findAny();
    return consumer.orElseThrow(() -> new ConsumerNotFoundException(groupId));
  }
}