<template>
  <div class="gallery-list-container" ref="galleryRef">
    <div v-for="item in galleryList" :key="item.id" class="masonry-item">
      <!-- 로딩 전 스켈레톤 (옵션) -->
      <Skeleton
        v-if="loadingMap[item.id]"
        shape="rectangle"
        width="100%"
        height="200px"
      />

      <!-- 로딩 완료된 이미지 -->
      <img
        v-else
        :src="item.firebaseUrl"
        :alt="`Image ${item.id}`"
        loading="lazy"
        @load="onImageLoad(item.id)"
      />
      <p class="image-caption">{{ item.imageUrl }}</p>
    </div>
    <!-- 무한 스크롤 트리거 -->
    <div ref="loadMoreTrigger" class="load-more-trigger"></div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import Skeleton from 'primevue/skeleton';
import { fetchImageUrl } from '@/utils/image-load-utils';
// 위에서 보여주신 함수 (filePath -> downloadUrl)

const galleryList = ref([]);
const page = ref(0);
const totalPages = ref(1);

// 각 이미지 로딩 상태를 관리 (true → 스켈레톤 표시)
const loadingMap = ref({});

// 무한 스크롤 관찰용 엘리먼트
const loadMoreTrigger = ref(null);

async function loadData() {
  if (page.value < totalPages.value) {
    try {
      const res = await axios.get(
        `/api/gallery?moeimId=1&page=${page.value}&size=12`
      );
      const { content, totalPages: tPages } = res.data;
      totalPages.value = tPages;

      // 1) 로딩 전에 스켈레톤 ON
      content.forEach((item) => {
        loadingMap.value[item.id] = true;
      });

      // 2) DB에 저장된 imageUrl이 'gallery/xxx.jpg' 등 경로만 있다 가정
      for (const item of content) {
        try {
          const url = await fetchImageUrl(item.imageUrl);
          item.firebaseUrl = url;
        } catch (e) {
          // 오류 발생 시, fallback 처리
          item.firebaseUrl = null;
        }
      }

      // 기존 목록에 추가
      galleryList.value.push(...content);
      page.value += 1;
    } catch (error) {
      console.error(error);
    }
  }
}

function initIntersectionObserver() {
  const observer = new IntersectionObserver(
    (entries) => {
      const [entry] = entries;
      if (entry.isIntersecting) {
        loadData();
      }
    },
    {
      root: null,
      rootMargin: '0px',
      threshold: 0.1,
    }
  );
  if (loadMoreTrigger.value) {
    observer.observe(loadMoreTrigger.value);
  }
}

function onImageLoad(id) {
  // 이미지가 실제로 로드되었으면 스켈레톤 OFF
  loadingMap.value[id] = false;
}

onMounted(() => {
  loadData();
  initIntersectionObserver();
});
</script>

<style scoped>
.gallery-list-container {
  /* Masonry (CSS-only) 예시 */
  column-count: 4;
  column-gap: 1rem;
}
.masonry-item {
  break-inside: avoid;
  margin-bottom: 1rem;
  display: block;
  width: 100%;
}
.load-more-trigger {
  height: 1px;
}
.image-caption {
  font-size: 0.9rem;
  color: #666;
  margin-top: 4px;
}
</style>
