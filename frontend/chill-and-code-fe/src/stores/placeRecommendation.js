import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import api from '@/api/axios'

// 장소 추천 스토어 - 백엔드 API 연동
export const usePlaceRecommendationStore = defineStore('placeRecommendation', () => {
  const selection = ref({
    style: '', // 워케이션 스타일 (힐링, 작업몰입, 액티비티)
    budget: '', // 예산 (문자열로 전달)
    region: '', // 선호 지역 (optional)
    transport: '' // 이동수단 (optional)
  })
  
  const result = ref([]) // 추천 장소 카드 배열
  const loading = ref(false)
  const error = ref('')

  function updateSelection(patch) {
    selection.value = { ...selection.value, ...patch }
  }

  // 장소 추천 API 호출
  async function fetchPlaceRecommendation() {
    loading.value = true
    error.value = ''
    try {
      const params = {
        style: selection.value.style,
        budget: selection.value.budget
      }
      
      // region과 transport는 값이 있을 때만 추가
      if (selection.value.region && selection.value.region.trim()) {
        params.region = selection.value.region
      }
      
      if (selection.value.transport && selection.value.transport.trim()) {
        params.transport = selection.value.transport
      }
      
      console.log('🔍 장소 추천 API 호출 파라미터:', params)
      
      const response = await api.get('/recommend/places', { params })
      
      console.log('✅ 장소 추천 API 응답:', response.data)
      
      result.value = response.data
      return result.value
    } catch (e) {
      console.error('❌ 장소 추천 API 에러:', e?.response?.data || e.message)
      error.value = e?.response?.data?.message || e.message
      throw e
    } finally {
      loading.value = false
    }
  }

  const isReadyForSubmit = computed(() => {
    return selection.value.style && selection.value.budget
  })

  function reset() {
    selection.value = {
      style: '',
      budget: '',
      region: '',
      transport: ''
    }
    result.value = []
    loading.value = false
    error.value = ''
  }

  return {
    selection,
    result,
    loading,
    error,
    updateSelection,
    fetchPlaceRecommendation,
    isReadyForSubmit,
    reset,
  }
})
