import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import api from '@/api/axios'

// 기간 추천 스토어 - 백엔드 API 연동
export const useRecommendationStore = defineStore('recommendation', () => {
  const selection = ref({
    style: null, // 'RELAX' | 'WORK' | 'BALANCE'
    minDays: null,
    maxDays: null,
    remoteWorkAllowed: true
  })
  
  const result = ref(null) // 추천 결과 { primary, reason, alternatives }
  const loading = ref(false)
  const error = ref('')

  function updateSelection(patch) {
    selection.value = { ...selection.value, ...patch }
  }

  // 기간 추천 API 호출
  async function fetchPeriodRecommendation() {
    loading.value = true
    error.value = ''
    try {
      const response = await api.post('/recommend/period', {
        style: selection.value.style,
        minDays: selection.value.minDays,
        maxDays: selection.value.maxDays,
        remoteWorkAllowed: selection.value.remoteWorkAllowed
      })
      
      result.value = response.data.data
      return result.value
    } catch (e) {
      error.value = e?.response?.data?.message || e.message
      throw e
    } finally {
      loading.value = false
    }
  }

  const isReadyForSubmit = computed(() => {
    return selection.value.minDays > 0 && selection.value.maxDays > 0 && 
           selection.value.minDays <= selection.value.maxDays
  })

  function reset() {
    selection.value = {
      style: null,
      minDays: null,
      maxDays: null,
      remoteWorkAllowed: true
    }
    result.value = null
    loading.value = false
    error.value = ''
  }

  return {
    selection,
    result,
    loading,
    error,
    updateSelection,
    fetchPeriodRecommendation,
    isReadyForSubmit,
    reset,
  }
})
