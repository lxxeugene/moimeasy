<template>
    <div class="moeim-enter-container">
      <h2 class="title">모임 코드 입력</h2>
      <div class="form-group">
        <input
          id="moeimCode"
          type="text"
          v-model="formData.moeimCode"
          placeholder="Enter 모임 코드"
        />
      </div>
      <button class="btn-primary" @click="enterMoeim">모임 입장</button>
    </div>
  </template>
  
  <script>
  export default {
    name: "EnterMoeim",
    data() {
      return {
        formData: {
          moeimCode: "",
        },
      };
    },
    methods: {
      enterMoeim() {
        console.log("모임 코드 입력 완료:", this.formData.moeimCode);
        alert("모임에 입장되었습니다!");
        this.$router.push("/"); // 메인 화면으로 이동
      },
    },
  };
  </script>
  
  <style scoped>
  .moeim-enter-container {
    min-width: 400px;
    margin: 50px auto;
    padding: 20px;
    border: 1px solid #ddd;
    border-radius: 8px;
    background-color: #fff;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    text-align: center;
  }
  
  .title {
    font-size: 24px;
    color: #414651;
    font-weight: bold;
    margin-bottom: 20px;
  }
  
  .form-group {
    margin-bottom: 20px;
  }
  

  
  input {
    width: 100%;
    padding: 10px;
    border: 1px solid #ddd;
    border-radius: 5px;
  }
  
  .btn-primary {
    width: 100%;
    background-color: #7f56d9;
    color: white;
    padding: 10px;
    border-radius: 5px;
    font-size: 16px;
    font-weight: bold;
    cursor: pointer;
    border: none;
  }
  
  .btn-primary:hover {
    background-color: #6a48b0;
  }
  </style>
  