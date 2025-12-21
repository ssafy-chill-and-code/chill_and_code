import { ref, computed } from 'vue'
import { defineStore } from 'pinia'

// 장소 추천 옵션 선택 전용 스토어 (API 미확정 상태: 호출 금지, 구조만 유지)
export const usePlaceRecommendationStore = defineStore('placeRecommendation', () => {
  const options = ref([]) // 장소 추천 옵션(예: 지역/예산/업무환경 등) — API 확정 전 비움
  const selection = ref({})
  const result = ref(null)
  const loading = ref({ options: false, submit: false, result: false })
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

  async function fetchResult() {
    loading.value.result = true
    error.value = ''
    try {
      // API 명세 확정 전: 실제 호출/더미 데이터 금지
      result.value = null
      return result.value
    } catch (e) {
      error.value = e?.response?.data?.message || e.message
      throw e
    } finally {
      loading.value.result = false
    }
  }

  async function submitSelection() {
    loading.value.submit = true
    error.value = ''
    try {
      // 명세 확정 전에는 아무 동작도 하지 않음
      return { ok: false }
    } catch (e) {
      error.value = e?.response?.data?.message || e.message
      throw e
    } finally {
      loading.value.submit = false
    }
  }

  function reset() {
    options.value = []
    selection.value = {}
    result.value = null
    loading.value = { options: false, submit: false, result: false }
    error.value = ''
  }

  return {
    options,
    selection,
    result,
    loading,
    error,
    fetchOptions,
    updateSelection,
    isReadyForSubmit,
    fetchResult,
    submitSelection,
    reset,
  }
})
