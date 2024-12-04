import "./assets/main.css";

import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import Header from "./components/Header.vue";
import SideBar from "./components/SideBar.vue";
import PrimeVue from "primevue/config"; // PrimeVue 추가
import Aura from "@primevue/themes/aura";
import ToastService from "primevue/toastservice";
import ConfirmationService from "primevue/confirmationservice";
import DialogService from "primevue/dialogservice";
import { createPinia } from "pinia";
const pinia = createPinia();
const app = createApp(App);
app.component("Header", Header);
app.component("SideBar", SideBar);
app.use(PrimeVue, {
  theme: {
    preset: Aura,
    options: {
      prefix: "p",
      darkModeSelector: "system",
      cssLayer: false,
    },
  },
});
app.use(ConfirmationService);
app.use(ToastService);
app.use(DialogService);
app.use(pinia);
app.use(router).mount("#app");
