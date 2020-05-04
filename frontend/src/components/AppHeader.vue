<template>
    <div class="pb-2 mt-5 mb-4 border-bottom border-secondary">
        <div class="container">
            <div class="container-fluid pl-0">
                <div id="stoker-header-title-line" class="row">
                    <div id="stoker-logo" class="col-md-1">
                        <a href="/"><img alt="logo" height="120" src="../assets/logo_transparent.png"/></a>
                    </div>
                    <div id="title" class="col-md-10">
                        <h1 class="mb-0 display-1">
                            <a id="logo-ref" class="text-muted" href="/">Stoker</a>
                        </h1>
                    </div>
                    <div id="stoker-github-star" class="col-md-1">
                        <a class="github-button" href="https://github.com/garrett528/stoker" data-show-count="false"
                           aria-label="Star Stoker on GitHub" data-size="large">Star</a>
                    </div>
                </div>
            </div>
        </div>
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

#logo-ref {
    font-family: 'Pacifico',sans-serif;
    text-decoration: none;
}
</style>
