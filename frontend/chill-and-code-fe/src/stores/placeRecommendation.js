import { ref, computed } from 'vue'
import { defineStore } from 'pinia'

// 장소 추천 옵션 선택 전용 스토어 (API 미확정 상태: 호출 금지, 구조만 유지)
export const usePlaceRecommendationStore = defineStore('placeRecommendation', () => {
  const options = ref([]) // 장소 추천 옵션(예: 지역/예산/업무환경 등) — API 확정 전 비움
  const selection = ref({})
  const loading = ref({ options: false, submit: false })
  const error = ref('')

  async function fetchOptions() {
    loading.value.options = true
    error.value = ''
    try {
      // API 명세 확정 전: 실제 호출/더미 데이터 금지
      options.value = []
      return options.value
    } catch (e) {
      error.value = e?.response?.data?.message || e.message
      throw e
    } finally {
      loading.value.options = false
    }
  }

  function updateSelection(patch) {
    selection.value = { ...selection.value, ...patch }
  }

  const isReadyForSubmit = computed(() => false) // 명세 의존, 현재는 비활성

  function reset() {
    options.value = []
    selection.value = {}
    loading.value = { options: false, submit: false }
    error.value = ''
  }

  return {
    options,
    selection,
    loading,
    error,
    fetchOptions,
    updateSelection,
    isReadyForSubmit,
    reset,
  }
})

