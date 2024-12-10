<template>
  <!-- 다이얼로그 영역 -->
  <Dialog
    v-model:visible="visible"
    modal
    header="일정 추가"
    :style="{ width: '25rem' }"
    @hide="rejectDialog"
  >
    <span class="add-dialog-subtitle">새 일정의 타이틀을 입력하세요</span>
    <div class="add-dialog-inputbox">
      <label for="eventTitle" class="add-dialog-inputbox-label">일정명</label>
      <InputText
        id="eventTitle"
        class="add-dialog-input"
        autocomplete="off"
        v-model="newEventTitle"
      />
    </div>
    <template #footer>
      <Button
        label="취소"
        text
        severity="secondary"
        @click="rejectDialog"
        autofocus
      />
      <Button
        label="추가"
        outlined
        severity="secondary"
        @click="resolveDialog"
        autofocus
      />
    </template>
  </Dialog>
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
import '@/views/schedule/components/ScheduleCalendar.style.css';
import { defineComponent } from 'vue';
import FullCalendar from '@fullcalendar/vue3'; // FullCalendar Vue3 컴포넌트
import dayGridPlugin from '@fullcalendar/daygrid'; // 월간 보기 플러그인
import timeGridPlugin from '@fullcalendar/timegrid'; // 주간 및 일별 보기 플러그인
import interactionPlugin from '@fullcalendar/interaction'; // 상호작용(클릭 및 드래그) 플러그인
import { INITIAL_EVENTS, createEventId } from '@/utils/event-utils'; // 초기 이벤트 및 ID 생성 유틸리티->제거예정
import googleCalendarPlugin from '@fullcalendar/google-calendar';
import Avatar from 'primevue/avatar';
import Dialog from 'primevue/dialog';
import axios from 'axios';

// 구글 캘린더 API 키와 캘린더 ID
const googleCalendarApiKey = import.meta.env.VITE_GOOGLE_API_KEY; // 구글 API 키
const googleCalendarId = 'ko.south_korea#holiday@group.v.calendar.google.com'; // 구글 캘린더 ID

export default defineComponent({
  components: {
    FullCalendar, // FullCalendar 컴포넌트 등록
    Avatar,
    Dialog,
  },
  data() {
    return {
      visible: false, // Dialog 표시 여부
      newEventTitle: '', // 다이얼로그 입력 - 일정명
      newEventDescription: '', // 다이얼로그 입력 - 일정상세
      newEventLocation: '', // 다이얼로그 입력 - 장소
      newEventAttendants: '', // 다이얼로그 입력 - 참석자
      newEventPriority: '', // 다이얼로그 입력 - 우선순위
      resolvePromise: null, // Promise resolve 함수
      rejectPromise: null, // Promise reject 함수
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
        buttonText: {
          today: 'Today', // 'today' 버튼의 텍스트를 변경
          month: 'Month', // 'month' 버튼의 텍스트를 변경
          week: 'Week', // 'week' 버튼의 텍스트를 변경
          day: 'Day', // 'day' 버튼의 텍스트를 변경
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
        eventsSet: this.handleEvents, // 모든 이벤트 변경 시 호출되는 핸들러
        eventAdd: this.handleEventAdd, // 이벤트 추가 시 호출
        eventChange: this.handleEventChange, // 이벤트 변경 시 호출
        eventRemove: this.handleEventRemove, // 이벤트 삭제 시 호출
        // 구글 캘린더 연동 공휴일 가져오기
        eventSources: [
          {
            googleCalendarId: googleCalendarId,
            className: 'holiday-event',
          },
        ],

        customButtons: {
          myCustomButton: {
            text: 'Add',
            click: () => {
              const calendarApi = this.calendarRef.getApi(); // 캘린더 API 가져오기
              calendarApi.addEvent({
                title: 'New Event',
                start: new Date(),
                end: new Date(),
                // allDay: true,
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
    async handleDateSelect(selectInfo) {
      try {
        // 다이얼로그를 띄우고 입력값을 기다림
        const title = await this.openDialog(); // 여기서 이제 객체로 여러 데이터를 받아오기
        if (title) {
          const calendarApi = selectInfo.view.calendar;
          calendarApi.addEvent({
            id: createEventId(), // 유니크 ID 생성
            title, //다이얼로그 입력 타이틀
            start: selectInfo.startStr, // 시작 날짜
            end: selectInfo.endStr, // 종료 날짜
            allDay: selectInfo.allDay, // 하루 종일 여부
            extendedProps: {
              description: '', // 기본값 처리
              location: '미정',
              attendants: [],
              priority: '보통',
            },
          });
        }
      } catch (error) {
        console.log('다이얼로그에서 입력 취소:', error);
      } finally {
        selectInfo.view.calendar.unselect(); // 선택 해제
      }
    },
    openDialog() {
      this.visible = true;
      this.newEventTitle = ''; // 입력 초기화
      return new Promise((resolve, reject) => {
        this.resolvePromise = resolve;
        this.rejectPromise = reject;
      });
    },
    resolveDialog() {
      if (this.newEventTitle.trim()) {
        this.resolvePromise(this.newEventTitle.trim()); // 입력값 반환
        console.log('반환타이틀', this.newEventTitle.trim());
      } else {
        this.resolvePromise(null); // 빈 값 반환
      }
      this.closeDialog();
    },
    rejectDialog() {
      this.rejectPromise('취소됨');
      this.closeDialog();
    },
    closeDialog() {
      this.visible = false;
    },
    // 주말 표시 여부를 토글하는 메서드
    handleWeekendsToggle() {
      this.calendarOptions.weekends = !this.calendarOptions.weekends; // 주말 표시 여부 변경
    },
    // 이벤트 클릭 시 호출되는 핸들러
    handleEventClick(clickInfo) {
      console.log(clickInfo); // clickInfo 객체 확인
      if (
        confirm(`이벤트 '${clickInfo.event.title}'을(를) 삭제하시겠습니까?`)
      ) {
        clickInfo.event.remove(); // 이벤트 삭제
      }
    },
    // 모든 이벤트 변경 시 호출되는 핸들러
    handleEvents(events) {
      console.log(events); // clickInfo 객체 확인
      this.currentEvents = events; // 현재 이벤트를 업데이트
    },
    // 이벤트 추가 시 추가된 일정데이터와 함께 호출
    async handleEventAdd(eventInfo) {
      const event = eventInfo.event;
      console.log('이벤트 추가시 호출됨:', event);
      try {
        const response = await axios.post('/events', {
          id: event.id, // 기본 필드
          title: event.title,
          start: event.start, // 시작 시간
          end: event.end, // 종료 시간
          allDay: event.allDay, // 하루 종일 여부
          description: event.extendedProps.description, // 일정 상세
          location: event.extendedProps.location, // 장소
          attendants: event.extendedProps.attendants, // 참가자
          priority: event.extendedProps.priority, // 우선순위
        });
        console.log('이벤트 추가 성공:', response.data);
      } catch (error) {
        console.error('이벤트 추가 실패:', error);
      }
    },

    // 이벤트 변경 시 호출
    async handleEventChange(eventInfo) {
      const event = eventInfo.event;
      try {
        const response = await axios.put(`/events/${event.id}`, {
          title: event.title,
          start: event.start,
          end: event.end,
          allDay: event.allDay,
        });
        console.log('이벤트 변경 성공:', response.data);
      } catch (error) {
        console.error('이벤트 변경 실패:', error);
      }
    },

    // 이벤트 삭제 시 호출
    async handleEventRemove(eventInfo) {
      const event = eventInfo.event;
      try {
        const response = await axios.delete(`/events/${event.id}`);
        console.log('이벤트 삭제 성공:', response.data);
      } catch (error) {
        console.error('이벤트 삭제 실패:', error);
      }
    },
  },
});
</script>
