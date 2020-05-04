import Vue from "vue";
import Vuex from "vuex";
import axios from "axios";

Vue.use(Vuex);

export default new Vuex.Store({
    state: {
        clusterSummary: {},
        brokerConnect: '',
        brokers: [],
        topics: []
    },
    getters: {},
    mutations: {
        setClusterSummary(state, summary) {
            state.clusterSummary = summary;
        },
        setBrokerConnect(state, brokerConnect) {
            state.brokerConnect = brokerConnect;
        },
        setBrokers(state, brokers) {
            state.brokers = brokers;
        },
        setTopics(state, topics) {
            state.topics = topics;
        }
    },
    actions: {
        getClusterSummary({ commit }) {
            axios.get("/cluster/summary")
                .then(response => {
                    commit('setClusterSummary', response.data);
                })
                .catch(error => {
                    console.log(error);
                })
        },
        getBrokerConnect({ commit }) {
            axios.get("/cluster/brokerConnect")
                .then(response => {
                    commit('setBrokerConnect', response.data)
                })
                .catch(error => {
                    console.log(error);
                })
        },
        getBrokers({ commit }) {
            axios.get("/brokers/")
                .then(response => {
                    commit('setBrokers', response.data)
                })
                .catch(error => {
                    console.log(error);
                })
        },
        getTopics({ commit }) {
            axios.get("/topics/")
                .then(response => {
                    commit('setTopics', response.data)
                })
                .catch(error => {
                    console.log(error)
                })
        }
    }
})