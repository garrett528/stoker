import Vue from "vue";
import Router from "vue-router";
import Home from "./views/Home.vue";
import TopicDetail from "@/views/TopicDetail";

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
        }
    ]
});