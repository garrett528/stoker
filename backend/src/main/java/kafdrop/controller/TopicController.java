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
import kafdrop.exception.TopicNotFoundException;
import kafdrop.model.*;
import kafdrop.service.*;
import org.springframework.http.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/topics")
public final class TopicController {
  private final KafkaMonitor kafkaMonitor;

  public TopicController(KafkaMonitor kafkaMonitor) {
    this.kafkaMonitor = kafkaMonitor;
  }

  @ApiOperation(value = "getTopic", notes = "Get details for a topic")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Success", response = TopicVO.class),
      @ApiResponse(code = 404, message = "Invalid topic name")
  })
  @GetMapping(path = "/{name:.+}", produces = MediaType.APPLICATION_JSON_VALUE)
  public TopicVO getTopic(@PathVariable("name") String topicName) {
    return kafkaMonitor.getTopic(topicName)
        .orElseThrow(() -> new TopicNotFoundException(topicName));
  }

  @ApiOperation(value = "getAllTopics", notes = "Get list of all topics")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Success", response = TopicVO.class, responseContainer = "List")
  })
  @GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<TopicVO> getAllTopics() {
    return kafkaMonitor.getTopics();
  }

  @ApiOperation(value = "getConsumers", notes = "Get consumers for a topic")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Success", response = ConsumerVO.class, responseContainer = "List"),
      @ApiResponse(code = 404, message = "Invalid topic name")
  })
  @GetMapping(path = "/{name:.+}/consumers", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<ConsumerVO> getConsumers(@PathVariable("name") String topicName) {
    final var topic = kafkaMonitor.getTopic(topicName)
        .orElseThrow(() -> new TopicNotFoundException(topicName));
    return kafkaMonitor.getConsumers(Collections.singleton(topic));
  }

  /**
   * API for topic creation
   * @param createTopicVO request
   */
  @ApiOperation(value = "createTopic", notes = "Create topic")
  @ApiResponses(value = {
      @ApiResponse(code = 201, message = "Success", response = ResponseEntity.class),
      @ApiResponse(code = 400, message = "Error", response = ResponseEntity.class)
  })
  @RequestMapping(path = "/create", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
  public ResponseEntity<String> createTopic(@RequestBody CreateTopicVO createTopicVO, Model model) {
      try {
        kafkaMonitor.createTopic(createTopicVO);
      } catch (Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("error: " + ex.getMessage());
      }
      return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}
