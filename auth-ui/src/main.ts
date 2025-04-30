import './assets/main.css';
import 'element-plus/dist/index.css'

import {createApp} from "vue";

import pinia from './stores/index.ts'
import ElementPlus from 'element-plus'

import App from './App.vue'
import router from './router'

const app = createApp(App)

app.use(pinia)
app.use(router)
app.use(ElementPlus)

app.mount('#app')
