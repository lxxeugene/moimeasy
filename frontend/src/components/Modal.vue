<template>
  <div class="modal-overlay" v-if="isVisible">
    <div class="modal-container">
      <div class="modal-header">
        <h3>{{ title }}</h3>
      </div>
      <div class="modal-body">
        <slot name="content"></slot>
      </div>
      <div class="modal-footer">
        <slot name="footer"></slot>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Modal",
  props: {
    isVisible: { type: Boolean, required: true },
    title: { type: String, default: "알림" },
  },
  emits: ["close"],
};
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-container {
  background: white;
  padding: 20px;
  border-radius: 12px;
  width: 400px;
  max-width: 90%;
  text-align: center;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
}

.modal-header h3 {
  color: #7f56d9;
  margin: 0 0 10px 0;
}

.modal-body {
  margin: 20px 0;
}

.modal-footer {
  margin-top: 20px;
}

button {
  background-color: #7f56d9;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 8px;
  cursor: pointer;
  font-size: 16px;
  font-weight: bold;
  width: 100%;
}

button:hover {
  background-color: #6a48b0;
}
</style>
