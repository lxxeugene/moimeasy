<template>
  <div class="signup-container">
    <h2 class="title">회원가입</h2>

        <!-- 오류 메시지 표시 -->
        <div v-if="errorMessage" class="error-message">
      <p>{{ errorMessage }}</p>
    </div>

    <!-- 성공 메시지 표시 -->
    <div v-if="successMessage" class="success-message">
      <p>{{ successMessage }}</p>
    </div>

    <form @submit.prevent="submitSignup">
      <div class="form-group">
        <!-- 이름 -->
        <label for="username">Username</label>
        <input
          type="text" id="username"
          v-model="form.username"
          placeholder="Your username"
          required/>
          <!-- <div v-if="errors.name" class="error-message">
            <p>{{ errors.name }}</p>
          </div> -->
      </div>

      <!-- 이메일 -->
      <div class="form-group">
        <label for="email">Email</label>
        <input
          type="email"
          id="email"
          v-model="form.email"
          @input="resetEmailCheck"
          placeholder="Your email"
          required/>
          <!-- <button type="button" class="check-button" @click="checkEmail" :disabled="isEmailChecked || isSubmitting">
            중복 확인
          </button>
          <div :class="feedbackEmailClass">{{ feedbackEmail }}</div>
          <div v-if="errors.email" class="error-message">
            <p>{{ errors.email }}</p>
          </div> -->
      </div>

      <!-- 닉네임 -->
      <div class="form-group">
        <label for="nickname">Nickname</label>
        <input
          type="nickname"
          v-model="form.nickname"
          @input="resetNicknameCheck"
          placeholder="Your nickname"
          required/>
          <!-- <button type="button" class="check-button" @click="checkNickname" :disabled="isNicknameChecked || isSubmitting">
            중복 확인
          </button>
          <div :class="feedbackNicknameClass">{{ feedbackNickname }}</div>
          <div v-if="errors.nickname" class="error-message">
            <p>{{ errors.nickname }}</p>
          </div> -->
      </div>

      <!-- 연락처 -->
      <div class="form-group">
        <label for="phone">Phone</label>
        <input
          type="tel"
          v-model="form.phone"
          placeholder="Your phone"
          required
          pattern="^[0-9\-+() ]+$"
          />
        </div>

      <div class="form-group">
        <label>Password</label>
        <input
          type="password"
          v-model="form.password"
          placeholder="Password"
          required
        />
      </div>
      <div class="form-group">
        <label>Confirm Password</label>
        <input
          type="password"
          v-model="form.confirmPassword"
          placeholder="Confirm password"
          required
        />
      </div>
      <div class="form-group-checkbox">
        <input
          type="checkbox"
          id="terms"
          v-model="form.acceptTerms"
          required
        />
        <label for="terms">I accept the terms and privacy policy</label>
      </div>
      <button type="submit" class="btn-primary">회원가입</button>
    </form>
    <div class="alternative">
      <!-- <p>Or Register with</p> -->
      <!-- <div class="social-buttons">
        <button class="social-btn">Facebook</button>
        <button class="social-btn">Google</button>
        <button class="social-btn">Apple</button>
      </div> -->
    </div>
    <p class="footer-text">
      Already have an account? <a href="#" @click.prevent="goToLogin">Log in</a>
    </p>
  </div>
</template>

<script>
import { formatDate } from '@fullcalendar/core';

export default {
  name: "Signup",
  data() {
    return {
      form: {
        username: "",
        email: "",
        nickname:"",
        phone:"",
        password: "",
        confirmPassword: "",
        acceptTerms: false,
      },
    };
  },
  methods: {
    submitSignup() {
      if (this.form.password !== this.form.confirmPassword) {
        alert("Passwords do not match!");
        return;
      }
      if (!this.formData.acceptTerms) {
        alert("You must accept the terms and privacy policy!");
        return;
      }
      console.log("회원가입 성공:", this.form.username);
      // 회원가입 완료 후 로그인 페이지로 이동
      this.$router.push("/login");
    },
    goToLogin() {
      // 로그인 페이지로 이동
      this.$router.push("/login");
    },
  },
};
</script>

<style scoped>
/* 스타일 추가 */
.signup-container {
  min-width: 500px;
  width: 400px;
  margin: 50px auto;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 8px;
  background-color: #fff;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.title {
  font-weight: bold;
  text-align: center;
  color: #414651;
  margin-bottom: 20px;
}

.form-group {
  margin-bottom: 20px;
}

label {
  color: #414651;
  display: block;

  margin-bottom: 5px;
}

input[type="text"],
input[type="email"],
input[type="password"], 
input[type="nickname"], 
input[type="tel"]{
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 5px;
  font-size: 14px;
}
.form-group-checkbox {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
}

.form-group-checkbox input {
  margin-right: 10px;
}

.btn-primary {
  display: block;
  width: 100%;
  background-color: #7f56d9;
  color: white;
  border: none;
  padding: 10px;
  border-radius: 5px;
  font-size: 16px;
  font-weight: bold;
  cursor: pointer;
  margin-top: 10px;
}

.btn-primary:hover {
  background-color: #6a48b0;
}
.alternative {
  color: #414651;
  text-align: center;
  margin-top: 20px;
}

.social-buttons {
  display: flex;
  justify-content: center;
  gap: 10px;
  margin-top: 10px;
}

.social-btn {
  background-color: #f1f1f1;
  border: none;
  padding: 8px 12px;
  border-radius: 5px;
  font-size: 14px;
  cursor: pointer;
}

.footer-text {
  color: #414651;
  text-align: center;
  margin-top: 20px;
}

.footer-text a {
  color: #7f56d9;
  text-decoration: none;
  font-weight: bold;
}

.footer-text a:hover {
  text-decoration: underline;
}
</style>
