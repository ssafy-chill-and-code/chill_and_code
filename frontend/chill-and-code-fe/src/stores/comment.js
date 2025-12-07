import { ref } from 'vue'
import { defineStore } from 'pinia'
import api from '@/api/axios'

export const useCommentStore = defineStore('comment', () => {
  // commentsByPost[postId] = Comment[]
  const commentsByPost = ref({})
  const myComments = ref([])
  const loading = ref(false)
  const error = ref('')

  async function fetchCommentsByPost(postId) {
    loading.value = true
    error.value = ''
    try {
      const { data } = await api.get(`/posts/${postId}/comments`)
      const list = data?.data?.comments || []
      commentsByPost.value = { ...commentsByPost.value, [postId]: list }
      return list
    } catch (e) {
      error.value = e?.response?.data?.message || e.message
      throw e
    } finally {
      loading.value = false
    }
  }

  async function fetchMyComments() {
    loading.value = true
    error.value = ''
    try {
      const { data } = await api.get('/comments/my')
      myComments.value = data?.data?.comments || []
      return myComments.value
    } catch (e) {
      error.value = e?.response?.data?.message || e.message
      throw e
    } finally {
      loading.value = false
    }
  }

  async function createComment(postId, content) {
    error.value = ''
    const { data } = await api.post(`/posts/${postId}/comments`, { content })
    return data
  }

  async function updateComment(commentId, content) {
    error.value = ''
    const { data } = await api.patch(`/comments/${commentId}`, { content })
    return data
  }

  async function deleteComment(commentId) {
    error.value = ''
    const { data } = await api.delete(`/comments/${commentId}`)
    return data
  }

  return {
    commentsByPost,
    myComments,
    loading,
    error,
    fetchCommentsByPost,
    fetchMyComments,
    createComment,
    updateComment,
    deleteComment,
  }
})

