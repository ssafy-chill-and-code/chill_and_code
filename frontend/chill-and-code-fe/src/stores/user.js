import { defineStore } from 'pinia'
import axios from 'axios'
import { resetAllStores } from './reset'

export const useUserStore = defineStore('user', {
  state: () => ({
    user: null,
    isLoggedIn: false,
  }),
  actions: {
    async signup(payload) {
      await axios.post('/users', payload)
    },
    async login(payload) {
      await axios.post('/users/login', payload)
      await this.fetchProfile()
      this.isLoggedIn = true
    },
    async logout() {
      await axios.post('/users/logout')
      this.user = null
      this.isLoggedIn = false
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
    },
  },
})
