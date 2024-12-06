<template>
  <div class="app-layout">
    <!-- 토글 버튼 -->
    <button class="toggle-button" @click="toggleSidebar">
      {{ visible ? "Close Sidebar" : "Open Sidebar" }}
    </button>

    <!-- 사이드바 -->
    <div class="sidebar" :class="{ expanded: visible }">
      <p>Sidebar Content</p>
    </div>

    <!-- 메인 콘텐츠 -->
    <div class="main-content">
      <h1>Main Content</h1>
      <p>This is the main content area.</p>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";

const visible = ref(false); // 사이드바 표시 여부

// 사이드바 토글 함수
const toggleSidebar = () => {
  visible.value = !visible.value; // 상태 반전
};
</script>

<style>
/* 레이아웃 설정 */
.app-layout {
  display: flex;
  height: 100vh;
  overflow: hidden; /* 스크롤 방지 */
}

/* 토글 버튼 스타일 */
.toggle-button {
  position: fixed;
  top: 20px;
  left: 20px;
  padding: 10px 20px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  z-index: 1000;
  transition: all 0.3s ease;
}

.toggle-button:hover {
  background-color: #0056b3;
}

/* 사이드바 스타일 */
.sidebar {
  width: 0;
  height: 100%;
  background-color: #333;
  color: white;
  overflow: hidden;
  transition: all 0.3s ease; /* 부드러운 애니메이션 */
}

.sidebar.expanded {
  width: 250px; /* 펼쳐진 상태 */
  padding: 20px; /* 내용이 보이도록 패딩 추가 */
}

/* 메인 콘텐츠 스타일 */
.main-content {
  flex-grow: 1;
  padding: 20px;
  background-color: #f8f9fa;
}
</style>
