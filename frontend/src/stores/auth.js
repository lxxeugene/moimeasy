// src/stores/auth.js
import { defineStore } from 'pinia'
import api from '../axios' // 중앙집중식 Axios 인스턴스 임포트
import router from '../router'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    accessToken: localStorage.getItem('accessToken') || null,
    user: JSON.parse(localStorage.getItem('user')) || null,
    errorMessage: ''
  }),
  actions: {
    async login(email, password) {
      try {
        const response = await api.post('/api/v1/login', { email, password })

        // Access Token 저장
        this.accessToken = response.data.accessToken
        localStorage.setItem('accessToken', this.accessToken)

        // 사용자 정보 저장
        this.user = {
          userId: response.data.userId,
          moeimId: response.data.moeimId,
          name: response.data.name,
          email: response.data.email,
          nickname: response.data.nickname,
          roleId: response.data.roleId
        }
        localStorage.setItem('user', JSON.stringify(this.user))

        // 로그인 성공 시 리디렉션
        router.push('/main') // 원하는 페이지로 변경 가능
      } catch (error) {
        if (error.response && error.response.data) {
          this.errorMessage = error.response.data
        } else {
          this.errorMessage = '로그인 중 오류가 발생했습니다.'
        }
        throw new Error(this.errorMessage)
      }
    },
    async logout() {
      try {
        await api.post('/logout')
      } catch (error) {
        console.error('로그아웃 중 오류가 발생했습니다.', error)
      } finally {
        this.accessToken = null
        this.user = null
        localStorage.removeItem('accessToken')
        localStorage.removeItem('user')
        router.push('/login')
      }
    },
    async refreshAccessToken() {
      try {
        const response = await api.post('/refresh-token')
        this.accessToken = response.data.accessToken
        localStorage.setItem('accessToken', this.accessToken)
      } catch (error) {
        console.error('Access Token 갱신 실패:', error)
        await this.logout()
        throw error
      }
    }
  }
})
