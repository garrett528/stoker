<template>
    <div id="app">
        <AppHeader></AppHeader>
        <div id="container" class="container-fluid pl-5 pr-5">
            <h3 class="text-white text-left">Topic Messages: <router-link class="text-info" :to="'/topics/' + name">{{ name }}</router-link></h3>
            <div id="partition-container" class="row m-2">
                <div id="partition-sizes" class="col-12 text-left">
                    <span class="badge badge-light">First Offset: {{ topic.partitions[0].firstOffset }}</span>&nbsp;&nbsp;
                    <span class="badge badge-light">Last Offset: {{ topic.partitions[0].size }}</span>&nbsp;&nbsp;
                    <span class="badge badge-light">Size: {{ topic.partitions[0].size - topic.partitions[0].firstOffset }}</span>
                </div>
            </div>

            <div id="form-container" class="row">
                <div id="message-form-panel" class="card col-9">
                    <form id="message-form" class="form-inline card-body" @submit.prevent="processForm">
                        <div class="form-group mr-2">
                            <label for="partition" class="control-label text-white p-1">Partition</label>
                            <select class="form-control" id="partition" name="partition" v-model="partition">
                                <option v-for="partition in topic.partitions" v-bind:key="partition.id" :value="partition.id">{{ partition.id }}</option>
                            </select>
                        </div>
                        <div class="form-group mr-2">
                            <label class="control-label text-white p-1" for="offset">Offset</label>
                            <input type="number" class="form-control" id="offset" value="0" min="0" v-model="offset">
                        </div>
                        <div class="form-group mr-2">
                            <label class="control-label text-white p-1" for="count">Number of messages</label>
                            <input type="number" class="form-control" id="count" value="100" min="0" v-model="count">
                        </div>
                        <div class="form-group mr-2">
                            <label class="control-label text-white p-1" for="format">Format</label>
                            <select class="form-control" id="format" name="format" v-model="format">
                                <option value="AVRO">AVRO</option>
                                <option value="DEFAULT">DEFAULT</option>
<!--                                <option value="PROTOBUF">PROTOBUF</option>-->
                            </select>
                        </div>
                        <button class="btn btn-info ml-2" type="submit"><i class="fa fa-search"></i> View Messages</button>
                    </form>
                </div>
            </div>
            <div v-if="messages.length > 0" id="message-display" class="container-fluid">
                <div class="text-white text-left" v-for="message in messages" v-bind:key="message.offset">
                    <span class="badge badge-light">Offset:</span>&nbsp;{{ message.offset }}
                    <span class="badge badge-light">Key:</span>&nbsp;{{ message.key }}
                    <span class="badge badge-light">Timestamp:</span>&nbsp;{{ getFormattedDate(message.timestamp) }}
                    <span class="badge badge-light">Headers:</span>&nbsp;{{ isEmpty(message.headers) }}
                    <div>
                        <pre>{{ JSON.stringify(JSON.parse(message.message), null, 3) }}</pre>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import AppHeader from "@/components/AppHeader";
    import axios from "axios";

    export default {
        name: "TopicMessages",
        props: ['name'],
        components: {
            AppHeader
        },
        data() {
            return {
                messages: [],
                topic: [],
                partition: 0,
                offset: 0,
                count: 100,
                format: 'AVRO'
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
            getPartitionMessages: function(name, params) {
                axios.get("/messages/topic/" + name + "/messages?" + params)
                    .then(response => {
                        this.messages = response.data
                    })
                    .catch(error => {
                        console.log(error);
                        alert("error encountered submitting request. are you sure the message type is correct?")
                    })
            },
            processForm: function() {
                const parameters = new URLSearchParams({
                    partition: this.partition.toString(),
                    offset: this.offset.toString(),
                    count: this.count.toString(),
                    format: this.format })
                this.getPartitionMessages(this.name, parameters)
            },
            getFormattedDate: function(timestamp) {
                let date = Date.parse(timestamp);
                let d = new Date(date)
                return d.getFullYear() + "-" +
                    ("00" + (d.getMonth() + 1)).slice(-2) + "-" +
                    ("00" + d.getDate()).slice(-2) + " " +
                    ("00" + d.getHours()).slice(-2) + ":" +
                    ("00" + d.getMinutes()).slice(-2) + ":" +
                    ("00" + d.getSeconds()).slice(-2);
            },
            isEmpty: function(obj) {
                if (Object.keys(obj).length === 0)
                    return "empty";
                else
                    return obj;
            },
        },
        created() {
            this.getTopic(this.name)
        }
    }
</script>

<style scoped>

</style>