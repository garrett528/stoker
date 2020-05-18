<template>
    <div class="pb-2 mt-5 mb-4">
        <div class="container">
            <div class="container-fluid pl-0 mb-3">
                <div id="stoker-logo" class="text-center">
                    <router-link :to="'/'"><img alt="logo" height="120" src="../assets/logo_transparent.png"/></router-link>
                </div>
            </div>
        </div>

        <nav class="navbar navbar-expand-lg navbar-light bg-dark">
            <router-link id="navbar-brand" class="navbar-brand" :to="'/'" data-ol-has-click-handler="">Stoker</router-link>

            <div class="navbar-collapse" id="navbarColor01">
                <ul class="navbar-nav mr-auto">
                    <li class="nav-item">
                        <router-link class="nav-link" :to="'/'">Home</router-link>
                    </li>
                    <li class="nav-item">
                        <router-link class="nav-link" :to="'/kafkaConnect'">Kafka Connect</router-link>
                    </li>
                    <li class="nav-item">
                        <router-link class="nav-link" :to="'/schemaRegistry'">Schema Registry</router-link>
                    </li>
                </ul>
            </div>
        </nav>
    </div>
</template>

<script>
    export default {
        name: "Header"
    }
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css?family=Pacifico');

#stoker-logo {
    text-align: left;
}

#stoker-header-title-line {
    height: 10em;
}

#navbar-brand {
    font-family: 'Pacifico',sans-serif;
    text-decoration: none;
}
</style>
