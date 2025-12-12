import { ref } from 'vue'
import { defineStore } from 'pinia'
import api from '@/api/axios'

export const usePostStore = defineStore('post', () => {
  const posts = ref([])
  const myPosts = ref([])
  const post = ref(null)
  const regionRanks = ref([])
  const loading = ref(false)
  const error = ref('')

  async function fetchPostList(params = {}) {
    loading.value = true
    error.value = ''
    try {
      const { data } = await api.get('/posts', { params })
      posts.value = data?.data?.posts || []
      return posts.value
    } catch (e) {
      error.value = e?.response?.data?.message || e.message
      throw e
    } finally {
      loading.value = false
    }
  }

  async function fetchRegionRank(params = {}) {
    try {
      const { data } = await api.get('/posts/region-rank', { params })
      regionRanks.value = data?.data?.ranks || []
      return regionRanks.value
    } catch (e) {
      // 랭킹 실패는 전체 로딩에 영향 주지 않도록 에러만 보관
      console.error('fetchRegionRank error:', e?.response?.data?.message || e.message)
      return []
    }
  }

  async function fetchPostDetail(postId) {
    loading.value = true
    error.value = ''
    try {
      const { data } = await api.get(`/posts/${postId}`)
      post.value = data?.data || null
      return post.value
    } catch (e) {
      error.value = e?.response?.data?.message || e.message
      throw e
    } finally {
      loading.value = false
    }
  }

  async function fetchMyPosts() {
    loading.value = true
    error.value = ''
    try {
      const { data } = await api.get('/posts/my')
      myPosts.value = data?.data?.posts || []
      return myPosts.value
    } catch (e) {
      error.value = e?.response?.data?.message || e.message
      throw e
    } finally {
      loading.value = false
    }
  }

  async function createPost(postData) {
    error.value = ''
    const { data } = await api.post('/posts', postData)
    return data
  }

  async function updatePost(postId, postData) {
    error.value = ''
    const { data } = await api.patch(`/posts/${postId}`, postData)
    return data
  }

  async function deletePost(postId) {
    error.value = ''
    const { data } = await api.delete(`/posts/${postId}`)
    return data
  }

  return {
    posts,
    myPosts,
    post,
    loading,
    error,
    fetchPostList,
    fetchPostDetail,
    fetchMyPosts,
    createPost,
    updatePost,
    deletePost,
    regionRanks,
    fetchRegionRank,
  }
})
