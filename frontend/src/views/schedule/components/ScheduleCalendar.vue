<template>
  <div class="demo-app">
    <!-- 메인 캘린더 영역 -->
    <div class="demo-app-main">
      <FullCalendar class="demo-app-calendar" :options="calendarOptions">
        <!-- 이벤트 커스터마이징 -->
        <template v-slot:eventContent="arg">
          <b>{{ arg.timeText }}</b>
          <i>{{ arg.event.title }}</i>
        </template>
      </FullCalendar>
    </div>

    <!-- 사이드바 -->
  </div>
</template>

<script>
import { defineComponent } from 'vue';
import FullCalendar from '@fullcalendar/vue3'; // FullCalendar Vue3 컴포넌트
import dayGridPlugin from '@fullcalendar/daygrid'; // 월간 보기 플러그인
import timeGridPlugin from '@fullcalendar/timegrid'; // 주간 및 일별 보기 플러그인
import interactionPlugin from '@fullcalendar/interaction'; // 상호작용(클릭 및 드래그) 플러그인
import { INITIAL_EVENTS, createEventId } from '@/utils/event-utils'; // 초기 이벤트 및 ID 생성 유틸리티->제거예정
import googleCalendarPlugin from '@fullcalendar/google-calendar';

// 구글 캘린더 API 키와 캘린더 ID
const googleCalendarApiKey = import.meta.env.VITE_GOOGLE_API_KEY; // 구글 API 키
const googleCalendarId = 'ko.south_korea#holiday@group.v.calendar.google.com'; // 구글 캘린더 ID

export default defineComponent({
  components: {
    FullCalendar, // FullCalendar 컴포넌트 등록
  },
  data() {
    return {
      // FullCalendar 설정 옵션
      calendarOptions: {
        plugins: [
          dayGridPlugin, // 월간 보기 플러그인
          timeGridPlugin, // 주간 및 일별 보기 플러그인
          interactionPlugin, // 날짜 클릭 및 드래그를 위한 플러그인
          googleCalendarPlugin, // 구글 캘린더플러그인
        ],
        headerToolbar: {
          // 캘린더 상단 헤더 설정
          left: 'today myCustomButton', // 왼쪽에 이전/다음/오늘 버튼
          center: 'prev title next', // 중앙에 제목 표시
          right: 'timeGridDay,timeGridWeek,dayGridMonth', // 오른쪽에 보기 전환 버튼
        },
        initialView: 'dayGridMonth', // 초기 보기: 월간 보기
        initialEvents: INITIAL_EVENTS, // 초기 이벤트 설정
        editable: true, // 이벤트 수정 가능 여부
        selectable: true, // 날짜 선택 가능 여부
        selectMirror: true, // 날짜 선택 시 미러 효과 활성화
        dayMaxEvents: true, // 하루 최대 이벤트 수 표시
        weekends: true, // 주말 표시 여부
        locale: 'ko', // 언어 설정: 한국어
        googleCalendarApiKey: googleCalendarApiKey,
        dayCellContent: (info) => {
          return info.date.getDate(); //'일' 텍스트제거 후 날짜의 '일(day)숫자'만 반환
        },
        select: this.handleDateSelect, // 날짜 선택 이벤트 핸들러
        eventClick: this.handleEventClick, // 이벤트 클릭 핸들러
        eventsSet: this.handleEvents, // 이벤트 변경 시 호출되는 핸들러
        // 구글 캘린더 연동 공휴일 가져오기
        eventSources: [
          {
            googleCalendarId: googleCalendarId,
            className: 'holiday-event',
          },
        ],

        customButtons: {
          myCustomButton: {
            text: 'Add Event',
            click: () => {
              const calendarApi = this.calendarRef.getApi(); // 캘린더 API 가져오기
              calendarApi.addEvent({
                title: 'New Event',
                start: new Date(),
                allDay: true,
              });
              alert('새 이벤트가 추가되었습니다!');
            },
          },
        },
        /* 원격 데이터베이스와 동기화하려면 아래 이벤트를 사용할 수 있습니다:
        eventAdd: // 이벤트 추가 시 호출
        eventChange: // 이벤트 변경 시 호출
        eventRemove: // 이벤트 삭제 시 호출
        */
      },
      currentEvents: [], // 현재 캘린더에 표시된 이벤트 저장
    };
  },
  methods: {
    // 주말 표시 여부를 토글하는 메서드
    handleWeekendsToggle() {
      this.calendarOptions.weekends = !this.calendarOptions.weekends; // 주말 표시 여부 변경
    },
    // 날짜 선택 이벤트 핸들러
    handleDateSelect(selectInfo) {
      let title = prompt('새 이벤트의 제목을 입력하세요'); // 사용자 입력 요청
      let calendarApi = selectInfo.view.calendar; // 캘린더 API 가져오기

      calendarApi.unselect(); // 선택된 날짜 해제

      if (title) {
        calendarApi.addEvent({
          id: createEventId(), // 유니크 ID 생성
          title,
          start: selectInfo.startStr, // 시작 날짜
          end: selectInfo.endStr, // 종료 날짜
          allDay: selectInfo.allDay, // 하루 종일 여부
        });
      }
    },
    // 이벤트 클릭 시 호출되는 핸들러
    handleEventClick(clickInfo) {
      if (
        confirm(`이벤트 '${clickInfo.event.title}'을(를) 삭제하시겠습니까?`)
      ) {
        clickInfo.event.remove(); // 이벤트 삭제
      }
    },
    // 이벤트 변경 시 호출되는 핸들러
    handleEvents(events) {
      this.currentEvents = events; // 현재 이벤트를 업데이트
    },
  },
});
</script>

<style>
/* 스타일 정의 */

.demo-app {
  display: flex;
  min-height: 100%;
  font-family:
    Arial,
    Helvetica Neue,
    Helvetica,
    sans-serif;
  font-size: 14px;
}

.demo-app-sidebar {
  width: 200px;
  line-height: 1.5;
  background: #f9f1ff;
  border-right: 1px solid #d3e2e8;
}

.demo-app-sidebar-section {
  padding: 2em;
}

.demo-app-main {
  flex-grow: 1;
  padding: 2em;
}

.fc {
  max-width: 1100px;
  margin: 0 auto;
}

/* 헤더 툴바 */
.fc-header-toolbar {
}
/* 헤더 좌측  */
.fc-toolbar-chunk:nth-of-type(1) {
  width: 200px;
  .fc-button {
    border: 1px solid #e9e9e9 !important;
    color: #787a7b !important; /* 텍스트 색상 */
  }
}
/* 헤더 중앙 */
.fc-toolbar-chunk:nth-of-type(2) {
  display: flex;
  justify-content: center;
  align-items: center;
}

/* 헤더 우측  */
.fc-toolbar-chunk:nth-of-type(3) {
  /*버튼 커스텀 */
  .fc-button {
    background-color: #fafbfd !important;
    color: #000000; /* 텍스트 색상 */
    cursor: pointer;
  }
  .fc-button:nth-of-type(1) {
    border: 1px solid #d4d4d4 !important;
    border-top-left-radius: 10px;
    border-bottom-left-radius: 10px;
  }
  .fc-button:nth-of-type(2) {
    border: 1px solid #d4d4d4 !important;
  }
  .fc-button:nth-of-type(3) {
    border: 1px solid #d4d4d4 !important;
    border-top-right-radius: 10px;
    border-bottom-right-radius: 10px;
  }

  .fc-button:hover {
    background-color: #f1f4f9 !important;
  }
  .fc-button:focus {
    background-color: #5a8dff !important;
    color: #ffff !important;
    border: 1px solid #5a8dff !important;
  }
}

/* 헤더 버튼 */
.fc-button {
  background-color: #ffffff !important;
  border: none !important;
  color: #000000 !important; /* 텍스트 색상 */
  cursor: pointer;
}
.fc-button:hover {
  background-color: #f1f4f9 !important;
}
.fc-button:focus {
  box-shadow: none !important;
}

/* 테이블 헤더 셀 */
.fc-col-header-cell {
  height: 30px;
  background: #f1f4f9;
  border: none !important;
}

.fc-col-header-cell:nth-of-type(1) {
  border-top-left-radius: 10px;
}
.fc-col-header-cell:nth-of-type(7) {
  border-top-right-radius: 10px;
}

.fc-col-header-cell > div {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 30px;
  font-weight: 500;
}
/* 날짜 패딩효과 */
.fc-daygrid-day-number {
  padding: 10px !important;
}
.fc-theme-standard .fc-scrollgrid {
  border-top-left-radius: 10px;
  border-top-right-radius: 10px;
}

/* 기본 이벤트 스타일 */
.fc-event {
  background-color: #ebe2fd;
  color: #7551e9;
  font-weight: 600;
  border-left: 4px solid #7551e9;
  border-radius: 0px;
  margin: 0;
  padding: 5px;
  cursor: pointer;
}

/*올 데이 이벤트 */
.fc-h-event {
  border: none;
  background-color: #e4f8f1;
  border-left: 4px solid #74cdab !important;
  i {
    color: #74cdab;
    font-weight: 600;
  }
}

/* 마우스 호버 이벤트 */
.fc-event:hover {
  /* background-color: #256428;  */
  /* 더 진한 녹색 */
  /* cursor: pointer; */
}

/* 중요 이벤트 */
.important-event {
  background-color: #f44336; /* 빨간색 배경 */
  border-color: #d32f2f;
  color: white;
}

/* .holiday-event {
  background-color: #fff4e7;

  border: none;
  border-left: 4px solid #ffb85c !important;
  i {
    color: #ffb85c;
  }
} */
.holiday-event {
  background-color: #ffe2e6;

  border: none;
  border-left: 4px solid #fd7385 !important;
  i {
    color: #fd7385;
  }
}

.fc-direction-ltr {
  margin: 0 !important;
}
</style>
