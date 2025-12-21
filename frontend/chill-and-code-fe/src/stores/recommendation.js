import { ref, computed } from 'vue'
import { defineStore } from 'pinia'

// 절대 규칙 준수: API 미확정 상태에서는 실제 호출/더미 데이터 금지
export const useRecommendationStore = defineStore('recommendation', () => {
  const options = ref([]) // 기간 추천 옵션(그룹/항목) — API 확정 전까지 빈 배열 유지
  const selection = ref({}) // 사용자의 선택 상태(키-값 형태), 실제 제출은 API 확정 후
  const result = ref(null) // 추천 결과(요약/대안/이유)

  const loading = ref({ options: false, submit: false, result: false })
  const error = ref('')

  // API 제공 전까지는 구조만 유지하고, 화면에 준비중 표기를 유도
  async function fetchOptions() {
    loading.value.options = true
    error.value = ''
    try {
      // API 명세 확정 전: 호출하지 않음. 화면에서 ReadyBadge로 처리.
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
    // 단순 로컬 머지 (도메인 로직 없음)
    selection.value = { ...selection.value, ...patch }
  }

  // 결과 조회(API 확정 전: 구조만 유지)
  async function fetchResult(params = {}) {
    loading.value.result = true
    error.value = ''
    try {
      // API 명세 확정 전: 호출하지 않음. 화면에서 ReadyBadge로 처리.
      result.value = null
      return result.value
    } catch (e) {
      error.value = e?.response?.data?.message || e.message
      throw e
    } finally {
      loading.value.result = false
    }
  }

  // 선택 제출 (API 확정 전: no-op)
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

  const isReadyForSubmit = computed(() => {
    // 제출 활성 조건은 API/스키마에 의존 — 현재는 항상 false
    return false
  })

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
    fetchResult,
    submitSelection,
    isReadyForSubmit,
    reset,
  }
})
