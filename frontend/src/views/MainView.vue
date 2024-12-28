<template>
  <div class="main-page-container">
    <div class="main-page-header">
      <h1>{{ user.nickname }}님 환영합니다. 즐거운 하루보내세요 😃</h1>
    </div>
    <!-- 상단 섹션: 프로필, 달력, 채팅 -->
    <div class="top-section">
      <div class="calendar-section scrollable wide">
        <ScheduleCalendarMini />
      </div>
      <div class="chat-section scrollable narrow">
        <ChatLayoutView />
      </div>
    </div>

    <!-- 하단 섹션: 게시판, 카테고리, 정산 -->
    <div class="bottom-section">
      <div class="board-section scrollable">
        <BoardView />
      </div>
      <div class="category-section scrollable">
        <CategoryChart />
      </div>
    </div>
  </div>
</template>

<script>
import UserProfile from '@/views/user/UserProfileView.vue';
import BoardView from '@/views/board/BoardView.vue';
import ScheduleCalendarMini from '@/views/schedule/components/ScheduleCalendarMini.vue';
import SettlementView from '@/views/moeim/SettlementView.vue';
import ChatLayoutView from '@/views/chat/ChatRoomListView.vue';
import { useAuthStore } from '@/stores/auth';
import CategoryChart from './transaction/CategoryChart.vue';

export default {
  name: 'MainPage',
  components: {
    UserProfile,
    BoardView,
    ScheduleCalendarMini,
    SettlementView,
    ChatLayoutView,
    CategoryChart,
  },
  setup() {
    const authStore = useAuthStore();
    const accessToken = authStore.accessToken;
    const user = authStore.user;

    console.log('Access Token:', accessToken);
    console.log('user :', user);

    return {
      accessToken,
      user,
    };
  },
};
</script>

<style scoped>
.main-page-container {
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  padding: 10px;
  background-color: #f5f7fa;
  height: 100vh;
  width: 70%;
  /* 전체 화면 높이 */
}
/* 메인 헤더 인삿말 */
.main-page-header {
  margin-bottom: 10px;
  padding: 20px 10px;
  background-color: white;
  border-radius: 10px;
  color: rgb(56, 55, 55);
  box-shadow: 0 2px 4px rgba(142, 76, 166, 0.1);
  font-size: 1.5rem;
  font-weight: 500;
  /* 헤더 */
}

/* 상단 섹션 */
.top-section,
.bottom-section {
  display: flex;
  gap: 10px;
  flex: 1;
  /* 동일한 높이 */
}

/* 가로 비율 조정 */
.profile-section,
.chat-section {
  flex: 1;
  /* 좁은 너비 */
  background-color: white;
  padding: 10px;
  border-radius: 10px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  overflow-y: auto;
  /* 스크롤 활성화 */
  max-height: 45vh;
}

.calendar-section {
  flex: 2;
  /* 넓은 너비 */
  background-color: white;
  padding: 10px;
  border-radius: 10px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  overflow-y: auto;
  max-height: 45vh;
}

/* 하단 섹션 */
.board-section,
.category-section,
.settlement-section {
  flex: 1;
  background-color: white;
  padding: 10px;
  border-radius: 10px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  overflow-y: auto;
  max-height: 45vh;
}

/* 스크롤 활성화 */
.scrollable {
  overflow-y: auto;
  height: 100%;
}

/* 섹션 너비 조정 */
.narrow {
  flex: 1;
  /* 좁은 섹션 */
}

.wide {
  flex: 1;
  /* 넓은 섹션 */
}
</style>
