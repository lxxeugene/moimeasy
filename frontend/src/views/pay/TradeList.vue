<template>
  <div class="total-frame">
    <PayMenuList /> <!-- 메뉴 위치 -->
    <div class="title-bar datatable-aligned">
      <div class="title">11월 거래내역</div>
      <div class="icon-button-group">
        <i class="pi pi-caret-left" style="font-size: 1rem"></i>
        <i class="pi pi-calendar-times" style="font-size: 1rem"></i>
        <i class="pi pi-caret-right" style="font-size: 1rem"></i>
      </div>
    </div>
    <div class="card datatable-card">
      <DataTable :value="products" ref="dt" class="centered-datatable" tableStyle="min-width: 50rem">
        <template #header>
          <div class="export-button-container">
            <Button icon="pi pi-external-link" label="내보내기" iconPos="right" @click="exportCSV($event)"
              class="export-button" />
          </div>
        </template>
        <Column v-for="col of columns" :key="col.field" :field="col.field" :header="col.header"></Column>
      </DataTable>
    </div>
  </div>
</template>

<script setup>
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import PayMenuList from './PayMenuList.vue';
import Button from 'primevue/button';
import 'primeicons/primeicons.css';
import { ref, onMounted } from 'vue';

const dt = ref();
const products = ref([]);
const exportCSV = () => {
  dt.value.exportCSV();
};
const columns = [
  { field: 'tradedate', header: '날짜' },
  { field: 'tradelist', header: '거래내역' },
  { field: 'trademoney', header: '금액' }
];

// 간단한 테스트 데이터 설정
onMounted(() => {
  products.value = [
    { tradedate: '11.30', tradelist: '안성준4', trademoney: 10000 },
    { tradedate: '11.25', tradelist: '안성준3', trademoney: 10000 },
    { tradedate: '11.24', tradelist: '엄마손집밥', trademoney: -30000 },
    { tradedate: '11.20', tradelist: '안성준2', trademoney: 10000 },
    { tradedate: '11.08', tradelist: '사구도시락', trademoney: -110000 },
    { tradedate: '11.07', tradelist: '테스트2', trademoney: 30000 },
    { tradedate: '11.02', tradelist: '안성준1', trademoney: 50000 },
    { tradedate: '11.01', tradelist: '테스트1', trademoney: 50000 },
  ];
});
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
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
  width: 100%;
}

.icon-button-group {
  display: flex;
  align-items: center;
  gap: 20px;
}

.title {
  font-size: 1.5rem;
  font-weight: bold;
  margin: 0 auto;
}

/* DataTable을 감싸는 card 스타일 */
.datatable-card {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
}

/* DataTable과 아이콘 그룹을 같은 폭으로 정렬하는 스타일 */
.datatable-aligned {
  width: 50rem;
  /* DataTable의 최소 폭과 맞춤 */
  margin: 0 auto;
  /* 가운데 정렬 */
}

/* Export 버튼 컨테이너 스타일 */
.export-button-container {
  display: flex;
  justify-content: flex-end;
  /* 오른쪽 끝에 정렬 */
  width: 100%;
  /* DataTable과 동일한 너비 사용 */
  padding-right: 1rem;
  /* 약간의 여백을 줘서 오른쪽에 붙지 않도록 설정 */
}

/* 버튼 스타일 */
:deep(.export-button) {
  background-color: #7f56d9 !important;
  color: #ffffff !important;
  padding: 0.5rem 1rem !important;
  border-radius: 20px !important;
  font-size: 0.9rem !important;
}

:deep(.export-button:hover) {
  background-color: #6c44c3 !important;
}

:deep(.export-button:active) {
  background-color: #5a37a7 !important;
}

/* 버튼 레이블 */
:deep(.export-button .p-button-label) {
  font-weight: bold !important;
}
</style>
