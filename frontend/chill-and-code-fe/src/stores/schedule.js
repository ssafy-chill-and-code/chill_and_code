import { defineStore } from 'pinia'
import axios from '@/api/axios'

export const useScheduleStore = defineStore('schedule', {
  state: () => ({
    schedules: [],
    summary: null,
  }),
  actions: {
    async createSchedule(payload) {
      const { data } = await axios.post('/schedules', payload)
      return data
    },
    async fetchSchedules(monthString, types = null) {
      const params = { month: monthString }
      if (types && types.length > 0) {
        params.type = types
      }
      const { data } = await axios.get('/schedules', { params })
      this.schedules = data?.data?.schedules || []
    },
    async fetchSummary(monthString) {
      const { data } = await axios.get('/schedules/summary', { 
        params: { month: monthString } 
      })
      this.summary = data?.data || null
      return this.summary
    },
    async updateSchedule(id, payload) {
      const { data } = await axios.patch(`/schedules/${id}`, payload)
      return data
    },
    async deleteSchedule(id) {
      await axios.delete(`/schedules/${id}`)
      this.schedules = this.schedules.filter((s) => String(s.scheduleId) !== String(id))
    },
  },
})
