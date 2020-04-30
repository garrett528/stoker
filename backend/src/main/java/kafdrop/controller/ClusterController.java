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
import kafdrop.config.KafkaConfiguration;
import kafdrop.model.*;
import kafdrop.service.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/cluster")
public final class ClusterController {
  private final KafkaConfiguration kafkaConfiguration;

  private final KafkaMonitor kafkaMonitor;

  public ClusterController(KafkaConfiguration kafkaConfiguration, KafkaMonitor kafkaMonitor) {
    this.kafkaConfiguration = kafkaConfiguration;
    this.kafkaMonitor = kafkaMonitor;
  }

  @ApiOperation(value = "getClusterSummary", notes = "Get high level topic and partition data for the Kafka cluster")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Success", response = ClusterSummaryVO.class)
  })
  @GetMapping(path = "/summary", produces = MediaType.APPLICATION_JSON_VALUE)
  public ClusterSummaryVO getClusterSummary() throws IOException {
    return kafkaMonitor.getClusterSummary(kafkaMonitor.getTopics());
  }

  @ApiOperation(value = "getBrokerConnect", notes = "Get broker connection string")
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Success", response = String.class)
  })
  @GetMapping(path = "/brokerConnect", produces = MediaType.APPLICATION_JSON_VALUE)
  public String getBrokerConnect() throws IOException {
    return kafkaConfiguration.getBrokerConnect();
  }
}
