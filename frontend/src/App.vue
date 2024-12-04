<template>
  <div class="app-layout">
    <!-- 사이드바 -->
    <SideBar class="sidebar" :class="{ expanded: uiStore.visible }" />
    <!-- 스피드다이얼 -->
    <div
      class="speed-dial-box"
      :class="{ 'slide-in': !uiStore.visible }"
      v-show="!uiStore.visible"
    >
      <SpeedDial
        :model="items"
        :radius="80"
        type="semi-circle"
        direction="right"
        buttonClass="btn-color"
        style="position: absolute; top: calc(50% - 2rem); left: 10px"
      />
    </div>
    <!-- 메인 컨텐츠 -->
    <div class="contents-wrapper">
      <Header class="header" />
      <router-view />
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { useToast } from "primevue/usetoast";
import { useRouter } from "vue-router";
import { useUIStore } from "@/stores/uiStore";
const uiStore = useUIStore();
const toast = useToast();
const items = ref([
  {
    label: "Add",
    icon: "pi pi-pencil",
    command: () => {
      toast.add({
        severity: "info",
        summary: "Add",
        detail: "Data Added",
        life: 3000,
      });
    },
  },
  {
    label: "Update",
    icon: "pi pi-refresh",
    command: () => {
      toast.add({
        severity: "success",
        summary: "Update",
        detail: "Data Updated",
        life: 3000,
      });
    },
  },
  {
    label: "Menu",
    icon: "pi pi-bars",
    command: () => {
      console.log("Menu Command Triggered"); // 이 로그가 출력되는지 확인
      uiStore.toggleSidebar();
      console.log("Sidebar visible state:", uiStore.visible);
    },
  },
  {
    label: "Delete",
    icon: "pi pi-trash",
    command: () => {
      toast.add({
        severity: "error",
        summary: "Delete",
        detail: "Data Deleted",
        life: 3000,
      });
    },
  },

  {
    label: "Vue Website",
    icon: "pi pi-external-link",
    command: () => {
      window.location.href = "https://vuejs.org/";
    },
  },
]);
</script>

<style>
.app-layout {
  display: flex;
}
.sidebar {
  flex: 0; /*디폴트 : 접힌 상태 */
  background-color: #333;
  color: white;
  transition: all 0.3s ease-in-out;
}

.sidebar.expanded {
  flex: 0.52; /* 펼쳐진 상태 */
}

.contents-wrapper {
  flex: 3;
  background-color: #f8f9fa;
  overflow: auto;
}

.hidden {
  display: none; /* 애니메이션 종료 후 숨기기 */
}
/* Speed Dial 버튼 커스텀 스타일적용  */
.btn-color {
  background: var(--main-color) !important;
  border: none !important;
}

/*스피드 다이얼 애니메이션 추가 */
@keyframes slideInFromLeft {
  0% {
    opacity: 0;
    transform: translateX(-200%) translateY(-50%) scale(0.5) rotate(-30deg);
  }
  60% {
    opacity: 0.7;
    transform: translateX(15%) translateY(-50%) scale(1.2) rotate(10deg);
  }
  80% {
    opacity: 0.9;
    transform: translateX(-5%) translateY(-50%) scale(0.95) rotate(-5deg);
  }
  100% {
    opacity: 1;
    transform: translateX(0) translateY(-50%) scale(1) rotate(0deg);
  }
}

.slide-in {
  animation: slideInFromLeft 0.7s cubic-bezier(0.215, 0.61, 0.355, 1) forwards;
}

.speed-dial-box {
  position: fixed;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  z-index: 1000;
  will-change: transform, opacity;
}
</style>
