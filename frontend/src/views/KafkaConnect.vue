<template>
    <div id="app">
        <AppHeader></AppHeader>
        <div id="container" class="container-fluid pl-5 pr-5">
            <h2 class="text-white text-left">Kafka Connect</h2>

            <table class="table table-bordered" style="margin-top:5px">
                <thead>
                <tr>
                    <th><i class="fa fa-tag"></i> Connector</th>
                    <th><i class="fa fa-hourglass-end"></i> Status</th>
                    <th><i class="fa fa-tasks"></i> Tasks</th>
                </tr>
                </thead>
                <tbody>
                <tr v-if="connectorStatuses.length === 0">
                    <td colspan="3">No connectors available</td>
                </tr>
                <tr v-else v-for="connector in connectorStatuses" v-bind:key="connector.name" class="dataRow">
                    <td class="text-left"><router-link class="text-info" :to="'/kafkaConnect/' + connector.name">{{ connector.name }}</router-link></td>

                    <td v-if="connector.status === 'FAILED'" class="danger text-danger"><i class="fa fa-exclamation-triangle"></i> {{ connector.status }}</td>
                    <td v-else class="success"><i class="fa fa-bolt text-light"></i> {{ connector.status }}</td>
                <td>{{ displayTaskStatus(connector.taskList) }}</td>
                </tr>
            </tbody>
            </table>
        </div>
    </div>
</template>

<script>
    import AppHeader from "@/components/AppHeader";
    import { mapActions, mapGetters } from "vuex";

    export default {
        name: "KafkaConnect",
        components: {
            AppHeader
        },
        computed: {
            ...mapGetters(['connectorStatuses'])
        },
        methods: {
            ...mapActions(['getConnectorStatuses']),
            displayTaskStatus: function(taskList) {
                let failedTasks = [];
                for (let i = 0; i < taskList.length; i++) {
                    let task = taskList[i];
                    if (task.state !== 'RUNNING') {
                        failedTasks.push(task.id);
                    }
                }

                if (failedTasks.length === 0) {
                    return "All tasks are running successfully"
                } else {
                    let failedIds = failedTasks.join(", ");
                    return "Failed Task IDs: [" + failedIds + "]";
                }
            }
        },
        created() {
            this.getConnectorStatuses();
        }
    }
</script>

<style scoped>

</style>