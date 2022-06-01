import Vue from 'vue'
import App from './App.vue'
import axios from 'axios'
import VueAxios from 'vue-axios'
import 'ant-design-vue/dist/antd.css'
import Antd from 'ant-design-vue'

Vue.use(Antd)

Vue.use(VueAxios, axios)

axios.defaults.baseURL = 'http://localhost:' + window.location.port

// axios.defaults.baseURL = 'http://localhost:8081'

axios.defaults.timeout = 160000

axios.defaults.withCredentials = true

Vue.config.productionTip = false

new Vue({
  render: (h) => h(App),
}).$mount('#app')
