<template>
  <div class="app-layout">
    <SideBar
      class="sidebar"
      :class="{ 'slide-out': !visible }"
      @draw-event="handleDrawEvent"
    />
    <div class="contents-wrapper">
      <Header class="header" />
      <router-view />
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
const visible = ref(true);

const handleDrawEvent = () => {
  visible.value = !visible.value;
};
</script>
<style>
.app-layout {
  display: flex;
}
.sidebar {
  flex: 0.58;
}

.contents-wrapper {
  flex: 3;
}

.slide-out {
  animation: slideOut 1.5s forwards; /* 1.5초 동안 애니메이션 실행 */
}

@keyframes slideOut {
  0% {
    opacity: 1; /* 완전히 보임 */
    transform: translateX(0); /* 원래 위치 */
  }
  100% {
    opacity: 0; /* 사라짐 */
    transform: translateX(-100%); /* 화면 왼쪽으로 이동 */
  }
}

.hidden {
  display: none; /* 애니메이션이 끝난 후 숨기기 */
}
</style>
