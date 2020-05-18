<template>
    <div id="app">
        <AppHeader></AppHeader>
        <div id="container" class="container-fluid pl-5 pr-5">
            <h3 class="text-white text-left">Connector: {{ name }}</h3>

            <div class="row">
                <div id="config-container" class="col-8">
                    <div class="row no-gutters">
                        <div class="col-9">
                            <h4 class="text-white text-left">Configuration</h4>
                        </div>
                        <div class="col-3">
                            <button type="button" class="btn btn-info mb-2" v-on:click="updateConfigJson"><i class="fa fa-wrench"></i>&nbsp;&nbsp;Update Configuration</button>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-12">
                            <v-jsoneditor ref="editor" v-model="configJson" :options="{mode: 'code'}" :plus="false" height="600px" @error="onError"></v-jsoneditor>
                        </div>
                    </div>
                </div>

                <div id="tasks-container" class="col-4">
                <h4 class="text-white text-left">Tasks</h4>
                    <table class="table table-bordered" style="margin-top:5px">
                        <thead>
                        <tr>
                            <th><i class="fa fa-tasks"></i> Task ID</th>
                            <th>State</th>
                            <th>Worker ID</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr v-for="task in status.taskList" v-bind:key="task.id">
                            <td>{{ task.id }}</td>
                            <td v-if="task.state == 'FAILED'">
                                <popper trigger="hover" :options="{placement: 'bottom',modifiers: {offset:{offset: '0,10px'}}}">
                                    <a class="text-danger" slot="reference">{{ task.state }}</a>
                                    <div class="card text-white border-secondary mb-3 popper">
                                        <div class="card-body">
                                            <p class="card-text">{{ task.trace }}</p>
                                        </div>
                                    </div>
                                </popper>
                            </td>
                            <td v-else>{{ task.state }}</td>
                            <td>{{ task.workerId }}</td>
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
    import VJsoneditor from "v-jsoneditor";
    import axiosRetry from "axios-retry";
    import Popper from 'vue-popperjs';
    import 'vue-popperjs/dist/vue-popper.css';

    export default {
        name: "ConnectorDetail",
        props: ['name'],
        components: {
            AppHeader,
            VJsoneditor,
            Popper
        },
        data () {
            return {
                configJson: '',
                tasks: [],
                status: {},
                timeout: null,
                showCard: false,
                isLoaded: false
            }
        },
        methods: {
            getConnector: function(name) {
                const client = axios.create();
                axiosRetry(client, {retries: 3})
                client.get("/connect/" + name)
                    .then(response => {
                        this.tasks = response.data.tasks
                        this.configJson = response.data.configJson
                    })
                    .catch(error => {
                        console.log(error);
                    })
            },
            onError: function() {
                console.log('Jsoneditor error!');
            },
            updateConfigJson: async function() {
                let newConfigJson = JSON.stringify(this.$refs.editor.editor.get());
                let confirmMsg = "Are you sure you want to update the configuration?"
                if (confirm(confirmMsg)) {
                    await axios.post('/connect/upsertConfiguration/' + this.name, newConfigJson)
                        .then(response => {
                            if (response.status === 200) {
                                this.$router.go(0);
                            }
                            else {
                                alert(response.toString())
                            }
                        })
                        .catch(error => {
                            alert(error.toString())
                            this.$router.go(0);
                        })
                }
            },
            getConnectorStatus: function(name) {
                axios.get('/connect/' + name + '/status')
                .then(response => {
                    this.status = response.data;
                })
                .catch(error => {
                    console.log(error);
                })
            },
            mouseover: function (event) {
                console.log(event.pageX, event.pageY)
                clearTimeout(this.timeout)
                var self = this
                this.timeout = setTimeout(function () {
                    self.showCard = true
                    setTimeout(function () {
                        self.isLoaded=true;
                    }, 500)
                }, 500)
            },
            mouseleave: function () {
                var self = this
                this.timeout = setTimeout(function () {
                    self.showCard = false
                    self.isLoaded = false
                }, 200)
            },
            cardOver: function () {
                console.log('card over')
                clearTimeout(this.timeout);
                this.showCard = true
            },
            cardLeave: function () {
                var self = this
                this.timeout = setTimeout(function () {
                    self.showCard = false
                    self.isLoaded = false
                }, 200)
            }
        },
        created () {
            this.getConnector(this.name);
            this.getConnectorStatus(this.name);
        }
    }
</script>

<style scoped>

</style>