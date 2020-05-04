<template>
    <div id="container" class="container-fluid pl-5 pr-5">
        <div id="header-container" class="m-0">
            <div id="topic-header" class="float-left">
                <h3 class="text-white text-left"><i class="fa fa-database"></i> Topic Overview
                <span class="h6"><router-link class="text-light" :to="'/acl'"><i class="fa fa-lock"></i> ACLs</router-link></span>
                </h3>
            </div>
            <div id="new-topic-button-container" class="float-right">
                <router-link class="btn btn-outline-light text-light" :to="'/topic/create'"><i class="fa fa-plus"></i>&nbsp;&nbsp;New Topic</router-link>
            </div>
        </div>
        <div id="topic-overview">
            <table class="table table-bordered text-left mt-2">
                <thead>
                <tr>
                    <th id="topic-filter">
                        <span class="d-inline-block">Name</span>
                        <span class="w-auto d-inline-block align-bottom ml-1 mr-1"><input id="filter" class="form-control bg-dark text-white border-light" type="text" v-model="topicFilter"/></span>
                        <span class="d-inline-block">({{ filteredTopics.length }})</span>
                    </th>
                    <th>
                        Partitions
                        <a title="Number of partitions in the topic"
                           data-toggle="tooltip" data-placement="top" href="#"
                        ><i class="fa fa-question-circle"></i></a>
                    </th>
                    <th>
                        % Preferred
                        <a title="Percent of partitions where the preferred broker has been assigned leadership"
                           data-toggle="tooltip" data-placement="top" href="#"
                        ><i class="fa fa-question-circle"></i></a>
                    </th>
                    <th>
                        # Under-replicated
                        <a title="Number of partition replicas that are not in sync with the primary partition"
                           data-toggle="tooltip" data-placement="top" href="#"
                        ><i class="fa fa-question-circle"></i></a>
                    </th>
                    <th>Custom Config</th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="topic in filteredTopics" v-bind:key="topic.name" class="text-center">
                    <td class="text-left"><router-link class="text-info" :to="'/topics/' + topic.name">{{ topic.name }}</router-link></td>
                    <td>{{ topic.partitions.length }}</td>

                    <td v-if="topic.preferredReplicaPercent < 1" class="text-warning">{{ percentify(topic.preferredReplicaPercent) }}</td>
                    <td v-else>{{ percentify(topic.preferredReplicaPercent) }}</td>

                    <td v-if="topic.underReplicatedPartitions.length > 0" class="text-warning">{{ topic.underReplicatedPartitions.length }}</td>
                    <td v-else>{{ topic.underReplicatedPartitions.length }}</td>

                    <td>{{ Object.keys(topic.config).length > 0 ? "Yes" : "No" }}</td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</template>

<script>
    import { mapState } from "vuex";

    export default {
        name: "TopicSummary",
        data () {
            return {
                topicFilter: ''
            }
        },
        computed: {
            ...mapState(['topics']),
            filteredTopics: function () {
                return this.$store.state.topics.filter(t => t.name.includes(this.topicFilter) )
            }
        }
    }
</script>

<style scoped>

</style>