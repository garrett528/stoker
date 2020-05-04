<template>
  <router-view></router-view>
</template>

<script>
  import { mapActions } from "vuex";

  export default {
    name: 'App',
    methods: {
      ...mapActions(['getClusterSummary', 'getBrokerConnect', 'getBrokers', 'getTopics'])
    },
    created() {
      this.getClusterSummary();
      this.getBrokerConnect();
      this.getBrokers();
      this.getTopics();
    }
  }
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
