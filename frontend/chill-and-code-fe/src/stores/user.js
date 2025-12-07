import { defineStore } from 'pinia'
import axios from 'axios'

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
    },
    async fetchProfile() {
      const { data } = await axios.get('/users/me')
      this.user = data
      this.isLoggedIn = !!data
    },
    async updateProfile(payload) {
      const { data } = await axios.patch('/users/me', payload)
      this.user = data
    },
  },
})

