<template>
    <div id="app">
        <AppHeader></AppHeader>
        <ClusterSummary></ClusterSummary>
        <BrokerSummary></BrokerSummary>
        <TopicSummary></TopicSummary>
    </div>
</template>

<script>
    import AppHeader from "@/components/AppHeader";
    import ClusterSummary from "@/components/ClusterSummary";
    import BrokerSummary from "@/components/BrokerSummary";
    import TopicSummary from "@/components/TopicSummary";

    export default {
        name: "Home",
        components: {
            AppHeader,
            ClusterSummary,
            BrokerSummary,
            TopicSummary
        }
    }
</script>

<style scoped>

</style>