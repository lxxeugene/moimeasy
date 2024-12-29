<template>
  <div class="gallery-container">
    <Button
      label="이미지업로드"
      icon="pi pi-cloud-upload"
      @click="toggleShowUploader"
    />
    <!-- 이미지 파일 업로더 -->

    <GalleryFileUploader v-if="isUploaderVisible" />
    <!-- 이미지 리스트-->
    <GalleryList />
  </div>
</template>

<script setup>
import GalleryFileUploader from './components/GalleryFileUploader.vue';
import GalleryList from './components/GalleryList.vue';
import { ref } from 'vue';

//
const isUploaderVisible = ref(false);
const toggleShowUploader = () => {
  isUploaderVisible.value = !isUploaderVisible.value;
};
</script>

<style scoped>
.gallery-container {
  width: 98%;
  margin: 10px;
  height: auto;
}
</style>
