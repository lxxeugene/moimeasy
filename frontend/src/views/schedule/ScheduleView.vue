<template>
  <div class="schedule-section">
    <!-- <div class="schedule-title">
      <h2>일정 캘린더</h2>
    </div> -->
    <div class="calendar-container">
      <ScheduleCalendar />
    </div>
  </div>
</template>

<script setup>
import ScheduleCalendar from './ScheduleCalendar.vue';
</script>

<style scoped>
.schedule-section {
  /* background-color: var(--secondary-color); */
  background-color: #f9f1ff;
  width: 100%;
  overflow-x: auto;
}
/* 
.schedule-title {
  height: 40px;
  width: 400px;
  background: #ffffff;
  border-radius: 10px;
  margin: 10px auto;
  display: flex;
  justify-content: center;
  align-items: center;
} */
.calendar-container {
  background-color: #ffffff;
  padding: 20px;
  border-radius: 5%;
  width: 1100px;
  margin: 40px auto;
}
</style>
