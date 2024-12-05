<template>
    <div class="list-container">
      <h2 class="title">초대 목록</h2>
      <table class="custom-table">
        <thead>
          <tr>
            <th>이메일</th>
            <th>상태</th>
            <th>초대 날짜</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="invitation in invitations" :key="invitation.id">
            <td>{{ invitation.email }}</td>
            <td>{{ invitation.status }}</td>
            <td>{{ invitation.createdAt }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </template>
  
  <script>
  import axios from "axios";
  
  export default {
    data() {
      return {
        invitations: [], // 초대 데이터를 저장
      };
    },
    methods: {
      async fetchInvitations() {
        try {
          const response = await axios.get("/api/v1/invitations");
          this.invitations = response.data.map((invitation) => ({
            id: invitation.id,
            email: invitation.email,
            status: invitation.status,
            createdAt: new Date(invitation.createdAt).toLocaleString(),
          }));
        } catch (error) {
          console.error("Error fetching invitations:", error);
        }
      },
    },
    mounted() {
      this.fetchInvitations();
    },
  };
  </script>
  
  <style scoped>
  /* 테이블 스타일 */
  .list-container {
    width: 600px;
    min-width: 600px;
    margin: 0 auto;
    padding: 20px;
    background-color: #fff;
    border-radius: 8px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  }
  
  .title {
    font-size: 24px;
    font-weight: bold;
    color: #414651;
    margin-bottom: 20px;
  }
  
  .custom-table {
    width: 100%;
    border-collapse: collapse;
    margin-top: 10px;
  }
  
  .custom-table th,
  .custom-table td {
    padding: 10px;
    border: 1px solid #ddd;
    color: #414651;
  }
  
  .custom-table th {
    background-color: #f4f4f4;
    text-align: left;
  }
  </style>
  