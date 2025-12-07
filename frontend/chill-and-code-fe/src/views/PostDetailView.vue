<script setup>
import { onMounted, ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { usePostStore } from '@/stores/post'
import { useCommentStore } from '@/stores/comment'

const route = useRoute()
const router = useRouter()
const postStore = usePostStore()
const commentStore = useCommentStore()

const postId = route.params.postId
const message = ref('')

const newComment = ref('')
const editingId = ref(null)
const editingContent = ref('')

const comments = computed(() => commentStore.commentsByPost[postId] || [])

async function load() {
  await postStore.fetchPostDetail(postId)
  await commentStore.fetchCommentsByPost(postId)
}

function toList() {
  router.push({ name: 'posts' })
}

function toEdit() {
  router.push({ name: 'post-edit', params: { postId } })
}

async function removePost() {
  if (!confirm('Delete this post?')) return
  try {
    await postStore.deletePost(postId)
    toList()
  } catch (e) {
    message.value = e?.response?.data?.message || e.message
  }
}

async function addComment() {
  message.value = ''
  if (!newComment.value.trim()) return
  try {
    await commentStore.createComment(postId, newComment.value)
    newComment.value = ''
    await commentStore.fetchCommentsByPost(postId)
  } catch (e) {
    message.value = e?.response?.data?.message || e.message
  }
}

function startEdit(c) {
  editingId.value = c.commentId
  editingContent.value = c.content
}

function cancelEdit() {
  editingId.value = null
  editingContent.value = ''
}

async function saveEdit() {
  try {
    await commentStore.updateComment(editingId.value, editingContent.value)
    await commentStore.fetchCommentsByPost(postId)
    cancelEdit()
  } catch (e) {
    message.value = e?.response?.data?.message || e.message
  }
}

async function removeComment(id) {
  if (!confirm('Delete this comment?')) return
  try {
    await commentStore.deleteComment(id)
    await commentStore.fetchCommentsByPost(postId)
  } catch (e) {
    message.value = e?.response?.data?.message || e.message
  }
}

onMounted(load)
</script>

<template>
  <div>
    <h2 class="page-title">게시글 상세</h2>

    <section class="post-detail-section">
      <div class="post-title-placeholder">
        {{ postStore.post ? postStore.post.title : '게시글 제목' }}
      </div>
      <div class="post-content-placeholder">
        {{ postStore.post ? postStore.post.content : '게시글 내용' }}
      </div>
      <div class="form-group">
        <button class="btn" @click="toList">목록</button>
        <button class="btn" @click="toEdit">수정</button>
        <button class="btn" @click="removePost">삭제</button>
      </div>
      <div v-if="postStore.loading">Loading...</div>
      <div v-if="postStore.error">{{ postStore.error }}</div>
      <div v-if="message">{{ message }}</div>
    </section>

    <section class="comment-write-section">
      <div class="form-group">
        <label class="form-label">댓글 입력</label>
        <textarea class="form-textarea" v-model="newComment" placeholder="댓글을 입력하세요"></textarea>
      </div>
      <button class="btn" @click="addComment">댓글 등록</button>
    </section>

    <section class="comment-list-section">
      <ul>
        <li v-for="c in comments" :key="c.commentId">
          <div v-if="editingId !== c.commentId">
            <div>{{ c.content }}</div>
            <div>by {{ c.nickname || c.userId }}</div>
            <button class="btn" @click="startEdit(c)">수정</button>
            <button class="btn" @click="removeComment(c.commentId)">삭제</button>
          </div>
          <div v-else>
            <input class="form-input" v-model="editingContent" />
            <button class="btn" @click="saveEdit">저장</button>
            <button class="btn" @click="cancelEdit">취소</button>
          </div>
        </li>
      </ul>
      <div v-if="commentStore.loading">Loading...</div>
      <div v-if="commentStore.error">{{ commentStore.error }}</div>
    </section>
  </div>
</template>
