<template>
    <div id="container" class="container-fluid pl-5 pr-5">
        <h2 class="text-white text-left">Kafka Cluster Overview</h2>
        <div id="cluster-overview">
            <table class="table table-bordered text-left mt-2">
                <tbody>
                <tr>
                    <td><i class="fa fa-server"></i>&nbsp;&nbsp;Bootstrap Servers</td>
                    <td>{{ brokerConnect }}</td>
                </tr>
                <tr>
                    <td><i class="fa fa-database"></i>&nbsp;&nbsp;Total topics</td>
                    <td>{{ clusterSummary.topicCount}}</td>
                </tr>
                <tr>
                    <td><i class="fa fa-archive"></i>&nbsp;&nbsp;Total partitions</td>
                    <td>{{ clusterSummary.partitionCount }}</td>
                </tr>
                <tr>
                    <td><i class="fa fa-trophy"></i>&nbsp;&nbsp;Total preferred partition leader</td>
                    <td v-if="clusterSummary.preferredReplicaPercent < 1.0" class="text-warning">{{ percentify(clusterSummary.preferredReplicaPercent) }}</td>
                    <td v-else>{{ percentify(clusterSummary.preferredReplicaPercent) }}</td>
                </tr>
                <tr>
                    <td><i class="fa fa-heartbeat"></i>&nbsp;&nbsp;Total under-replicated partitions</td>
                    <td v-if="clusterSummary.underReplicatedCount > 0" class="text-warning">{{ clusterSummary.underReplicatedCount }}</td>
                    <td v-else>{{ clusterSummary.underReplicatedCount }}</td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</template>

<script>
    import { mapState } from "vuex";

    export default {
        name: "ClusterSummary",
        computed: {
            ...mapState(['brokerConnect', 'clusterSummary'])
        }
    }
</script>

<style scoped>

</style>