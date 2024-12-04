<template>
  <div class="app-layout">
    <!-- 사이드바 -->
    <SideBar class="sidebar" />
    <!-- 스피드다이얼 -->
    <SpeedDial
      v-if="uiStore.visible"
      :model="items"
      :radius="80"
      type="semi-circle"
      direction="right"
      buttonClass="btn-color"
      style="position: absolute; top: calc(50% - 2rem); left: 0"
    />
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
const router = useRouter();

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
.slide-out {
  animation: slideOut 1.5s forwards;
}

@keyframes slideOut {
  0% {
    opacity: 1;
    transform: translateX(0);
  }
  100% {
    opacity: 0;
    transform: translateX(-100%);
  }
}

.hidden {
  display: none; /* 애니메이션 종료 후 숨기기 */
}
/* Speed Dial 버튼 색상  */
.btn-color {
  background: var(--main-color) !important;
  border: none !important;
}
</style>
