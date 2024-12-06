<template>
    <div class="profile-container">
      <h2>회원 관리 &gt; 회원 내역 &gt; 회원 프로필</h2>
      <form v-if="user">
        <div class="form-group">
          <label for="username">Username</label>
          <input id="username" v-model="user.userName" type="text" readonly />
        </div>
        <div class="form-group">
          <label for="email">Email address</label>
          <input id="email" v-model="user.email" type="email" readonly />
        </div>
        <div class="form-group">
          <label for="phone">Phone number</label>
          <input id="phone" v-model="user.phone" type="text" readonly />
        </div>
        <div class="form-group">
          <label for="address">주소</label>
          <input id="address" v-model="user.address" type="text" readonly />
        </div>
        <div class="form-group">
          <label for="createAt">가입 날짜</label>
          <input id="createAt" v-model="user.createAt" type="text" readonly />
        </div>
      </form>
      <div v-else>사용자 정보를 불러오는 중입니다...</div>
    </div>
  </template>
  
  <script>
  import axios from "axios";
  
  export default {
    data() {
      return {
        user: null,
      };
    },
    mounted() {
      const userId = this.$route.params.userId; // URL에서 userId 가져오기
      this.loadUserData(userId);
    },
    methods: {
      async loadUserData(userId) {
        try {
          const response = await axios.get(`/api/v1/users/${userId}`); // API 호출
          this.user = response.data; // 응답 데이터를 user에 저장
        } catch (error) {
          console.error("사용자 정보를 불러오는데 실패했습니다:", error);
          alert("사용자 정보를 불러오는데 실패했습니다.");
        }
      },
    },
  };
  </script>
  
  <style scoped>
  .profile-container {
    max-width: 800px;
    margin: 0 auto;
    padding: 20px;
    background-color: #fff;
    border-radius: 8px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  }
  
  h2 {
    margin-bottom: 20px;
    color: #414651; /* 폰트 색상 */
  }
  
  .form-group {
    margin-bottom: 15px;
  }
  
  label {
    display: block;
    margin-bottom: 5px;
    font-weight: bold;
    color: #414651; /* 폰트 색상 */
  }
  
  input {
    width: 100%;
    padding: 10px;
    border: 1px solid #ccc;
    border-radius: 5px;
    color: #414651; /* 폰트 색상 */
  }
  
  input[readonly] {
    background-color: #f9f9f9;
  }
  </style>
  