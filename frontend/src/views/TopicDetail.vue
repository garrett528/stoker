<template>
    <div id="app">
        <AppHeader></AppHeader>
        <div id="container" class="container-fluid pl-5 pr-5">
            <h3 class="text-white text-left">Topic: {{ name }}</h3>
            <div id="grid-container" class="row">

                <div id="partition-detail" class="col-8">
                    <div id="partition-detail-header-container" class="float-left">
                        <h4 class="text-white text-left">Partition Detail</h4>
                    </div>
                    <div id="view-messages-button-container" class="float-right">
                        <router-link class="text-info" :to="'/topics/' + name + '/messages/'">
                            <button type="button" class="btn btn-dark mb-2"><i class="fa fa-eye"></i>&nbsp;&nbsp;View Messages</button>
                        </router-link>
                    </div>

                    <table class="table table-bordered text-left mt-2">
                        <thead>
                        <tr>
                            <th>Partition</th>
                            <th>First Offset</th>
                            <th>Last Offset</th>
                            <th>Size</th>
                            <th>Leader Node</th>
                            <th>Replica Nodes</th>
                            <th>In-sync Replica Nodes</th>
                            <th>Offline Replica Nodes</th>
                            <th>Preferred Leader</th>
                            <th>Under-replicated</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr v-for="partition in topic.partitions" v-bind:key="partition.id" class="text-center">
                            <td>{{ partition.id }}</td>
                            <td>{{ partition.firstOffset }}</td>
                            <td>{{ partition.size }}</td>
                            <td>{{ partition.size - partition.firstOffset }}</td>

                            <td v-if="partition.leader.length == 0" class="bg-warning">{{ partition.leader.id }}</td>
                            <td v-else>{{ partition.leader.id }}</td>

                            <td>{{ prettyPrintNodeList(partition.replicas) }}</td>
                            <td>{{ prettyPrintNodeList(partition.inSyncReplicas) }}</td>
                            <td>{{ prettyPrintNodeList(partition.offlineReplicas) }}</td>

                            <td v-if="!partition.leaderPreferred" class="bg-warning">{{ partition.leaderPreferred ? "Yes" : "No" }}</td>
                            <td v-else>{{ partition.leaderPreferred ? "Yes" : "No" }}</td>

                            <td v-if="partition.underReplicated" class="bg-warning">{{ partition.underReplicated ? "Yes" : "No" }}</td>
                            <td v-else>{{ partition.underReplicated ? "Yes" : "No" }}</td>
                        </tr>
                        </tbody>
                    </table>
                </div>

                <div id="config-consumer-detail-container" class="col-4">
                    <h4 class="text-white text-left">Configuration</h4>
                    <table class="table table-bordered text-left mt-2">
                        <tr v-for="(value,config) in topic.config" v-bind:key="config">
                            <td>{{ config }}</td>
                            <td>{{ value }}</td>
                        </tr>
                    </table>

                    <div id="consumers-header-container" class="float-left">
                        <h4 class="text-white text-left">Consumers</h4>
                    </div>
                    <div id="delete-empty-groups-container" class="float-right">
                        <button type="button" class="btn btn-danger mb-2" v-on:click="deleteEmptyConsumerGroups"><i class="fa fa-window-close"></i>&nbsp;&nbsp;Delete Empty Groups</button>
                    </div>

                    <table class="table table-bordered text-left mt-2">
                        <thead>
                        <tr>
                            <th>Group ID</th>
                            <th>Lag</th>
                            <th>State</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr v-for="consumer in consumers" v-bind:key="consumer.groupId">
                            <td>{{ consumer.groupId }}</td>
                            <td>{{ getTopicConsumerLag(consumer.topics) }}</td>

                            <td v-if="consumer.state !== 'Stable'" class="bg-danger">{{ consumer.state }}</td>
                            <td v-else>{{ consumer.state }}</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import AppHeader from "@/components/AppHeader";
    import axios from "axios";

    export default {
        name: "TopicDetail",
        props: ['name'],
        components: {
            AppHeader
        },
        data() {
            return {
                topic: {},
                consumers: []
            }
        },
        methods: {
            getTopic: function(name) {
                axios.get("/topics/" + name)
                    .then(response => {
                        this.topic = response.data
                    })
                    .catch(error => {
                        console.log(error);
                    })
            },
            getTopicConsumers: function(name) {
                axios.get("/topics/" + name + "/consumers")
                    .then(response => {
                        this.consumers = response.data
                    })
                    .catch(error => {
                        console.log(error);
                    })
            },
            prettyPrintNodeList: function(nodeList) {
                return nodeList.map(elem => elem.id.toString()).join(', ')
            },
            getTopicConsumerLag: function(consumerTopics) {
                let lag = 0;
                for (let i = 0; i < consumerTopics.length; i++) {
                    let cTopic = consumerTopics[i];
                    if (cTopic === this.name) {
                        lag += parseInt(cTopic.lag)
                    }
                }
                return lag;
            },
            deleteEmptyConsumerGroups: function() {
              let emptyGroups = this.buildEmptyConsumerGroupList();
              if (emptyGroups.length === 0) {
                  alert("No empty groups to be deleted!");
                  return;
              }
              let confirmMsg = "Groups to be deleted: " + emptyGroups.toString() + "\n Are you sure?"
              if (confirm(confirmMsg)) {
                  axios.post('/consumers/delete', emptyGroups)
                  .then(response => {
                      console.log(response);
                      this.$router.go(0);
                  })
                  .catch(error => {
                      alert("Error occurred during deletion: " + error.toString())
                      this.$router.go(0);
                  })
              }
            },
            buildEmptyConsumerGroupList: function() {
                let emptyGroups = [];
                let consumers = this.consumers;
                for (let i = 0; i < consumers.length; i++) {
                    if (consumers[i].state === 'Empty') {
                        emptyGroups.push(consumers[i].groupId);
                    }
                }
                return emptyGroups;
            }
        },
        created() {
            this.getTopic(this.name);
            this.getTopicConsumers(this.name);
        }
    }
</script>

<style scoped>

</style>