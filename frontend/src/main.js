import './assets/main.css'

import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import PrimeVue from 'primevue/config'; // PrimeVue 추가
import Aura from '@primevue/themes/aura'

createApp(App).use(PrimeVue, {theme :  { preset: Aura, options:{
    prefix: 'p',
    darkModeSelector: 'system',
    cssLayer: false
    }
}
                                }).use(router).mount('#app');

