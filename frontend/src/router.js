import Vue from "vue";
import Router from "vue-router";
import Home from "./views/Home.vue";
import TopicDetail from "@/views/TopicDetail";
import TopicMessages from "@/views/TopicMessages";
import KafkaConnect from "@/views/KafkaConnect";
import ConnectorDetail from "@/views/ConnectorDetail";

Vue.use(Router);

export default new Router({
    mode: 'history',
    routes: [
        {
            path: "/",
            name: "home",
            component: Home
        },
        {
            path: "/topics/:name",
            name: "topicDetail",
            component: TopicDetail,
            props: true
        },
        {
            path: "/topics/:name/messages",
            name: "topicMessages",
            component: TopicMessages,
            props: true
        },
        {
            path: "/kafkaConnect",
            name: "kafkaConnect",
            component: KafkaConnect
        },
        {
            path: "/kafkaConnect/:name",
            name: "connectorDetail",
            component: ConnectorDetail,
            props: true
        }
    ]
});