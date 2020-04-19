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
import kafdrop.model.*;
import kafdrop.service.*;
import org.springframework.boot.info.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@RestController
public final class ClusterController {
  private final KafkaMonitor kafkaMonitor;

  public ClusterController(KafkaMonitor kafkaMonitor) {
    this.kafkaMonitor = kafkaMonitor;
  }

  private static BuildProperties blankBuildProperties() {
    final var properties = new Properties();
    properties.setProperty("version", "3.x");
    properties.setProperty("time", String.valueOf(System.currentTimeMillis()));
    return new BuildProperties(properties);
  }

  @ApiOperation(value = "getCluster", notes = "Get high level broker, topic, partition, and Connect data for the Kafka cluster")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Success", response = ClusterInfoVO.class)
  })
  @GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
  public ClusterInfoVO getCluster() throws IOException {
    final var vo = new ClusterInfoVO();
    vo.brokers = kafkaMonitor.getBrokers();
    vo.topics = kafkaMonitor.getTopics();
    vo.summary = kafkaMonitor.getClusterSummary(vo.topics);
    vo.connectors = kafkaMonitor.getConnectorStatuses();
    return vo;
  }

  /**
   * Simple DTO to encapsulate the cluster state.
   */
  public static final class ClusterInfoVO {
    ClusterSummaryVO summary;
    List<BrokerVO> brokers;
    List<TopicVO> topics;
    List<ConnectorStatusVO> connectors;
  }
}
