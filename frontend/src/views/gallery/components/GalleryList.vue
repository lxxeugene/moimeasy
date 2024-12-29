<template>
  <div class="gallery-list-container" ref="galleryRef">
    <div class="masonry-grid">
      <div v-for="item in galleryList" :key="item.id" class="masonry-item">
        <div v-if="isLoading(item.id)" class="skeleton-wrapper">
          <Skeleton shape="rectangle" width="100%" height="200px" />
        </div>
        <img
          :src="item.imageUrl"
          :alt="`Image ${item.id}`"
          :class="{ 'image-loaded': !isLoading(item.id) }"
          loading="lazy"
          @load="onImageLoad(item)"
        />
      </div>
    </div>
    <div ref="loadMoreTrigger" class="load-more-trigger"></div>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue';
import axios from 'axios';
import Skeleton from 'primevue/skeleton';
import Masonry from 'masonry-layout';
import imagesLoaded from 'imagesloaded';

const galleryRef = ref(null);
const galleryList = ref([]);
const page = ref(0);
const totalPages = ref(1);
const loadingMap = ref({});
const loadMoreTrigger = ref(null);
let msnry = null;

function initMasonry() {
  nextTick(() => {
    const grid = galleryRef.value?.querySelector('.masonry-grid');
    if (!grid) return;

    if (msnry) {
      msnry.destroy();
    }

    imagesLoaded(grid, () => {
      msnry = new Masonry(grid, {
        itemSelector: '.masonry-item',
        columnWidth: '.masonry-item',
        percentPosition: true,
        gutter: 16,
        transitionDuration: '0.3s',
      });
    });
  });
}

function isLoading(id) {
  return loadingMap.value[id] !== false;
}

async function loadData() {
  if (page.value >= totalPages.value) return;

  try {
    const user = JSON.parse(localStorage.getItem('user'));
    const moeimId = user?.moeimId;
    const res = await axios.get(
      `/api/v1/gallery?moeimId=${moeimId}&page=${page.value}&size=12`
    );

    const { content, totalPages: tPages } = res.data;
    totalPages.value = tPages;

    content.forEach((item) => {
      loadingMap.value[item.id] = true;
    });

    galleryList.value.push(...content);
    page.value += 1;

    await nextTick();
    initMasonry();
  } catch (error) {
    console.error('Failed to load gallery data:', error);
  }
}

function onImageLoad(item) {
  loadingMap.value[item.id] = false;
  nextTick(() => {
    if (msnry) {
      msnry.layout();
    }
  });
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
      rootMargin: '100px',
      threshold: 0.1,
    }
  );

  if (loadMoreTrigger.value) {
    observer.observe(loadMoreTrigger.value);
  }
}

onMounted(async () => {
  await loadData();
  initIntersectionObserver();
});
</script>

<style scoped>
.gallery-list-container {
  width: 100%;
  padding: 16px;
}

.masonry-grid {
  width: 100%;
}

.masonry-item {
  width: calc(25% - 16px); /* 4개의 이미지가 한 행에 표시되도록 변경 */
  margin-bottom: 16px;
  break-inside: avoid;
  position: relative;
}

.skeleton-wrapper {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 1;
}

.masonry-item img {
  width: 100%;
  height: auto;
  display: block;
  border-radius: 8px;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.masonry-item img.image-loaded {
  opacity: 1;
}

/* 반응형 breakpoint 수정 */
@media screen and (max-width: 1490px) {
  .masonry-item {
    width: calc(33.333% - 16px); /* 큰 화면에서 3개 */
  }
}

@media screen and (max-width: 1024px) {
  .masonry-item {
    width: calc(50% - 16px); /* 중간 화면에서 2개 */
  }
}

@media screen and (max-width: 640px) {
  .masonry-item {
    width: 100%; /* 작은 화면에서 1개 */
  }
}

.load-more-trigger {
  height: 20px;
  margin-top: 20px;
}
</style>
