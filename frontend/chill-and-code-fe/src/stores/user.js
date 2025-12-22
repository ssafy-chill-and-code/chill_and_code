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
    /**
     * 로컬 인증 상태만 정리 (서버 호출 없음)
     * 비밀번호 변경 후와 같이 Refresh Token이 이미 무효화된 경우 사용
     */
    clearLocalAuth() {
      this.user = null
      this.isLoggedIn = false
      this.accessToken = null
      sessionStorage.removeItem('accessToken')
      // 모든 store 상태 초기화
      resetAllStores()
    },
    
    /**
     * 서버 logout API 호출 후 로컬 상태 정리
     * 서버 호출 실패해도 로컬 상태는 항상 정리됨
     */
    async logout() {
      try {
        await axios.post('/auth/logout')
      } catch (e) {
        // 서버 logout API 실패해도 로컬 상태는 정리
        // (예: 이미 Refresh Token이 revoke된 경우)
        console.warn('Logout API 호출 실패 (로컬 상태는 정리됨):', e?.response?.data || e.message)
      } finally {
        // 서버 호출 성공/실패와 관계없이 항상 로컬 상태 정리
        this.clearLocalAuth()
      }
    },
    async fetchProfile() {
      const { data } = await axios.get('/users/me')
      this.user = data.data
      this.isLoggedIn = !!data.data
    },
    async updateProfile(payload) {
      const { data } = await axios.patch('/users/me', payload)
      // 프로필 수정 후 다시 조회하여 최신 정보 반영
      await this.fetchProfile()
      return data
    },
    async changePassword(payload) {
      const { data } = await axios.patch('/users/me/password', payload)
      return data
    },
  },
})
