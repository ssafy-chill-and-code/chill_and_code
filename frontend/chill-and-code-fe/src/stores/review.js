import { ref } from 'vue'
import { defineStore } from 'pinia'
import api from '@/api/axios'

export const useReviewStore = defineStore('review', () => {
  const reviewSummary = ref(null) // PlaceReviewSummaryResponse
  const loading = ref(false)
  const error = ref('')

  // 리뷰 목록 조회
  async function fetchReviews(placeName, region) {
    loading.value = true
    error.value = ''
    try {
      const { data } = await api.get('/reviews', {
        params: {
          placeName,
          region: region || undefined
        }
      })
      // ApiResponse.success(summary) 형태로 반환되므로 data.data에 실제 데이터
      reviewSummary.value = data?.data || null
      return reviewSummary.value
    } catch (e) {
      error.value = e?.response?.data?.message || e.message
      throw e
    } finally {
      loading.value = false
    }
  }

  // 리뷰 작성
  async function createReview(placeName, region, rating, content) {
    error.value = ''
    try {
      const { data } = await api.post('/reviews', {
        placeName,
        region: region || null,
        rating,
        content: content || null
      })
      return data
    } catch (e) {
      error.value = e?.response?.data?.message || e.message
      throw e
    }
  }

  // 리뷰 요약 초기화
  function clearReviewSummary() {
    reviewSummary.value = null
    error.value = ''
  }

  return {
    reviewSummary,
    loading,
    error,
    fetchReviews,
    createReview,
    clearReviewSummary
  }
})

