<template>
  <div class="total-frame">
    <PayMenuList /> <!-- 메뉴 위치 -->
    <div class="title-bar">
      <div class="title">11월 회비 납부 내역</div>
      <div class="icon-button-group">
        <i class="pi pi-caret-left" style="font-size: 1rem"></i>
        <i class="pi pi-calendar-times" style="font-size: 1rem"></i>
        <i class="pi pi-caret-right" style="font-size: 1rem"></i>
        <Button label="납부하기" icon="pi pi-check" iconPos="right" rounded class="pay-button" @click="visible1 = true" />
      </div>
    </div>

    <!-- 금액 입력 모달창 -->
    <Dialog v-model:visible="visible1" modal :style="{ width: '30rem', height: '40rem' }" @hide="resetValue">
      <template #header>
        <div class="text-header">
          보낼 금액
        </div>
      </template>
      <div class="text-display">
        <InputText type="text" v-model="value" readonly class="text-input" />
      </div>
      <div class="number-buttons">
        <Button v-for="num in numbers" :key="num" rounded class="number-button" @click="handleButtonClick(num)">
          <template v-if="num === 'x'">
            <i class="pi pi-times-circle" style="font-size: 1.5rem"></i>
          </template>
          <template v-else>
            {{ num }}
          </template>
        </Button>
      </div>
      <Button label="다음" rounded class="next-button" @click="showNextModal" />
    </Dialog>

    <!-- 안내창 모달 -->
    <Dialog v-model:visible="visible2" modal :style="{ width: '30rem', height: '25rem' }" @hide="resetValue">
      <template #header>
        <div style="text-align: center; font-size: 1.2em; margin-top: 30px; color: black;">
          <span style="font-weight: bold;">LEFT THREE TABLE</span> 님에게 원 이체하시겠습니까 ?
          <p>출금계좌 : 신한(안성준)123456789</p>
          <p>입금계좌 : 토스(L3T)1234567890</p>
        </div>
      </template>
      <div class="text-display">
        <InputText type="text" v-model="value" readonly class="text-input" />
      </div>
      <div class="button-group">
        <Button label="확인" rounded class="next-button" @click="showNextModal3" />
        <Button label="취소" rounded class="next-button" @click="visible2 = false" />
      </div>
    </Dialog>

    <Dialog v-model:visible="visible3" modal :style="{ width: '27rem', height: '40rem' }" @hide="resetValue"
      :closable="false">
      <template #header>
        <div
          style="display: flex; justify-content: center; align-items: center; font-size: 4em; margin-top: 50px; font-weight: bold; color: #7f56d9;">
          <i class="pi pi-check-circle" style="font-size: 8rem; margin-right: 20px;"></i>
          <div style="margin-top: 30px;">입금</div> <!-- margin-top을 이용해 여백 추가 -->
        </div>
      </template>

      <div style="display: flex; flex-direction: column; justify-content: center; align-items: center; height: 100%;">
        <p>안성준 님의 계좌에서 원을 성공적으로 입금하였습니다.</p>
        <Button label="닫기" rounded @click="visible3 = false" />
      </div>
    </Dialog>


    <div class="card">
      <DataTable :value="members" paginator :rows="5" tableStyle="min-width: 60rem">
        <Column field="number" header="번호" style="width: 12%" />
        <Column header="사진" style="width: 12%">
          <template #body="slotProps">
            <img :src="`/assets/${slotProps.data.photo}`" alt="사진"
              style="width: 40px; height: 40px; border-radius: 50%" />
          </template>
        </Column>
        <Column field="name" header="이름" style="width: 12%" />
        <Column field="role" header="회원구분" style="width: 12%" />
        <Column field="amount" header="금액" style="width: 12%" />
        <Column field="payDate" header="납부일자" style="width: 12%" />
        <Column header="상태" style="width: 12%">
          <template #body="slotProps">
            <span :class="getStatusClass(slotProps.data.status)">
              {{ slotProps.data.status }}
            </span>
          </template>
        </Column>
        <Column field="note" header="비고" style="width: 20%" />
      </DataTable>
    </div>
  </div>
</template>

<script setup>
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import Button from 'primevue/button';
import PayMenuList from './PayMenuList.vue';
import 'primeicons/primeicons.css';
import Dialog from 'primevue/dialog';
import InputText from 'primevue/inputtext';
import { ref } from "vue";

const visible1 = ref(false); // 금액 입력 모달창
const visible2 = ref(false); // 두 번째 안내 모달
const visible3 = ref(false); // 두 번째 안내 모달

function showNextModal() {
  visible1.value = false;  // 첫 번째 모달 닫기
  visible2.value = true;   // 두 번째 모달 열기
}

function showNextModal3() {
  visible2.value = false;  // 두 번째 모달 닫기
  visible3.value = true;   // 세 번째 모달 열기
}

// InputText의 값
const value = ref("")

// 숫자 버튼 목록
const numbers = ['1', '2', '3', '4', '5', '6', '7', '8', '9', '.', '0', 'x'];

const members = [
  {
    number: '01',
    photo: 'avatar-image0.png', // 실제 이미지 경로로 변경
    name: '안성준',
    role: '운영자',
    amount: '50,000',
    payDate: '2024-11-30',
    status: '납부완료',
    note: '테스트입니다.'
  },
  {
    number: '02',
    photo: 'avatar-image1.png',
    name: '김지훈',
    role: '일반회원',
    amount: '50,000',
    payDate: '2024-11-30',
    status: '납부완료',
  },
  {
    number: '03',
    photo: 'avatar-image2.png',
    name: '장현수',
    role: '일반회원',
    amount: '50,000',
    payDate: '2024-11-30',
    status: '납부완료',
    note: '',
  },
  {
    number: '04',
    photo: 'avatar-image3.png',
    name: '이유진',
    role: '일반회원',
    amount: '50,000',
    payDate: '',
    status: '회비 미납',
    note: '',
  },
  {
    number: '05',
    photo: 'avatar-image4.png',
    name: '김철수',
    role: '일반회원',
    amount: '50,000',
    payDate: '',
    status: '회비 미납',
    note: '',
  },
  {
    number: '06',
    photo: 'avatar-image0.png', // 실제 이미지 경로로 변경
    name: '안성준',
    role: '운영자',
    amount: '50,000',
    payDate: '2024-11-30',
    status: '납부완료',
    note: '테스트입니다.',
  },
  {
    number: '07',
    photo: 'avatar-image1.png',
    name: '김지훈',
    role: '일반회원',
    amount: '50,000',
    payDate: '2024-11-30',
    status: '납부완료',
    note: '',
  },
  {
    number: '08',
    photo: 'avatar-image2.png',
    name: '장현수',
    role: '일반회원',
    amount: '50,000',
    payDate: '2024-11-30',
    status: '납부완료',
    note: '',
  },
  {
    number: '09',
    photo: 'avatar-image3.png',
    name: '이유진',
    role: '일반회원',
    amount: '50,000',
    payDate: '',
    status: '회비 미납',
    note: '',
  },
  {
    number: '10',
    photo: 'avatar-image4.png',
    name: '김철수',
    role: '일반회원',
    amount: '50,000',
    payDate: '',
    status: '회비 미납',
    note: '',
  },
];

// 버튼 클릭 시 호출
function handleButtonClick(num) {
  if (num === 'x') {
    // 'x' 클릭 시 마지막 문자 삭제
    value.value = value.value.slice(0, -1);
  } else {
    // 숫자 또는 '.' 추가
    value.value += num;
  }
}

// 상태에 따라 CSS 클래스를 반환하는 함수
function getStatusClass(status) {
  return status === '납부완료' ? 'text-green-500 font-bold' : 'text-red-500 font-bold';
}

function resetValue() {
  value.value = ""; // 입력 값 초기화
}

</script>

<style scoped>
.total-frame {
  display: flex;
  flex-direction: column;
  gap: 20px;
  align-items: flex-start;
  padding: 1rem;
  margin: 10px;
}

.title-bar {
  display: flex;
  /* Flexbox 사용 */
  justify-content: space-between;
  /* 양쪽 정렬 */
  align-items: center;
  /* 세로 가운데 정렬 */
  margin-bottom: 1rem;
  /* 아래쪽 여백 */
  width: 100%;
  /* 부모의 전체 폭 사용 */
}

.icon-button-group {
  display: flex;
  /* 아이콘과 버튼을 가로로 배치 */
  align-items: center;
  /* 세로 가운데 정렬 */
  gap: 20px;
  /* 아이콘과 버튼 사이 여백 */
}

.number-buttons {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  /* 3개의 열로 배치 */
  gap: 10px;
  /* 버튼 사이의 간격 */
  margin-top: 20px;
}

.number-button {
  width: 70px;
  /* 버튼의 너비 */
  height: 70px;
  /* 버튼의 높이 */
  background-color: white;
  /* 버튼 배경색 */
  color: black;
  /* 버튼 텍스트 색상 */
  font-size: 1.2em;
  /* 텍스트 크기 */
  border: none;
  /* 테두리 제거 */
  border-radius: 10px;
  /* 둥근 모서리 */
  display: flex;
  align-items: center;
  justify-content: center;
  margin-left: 20px;
}

.number-button:hover {
  background-color: #333333;
  /* 호버 시 색상 변경 */
}

.number-button:active {
  background-color: #555555;
  /* 클릭 시 색상 변경 */
}

.next-button {
  margin-top: 20px;
  width: 100%;
}

.card {
  width: 100%;
}

.title {
  font-size: 1.5rem;
  font-weight: bold;
}

.text-green-500 {
  color: #10b981;
}

.text-red-500 {
  color: #ef4444;
}

.text-header {
  display: flex;
  justify-content: center;
  align-items: center;
  text-align: center;
  /* 텍스트 가운데 정렬 */
  font-size: 2em;
  margin-top: 20px;
  margin-left: 150px;
  font-weight: bold;
  color: lightgrey;
}

.text-display {
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 20px 20;
  /* 위아래 간격 */
}

.text-input {
  font-size: 2rem;
  /* 텍스트 크기 */
  font-weight: bold;
  /* 텍스트 굵게 */
  text-align: center;
  /* 텍스트 가운데 정렬 */
  border: none;
  /* 테두리 제거 */
  outline: none;
  /* 포커스 시 외곽선 제거 */
  background: transparent;
  /* 배경 투명 */
  width: 100%;
  /* 너비를 부모에 맞춤 */
}

/* 버튼 스타일 */
:deep(.pay-button.p-button) {
  background-color: #7f56d9 !important;
  color: #ffffff !important;
  padding: 0.5rem 1rem !important;
  border-radius: 20px !important;
  font-size: 0.9rem !important;
  margin-right: 50px;
}

:deep(.pay-button.p-button:hover) {
  background-color: #6c44c3 !important;
}

:deep(.pay-button.p-button:active) {
  background-color: #5a37a7 !important;
}

/* 버튼 레이블 */
:deep(.pay-button .p-button-label) {
  font-weight: bold !important;
}
</style>
