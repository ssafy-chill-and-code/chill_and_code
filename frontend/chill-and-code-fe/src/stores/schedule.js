import { defineStore } from 'pinia'
import axios from '@/api/axios'

export const useScheduleStore = defineStore('schedule', {
  state: () => ({
    schedules: [],
    prefilledPlace: null, // 추천 장소에서 넘어온 정보
    prefilledPeriod: null, // 추천받은 기간 정보
  }),
  actions: {
    // 장소 추천에서 일정 만들기로 연결
    setPrefilledPlace(place, period) {
      this.prefilledPlace = place
      this.prefilledPeriod = period
    },
    // 프리필 정보 초기화
    clearPrefilledPlace() {
      this.prefilledPlace = null
      this.prefilledPeriod = null
    },
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
