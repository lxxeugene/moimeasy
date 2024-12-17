<template>
  <Card class="card-container">
    <template #title>
      <div class="title">아이디 찾기</div>
    </template>
    <template #content>
      <!-- 입력 폼 -->
      <form @submit.prevent="submitForm" class="form">
        <div class="input-container">
          <label for="nickname">Nickname</label>
          <FloatLabel>
            <InputText
              id="nickname"
              v-model="nickname"
              placeholder="Nickname"
            />
          </FloatLabel>
        </div>

        <div class="input-container">
          <label for="phone">Phone</label>
          <FloatLabel>
            <InputText
              id="phone"
              v-model="phone"
              placeholder="Phone"
            />
          </FloatLabel>
        </div>

        <div class="submit-button">
          <Button
            label="Find Email"
            type="submit"
            icon="pi pi-search"
            :loading="loading"
          />
        </div>
      </form>

      <!-- 결과 메시지 -->
      <div v-if="email" class="result">
        <Message severity="success">
          Your Email: <strong>{{ email }}</strong>
          <div style="margin-top: 10px; color: gray;">
            10초 후에 로그인 페이지로 이동합니다...
          </div>
        </Message>
      </div>

      <div v-if="error" class="error">
        <Message severity="error">{{ error }}</Message>
      </div>
    </template>
  </Card>
</template>

  
  <script>
  import api from '@/axios';
  import { useAuthStore } from '@/stores/auth';
  import { defineComponent, ref } from 'vue';
  
  export default defineComponent({
    name: 'FindEmailView',
    setup() {
      const nickname = ref('');
      const phone = ref('');
      const email = ref('');
      const error = ref('');
      const loading = ref(false);
  
      const submitForm = async () => {
        error.value = '';
        email.value = '';
        loading.value = true;
  
        try {
          const response = await api.post('/api/v1/find/email', {
            nickname: nickname.value,
            phone: phone.value,
          });
          email.value = response.data.email;
              // 이메일 찾기 성공 후 10초 후 로그인 페이지로 리다이렉트
      setTimeout(() => {
      console.log('로그인 페이지로 이동합니다.');
      window.location.href = '/login'; // 또는 this.$router.push('/login') 사용
    }, 10000); // 10000ms = 10초
        } catch (err) {
          if (err.response?.status === 404) {
            error.value = '일치하는 이메일을 찾을 수 없습니다.';
          } else {
            error.value = '서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.';
          }
        } finally {
          loading.value = false;
        }
      };
  
      return { nickname, phone, email, error, loading, submitForm };
    },
  });
  </script>
  
  <style scoped>
.card-container {
  width: 400px;
  border: 1px solid #ddd; /* 테두리 색상 */
  min-width: 400px;
  margin: 50px auto;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
  padding: 20px; /* 내부 여백 */
  background-color: #fff; /* 배경색 */
}

.title {
  font-size: 1.5rem;
  font-weight: bold;
  text-align: center;
  margin-bottom: 1.5rem;
}

.form {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
  width: 100%;
}

.input-container {
  display: flex;
  flex-direction: column;
}

.input-container label {
  margin-bottom: 0.5rem;
  font-size: 0.9rem;
  color: #414651;
}

.submit-button button {
  background-color: #8a4af3;
  color: white;
  padding: 0.75rem;
  font-weight: bold;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}

.submit-button button:hover {
  background-color: #6c38cc;
}

.result, .error {
  margin-top: 1rem;
}

  </style>
  