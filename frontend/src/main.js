import Vue from 'vue'
import App from './App.vue'
import router from "./router";
import store from "./store";
import 'bootstrap';
import 'github-buttons/dist/buttons.min';
import 'bootswatch/dist/darkly/bootstrap.min.css'
import axios from 'axios'
import '@fortawesome/fontawesome-free/css/all.min.css'

Vue.config.productionTip = false
Vue.prototype.$http = axios

Vue.mixin({
  methods: {
    percentify: function (value) {
      let replicaPercent = value * 100;
      return replicaPercent.toString(10) + "%";
    }
  }
})

new Vue({
  router,
  store,
  render: h => h(App),
}).$mount('#app')
