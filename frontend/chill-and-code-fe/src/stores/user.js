import { defineStore } from 'pinia'
import axios from '@/api/axios'
import { resetAllStores } from './reset'

export const useUserStore = defineStore('user', {
  state: () => ({
    user: null,
    isLoggedIn: false,
    accessToken: sessionStorage.getItem('accessToken') || null,
  }),
  actions: {
    async signup(payload) {
      const { data } = await axios.post('/users', payload)
      return data
    },
    async login(payload) {
      const { data } = await axios.post('/users/login', payload)
      
      // Access Token 저장 (sessionStorage 사용)
      if (data.data?.accessToken) {
        this.accessToken = data.data.accessToken
        sessionStorage.setItem('accessToken', data.data.accessToken)
      } else {
        throw new Error('서버로부터 Access Token을 받지 못했습니다.')
      }
      
      // 프로필 조회
      try {
        await this.fetchProfile()
        this.isLoggedIn = true
      } catch (e) {
        console.warn('프로필 조회 실패:', e)
        // 토큰은 저장되었으므로 로그인 상태로 설정
        this.isLoggedIn = true
        this.user = { userId: data.data.userId }
      }
      
      return data
    },
    async logout() {
      await axios.post('/auth/logout')
      this.user = null
      this.isLoggedIn = false
      this.accessToken = null
      sessionStorage.removeItem('accessToken')
      // 모든 store 상태 초기화
      resetAllStores()
    },
    async fetchProfile() {
      const { data } = await axios.get('/users/me')
      this.user = data.data
      this.isLoggedIn = !!data.data
    },
    async updateProfile(payload) {
      const { data } = await axios.patch('/users/me', payload)
      this.user = data.data
      return data
    },
  },
})
