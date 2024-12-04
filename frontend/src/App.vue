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
export default {
  name: "App",
};
</script>
