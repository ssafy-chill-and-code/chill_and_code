import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import api from '@/api/axios'

// 기간 추천 스토어 - 백엔드 API 연동
export const useRecommendationStore = defineStore('recommendation', () => {
  const selection = ref({
    style: null, // 'RELAX' | 'WORK' | 'BALANCE'
    minDays: null,
    maxDays: null,
    remoteWorkAllowed: false
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
      console.log('🚀 [Store] API 요청 시작')
      console.log('요청 데이터:', {
        style: selection.value.style,
        minDays: selection.value.minDays,
        maxDays: selection.value.maxDays,
        remoteWorkAllowed: selection.value.remoteWorkAllowed
      })
      
      const response = await api.post('/recommend/period', {
        style: selection.value.style,
        minDays: selection.value.minDays,
        maxDays: selection.value.maxDays,
        remoteWorkAllowed: selection.value.remoteWorkAllowed
      })
      
      console.log('✅ [Store] API 응답 받음')
      console.log('response.data:', response.data)
      console.log('response.data.data:', response.data.data)
      console.log('primary:', response.data.data?.primary)
      console.log('alternatives:', response.data.data?.alternatives)
      console.log('alternatives 타입:', typeof response.data.data?.alternatives)
      console.log('alternatives 배열인가?', Array.isArray(response.data.data?.alternatives))
      
      result.value = response.data.data
      console.log('💾 [Store] result.value 저장 완료:', result.value)
      
      return result.value
    } catch (e) {
      console.error('❌ [Store] API 에러:', e)
      console.error('에러 응답:', e?.response?.data)
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
      remoteWorkAllowed: false
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
