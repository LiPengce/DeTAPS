import { createApp } from 'vue'
import App from './App.vue'
import router from '@/router/index.ts'
import ElementPlus from 'element-plus'
import "@/mock/index";
import 'element-plus/theme-chalk/index.css'
import '@metamask/detect-provider'
import pinia from '@/store/index.ts'
import Web3 from 'web3';
console.log(Web3);
const web3 = new Web3('http://47.96.177.120:8545');
console.log('Initialized Web3:', web3);

const app = createApp(App)
app.use(pinia).use(router).use(ElementPlus).mount('#app')
app.config.globalProperties.$web3 = web3;
