<template>
  <div class="app-layout">
    <SideBar class="sidebar" />
    <div class="contents-wrapper">
      <Header class="header" />
      <router-view />
    </div>
  </div>
</template>

<script>
import SideBar from "./components/SideBar.vue";

export default {
  name: "App",
};
</script>
