<template>
  <Dialog
    v-model:visible="visible"
    :modal="false"
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
      <label for="eventDescription" class="add-dialog-inputbox-label"
        >설명</label
      >
      <InputText
        id="eventDescription"
        class="add-dialog-input"
        autocomplete="off"
        v-model="newEventDescription"
      />
      <label for="eventLocation" class="add-dialog-inputbox-label">장소</label>
      <InputText
        id="eventLocation"
        class="add-dialog-input"
        autocomplete="off"
        v-model="newEventLocation"
      />
      <label for="eventAttendants" class="add-dialog-inputbox-label"
        >참석자</label
      >
      <InputText
        id="eventAttendants"
        class="add-dialog-input"
        autocomplete="off"
        v-model="newEventAttendants"
      />
      <label for="eventPriority" class="add-dialog-inputbox-label"
        >우선순위</label
      >
      <InputText
        id="eventPriority"
        class="add-dialog-input"
        autocomplete="off"
        v-model="newEventPriority"
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
    <div class="demo-app-main">
      <FullCalendar
        ref="calendarRef"
        class="demo-app-calendar"
        :options="calendarOptions"
      >
        <template v-slot:eventContent="arg">
          <b>{{ arg.timeText }}</b>
          <i>{{ arg.event.title }}</i>
        </template>
      </FullCalendar>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import '@/views/schedule/components/ScheduleCalendar.style.css';
import FullCalendar from '@fullcalendar/vue3';
import dayGridPlugin from '@fullcalendar/daygrid';
import timeGridPlugin from '@fullcalendar/timegrid';
import interactionPlugin from '@fullcalendar/interaction';
import googleCalendarPlugin from '@fullcalendar/google-calendar';
import { createEventId } from '@/utils/event-utils';
import axios from 'axios';
import { useLoadingStore } from '@/stores/useLoadingStore';

// 스토어
const loadingStore = useLoadingStore();
// PrimeVue 컴포넌트
import Dialog from 'primevue/dialog';
import Button from 'primevue/button';
import InputText from 'primevue/inputtext';

// 구글 캘린더 API 설정
const googleCalendarApiKey = import.meta.env.VITE_GOOGLE_API_KEY;
const googleCalendarId = 'ko.south_korea#holiday@group.v.calendar.google.com';

// Refs 데이터 정의
const visible = ref(false);
const calendarRef = ref(null);
const newEventTitle = ref('');
const newEventDescription = ref('');
const newEventLocation = ref('');
const newEventAttendants = ref('');
const newEventPriority = ref('');
const newStartTime = ref('');
const newEndTime = ref('');

// Reactive 데이터 정의
const currentEvents = ref([]);
const resolvePromise = ref(null);
const rejectPromise = ref(null);

// Fullcalendar 옵션 설정
const calendarOptions = reactive({
  plugins: [
    dayGridPlugin,
    timeGridPlugin,
    interactionPlugin,
    googleCalendarPlugin,
  ],
  headerToolbar: {
    left: 'today myCustomButton',
    center: 'prev title next',
    right: 'timeGridDay,timeGridWeek,dayGridMonth',
  },
  buttonText: {
    today: 'Today',
    month: 'Month',
    week: 'Week',
    day: 'Day',
  },
  initialView: 'dayGridMonth',
  // events: [],
  editable: true,
  selectable: true,
  selectMirror: true,
  dayMaxEvents: true,
  weekends: true,
  locale: 'ko',
  googleCalendarApiKey: googleCalendarApiKey,
  dayCellContent: (info) => info.date.getDate(),
  select: handleDateSelect,
  eventClick: handleEventClick,
  eventsSet: handleEvents,
  eventAdd: handleEventAdd,
  eventChange: handleEventChange,
  eventRemove: handleEventRemove,
  // eventSources: [
  //   {
  //     googleCalendarId: googleCalendarId,
  //     className: 'holiday-event',
  //   },
  // ],
  customButtons: {
    myCustomButton: {
      text: 'Add',
      click: () => {
        const calendarApi = calendarRef.value.getApi();
        calendarApi.addEvent({
          title: 'New Event',
          start: new Date(),
          end: new Date(),
        });
        alert('새 이벤트가 추가되었습니다!');
      },
    },
  },
});

//아래부터 메서드 정의
async function handleDateSelect(selectInfo) {
  try {
    const eventData = await openDialog(selectInfo);
    if (eventData) {
      const calendarApi = selectInfo.view.calendar;
      calendarApi.addEvent({
        id: createEventId(),
        title: eventData.title,
        start: selectInfo.startStr,
        end: selectInfo.endStr,
        allDay: selectInfo.allDay,
        // className: 'important-event',
        extendedProps: {
          description: eventData.description || '',
          location: eventData.location || '미정',
          attendants: eventData.attendants || [],
          priority: eventData.priority || '보통',
        },
      });
    }
  } catch (error) {
    console.log('다이얼로그에서 입력 취소:', error);
  } finally {
    selectInfo.view.calendar.unselect();
  }
}

function openDialog(selectInfo) {
  visible.value = true;
  newEventTitle.value = '';
  newEventDescription.value = '';
  newEventLocation.value = '';
  newEventAttendants.value = '';
  newEventPriority.value = '';
  newStartTime.value = selectInfo.startStr;
  newEndTime.value = selectInfo.endStr;

  return new Promise((resolve, reject) => {
    resolvePromise.value = resolve;
    rejectPromise.value = reject;
  });
}

function resolveDialog() {
  if (newEventTitle.value.trim()) {
    const eventData = {
      title: newEventTitle.value.trim(),
      description: newEventDescription.value.trim(),
      location: newEventLocation.value.trim(),
      attendants: newEventAttendants.value
        .split(',')
        .map((attendant) => attendant.trim()),
      priority: newEventPriority.value.trim(),
    };
    resolvePromise.value(eventData);
  } else {
    resolvePromise.value(null);
  }
  closeDialog();
}

function rejectDialog() {
  rejectPromise.value('취소됨');
  closeDialog();
}

function closeDialog() {
  visible.value = false;
}

function handleEventClick(clickInfo) {
  console.log(clickInfo);
  if (confirm(`이벤트 '${clickInfo.event.title}'을(를) 삭제하시겠습니까?`)) {
    clickInfo.event.remove();
  }
}

function handleEvents(events) {
  console.log(events);
  currentEvents.value = events;
}

async function handleEventAdd(eventInfo) {
  const event = eventInfo.event;
  console.log('이벤트 추가시 호출됨:', event);
  try {
    const response = await axios.post('/api/v1/events', {
      eventCode: event.id,
      scheduleTitle: event.title,
      startTime: event.start,
      endTime: event.end,
      isAllDayEvent: event.allDay,
      description: event.extendedProps.description,
      location: event.extendedProps.location,
      attendants: event.extendedProps.attendants,
      priority: event.extendedProps.priority,
    });
    console.log('이벤트 추가 성공:', response.data);
  } catch (error) {
    console.error('이벤트 추가 실패:', error);
  }
}

async function handleEventChange(eventInfo) {
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
}

async function handleEventRemove(eventInfo) {
  console.log('삭제이벤트호출');

  const event = eventInfo.event;
  try {
    const response = await axios.delete(`/api/v1/events/${event.id}`);
    console.log('이벤트 삭제 성공:', response.data);
  } catch (error) {
    console.error('이벤트 삭제 실패:', error);
  }
}

async function fetchEvents() {
  loadingStore.startLoading(); // 로딩 스피너
  try {
    const response = await axios.get('/api/v1/events');
    const eventData = response.data.map((event) => ({
      id: event.eventCode,
      title: event.scheduleTitle,
      start: event.startTime,
      end: event.endTime,
      allDay: event.isAllDayEvent,
      extendedProps: {
        description: event.description || '',
        location: event.location || '미정',
        attendants: event.attendants || [],
        priority: event.priority || '보통',
      },
    }));

    // 구글 캘린더 일정데이터 & DB 일정데이터  eventSources로 추가
    calendarOptions.eventSources = [
      {
        googleCalendarId: googleCalendarId,
        className: 'holiday-event',
      },
      {
        events: eventData, // DB에서 가져온 이벤트 리스트
      },
    ];
  } catch (error) {
    console.error('서버에서 이벤트를 가져오는 데 실패했습니다:', error);
  } finally {
    loadingStore.stopLoading(); // 로딩스피너 제거
  }
}

// 컴포넌트 마운트 시 이벤트 fetch로 가져옴
onMounted(() => {
  fetchEvents();
});
</script>

<style scoped>
/* 기존 스타일 유지 */
</style>
