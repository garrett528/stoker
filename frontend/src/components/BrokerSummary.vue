<template>
    <div id="container" class="container-fluid pl-5 pr-5">
        <h3 class="text-white text-left"><i class="fa fa-server"></i> Broker Overview</h3>
        <div id="broker-overview">
            <table class="table table-bordered text-left mt-2">
                <thead>
                <tr>
                    <th><i class="fa fa-tag"></i>&nbsp;&nbsp;ID</th>
                    <th><i class="fa fa-laptop"></i>&nbsp;&nbsp;Host</th>
                    <th><i class="fa fa-plug"></i>&nbsp;&nbsp;Port</th>
                    <th><i class="fa fa-server"></i>&nbsp;&nbsp;Rack</th>
                    <th><i class="fa fa-trophy"></i>&nbsp;&nbsp;Controller</th>
                    <th>
                        <i class="fa fa-archive"></i>&nbsp;&nbsp;Number of partitions (% of total)
                        <a title="# of partitions this broker is the leader for"
                           data-toggle="tooltip" data-placement="top" href="#">
                            <i class="fa fa-question-circle"></i>
                        </a>
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="broker in brokers" v-bind:key="broker.id" class="text-center">
                    <td>{{ broker.id }}</td>
                    <td class="text-left">{{ broker.host }}</td>
                    <td>{{ broker.port }}</td>
                    <td>{{ broker.rack ? broker.rack : "-" }}</td>
                    <td>{{ broker.controller ? "Yes" : "No" }}</td>
                    <td>{{ brokerPartitions(broker.id) }}</td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</template>

<script>
    import { mapState } from "vuex";

    export default {
        name: "BrokerSummary",
        computed: {
            ...mapState(['brokers', 'clusterSummary'])
        },
        methods: {
            brokerPartitions: function(brokerId)  {
                let partitions = this.$store.state.clusterSummary.brokerLeaderPartitionCount[brokerId];
                let totalPartitions = this.$store.state.clusterSummary.partitionCount;
                let percentOfTotal = (partitions/totalPartitions).toFixed(2) * 100 + "%";

                return partitions + " (" + percentOfTotal + ")";
            }
        }
    }
</script>

<style scoped>

</style>