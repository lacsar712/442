import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import 'layui/dist/css/layui.css'
import './assets/styles/global.css'

const app = createApp(App)
app.use(router)
app.mount('#app')
